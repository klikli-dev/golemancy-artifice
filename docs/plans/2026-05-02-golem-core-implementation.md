<!--
SPDX-FileCopyrightText: 2026 klikli-dev

SPDX-License-Identifier: MIT
-->

# Golem Core Implementation Plan

> **For agentic workers:** Use subagent-driven-development or executing-plans skill to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Build the first version of the core-driven golem architecture, with the first real core moving items from one inventory to another.

**Architecture:** The wooden golem stays thin and owns only a synced installed-core `ItemStack`, a dedicated internal core slot, a typed active runtime, and core lifecycle orchestration. Each registered core item exposes a `CoreDefinition` facade that composes persistence mapping, runtime creation, status evaluation, AI package creation, diagnostics, and binding-tool actions. The first concrete core is an inventory-transfer core that copies items from one configured inventory to another and rebuilds the brain on insert, removal, and tool-driven reconfiguration.

**Tech Stack:** Java 25, NeoForge 26.1.2, Minecraft Brain/Behavior API, SynchedEntityData, Data Components, custom payloads, GeckoLib, Minecraft/NeoForge GameTests via `runGameTestServer`.

**Testing rule:** Do not add JUnit tests for this feature. All automated validation should use Minecraft/NeoForge GameTests and run through `./gradlew.bat runGameTestServer`.

---

## File structure

### Existing files to modify

- Modify: `src/main/java/com/klikli_dev/golemancyartifice/GolemancyArtifice.java` — register new item, component, menu, payload, and GameTest hooks.
- Modify: `src/main/java/com/klikli_dev/golemancyartifice/GolemancyArtificeClient.java` — register screens and client payload/render hooks.
- Modify: `src/main/java/com/klikli_dev/golemancyartifice/registry/ItemRegistry.java` — register the transfer core and binding tool items.
- Modify: `src/main/java/com/klikli_dev/golemancyartifice/registry/CreativeModeTabRegistry.java` — expose new items in the creative tab.
- Modify: `src/main/java/com/klikli_dev/golemancyartifice/datagen/lang/ENUSLanguageProvider.java` — add names and status text.
- Modify: `src/main/java/com/klikli_dev/golemancyartifice/datagen/model/GolemancyArtificeItemModelProvider.java` — add generated item models for the core and tool.
- Modify: `src/main/java/com/klikli_dev/golemancyartifice/content/entity/golem/wooden/WoodenGolemEntity.java` — add core slot, runtime, save/load, status, and brain rebuild lifecycle.
- Modify: `src/main/java/com/klikli_dev/golemancyartifice/content/entity/golem/wooden/WoodenGolemAi.java` — shrink to no-core idle package or replace with thin no-core package helper.

### New core framework files

- Create: `src/main/java/com/klikli_dev/golemancyartifice/content/golem/core/CoreDefinition.java`
- Create: `src/main/java/com/klikli_dev/golemancyartifice/content/golem/core/CoreItem.java`
- Create: `src/main/java/com/klikli_dev/golemancyartifice/content/golem/core/runtime/ActiveCoreRuntime.java`
- Create: `src/main/java/com/klikli_dev/golemancyartifice/content/golem/core/runtime/NoCoreRuntime.java`
- Create: `src/main/java/com/klikli_dev/golemancyartifice/content/golem/core/status/CoreRunState.java`
- Create: `src/main/java/com/klikli_dev/golemancyartifice/content/golem/core/status/CoreDiagnostic.java`
- Create: `src/main/java/com/klikli_dev/golemancyartifice/content/golem/core/host/GolemCoreHost.java`
- Create: `src/main/java/com/klikli_dev/golemancyartifice/content/golem/core/slot/GolemCoreSlot.java`

### New inventory-transfer core files

- Create: `src/main/java/com/klikli_dev/golemancyartifice/content/golem/core/transfer/InventoryTransferCoreDefinition.java`
- Create: `src/main/java/com/klikli_dev/golemancyartifice/content/golem/core/transfer/InventoryTransferRuntime.java`
- Create: `src/main/java/com/klikli_dev/golemancyartifice/content/golem/core/transfer/InventoryTransferConfigMapper.java`
- Create: `src/main/java/com/klikli_dev/golemancyartifice/content/golem/core/transfer/InventoryTransferStatusEvaluator.java`
- Create: `src/main/java/com/klikli_dev/golemancyartifice/content/golem/core/transfer/InventoryTransferBehaviorContext.java`
- Create: `src/main/java/com/klikli_dev/golemancyartifice/content/golem/core/transfer/InventoryTransferBrainFactory.java`
- Create: `src/main/java/com/klikli_dev/golemancyartifice/content/golem/core/transfer/InventoryTransferDiagnostics.java`
- Create: `src/main/java/com/klikli_dev/golemancyartifice/content/golem/core/transfer/behavior/TransferConfiguredInventoryItemsBehavior.java`

### New data component and registry files

- Create: `src/main/java/com/klikli_dev/golemancyartifice/registry/DataComponentTypeRegistry.java`

### New binding tool, menu, screen, and payload files

- Create: `src/main/java/com/klikli_dev/golemancyartifice/content/item/GolemBindingToolItem.java`
- Create: `src/main/java/com/klikli_dev/golemancyartifice/content/menu/GolemBindingToolMenu.java`
- Create: `src/main/java/com/klikli_dev/golemancyartifice/registry/MenuRegistry.java`
- Create: `src/main/java/com/klikli_dev/golemancyartifice/registry/PayloadRegistry.java`
- Create: `src/main/java/com/klikli_dev/golemancyartifice/network/payload/ServerboundSelectBindingActionPayload.java`
- Create: `src/main/java/com/klikli_dev/golemancyartifice/network/payload/ServerboundApplyBindingTargetPayload.java`
- Create: `src/main/java/com/klikli_dev/golemancyartifice/client/screen/GolemBindingToolScreen.java`

### New client render helper files

- Create: `src/main/java/com/klikli_dev/golemancyartifice/client/render/GolemCoreStatusParticles.java`

### New GameTest files

- Create: `src/main/java/com/klikli_dev/golemancyartifice/gametest/GolemancyGameTestFunctions.java`
- Create: `src/main/java/com/klikli_dev/golemancyartifice/gametest/GolemancyGameTests.java`

### Resources to create or generate

- Create: `src/main/resources/assets/golemancyartifice/textures/item/inventory_transfer_core.png`
- Create: `src/main/resources/assets/golemancyartifice/textures/item/golem_binding_tool.png`
- Generate: `src/generated/resources/assets/golemancyartifice/lang/en_us.json`
- Generate: `src/generated/resources/assets/golemancyartifice/items/inventory_transfer_core.json`
- Generate: `src/generated/resources/assets/golemancyartifice/items/golem_binding_tool.json`

---

### Task 1: Add the core framework contracts and GameTest scaffolding

**Files:**
- Create: `src/main/java/com/klikli_dev/golemancyartifice/content/golem/core/CoreDefinition.java`
- Create: `src/main/java/com/klikli_dev/golemancyartifice/content/golem/core/CoreItem.java`
- Create: `src/main/java/com/klikli_dev/golemancyartifice/content/golem/core/runtime/ActiveCoreRuntime.java`
- Create: `src/main/java/com/klikli_dev/golemancyartifice/content/golem/core/runtime/NoCoreRuntime.java`
- Create: `src/main/java/com/klikli_dev/golemancyartifice/content/golem/core/status/CoreRunState.java`
- Create: `src/main/java/com/klikli_dev/golemancyartifice/content/golem/core/status/CoreDiagnostic.java`
- Create: `src/main/java/com/klikli_dev/golemancyartifice/content/golem/core/host/GolemCoreHost.java`
- Create: `src/main/java/com/klikli_dev/golemancyartifice/gametest/GolemancyGameTestFunctions.java`
- Create: `src/main/java/com/klikli_dev/golemancyartifice/gametest/GolemancyGameTests.java`
- Modify: `src/main/java/com/klikli_dev/golemancyartifice/GolemancyArtifice.java`

