// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.golemancyartifice.content.entity.golem.wooden;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import java.util.List;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.ActivityData;
import net.minecraft.world.entity.ai.behavior.DoNothing;
import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
import net.minecraft.world.entity.ai.behavior.RandomLookAround;
import net.minecraft.world.entity.ai.behavior.RandomStroll;
import net.minecraft.world.entity.ai.behavior.RunOne;
import net.minecraft.world.entity.ai.behavior.SetEntityLookTargetSometimes;
import net.minecraft.world.entity.ai.behavior.Swim;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.schedule.Activity;

public final class WoodenGolemAi {
    private static final float IDLE_SPEED = 1.0F;
    private static final float SWIM_CHANCE = 0.8F;
    private static final UniformInt LOOK_INTERVAL = UniformInt.of(40, 80);
    private static final UniformInt RANDOM_LOOK_INTERVAL = UniformInt.of(150, 250);

    private WoodenGolemAi() {
    }

    public static List<ActivityData<WoodenGolemEntity>> getActivities() {
        return List.of(initCoreActivity(), initIdleActivity());
    }

    public static void updateActivity(WoodenGolemEntity entity) {
        entity.getBrain().setActiveActivityToFirstValid(ImmutableList.of(Activity.IDLE));
    }

    private static ActivityData<WoodenGolemEntity> initCoreActivity() {
        return ActivityData.create(
                Activity.CORE,
                0,
                ImmutableList.of(
                        new Swim<>(SWIM_CHANCE),
                        new LookAtTargetSink(45, 90),
                        new MoveToTargetSink()
                )
        );
    }

    private static ActivityData<WoodenGolemEntity> initIdleActivity() {
        return ActivityData.create(
                Activity.IDLE,
                ImmutableList.of(
                        Pair.of(0, SetEntityLookTargetSometimes.create(EntityType.PLAYER, 8.0F, LOOK_INTERVAL)),
                        Pair.of(1, new RandomLookAround(RANDOM_LOOK_INTERVAL, 30.0F, 0.0F, 0.0F)),
                        Pair.of(
                                2,
                                new RunOne(
                                        ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT),
                                        ImmutableList.of(
                                                Pair.of(RandomStroll.stroll(IDLE_SPEED), 2),
                                                Pair.of(new DoNothing(30, 60), 1)
                                        )
                                )
                        )
                )
        );
    }
}
