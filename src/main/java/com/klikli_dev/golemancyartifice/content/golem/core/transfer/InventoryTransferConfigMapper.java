// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

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
