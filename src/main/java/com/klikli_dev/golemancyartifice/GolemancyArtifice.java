// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.golemancyartifice;

import com.klikli_dev.golemancyartifice.datagen.DataGenerators;
import com.klikli_dev.golemancyartifice.gametest.GolemancyGameTestFunctions;
import com.klikli_dev.golemancyartifice.gametest.GolemancyGameTests;
import com.klikli_dev.golemancyartifice.registry.CreativeModeTabRegistry;
import com.klikli_dev.golemancyartifice.registry.EntityRegistry;
import com.klikli_dev.golemancyartifice.registry.ItemRegistry;
import net.minecraft.gametest.framework.TestFunctionLoader;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

@Mod(GolemancyArtifice.MODID)
public class GolemancyArtifice {
    public static final String MODID = "golemancyartifice";

    public GolemancyArtifice(IEventBus modEventBus, ModContainer modContainer) {
        TestFunctionLoader.registerLoader(new GolemancyGameTestFunctions());

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        CreativeModeTabRegistry.CREATIVE_MODE_TABS.register(modEventBus);
        EntityRegistry.ENTITY_TYPES.register(modEventBus);
        ItemRegistry.ITEMS.register(modEventBus);

        modEventBus.addListener(EntityRegistry::onEntityAttributeCreation);
        modEventBus.addListener(DataGenerators::onGatherData);
        modEventBus.addListener(GolemancyGameTests::onRegisterGameTests);
    }
}