- [ ] **Step 1: Write the failing GameTest registration for the new core framework**

```java
// src/main/java/com/klikli_dev/golemancyartifice/gametest/GolemancyGameTestFunctions.java
package com.klikli_dev.golemancyartifice.gametest;

import com.klikli_dev.golemancyartifice.GolemancyArtifice;
import com.klikli_dev.golemancyartifice.content.golem.core.CoreItem;
import com.klikli_dev.golemancyartifice.registry.ItemRegistry;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestFunctionLoader;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

public final class GolemancyGameTestFunctions extends TestFunctionLoader {
    public static final ResourceKey<Consumer<GameTestHelper>> CORE_ITEM_EXPOSES_DEFINITION = ResourceKey.create(
            Registries.TEST_FUNCTION,
            Identifier.fromNamespaceAndPath(GolemancyArtifice.MODID, "core_item_exposes_definition")
    );

    @Override
    public void load(BiConsumer<ResourceKey<Consumer<GameTestHelper>>, Consumer<GameTestHelper>> register) {
        register.accept(CORE_ITEM_EXPOSES_DEFINITION, helper -> {
            if (!(ItemRegistry.INVENTORY_TRANSFER_CORE.get() instanceof CoreItem coreItem)) {
                helper.fail("Inventory transfer core item must extend CoreItem");
            }

            helper.assertTrue(coreItem.definition() != null, "CoreItem must expose a CoreDefinition");
            helper.succeed();
        });
    }
}
```

```java
// src/main/java/com/klikli_dev/golemancyartifice/gametest/GolemancyGameTests.java
package com.klikli_dev.golemancyartifice.gametest;

import com.klikli_dev.golemancyartifice.GolemancyArtifice;
import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.FunctionGameTestInstance;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;

public final class GolemancyGameTests {
    private GolemancyGameTests() {
    }

    public static void onRegisterGameTests(RegisterGameTestsEvent event) {
        Holder<TestEnvironmentDefinition<?>> environment = event.registerEnvironment(
                Identifier.fromNamespaceAndPath(GolemancyArtifice.MODID, "default")
        );

        event.registerTest(
                Identifier.fromNamespaceAndPath(GolemancyArtifice.MODID, "core_item_exposes_definition"),
                new FunctionGameTestInstance(
                        GolemancyGameTestFunctions.CORE_ITEM_EXPOSES_DEFINITION,
                        new TestData<>(environment, Identifier.withDefaultNamespace("empty"), 20, 0, true)
                )
        );
    }
}
```

```java
// src/main/java/com/klikli_dev/golemancyartifice/GolemancyArtifice.java
modEventBus.addListener(GolemancyGameTests::onRegisterGameTests);
```

- [ ] **Step 2: Run compile to verify it fails before the framework exists**

Run: `./gradlew.bat compileJava`

Expected: FAIL with missing symbols such as `CoreItem`, `INVENTORY_TRANSFER_CORE`, or `GolemancyGameTests`.

- [ ] **Step 3: Add the minimal framework contracts and wire the GameTest listener**

```java
// src/main/java/com/klikli_dev/golemancyartifice/content/golem/core/status/CoreRunState.java
package com.klikli_dev.golemancyartifice.content.golem.core.status;

public enum CoreRunState {
    RUNNABLE,
    UNCONFIGURED,
    BLOCKED
}
```

```java
// src/main/java/com/klikli_dev/golemancyartifice/content/golem/core/status/CoreDiagnostic.java
package com.klikli_dev.golemancyartifice.content.golem.core.status;

import net.minecraft.network.chat.Component;

public record CoreDiagnostic(Component message) {
}
```

```java
// src/main/java/com/klikli_dev/golemancyartifice/content/golem/core/runtime/ActiveCoreRuntime.java
package com.klikli_dev.golemancyartifice.content.golem.core.runtime;

import com.klikli_dev.golemancyartifice.content.golem.core.status.CoreDiagnostic;
import com.klikli_dev.golemancyartifice.content.golem.core.status.CoreRunState;
import java.util.List;

public interface ActiveCoreRuntime {
    CoreRunState runState();

    List<CoreDiagnostic> diagnostics();
}
```

```java
// src/main/java/com/klikli_dev/golemancyartifice/content/golem/core/runtime/NoCoreRuntime.java
package com.klikli_dev.golemancyartifice.content.golem.core.runtime;

import com.klikli_dev.golemancyartifice.content.golem.core.status.CoreDiagnostic;
import com.klikli_dev.golemancyartifice.content.golem.core.status.CoreRunState;
import java.util.List;

public final class NoCoreRuntime implements ActiveCoreRuntime {
    public static final NoCoreRuntime INSTANCE = new NoCoreRuntime();

    private NoCoreRuntime() {
    }

    @Override
    public CoreRunState runState() {
        return CoreRunState.UNCONFIGURED;
    }

    @Override
    public List<CoreDiagnostic> diagnostics() {
        return List.of();
    }
}
```

```java
// src/main/java/com/klikli_dev/golemancyartifice/content/golem/core/host/GolemCoreHost.java
package com.klikli_dev.golemancyartifice.content.golem.core.host;

import com.klikli_dev.golemancyartifice.content.golem.core.runtime.ActiveCoreRuntime;
import net.minecraft.world.item.ItemStack;

public interface GolemCoreHost {
    ItemStack installedCore();

    ActiveCoreRuntime activeCoreRuntime();
}
```

```java
// src/main/java/com/klikli_dev/golemancyartifice/content/golem/core/CoreDefinition.java
package com.klikli_dev.golemancyartifice.content.golem.core;

import com.klikli_dev.golemancyartifice.content.golem.core.host.GolemCoreHost;
import com.klikli_dev.golemancyartifice.content.golem.core.runtime.ActiveCoreRuntime;
import net.minecraft.world.item.ItemStack;

public interface CoreDefinition<R extends ActiveCoreRuntime> {
    R createRuntime(ItemStack stack, GolemCoreHost host);
}
```

```java
// src/main/java/com/klikli_dev/golemancyartifice/content/golem/core/CoreItem.java
package com.klikli_dev.golemancyartifice.content.golem.core;

import net.minecraft.world.item.Item;

public class CoreItem extends Item {
    private final CoreDefinition<?> definition;

    public CoreItem(Properties properties, CoreDefinition<?> definition) {
        super(properties);
        this.definition = definition;
    }

    public CoreDefinition<?> definition() {
        return this.definition;
    }
}
```

```java
// src/main/java/com/klikli_dev/golemancyartifice/GolemancyArtifice.java
import com.klikli_dev.golemancyartifice.gametest.GolemancyGameTestFunctions;
import com.klikli_dev.golemancyartifice.gametest.GolemancyGameTests;
import net.minecraft.gametest.framework.TestFunctionLoader;

TestFunctionLoader.registerLoader(new GolemancyGameTestFunctions());
modEventBus.addListener(GolemancyGameTests::onRegisterGameTests);
```

- [ ] **Step 4: Run compile to verify the framework contracts now compile**

Run: `./gradlew.bat compileJava`

Expected: FAIL later in the build because `ItemRegistry.INVENTORY_TRANSFER_CORE` still does not exist, but the new contracts compile.

- [ ] **Step 5: Commit the framework scaffolding**

