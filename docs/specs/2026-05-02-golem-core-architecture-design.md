<!--
SPDX-FileCopyrightText: 2026 klikli-dev

SPDX-License-Identifier: MIT
-->

# Golem Core Architecture Design

Date: 2026-05-02

## Summary

This design introduces a core-driven golem architecture where a golem holds one installed core item in a dedicated internal slot. The installed core determines the golem's persistent configuration, active runtime state, brain memories, sensors, activities, behaviors, configuration UI hooks, and special world-binding tool actions.

The architecture is runtime-centric and composition-first:

- the **core item** is the canonical persistent configuration source
- the **golem entity** stays thin and delegates to the installed core
- the **active runtime** is a typed mutable working copy owned by the entity
- the **brain** contains only transient AI-facing state for the currently installed core

## Goals

- Allow future golem cores to define distinct capabilities and jobs.
- Ensure a golem only carries the memories, sensors, and behavior set needed by its current core.
- Support clean server-side runtime state with minimal client sync.
- Keep the golem entity thin and mostly delegating.
- Use composition over inheritance for core logic.
- Keep core item configuration on the item itself using data components.
- Support both item-first configuration and attached in-world binding through a special tool.
- Distinguish clearly between unconfigured and temporarily blocked job states.

## Non-Goals

- Data-driven core definitions in the initial architecture.
- Multiple golem host entity classes in the initial architecture.
- Hopper or automation interaction with the core slot.
- Live syncing of brain memories to the client.
- Precise restoration of an in-progress subtask after save/load.

## Core Architectural Model

### Golem entity

The golem entity owns only the minimum host state:

- one dedicated internal core slot
- one synced installed-core `ItemStack`
- one active `ActiveCoreRuntime`
- minimal orchestration methods such as insert, remove, refresh, and rebuild
- coarse current status for rendering and interaction checks

The entity must not contain per-core job logic.

### Core items

Each core is its own registered item. All cores should share a common `CoreItem` base only for truly common item behavior. Per-core specialization should come from composition through a `CoreDefinition`, not from deep item inheritance.

Each `CoreItem` directly exposes its `CoreDefinition`.

### Core definition facade

Each core owns one `CoreDefinition` facade object. This is the main entrypoint for all core-specific behavior and configuration, but it is internally composed from smaller helpers.

The `CoreDefinition` coordinates:

- item config load/save
- runtime creation
- run-status evaluation
- memory and sensor declaration
- activity and behavior package construction
- typed behavior context creation
- special binding tool actions
- screen and menu hooks
- diagnostics generation

### Internal helper composition

`CoreDefinition` should remain a facade, not a god object. Internally it should delegate to well-bounded helper parts such as:

- config persistence mapper
- runtime factory
- run-status evaluator
- brain package builder
- behavior-context factory
- binding-action provider
- UI integration helper
- diagnostics generator

The exact helper split may vary, but responsibilities must remain separated.

## Data Ownership and Lifecycle

### Persistent config: core item

The installed core item is the canonical persistent configuration source.

Persistent configuration is stored as **multiple per-datum data components**, not one large monolithic payload component. Each core stores only the components it actually needs.

Examples:

- source inventory position
- destination inventory position
- work area
- item filter
- mode toggles

Each core reads missing components as defaults where appropriate and writes them back when saved.

### Active runtime: golem-owned working copy

When a core is inserted, its item config is copied into a typed mutable runtime object owned by the golem entity.

Examples:

- `ActiveCoreRuntime`
- `HaulerRuntime`
- `FarmerRuntime`

This runtime is the authoritative active working copy during execution.

### Brain state: transient AI-only state

The brain stores only transient AI-facing state for the current core, such as:

- temporary selected targets
- walk and look targets
- cooldowns
- temporary interaction state
- short-lived decision memory

The brain is not the owner of durable configuration.

## Core Slot Model

The golem uses a dedicated single-slot internal container for the installed core.

Rules:

- accepts only `CoreItem`
- not exposed for automation
- player-only interaction
- removal is allowed even during work, but it aborts current execution

## Insert, Remove, Edit, and Load Flows

### Core insert

On insert:

1. validate the item as a `CoreItem`
2. obtain the `CoreDefinition` from the item
3. load a typed runtime from item data components
4. replace the active runtime on the entity
5. rebuild the brain immediately for the new core

If the config is incomplete or invalid, insertion is still allowed. The runtime enters an **unconfigured** state and the golem remains idle.

### Core removal

On removal:

1. abort the current task
2. write runtime configuration back into the installed core item
3. clear the active runtime
4. discard transient AI state
5. rebuild the brain into no-core idle mode

### Save/load

On save:

- the installed core item already holds canonical persistent config
- no duplicate entity persistence of core job config is required

On load:

1. read the installed core item
2. recreate the typed runtime from item data
3. rebuild the brain from scratch

The system does not restore exact mid-task execution phase. It resumes from config and recomputes fresh targets and actions.

### Attached special-tool edit

Normal configuration is item-first. However, a special tool is allowed to edit an attached core in-place as a controlled exception.

Attached edit flow:

1. the tool selects a core-defined binding action
2. the tool mutates the installed core item first
3. the runtime is recreated from the item
4. the brain is fully rebuilt immediately

This preserves the rule that item config is always canonical.

## Brain Architecture

### Brain rebuild strategy

