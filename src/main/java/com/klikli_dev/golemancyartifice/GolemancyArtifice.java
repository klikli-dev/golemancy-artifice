// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.golemancyartifice;

import com.klikli_dev.golemancyartifice.datagen.DataGenerators;
import com.klikli_dev.golemancyartifice.registry.ModEntities;
import com.klikli_dev.golemancyartifice.registry.ModItems;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

@Mod(GolemancyArtifice.MODID)
public class GolemancyArtifice {
    public static final String MODID = "golemancyartifice";

    public GolemancyArtifice(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        ModEntities.ENTITY_TYPES.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);

        modEventBus.addListener(ModEntities::onEntityAttributeCreation);
        modEventBus.addListener(DataGenerators::onGatherData);
    }
}