```bash
git add src/main/java/com/klikli_dev/golemancyartifice/GolemancyArtifice.java src/main/java/com/klikli_dev/golemancyartifice/content/golem/core src/main/java/com/klikli_dev/golemancyartifice/gametest
git commit -m "feat: add golem core framework scaffolding"
```

### Task 2: Add data components, the transfer core item, and the thin golem core slot

**Files:**
- Create: `src/main/java/com/klikli_dev/golemancyartifice/registry/DataComponentTypeRegistry.java`
- Create: `src/main/java/com/klikli_dev/golemancyartifice/content/golem/core/slot/GolemCoreSlot.java`
- Create: `src/main/java/com/klikli_dev/golemancyartifice/content/golem/core/transfer/InventoryTransferCoreDefinition.java`
- Create: `src/main/java/com/klikli_dev/golemancyartifice/content/golem/core/transfer/InventoryTransferRuntime.java`
- Modify: `src/main/java/com/klikli_dev/golemancyartifice/registry/ItemRegistry.java`
- Modify: `src/main/java/com/klikli_dev/golemancyartifice/registry/CreativeModeTabRegistry.java`
- Modify: `src/main/java/com/klikli_dev/golemancyartifice/datagen/lang/ENUSLanguageProvider.java`
- Modify: `src/main/java/com/klikli_dev/golemancyartifice/datagen/model/GolemancyArtificeItemModelProvider.java`
- Modify: `src/main/java/com/klikli_dev/golemancyartifice/GolemancyArtifice.java`
- Modify: `src/main/java/com/klikli_dev/golemancyartifice/content/entity/golem/wooden/WoodenGolemEntity.java`
- Modify: `src/main/java/com/klikli_dev/golemancyartifice/gametest/GolemancyGameTestFunctions.java`
- Modify: `src/main/java/com/klikli_dev/golemancyartifice/gametest/GolemancyGameTests.java`

- [ ] **Step 1: Write the failing GameTest for inserting a transfer core into a golem**

```java
// add to GolemancyGameTestFunctions
public static final ResourceKey<Consumer<GameTestHelper>> GOLEM_ACCEPTS_TRANSFER_CORE = ResourceKey.create(
        Registries.TEST_FUNCTION,
        Identifier.fromNamespaceAndPath(GolemancyArtifice.MODID, "golem_accepts_transfer_core")
);

register.accept(GOLEM_ACCEPTS_TRANSFER_CORE, helper -> {
    var golem = helper.spawn(EntityRegistry.WOODEN_GOLEM.get(), 1, 2, 1);
    var coreStack = ItemRegistry.INVENTORY_TRANSFER_CORE.get().getDefaultInstance();

    golem.installCore(coreStack);

    helper.assertTrue(golem.installedCore().is(ItemRegistry.INVENTORY_TRANSFER_CORE.get()), "Installed core stack must match");
    helper.succeed();
});
```

```java
// add to GolemancyGameTests
event.registerTest(
        Identifier.fromNamespaceAndPath(GolemancyArtifice.MODID, "golem_accepts_transfer_core"),
        new FunctionGameTestInstance(
                GolemancyGameTestFunctions.GOLEM_ACCEPTS_TRANSFER_CORE,
                new TestData<>((Holder) environment, Identifier.withDefaultNamespace("empty"), 40, 0, true)
        )
);
```

- [ ] **Step 2: Run GameTests to confirm the new test fails before item and slot support exist**

Run: `./gradlew.bat runGameTestServer`

Expected: FAIL with errors for missing `INVENTORY_TRANSFER_CORE`, `installCore`, or `installedCore` APIs.

- [ ] **Step 3: Register the transfer-core item, its data components, and the golem core slot APIs**

```java
// src/main/java/com/klikli_dev/golemancyartifice/registry/DataComponentTypeRegistry.java
package com.klikli_dev.golemancyartifice.registry;

import com.klikli_dev.golemancyartifice.GolemancyArtifice;
import com.mojang.serialization.Codec;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class DataComponentTypeRegistry {
    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, GolemancyArtifice.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<GlobalPos>> TRANSFER_SOURCE = DATA_COMPONENTS.registerComponentType(
            "transfer_source",
            builder -> builder.persistent(GlobalPos.CODEC).networkSynchronized(GlobalPos.STREAM_CODEC)
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<GlobalPos>> TRANSFER_DESTINATION = DATA_COMPONENTS.registerComponentType(
            "transfer_destination",
            builder -> builder.persistent(GlobalPos.CODEC).networkSynchronized(GlobalPos.STREAM_CODEC)
    );
}
```

```java
// src/main/java/com/klikli_dev/golemancyartifice/content/golem/core/slot/GolemCoreSlot.java
package com.klikli_dev.golemancyartifice.content.golem.core.slot;

import com.klikli_dev.golemancyartifice.content.golem.core.CoreItem;
import net.minecraft.world.item.ItemStack;

public final class GolemCoreSlot {
    private ItemStack stack = ItemStack.EMPTY;

    public ItemStack get() {
        return this.stack;
    }

    public boolean mayPlace(ItemStack candidate) {
        return candidate.getItem() instanceof CoreItem;
    }

    public void set(ItemStack stack) {
        this.stack = stack.copy();
    }

    public ItemStack remove() {
        ItemStack removed = this.stack;
        this.stack = ItemStack.EMPTY;
        return removed;
    }
}
```

```java
// src/main/java/com/klikli_dev/golemancyartifice/content/golem/core/transfer/InventoryTransferRuntime.java
package com.klikli_dev.golemancyartifice.content.golem.core.transfer;

import com.klikli_dev.golemancyartifice.content.golem.core.runtime.ActiveCoreRuntime;
import com.klikli_dev.golemancyartifice.content.golem.core.status.CoreDiagnostic;
import com.klikli_dev.golemancyartifice.content.golem.core.status.CoreRunState;
import java.util.List;
import net.minecraft.core.GlobalPos;

public final class InventoryTransferRuntime implements ActiveCoreRuntime {
    private final GlobalPos source;
    private final GlobalPos destination;
    private final CoreRunState runState;
    private final List<CoreDiagnostic> diagnostics;

    public InventoryTransferRuntime(GlobalPos source, GlobalPos destination, CoreRunState runState, List<CoreDiagnostic> diagnostics) {
        this.source = source;
        this.destination = destination;
        this.runState = runState;
        this.diagnostics = diagnostics;
    }

    @Override
    public CoreRunState runState() {
        return this.runState;
    }

    @Override
    public List<CoreDiagnostic> diagnostics() {
        return this.diagnostics;
    }

    public GlobalPos source() {
        return this.source;
    }

    public GlobalPos destination() {
        return this.destination;
    }
}
```

```java
// src/main/java/com/klikli_dev/golemancyartifice/content/golem/core/transfer/InventoryTransferCoreDefinition.java
package com.klikli_dev.golemancyartifice.content.golem.core.transfer;

import com.klikli_dev.golemancyartifice.content.golem.core.CoreDefinition;
import com.klikli_dev.golemancyartifice.content.golem.core.host.GolemCoreHost;
import com.klikli_dev.golemancyartifice.content.golem.core.status.CoreRunState;
import java.util.List;
import net.minecraft.world.item.ItemStack;

public final class InventoryTransferCoreDefinition implements CoreDefinition<InventoryTransferRuntime> {
    @Override
    public InventoryTransferRuntime createRuntime(ItemStack stack, GolemCoreHost host) {
        return new InventoryTransferRuntime(null, null, CoreRunState.UNCONFIGURED, List.of());
    }
}
```

