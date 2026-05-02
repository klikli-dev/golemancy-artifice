// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.golemancyartifice.content.golem.core.transfer;

import com.klikli_dev.golemancyartifice.content.golem.core.status.CoreDiagnostic;
import com.klikli_dev.golemancyartifice.content.golem.core.status.CoreRunState;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

public final class InventoryTransferStatusEvaluator {
    public InventoryTransferRuntime evaluate(InventoryTransferRuntime runtime, ServerLevel level) {
        List<CoreDiagnostic> diagnostics = new ArrayList<>(runtime.diagnostics());
        if (runtime.runState() == CoreRunState.UNCONFIGURED) {
            return new InventoryTransferRuntime(runtime.source(), runtime.destination(), CoreRunState.UNCONFIGURED, List.copyOf(diagnostics));
        }

        InventoryTransferBehaviorContext context = new InventoryTransferBehaviorContext(runtime);
        Container source = context.sourceContainer(level);
        Container destination = context.destinationContainer(level);

        if (source == null) {
            diagnostics.add(new CoreDiagnostic(Component.literal("Source inventory is unavailable")));
        }

        if (destination == null) {
            diagnostics.add(new CoreDiagnostic(Component.literal("Destination inventory is unavailable")));
        }

        if (!diagnostics.isEmpty()) {
            return new InventoryTransferRuntime(runtime.source(), runtime.destination(), CoreRunState.BLOCKED, List.copyOf(diagnostics));
        }

        if (!hasMovableItem(source)) {
            diagnostics.add(new CoreDiagnostic(Component.literal("Source inventory has no movable items")));
            return new InventoryTransferRuntime(runtime.source(), runtime.destination(), CoreRunState.BLOCKED, List.copyOf(diagnostics));
        }

        if (!canInsertAny(source, destination)) {
            diagnostics.add(new CoreDiagnostic(Component.literal("Destination inventory is full")));
            return new InventoryTransferRuntime(runtime.source(), runtime.destination(), CoreRunState.BLOCKED, List.copyOf(diagnostics));
        }

        return new InventoryTransferRuntime(runtime.source(), runtime.destination(), CoreRunState.RUNNABLE, List.of());
    }

    private boolean hasMovableItem(Container source) {
        for (int slot = 0; slot < source.getContainerSize(); slot++) {
            if (!source.getItem(slot).isEmpty()) {
                return true;
            }
        }

        return false;
    }

    private boolean canInsertAny(Container source, Container destination) {
        for (int slot = 0; slot < source.getContainerSize(); slot++) {
            ItemStack stack = source.getItem(slot);
            if (stack.isEmpty()) {
                continue;
            }

            ItemStack remainder = tryInsertCopy(destination, stack.copyWithCount(1));
            if (remainder.isEmpty()) {
                return true;
            }
        }

        return false;
    }

    private ItemStack tryInsertCopy(Container destination, ItemStack stack) {
        ItemStack remainder = stack;
        for (int slot = 0; slot < destination.getContainerSize() && !remainder.isEmpty(); slot++) {
            if (!destination.canPlaceItem(slot, remainder)) {
                continue;
            }

            ItemStack existing = destination.getItem(slot);
            if (existing.isEmpty()) {
                return ItemStack.EMPTY;
            }

            if (!ItemStack.isSameItemSameComponents(existing, remainder)) {
                continue;
            }

            int max = Math.min(destination.getMaxStackSize(existing), existing.getMaxStackSize());
            if (existing.getCount() < max) {
                return ItemStack.EMPTY;
            }
        }

        return remainder;
    }
}
