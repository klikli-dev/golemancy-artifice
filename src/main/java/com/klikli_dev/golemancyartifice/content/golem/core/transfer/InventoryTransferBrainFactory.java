// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.golemancyartifice.content.golem.core.transfer;

import com.google.common.collect.ImmutableList;
import com.klikli_dev.golemancyartifice.content.entity.golem.wooden.WoodenGolemAi;
import com.klikli_dev.golemancyartifice.content.entity.golem.wooden.WoodenGolemEntity;
import com.klikli_dev.golemancyartifice.content.golem.core.status.CoreRunState;
import com.klikli_dev.golemancyartifice.content.golem.core.transfer.behavior.TransferConfiguredInventoryItemsBehavior;
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