```java
// src/main/java/com/klikli_dev/golemancyartifice/registry/ItemRegistry.java
public static final DeferredItem<Item> INVENTORY_TRANSFER_CORE = ITEMS.registerItem(
        "inventory_transfer_core",
        properties -> new CoreItem(properties.stacksTo(1), new InventoryTransferCoreDefinition())
);
```

```java
// src/main/java/com/klikli_dev/golemancyartifice/content/entity/golem/wooden/WoodenGolemEntity.java
private final GolemCoreSlot coreSlot = new GolemCoreSlot();

public ItemStack installedCore() {
    return this.coreSlot.get();
}

public void installCore(ItemStack stack) {
    if (!this.coreSlot.mayPlace(stack)) {
        throw new IllegalArgumentException("Only CoreItem stacks may be installed");
    }

    this.coreSlot.set(stack);
}
```

```java
// src/main/java/com/klikli_dev/golemancyartifice/GolemancyArtifice.java
DataComponentTypeRegistry.DATA_COMPONENTS.register(modEventBus);
```

- [ ] **Step 4: Update lang/models and regenerate data**

```java
// ENUSLanguageProvider.addItems()
this.addItem(ItemRegistry.INVENTORY_TRANSFER_CORE, "Inventory Transfer Core");
```

```java
// GolemancyArtificeItemModelProvider.getKnownItems()
return Stream.of(ItemRegistry.WOODEN_GOLEM_SPAWN_EGG.get(), ItemRegistry.INVENTORY_TRANSFER_CORE.get());
```

```java
// GolemancyArtificeItemModelProvider.registerModels()
itemModels.generateFlatItem(ItemRegistry.INVENTORY_TRANSFER_CORE.get(), net.minecraft.client.data.models.model.ModelTemplates.FLAT_ITEM);
```

Run: `./gradlew.bat runData`

Expected: PASS and generated lang/model files updated for the new core item.

- [ ] **Step 5: Run GameTests to confirm the golem now accepts the transfer core**

Run: `./gradlew.bat runGameTestServer`

Expected: PASS for `golemancyartifice:golem_accepts_transfer_core` and existing tests.

- [ ] **Step 6: Commit the core item and slot plumbing**

```bash
git add src/main/java/com/klikli_dev/golemancyartifice/GolemancyArtifice.java src/main/java/com/klikli_dev/golemancyartifice/content/entity/golem/wooden/WoodenGolemEntity.java src/main/java/com/klikli_dev/golemancyartifice/content/golem/core/slot/GolemCoreSlot.java src/main/java/com/klikli_dev/golemancyartifice/content/golem/core/transfer/InventoryTransferCoreDefinition.java src/main/java/com/klikli_dev/golemancyartifice/registry/DataComponentTypeRegistry.java src/main/java/com/klikli_dev/golemancyartifice/registry/ItemRegistry.java src/main/java/com/klikli_dev/golemancyartifice/registry/CreativeModeTabRegistry.java src/main/java/com/klikli_dev/golemancyartifice/datagen/lang/ENUSLanguageProvider.java src/main/java/com/klikli_dev/golemancyartifice/datagen/model/GolemancyArtificeItemModelProvider.java src/generated/resources
git commit -m "feat: add transfer core item and golem core slot"
```

### Task 3: Rebuild the wooden golem around no-core idle mode and typed active runtime

**Files:**
- Modify: `src/main/java/com/klikli_dev/golemancyartifice/content/entity/golem/wooden/WoodenGolemEntity.java`
- Modify: `src/main/java/com/klikli_dev/golemancyartifice/content/entity/golem/wooden/WoodenGolemAi.java`
- Modify: `src/main/java/com/klikli_dev/golemancyartifice/content/golem/core/CoreDefinition.java`
- Modify: `src/main/java/com/klikli_dev/golemancyartifice/content/golem/core/transfer/InventoryTransferCoreDefinition.java`
- Modify: `src/main/java/com/klikli_dev/golemancyartifice/content/golem/core/runtime/NoCoreRuntime.java`
- Modify: `src/main/java/com/klikli_dev/golemancyartifice/gametest/GolemancyGameTestFunctions.java`
- Modify: `src/main/java/com/klikli_dev/golemancyartifice/gametest/GolemancyGameTests.java`

- [ ] **Step 1: Write the failing GameTest for runtime rebuild into an unconfigured transfer runtime**

```java
// add to GolemancyGameTestFunctions
public static final ResourceKey<Consumer<GameTestHelper>> GOLEM_REBUILDS_RUNTIME_FOR_TRANSFER_CORE = ResourceKey.create(
        Registries.TEST_FUNCTION,
        Identifier.fromNamespaceAndPath(GolemancyArtifice.MODID, "golem_rebuilds_runtime_for_transfer_core")
);

register.accept(GOLEM_REBUILDS_RUNTIME_FOR_TRANSFER_CORE, helper -> {
    var golem = helper.spawn(EntityRegistry.WOODEN_GOLEM.get(), 1, 2, 1);

    golem.installCore(ItemRegistry.INVENTORY_TRANSFER_CORE.get().getDefaultInstance());
    golem.refreshInstalledCore();

    helper.assertTrue(golem.activeCoreRuntime() instanceof InventoryTransferRuntime, "Runtime must switch to transfer runtime");
    helper.assertValueEqual(golem.activeCoreRuntime().runState(), CoreRunState.UNCONFIGURED, "run state");
    helper.succeed();
});
```

- [ ] **Step 2: Run GameTests to verify refresh and runtime APIs are still missing**

Run: `./gradlew.bat runGameTestServer`

Expected: FAIL with missing `refreshInstalledCore`, `activeCoreRuntime`, or runtime-state support.

- [ ] **Step 3: Add the thin runtime lifecycle and no-core brain rebuild path**

```java
// CoreDefinition.java
import com.klikli_dev.golemancyartifice.content.entity.golem.wooden.WoodenGolemEntity;
import java.util.List;
import net.minecraft.world.entity.ai.ActivityData;
import net.minecraft.world.entity.ai.sensing.SensorType;

public interface CoreDefinition<R extends ActiveCoreRuntime> {
    R createRuntime(ItemStack stack, GolemCoreHost host);

    List<SensorType<?>> sensorTypes();

    List<ActivityData<WoodenGolemEntity>> activities(R runtime, WoodenGolemEntity entity);
}
```

```java
// WoodenGolemEntity.java
private ActiveCoreRuntime activeCoreRuntime = NoCoreRuntime.INSTANCE;

public ActiveCoreRuntime activeCoreRuntime() {
    return this.activeCoreRuntime;
}

public void refreshInstalledCore() {
    if (this.installedCore().getItem() instanceof CoreItem coreItem) {
        this.activeCoreRuntime = coreItem.definition().createRuntime(this.installedCore(), this);
    } else {
        this.activeCoreRuntime = NoCoreRuntime.INSTANCE;
    }

    this.rebuildBrain();
}

private void rebuildBrain() {
    Brain.Provider<WoodenGolemEntity> provider = Brain.provider(
            List.of(SensorType.NEAREST_LIVING_ENTITIES),
            entity -> this.installedCore().getItem() instanceof CoreItem coreItem
                    ? ((CoreDefinition<ActiveCoreRuntime>) coreItem.definition()).activities(this.activeCoreRuntime, entity)
                    : WoodenGolemAi.noCoreActivities()
    );

    this.brain = provider.makeBrain(this, this.getBrain().pack());
}
```

```java
// WoodenGolemAi.java
public static List<ActivityData<WoodenGolemEntity>> noCoreActivities() {
    return List.of(initCoreActivity(), initIdleActivity());
}

public static void updateActivity(WoodenGolemEntity entity) {
    entity.getBrain().setActiveActivityToFirstValid(ImmutableList.of(Activity.IDLE));
}
```

