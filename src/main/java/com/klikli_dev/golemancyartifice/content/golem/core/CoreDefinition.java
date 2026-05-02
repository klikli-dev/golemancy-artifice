// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.golemancyartifice.content.golem.core;

import com.klikli_dev.golemancyartifice.content.entity.golem.wooden.WoodenGolemEntity;
import com.klikli_dev.golemancyartifice.content.golem.core.host.GolemCoreHost;
import com.klikli_dev.golemancyartifice.content.golem.core.runtime.ActiveCoreRuntime;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.ActivityData;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.item.ItemStack;

public interface CoreDefinition<R extends ActiveCoreRuntime> {
    R createRuntime(ItemStack stack, GolemCoreHost host);

    List<SensorType<?>> sensorTypes();

    List<ActivityData<WoodenGolemEntity>> activities(R runtime, WoodenGolemEntity entity);

    List<String> bindingActions();

    ItemStack applyBindingAction(ItemStack stack, String action, BlockPos target, GolemCoreHost host);
}
