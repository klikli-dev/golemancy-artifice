// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.golemancyartifice.content.golem.core.transfer.behavior;

import com.google.common.collect.ImmutableMap;
import com.klikli_dev.golemancyartifice.content.entity.golem.wooden.WoodenGolemEntity;
import com.klikli_dev.golemancyartifice.content.golem.core.status.CoreRunState;
import com.klikli_dev.golemancyartifice.content.golem.core.transfer.InventoryTransferBehaviorContext;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.HopperBlockEntity;

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
            if (extracted.isEmpty()) {
                continue;
            }

            ItemStack remainder = HopperBlockEntity.addItem(source, destination, extracted.copyWithCount(1), null);
            if (remainder.isEmpty()) {
                source.removeItem(slot, 1);
                source.setChanged();
                destination.setChanged();
            }
            return;
        }
    }
}