```java
// InventoryTransferCoreDefinition.java
@Override
public List<SensorType<?>> sensorTypes() {
    return List.of(SensorType.NEAREST_LIVING_ENTITIES);
}

@Override
public List<ActivityData<WoodenGolemEntity>> activities(InventoryTransferRuntime runtime, WoodenGolemEntity entity) {
    return WoodenGolemAi.noCoreActivities();
}
```

- [ ] **Step 4: Run compile and GameTests to verify the thin runtime lifecycle works**

Run: `./gradlew.bat compileJava`

Expected: PASS.

Run: `./gradlew.bat runGameTestServer`

Expected: PASS for `golemancyartifice:golem_rebuilds_runtime_for_transfer_core` while the golem still uses no-core idle activities.

- [ ] **Step 5: Commit the runtime lifecycle refactor**

```bash
git add src/main/java/com/klikli_dev/golemancyartifice/content/entity/golem/wooden/WoodenGolemEntity.java src/main/java/com/klikli_dev/golemancyartifice/content/entity/golem/wooden/WoodenGolemAi.java src/main/java/com/klikli_dev/golemancyartifice/content/golem/core/CoreDefinition.java src/main/java/com/klikli_dev/golemancyartifice/content/golem/core/runtime/NoCoreRuntime.java src/main/java/com/klikli_dev/golemancyartifice/content/golem/core/transfer/InventoryTransferCoreDefinition.java src/main/java/com/klikli_dev/golemancyartifice/gametest
git commit -m "refactor: add thin golem runtime lifecycle"
```

### Task 4: Implement transfer runtime persistence, diagnostics, and item-moving AI

**Files:**
- Create: `src/main/java/com/klikli_dev/golemancyartifice/content/golem/core/transfer/InventoryTransferRuntime.java`
- Create: `src/main/java/com/klikli_dev/golemancyartifice/content/golem/core/transfer/InventoryTransferConfigMapper.java`
- Create: `src/main/java/com/klikli_dev/golemancyartifice/content/golem/core/transfer/InventoryTransferStatusEvaluator.java`
- Create: `src/main/java/com/klikli_dev/golemancyartifice/content/golem/core/transfer/InventoryTransferBehaviorContext.java`
- Create: `src/main/java/com/klikli_dev/golemancyartifice/content/golem/core/transfer/InventoryTransferBrainFactory.java`
- Create: `src/main/java/com/klikli_dev/golemancyartifice/content/golem/core/transfer/InventoryTransferDiagnostics.java`
- Modify: `src/main/java/com/klikli_dev/golemancyartifice/content/golem/core/transfer/InventoryTransferCoreDefinition.java`
- Modify: `src/main/java/com/klikli_dev/golemancyartifice/content/entity/golem/wooden/WoodenGolemEntity.java`
- Modify: `src/main/java/com/klikli_dev/golemancyartifice/gametest/GolemancyGameTestFunctions.java`
- Modify: `src/main/java/com/klikli_dev/golemancyartifice/gametest/GolemancyGameTests.java`

- [ ] **Step 1: Write the failing GameTests for unconfigured diagnostics and real inventory transfer**

```java
// add to GolemancyGameTestFunctions
public static final ResourceKey<Consumer<GameTestHelper>> TRANSFER_CORE_REPORTS_UNCONFIGURED = ResourceKey.create(
        Registries.TEST_FUNCTION,
        Identifier.fromNamespaceAndPath(GolemancyArtifice.MODID, "transfer_core_reports_unconfigured")
);

public static final ResourceKey<Consumer<GameTestHelper>> TRANSFER_CORE_MOVES_ITEM_BETWEEN_CHESTS = ResourceKey.create(
        Registries.TEST_FUNCTION,
        Identifier.fromNamespaceAndPath(GolemancyArtifice.MODID, "transfer_core_moves_item_between_chests")
);

register.accept(TRANSFER_CORE_REPORTS_UNCONFIGURED, helper -> {
    var golem = helper.spawn(EntityRegistry.WOODEN_GOLEM.get(), 2, 2, 2);
    golem.installCore(ItemRegistry.INVENTORY_TRANSFER_CORE.toStack());
    golem.refreshInstalledCore();

    helper.assertValueEqual(golem.activeCoreRuntime().runState(), CoreRunState.UNCONFIGURED, "run state");
    helper.assertFalse(golem.activeCoreRuntime().diagnostics().isEmpty(), "Unconfigured runtime must expose diagnostics");
    helper.succeed();
});

register.accept(TRANSFER_CORE_MOVES_ITEM_BETWEEN_CHESTS, helper -> {
    helper.setBlock(1, 1, 1, Blocks.CHEST);
    helper.setBlock(4, 1, 1, Blocks.CHEST);
    helper.getBlockEntity(new BlockPos(1, 1, 1), ChestBlockEntity.class).setItem(0, new ItemStack(Items.COBBLESTONE));

    var coreStack = ItemRegistry.INVENTORY_TRANSFER_CORE.toStack();
    coreStack.set(DataComponentTypeRegistry.TRANSFER_SOURCE.get(), GlobalPos.of(helper.getLevel().dimension(), helper.absolutePos(new BlockPos(1, 1, 1))));
    coreStack.set(DataComponentTypeRegistry.TRANSFER_DESTINATION.get(), GlobalPos.of(helper.getLevel().dimension(), helper.absolutePos(new BlockPos(4, 1, 1))));

    var golem = helper.spawn(EntityRegistry.WOODEN_GOLEM.get(), 2, 2, 3);
    golem.installCore(coreStack);
    golem.refreshInstalledCore();

    helper.succeedWhen(() -> {
        helper.assertContainerEmpty(new BlockPos(1, 1, 1));
        helper.assertContainerContainsSingle(new BlockPos(4, 1, 1), Items.COBBLESTONE);
    });
});
```

- [ ] **Step 2: Run GameTests to confirm diagnostics and transport logic are not implemented yet**

Run: `./gradlew.bat runGameTestServer`

Expected: FAIL because the transfer runtime still has no config mapper, no status evaluation, and no item-moving behaviors.

- [ ] **Step 3: Implement the transfer runtime, mapper, diagnostics, and AI package using composition**

```java
// src/main/java/com/klikli_dev/golemancyartifice/content/golem/core/transfer/InventoryTransferRuntime.java
package com.klikli_dev.golemancyartifice.content.golem.core.transfer;

import com.klikli_dev.golemancyartifice.content.golem.core.runtime.ActiveCoreRuntime;
import com.klikli_dev.golemancyartifice.content.golem.core.status.CoreDiagnostic;
import com.klikli_dev.golemancyartifice.content.golem.core.status.CoreRunState;
import java.util.List;
import net.minecraft.core.GlobalPos;

public final class InventoryTransferRuntime implements ActiveCoreRuntime {
    private GlobalPos source;
    private GlobalPos destination;
    private CoreRunState runState;
    private List<CoreDiagnostic> diagnostics;

    public InventoryTransferRuntime(GlobalPos source, GlobalPos destination, CoreRunState runState, List<CoreDiagnostic> diagnostics) {
        this.source = source;
        this.destination = destination;
        this.runState = runState;
        this.diagnostics = diagnostics;
    }

    public GlobalPos source() {
        return this.source;
    }

    public GlobalPos destination() {
        return this.destination;
    }

    @Override
    public CoreRunState runState() {
        return this.runState;
    }

    @Override
    public List<CoreDiagnostic> diagnostics() {
        return this.diagnostics;
    }
}
```

