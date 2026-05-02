// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.golemancyartifice.content.golem.core.transfer;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.level.block.entity.HopperBlockEntity;

public record InventoryTransferBehaviorContext(InventoryTransferRuntime runtime) {
    public Container sourceContainer(ServerLevel level) {
        if (this.runtime.source() == null || this.runtime.source().dimension() != level.dimension()) {
            return null;
        }

        return HopperBlockEntity.getContainerAt(level, this.runtime.source().pos());
    }

    public Container destinationContainer(ServerLevel level) {
        if (this.runtime.destination() == null || this.runtime.destination().dimension() != level.dimension()) {
            return null;
        }

        return HopperBlockEntity.getContainerAt(level, this.runtime.destination().pos());
    }
}
