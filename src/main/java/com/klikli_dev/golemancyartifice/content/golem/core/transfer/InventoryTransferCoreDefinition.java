// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.golemancyartifice.content.golem.core.transfer;

import com.klikli_dev.golemancyartifice.content.entity.golem.wooden.WoodenGolemAi;
import com.klikli_dev.golemancyartifice.content.entity.golem.wooden.WoodenGolemEntity;
import com.klikli_dev.golemancyartifice.content.golem.core.CoreDefinition;
import com.klikli_dev.golemancyartifice.content.golem.core.host.GolemCoreHost;
import com.klikli_dev.golemancyartifice.registry.DataComponentTypeRegistry;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.entity.ai.ActivityData;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.item.ItemStack;

public final class InventoryTransferCoreDefinition implements CoreDefinition<InventoryTransferRuntime> {
    private final InventoryTransferConfigMapper configMapper = new InventoryTransferConfigMapper();
    private final InventoryTransferBrainFactory brainFactory = new InventoryTransferBrainFactory();

    @Override
    public InventoryTransferRuntime createRuntime(ItemStack stack, GolemCoreHost host) {
        return this.configMapper.load(stack, host);
    }

    @Override
    public List<SensorType<?>> sensorTypes() {
        return List.of(SensorType.NEAREST_LIVING_ENTITIES);
    }

    @Override
    public List<ActivityData<WoodenGolemEntity>> activities(InventoryTransferRuntime runtime, WoodenGolemEntity entity) {
        return this.brainFactory.create(runtime);
    }

    @Override
    public List<String> bindingActions() {
        return List.of("set_source", "set_destination");
    }

    @Override
    public ItemStack applyBindingAction(ItemStack stack, String action, BlockPos target, GolemCoreHost host) {
        if (!(host instanceof WoodenGolemEntity golem)) {
            return stack;
        }

        GlobalPos globalPos = GlobalPos.of(golem.level().dimension(), target);
        if ("set_source".equals(action)) {
            stack.set(DataComponentTypeRegistry.TRANSFER_SOURCE.get(), globalPos);
        }

        if ("set_destination".equals(action)) {
            stack.set(DataComponentTypeRegistry.TRANSFER_DESTINATION.get(), globalPos);
        }

        return stack;
    }
}