```java
// src/main/java/com/klikli_dev/golemancyartifice/content/golem/core/transfer/InventoryTransferConfigMapper.java
package com.klikli_dev.golemancyartifice.content.golem.core.transfer;

import com.klikli_dev.golemancyartifice.content.golem.core.host.GolemCoreHost;
import com.klikli_dev.golemancyartifice.content.golem.core.status.CoreDiagnostic;
import com.klikli_dev.golemancyartifice.content.golem.core.status.CoreRunState;
import com.klikli_dev.golemancyartifice.registry.DataComponentTypeRegistry;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public final class InventoryTransferConfigMapper {
    public InventoryTransferRuntime load(ItemStack stack, GolemCoreHost host) {
        GlobalPos source = stack.get(DataComponentTypeRegistry.TRANSFER_SOURCE.get());
        GlobalPos destination = stack.get(DataComponentTypeRegistry.TRANSFER_DESTINATION.get());
        List<CoreDiagnostic> diagnostics = new ArrayList<>();

        if (source == null) {
            diagnostics.add(new CoreDiagnostic(Component.literal("Missing source inventory")));
        }

        if (destination == null) {
            diagnostics.add(new CoreDiagnostic(Component.literal("Missing destination inventory")));
        }

        CoreRunState runState = diagnostics.isEmpty() ? CoreRunState.RUNNABLE : CoreRunState.UNCONFIGURED;
        return new InventoryTransferRuntime(source, destination, runState, List.copyOf(diagnostics));
    }

    public ItemStack save(ItemStack stack, InventoryTransferRuntime runtime) {
        if (runtime.source() != null) {
            stack.set(DataComponentTypeRegistry.TRANSFER_SOURCE.get(), runtime.source());
        }

        if (runtime.destination() != null) {
            stack.set(DataComponentTypeRegistry.TRANSFER_DESTINATION.get(), runtime.destination());
        }

        return stack;
    }
}
```

```java
// src/main/java/com/klikli_dev/golemancyartifice/content/golem/core/transfer/behavior/TransferConfiguredInventoryItemsBehavior.java
package com.klikli_dev.golemancyartifice.content.golem.core.transfer.behavior;

import com.klikli_dev.golemancyartifice.content.entity.golem.wooden.WoodenGolemEntity;
import com.klikli_dev.golemancyartifice.content.golem.core.transfer.InventoryTransferBehaviorContext;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.item.ItemStack;

public final class TransferConfiguredInventoryItemsBehavior extends Behavior<WoodenGolemEntity> {
    private final InventoryTransferBehaviorContext context;

    public TransferConfiguredInventoryItemsBehavior(InventoryTransferBehaviorContext context) {
        super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED), 40, 40);
        this.context = context;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, WoodenGolemEntity body) {
        return this.context.runtime().runState() == CoreRunState.RUNNABLE;
    }

    @Override
    protected void start(ServerLevel level, WoodenGolemEntity body, long gameTime) {
        Container source = this.context.sourceContainer(level);
        Container destination = this.context.destinationContainer(level);
        if (source == null || destination == null) {
            return;
        }

        for (int slot = 0; slot < source.getContainerSize(); slot++) {
            ItemStack extracted = source.getItem(slot);
            if (!extracted.isEmpty()) {
                ItemStack single = extracted.copyWithCount(1);
                if (destination.canAddItem(single)) {
                    source.removeItem(slot, 1);
                    destination.addItem(single);
                    source.setChanged();
                    destination.setChanged();
                }
                return;
            }
        }
    }
}
```

```java
// src/main/java/com/klikli_dev/golemancyartifice/content/golem/core/transfer/InventoryTransferBehaviorContext.java
package com.klikli_dev.golemancyartifice.content.golem.core.transfer;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;

public record InventoryTransferBehaviorContext(InventoryTransferRuntime runtime) {
    public Container sourceContainer(ServerLevel level) {
        if (this.runtime.source() == null || this.runtime.source().dimension() != level.dimension()) {
            return null;
        }
        return level.getBlockEntity(this.runtime.source().pos()) instanceof BaseContainerBlockEntity container ? container : null;
    }

    public Container destinationContainer(ServerLevel level) {
        if (this.runtime.destination() == null || this.runtime.destination().dimension() != level.dimension()) {
            return null;
        }
        return level.getBlockEntity(this.runtime.destination().pos()) instanceof BaseContainerBlockEntity container ? container : null;
    }
}
```

```java
// src/main/java/com/klikli_dev/golemancyartifice/content/golem/core/transfer/InventoryTransferBrainFactory.java
package com.klikli_dev.golemancyartifice.content.golem.core.transfer;

import com.google.common.collect.ImmutableList;
import com.klikli_dev.golemancyartifice.content.entity.golem.wooden.WoodenGolemAi;
import com.klikli_dev.golemancyartifice.content.entity.golem.wooden.WoodenGolemEntity;
import com.klikli_dev.golemancyartifice.content.golem.core.status.CoreRunState;
import com.klikli_dev.golemancyartifice.content.golem.core.transfer.behavior.TransferConfiguredInventoryItemsBehavior;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import java.util.List;
import net.minecraft.world.entity.ai.ActivityData;
import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
import net.minecraft.world.entity.schedule.Activity;

public final class InventoryTransferBrainFactory {
    public List<ActivityData<WoodenGolemEntity>> create(InventoryTransferRuntime runtime) {
        if (runtime.runState() != CoreRunState.RUNNABLE) {
            return WoodenGolemAi.noCoreActivities();
        }

        InventoryTransferBehaviorContext context = new InventoryTransferBehaviorContext(runtime);

        return List.of(
                ActivityData.create(Activity.CORE, 0, ImmutableList.of(new LookAtTargetSink(45, 90), new MoveToTargetSink())),
                ActivityData.create(Activity.IDLE, ImmutableList.of(
                        Pair.of(0, new TransferConfiguredInventoryItemsBehavior(context))
                ))
        );
    }
}
```

```java
// InventoryTransferCoreDefinition.java
private final InventoryTransferConfigMapper configMapper = new InventoryTransferConfigMapper();
private final InventoryTransferBrainFactory brainFactory = new InventoryTransferBrainFactory();

@Override
public InventoryTransferRuntime createRuntime(ItemStack stack, GolemCoreHost host) {
    return this.configMapper.load(stack, host);
}

@Override
public List<ActivityData<WoodenGolemEntity>> activities(InventoryTransferRuntime runtime, WoodenGolemEntity entity) {
    return this.brainFactory.create(runtime);
}
```

- [ ] **Step 4: Run compile and GameTests to verify the first core really moves items**

Run: `./gradlew.bat compileJava`

Expected: PASS.

Run: `./gradlew.bat runGameTestServer`

Expected: PASS for:
- `golemancyartifice:transfer_core_reports_unconfigured`
- `golemancyartifice:transfer_core_moves_item_between_chests`

- [ ] **Step 5: Commit the first real inventory-transfer core**

```bash
git add src/main/java/com/klikli_dev/golemancyartifice/content/golem/core/transfer src/main/java/com/klikli_dev/golemancyartifice/content/entity/golem/wooden/WoodenGolemEntity.java src/main/java/com/klikli_dev/golemancyartifice/gametest
git commit -m "feat: add inventory transfer core runtime and ai"
```

### Task 5: Add coarse status sync and above-head problem indicators

**Files:**
- Create: `src/main/java/com/klikli_dev/golemancyartifice/client/render/GolemCoreStatusParticles.java`
- Modify: `src/main/java/com/klikli_dev/golemancyartifice/GolemancyArtifice.java`
- Modify: `src/main/java/com/klikli_dev/golemancyartifice/GolemancyArtificeClient.java`
- Modify: `src/main/java/com/klikli_dev/golemancyartifice/content/entity/golem/wooden/WoodenGolemEntity.java`
- Modify: `src/main/java/com/klikli_dev/golemancyartifice/datagen/lang/ENUSLanguageProvider.java`
- Modify: `src/main/java/com/klikli_dev/golemancyartifice/gametest/GolemancyGameTestFunctions.java`
- Modify: `src/main/java/com/klikli_dev/golemancyartifice/gametest/GolemancyGameTests.java`