The system uses **full brain rebuild** whenever the active core configuration changes materially.

Rebuild triggers:

- core insert
- core removal
- core type change
- attached special-tool edit

With no core installed, the golem has a minimal brain only.

### No-core behavior

Without a core, the golem should only:

- idle
- look at nearby players

No other meaningful autonomous behavior is provided by default.

### Core-provided brain package

Each `CoreDefinition` provides the brain package for that core:

- memory modules
- sensors
- activities
- behaviors
- typed behavior context

The entity should delegate brain assembly entirely to the current core definition.

### Memory policy

Custom `MemoryModuleType`s may be globally registered in code, but each rebuilt brain includes only the subset required by the current core.

This ensures the golem only carries the current job's AI-facing memory set.

### Sensor policy

Use vanilla-style boundaries between sensors and behaviors.

- sensors provide perception inputs
- behaviors make decisions and execute actions

The implementation should follow vanilla usage patterns instead of inventing a custom sensor-heavy model.

### Activity policy

Default preference:

- one main job activity per core
- runtime state plus memories coordinate phases inside that activity

Multiple activities are allowed only when a core truly has distinct operational modes that benefit from separate activity boundaries.

### Behavior access policy

Behaviors should not freely cast runtime objects from the entity.

Instead, the `CoreDefinition` should create a typed behavior context for its behavior package. Behaviors depend on that typed context and only read/write brain memories appropriate to that core.

### Reuse policy

The system should prefer reusable behavior building blocks, but allow bespoke behaviors where a workflow is unique. Reuse must not force weak abstractions.

## Run Status and Diagnostics

### Run-status categories

Run-status evaluation is owned by the runtime/definition layer, not inferred ad hoc from behavior failure.

At minimum, the architecture distinguishes:

- **Runnable**
- **Unconfigured**
- **Temporarily Blocked**

### Meaning

- **Unconfigured** means player action is required to make the core usable.
- **Temporarily Blocked** means the config is valid, but the world currently prevents progress.

This distinction must be reflected in AI decisions and player feedback.

### Wildcard: self-diagnostics contract

Each `CoreDefinition` should expose a diagnostics contract capable of producing a short structured explanation of why the core is not currently working.

Examples:

- missing source inventory
- work area not configured
- no valid targets in area
- destination full

This is not only for polish. It is an architectural debugging aid that should support:

- tool feedback
- config UI messaging
- future tooltips or debugging views
- easier support for new cores

## UI and Tooling

### Core configuration UI

Primary configuration is item-first. Each core may expose its own screen subclass for configuration.

Shared base classes are allowed only for true common plumbing. The architecture should avoid deep screen inheritance trees.

### Binding tool

The special world-binding tool is generic, but all binding semantics come from the current core's `CoreDefinition`.

The tool has a UI for selecting a current action. Those actions are core-defined, not generic hardcoded slots.

The selected action drives how world clicks are interpreted and how the core item is updated.

## Sync Model

### Always-on sync

Use vanilla entity sync via `SynchedEntityData`.

The installed core slot is represented as a real synced entity `ItemStack`. This automatically syncs the installed item and its synced components to the client.

### No extra always-on runtime sync

There should be no extra always-on sync for runtime brain-like state beyond the synced installed core item.

Runtime execution details remain server-side.

### Screen and special-case sync

Use packets only for:

- screen interactions
- special tool interactions
- detailed views that are not always needed

Brain memories are never synced directly.

## Player Feedback

### Coarse in-world indicators

Normal states show no indicator.

Problem states use an above-head visual effect:

- **red** for unconfigured
- **orange** for temporarily blocked

### Detailed feedback

Detailed reasons should be shown only through the tool or relevant UI, not through always-on in-world overlays.

## Future Flexibility

The initial architecture targets one shared golem entity class, but should avoid needless coupling that would prevent future reuse with other golem body types.

Use small host-facing interfaces where practical, but do not introduce a large abstraction layer up front.

Core definitions are Java-coded only in the initial design.

## Risks and Trade-offs

### Devil's advocate: `CoreDefinition` bloat

The biggest architectural risk is that `CoreDefinition` becomes a disguised god object.

Because it is the natural facade for item persistence, runtime creation, AI package creation, tool actions, UI hooks, and diagnostics, it can easily accumulate too much logic directly.

If that happens, the system will recreate the same coupling this design is trying to avoid.

### Mitigation

To prevent that outcome:

- keep `CoreDefinition` as a facade/coordinator only
- push real work into composable helper parts
- ensure helper responsibilities stay narrow and explicit
- avoid per-core copy-paste helpers that blur boundaries again

### Trade-off accepted

This design intentionally chooses:

- full brain rebuilds over continuity of in-progress execution
- explicit delegation over central registries
- typed per-core runtime/context over generic bags of state

These choices optimize for maintainability, clarity, and extensibility over minimal scaffolding.

## Recommended Initial Implementation Order

1. Add the dedicated core slot and synced installed-core `ItemStack`.
2. Introduce `CoreItem`, `CoreDefinition`, and `ActiveCoreRuntime` contracts.
3. Implement no-core idle brain rebuild and core-driven brain rebuild.
4. Add one real example core using the full pattern.
5. Add run-status and diagnostics support.
6. Add above-head red/orange status indicators.
7. Add the special binding tool and attached-core live reconfiguration path.
