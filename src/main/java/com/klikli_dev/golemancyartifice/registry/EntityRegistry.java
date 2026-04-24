// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.golemancyartifice.registry;

import com.klikli_dev.golemancyartifice.GolemancyArtifice;
import com.klikli_dev.golemancyartifice.content.entity.golem.wooden.WoodenGolemEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class EntityRegistry {
    public static final DeferredRegister.Entities ENTITY_TYPES = DeferredRegister.createEntities(GolemancyArtifice.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<WoodenGolemEntity>> WOODEN_GOLEM = ENTITY_TYPES.registerEntityType(
            "wooden_golem",
            WoodenGolemEntity::new,
            MobCategory.CREATURE,
            builder -> builder.sized(0.9F, 1.0F).clientTrackingRange(8));

    public static void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
        event.put(WOODEN_GOLEM.get(), WoodenGolemEntity.createAttributes().build());
    }
}