- [ ] **Step 1: Write the failing GameTest for server-side coarse status transitions**

```java
// add to GolemancyGameTestFunctions
public static final ResourceKey<Consumer<GameTestHelper>> TRANSFER_CORE_REPORTS_BLOCKED_WHEN_DESTINATION_IS_FULL = ResourceKey.create(
        Registries.TEST_FUNCTION,
        Identifier.fromNamespaceAndPath(GolemancyArtifice.MODID, "transfer_core_reports_blocked_when_destination_is_full")
);

register.accept(TRANSFER_CORE_REPORTS_BLOCKED_WHEN_DESTINATION_IS_FULL, helper -> {
    helper.setBlock(1, 1, 1, Blocks.CHEST);
    helper.setBlock(4, 1, 1, Blocks.CHEST);

    var sourceChest = helper.getBlockEntity(new BlockPos(1, 1, 1), ChestBlockEntity.class);
    var destinationChest = helper.getBlockEntity(new BlockPos(4, 1, 1), ChestBlockEntity.class);
    sourceChest.setItem(0, new ItemStack(Items.COBBLESTONE));

    for (int slot = 0; slot < destinationChest.getContainerSize(); slot++) {
        destinationChest.setItem(slot, new ItemStack(Items.DIRT, destinationChest.getMaxStackSize()));
    }

    var coreStack = ItemRegistry.INVENTORY_TRANSFER_CORE.toStack();
    coreStack.set(DataComponentTypeRegistry.TRANSFER_SOURCE.get(), GlobalPos.of(helper.getLevel().dimension(), helper.absolutePos(new BlockPos(1, 1, 1))));
    coreStack.set(DataComponentTypeRegistry.TRANSFER_DESTINATION.get(), GlobalPos.of(helper.getLevel().dimension(), helper.absolutePos(new BlockPos(4, 1, 1))));

    var golem = helper.spawn(EntityRegistry.WOODEN_GOLEM.get(), 2, 2, 3);
    golem.installCore(coreStack);
    golem.refreshInstalledCore();

    helper.succeedWhen(() -> helper.assertValueEqual(golem.currentCoreRunState(), CoreRunState.BLOCKED, "run state"));
});
```

- [ ] **Step 2: Run GameTests to confirm blocked-state reporting is missing**

Run: `./gradlew.bat runGameTestServer`

Expected: FAIL because the transfer core does not yet recompute blocked state or expose a coarse status accessor.

- [ ] **Step 3: Add coarse status recomputation, synced entity state, and the client renderer hook**

```java
// WoodenGolemEntity.java
private static final EntityDataAccessor<Integer> DATA_CORE_RUN_STATE = SynchedEntityData.defineId(WoodenGolemEntity.class, EntityDataSerializers.INT);

public CoreRunState currentCoreRunState() {
    return CoreRunState.values()[this.entityData.get(DATA_CORE_RUN_STATE)];
}

private void setCurrentCoreRunState(CoreRunState state) {
    this.entityData.set(DATA_CORE_RUN_STATE, state.ordinal());
}

@Override
protected void defineSynchedData(SynchedEntityData.Builder entityData) {
    super.defineSynchedData(entityData);
    entityData.define(DATA_CORE_RUN_STATE, CoreRunState.UNCONFIGURED.ordinal());
}
```

```java
// GolemCoreStatusParticles.java
package com.klikli_dev.golemancyartifice.client.render;

import com.klikli_dev.golemancyartifice.content.entity.golem.wooden.WoodenGolemEntity;
import com.klikli_dev.golemancyartifice.content.golem.core.status.CoreRunState;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.DustParticleOptions;
import org.joml.Vector3f;

public final class GolemCoreStatusParticles {
    private static final DustParticleOptions UNCONFIGURED = new DustParticleOptions(new Vector3f(1.0F, 0.0F, 0.0F), 1.0F);
    private static final DustParticleOptions BLOCKED = new DustParticleOptions(new Vector3f(1.0F, 0.5F, 0.0F), 1.0F);

    public void tick(WoodenGolemEntity entity) {
        if (!(entity.level() instanceof ClientLevel level) || entity.tickCount % 10 != 0) {
            return;
        }

        DustParticleOptions particle = switch (entity.currentCoreRunState()) {
            case UNCONFIGURED -> UNCONFIGURED;
            case BLOCKED -> BLOCKED;
            default -> null;
        };

        if (particle != null) {
            level.addParticle(particle, entity.getX(), entity.getY() + 1.4D, entity.getZ(), 0.0D, 0.02D, 0.0D);
        }
    }
}
```

- [ ] **Step 4: Run GameTests and then do a manual client validation pass for the red/orange indicator**

Run: `./gradlew.bat runGameTestServer`

Expected: PASS for `golemancyartifice:transfer_core_reports_blocked_when_destination_is_full`.

Run: `./gradlew.bat runClient`

Expected manual result:
- an unconfigured installed transfer core emits a red dust effect above the golem head
- a valid but blocked transfer core emits an orange dust effect above the golem head
- a runnable transfer core shows no indicator

- [ ] **Step 5: Commit the status transport and indicator work**

```bash
git add src/main/java/com/klikli_dev/golemancyartifice/client/render src/main/java/com/klikli_dev/golemancyartifice/content/entity/golem/wooden src/main/java/com/klikli_dev/golemancyartifice/GolemancyArtifice.java src/main/java/com/klikli_dev/golemancyartifice/GolemancyArtificeClient.java src/main/java/com/klikli_dev/golemancyartifice/gametest
git commit -m "feat: add golem core status feedback"
```

### Task 6: Add the binding tool, per-core actions, and attached-core live reconfiguration

**Files:**
- Create: `src/main/java/com/klikli_dev/golemancyartifice/content/item/GolemBindingToolItem.java`
- Create: `src/main/java/com/klikli_dev/golemancyartifice/content/menu/GolemBindingToolMenu.java`
- Create: `src/main/java/com/klikli_dev/golemancyartifice/registry/MenuRegistry.java`
- Create: `src/main/java/com/klikli_dev/golemancyartifice/network/payload/ServerboundSelectBindingActionPayload.java`
- Create: `src/main/java/com/klikli_dev/golemancyartifice/network/payload/ServerboundApplyBindingTargetPayload.java`
- Create: `src/main/java/com/klikli_dev/golemancyartifice/client/screen/GolemBindingToolScreen.java`
- Modify: `src/main/java/com/klikli_dev/golemancyartifice/content/golem/core/CoreDefinition.java`
- Modify: `src/main/java/com/klikli_dev/golemancyartifice/content/golem/core/transfer/InventoryTransferCoreDefinition.java`
- Modify: `src/main/java/com/klikli_dev/golemancyartifice/registry/ItemRegistry.java`
- Modify: `src/main/java/com/klikli_dev/golemancyartifice/registry/CreativeModeTabRegistry.java`
- Modify: `src/main/java/com/klikli_dev/golemancyartifice/datagen/lang/ENUSLanguageProvider.java`
- Modify: `src/main/java/com/klikli_dev/golemancyartifice/datagen/model/GolemancyArtificeItemModelProvider.java`
- Modify: `src/main/java/com/klikli_dev/golemancyartifice/GolemancyArtifice.java`
- Modify: `src/main/java/com/klikli_dev/golemancyartifice/GolemancyArtificeClient.java`
- Modify: `src/main/java/com/klikli_dev/golemancyartifice/content/entity/golem/wooden/WoodenGolemEntity.java`
- Modify: `src/main/java/com/klikli_dev/golemancyartifice/gametest/GolemancyGameTestFunctions.java`
- Modify: `src/main/java/com/klikli_dev/golemancyartifice/gametest/GolemancyGameTests.java`

