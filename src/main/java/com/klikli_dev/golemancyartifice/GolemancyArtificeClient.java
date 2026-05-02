// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.golemancyartifice;

import com.klikli_dev.golemancyartifice.client.render.GolemCoreStatusParticles;
import com.klikli_dev.golemancyartifice.client.screen.GolemBindingToolScreen;
import com.klikli_dev.golemancyartifice.content.entity.golem.wooden.WoodenGolemEntity;
import com.klikli_dev.golemancyartifice.content.entity.golem.wooden.WoodenGolemRenderer;
import com.klikli_dev.golemancyartifice.registry.EntityRegistry;
import com.klikli_dev.golemancyartifice.registry.MenuRegistry;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;

@Mod(value = GolemancyArtifice.MODID, dist = Dist.CLIENT)
public class GolemancyArtificeClient {
    private final GolemCoreStatusParticles coreStatusParticles = new GolemCoreStatusParticles();

    public GolemancyArtificeClient(IEventBus modEventBus, ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);

        modEventBus.addListener(this::onRegisterEntityRenderers);
        modEventBus.addListener(this::onRegisterMenuScreens);
        NeoForge.EVENT_BUS.addListener(this::onClientTick);
    }

    private void onRegisterEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityRegistry.WOODEN_GOLEM.get(), WoodenGolemRenderer::new);
    }

    private void onRegisterMenuScreens(RegisterMenuScreensEvent event) {
        event.register(MenuRegistry.GOLEM_BINDING_TOOL.get(), GolemBindingToolScreen::new);
    }

    private void onClientTick(ClientTickEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null || minecraft.player == null) {
            return;
        }

        for (WoodenGolemEntity entity : minecraft.level.getEntitiesOfClass(WoodenGolemEntity.class, minecraft.player.getBoundingBox().inflate(32.0D))) {
            this.coreStatusParticles.tick(entity);
        }
    }
}