- [ ] **Step 1: Write the failing GameTest for attached-core rebinding of source and destination**

```java
// add to GolemancyGameTestFunctions
public static final ResourceKey<Consumer<GameTestHelper>> BINDING_TOOL_RECONFIGURES_ATTACHED_TRANSFER_CORE = ResourceKey.create(
        Registries.TEST_FUNCTION,
        Identifier.fromNamespaceAndPath(GolemancyArtifice.MODID, "binding_tool_reconfigures_attached_transfer_core")
);

register.accept(BINDING_TOOL_RECONFIGURES_ATTACHED_TRANSFER_CORE, helper -> {
    var golem = helper.spawn(EntityRegistry.WOODEN_GOLEM.get(), 2, 2, 2);
    var coreStack = ItemRegistry.INVENTORY_TRANSFER_CORE.get().getDefaultInstance();

    golem.installCore(coreStack);
    golem.refreshInstalledCore();

    InventoryTransferCoreDefinition definition = (InventoryTransferCoreDefinition) ((CoreItem) golem.installedCore().getItem()).definition();
    golem.reconfigureInstalledCore(stack -> definition.applyBindingAction(stack, "set_source", helper.absolutePos(new BlockPos(1, 1, 1)), golem));
    golem.reconfigureInstalledCore(stack -> definition.applyBindingAction(stack, "set_destination", helper.absolutePos(new BlockPos(4, 1, 1)), golem));

    helper.assertTrue(golem.installedCore().has(DataComponentTypeRegistry.TRANSFER_SOURCE.get()), "Attached binding must mutate installed item first");
    helper.assertTrue(golem.installedCore().has(DataComponentTypeRegistry.TRANSFER_DESTINATION.get()), "Attached binding must mutate installed item first");
    helper.assertValueEqual(golem.activeCoreRuntime().runState(), CoreRunState.RUNNABLE, "run state");
    helper.succeed();
});
```

- [ ] **Step 2: Run GameTests to confirm the binding-action surface does not exist yet**

Run: `./gradlew.bat runGameTestServer`

Expected: FAIL because no binding tool, binding actions, or attached-core reconfiguration flow exists.

- [ ] **Step 3: Add the binding tool menu, core-defined actions, and attached-core rebuild flow**

```java
// CoreDefinition.java
import java.util.List;
import net.minecraft.core.BlockPos;

public interface CoreDefinition<R extends ActiveCoreRuntime> {
    R createRuntime(ItemStack stack, GolemCoreHost host);

    List<SensorType<?>> sensorTypes();

    List<ActivityData<WoodenGolemEntity>> activities(R runtime, WoodenGolemEntity entity);

    List<String> bindingActions();

    ItemStack applyBindingAction(ItemStack stack, String action, BlockPos target, GolemCoreHost host);
}
```

```java
// GolemBindingToolItem.java
package com.klikli_dev.golemancyartifice.content.item;

import net.minecraft.world.item.Item;

public class GolemBindingToolItem extends Item {
    public GolemBindingToolItem(Properties properties) {
        super(properties.stacksTo(1));
    }
}
```

```java
// InventoryTransferCoreDefinition.java
@Override
public List<String> bindingActions() {
    return List.of("set_source", "set_destination");
}

@Override
public ItemStack applyBindingAction(ItemStack stack, String action, BlockPos target, GolemCoreHost host) {
    GlobalPos globalPos = GlobalPos.of(((WoodenGolemEntity) host).level().dimension(), target);
    if (action.equals("set_source")) {
        stack.set(DataComponentTypeRegistry.TRANSFER_SOURCE.get(), globalPos);
    }

    if (action.equals("set_destination")) {
        stack.set(DataComponentTypeRegistry.TRANSFER_DESTINATION.get(), globalPos);
    }

    return stack;
}
```

```java
// WoodenGolemEntity.java
public void reconfigureInstalledCore(UnaryOperator<ItemStack> editor) {
    ItemStack edited = editor.apply(this.installedCore().copy());
    this.installCore(edited);
    this.refreshInstalledCore();
}
```

```java
// ItemRegistry.java
public static final DeferredItem<Item> GOLEM_BINDING_TOOL = ITEMS.registerItem(
        "golem_binding_tool",
        GolemBindingToolItem::new
);
```

- [ ] **Step 4: Regenerate data, run GameTests, and manually validate the tool UI in client**

Run: `./gradlew.bat runData`

Expected: PASS with generated assets for `golem_binding_tool`.

Run: `./gradlew.bat runGameTestServer`

Expected: PASS for `golemancyartifice:binding_tool_reconfigures_attached_transfer_core`.

Run: `./gradlew.bat runClient`

Expected manual result:
- opening the binding tool UI on an installed transfer core shows `set_source` and `set_destination`
- selecting a target updates the installed core item immediately
- the golem refreshes into the new runtime without manual removal and reinsertion

- [ ] **Step 5: Commit the binding tool and attached-core reconfiguration path**

```bash
git add src/main/java/com/klikli_dev/golemancyartifice/content/item/GolemBindingToolItem.java src/main/java/com/klikli_dev/golemancyartifice/content/menu src/main/java/com/klikli_dev/golemancyartifice/client/screen src/main/java/com/klikli_dev/golemancyartifice/network src/main/java/com/klikli_dev/golemancyartifice/registry/MenuRegistry.java src/main/java/com/klikli_dev/golemancyartifice/registry/ItemRegistry.java src/main/java/com/klikli_dev/golemancyartifice/registry/CreativeModeTabRegistry.java src/main/java/com/klikli_dev/golemancyartifice/datagen/lang/ENUSLanguageProvider.java src/main/java/com/klikli_dev/golemancyartifice/datagen/model/GolemancyArtificeItemModelProvider.java src/generated/resources src/main/java/com/klikli_dev/golemancyartifice/content/golem/core/CoreDefinition.java src/main/java/com/klikli_dev/golemancyartifice/content/golem/core/transfer/InventoryTransferCoreDefinition.java src/main/java/com/klikli_dev/golemancyartifice/content/entity/golem/wooden/WoodenGolemEntity.java src/main/java/com/klikli_dev/golemancyartifice/gametest
git commit -m "feat: add golem core binding tool"
```

### Task 7: Final validation, cleanup, and documentation pass

**Files:**
- Modify: `docs/specs/2026-05-02-golem-core-architecture-design.md` (only if implementation reveals a spec drift)
- Modify: `README.md` (only if you want a short developer note about running GameTests)

- [ ] **Step 1: Run the full validation suite used by this mod**

Run: `./gradlew.bat compileJava`

Expected: PASS.

Run: `./gradlew.bat runData`

Expected: PASS.

Run: `./gradlew.bat runGameTestServer`

Expected: PASS with all GameTests green.

- [ ] **Step 2: Run a final manual gameplay sanity check in the client**

Run: `./gradlew.bat runClient`

Expected manual result:
- wooden golem spawns and idles with no core
- transfer core can be installed and removed
- transfer core moves items from source inventory to destination inventory
- unconfigured state shows red effect
- blocked state shows orange effect
- binding tool can reconfigure an attached core

- [ ] **Step 3: Commit any final cleanup or docs updates**

```bash
git add README.md docs/specs/2026-05-02-golem-core-architecture-design.md
git commit -m "docs: finalize golem core implementation notes"
```
