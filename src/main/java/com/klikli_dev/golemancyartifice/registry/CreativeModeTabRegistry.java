// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.golemancyartifice.registry;

import com.klikli_dev.golemancyartifice.GolemancyArtifice;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CreativeModeTabRegistry {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, GolemancyArtifice.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> GOLEMANCY_ARTIFICE = CREATIVE_MODE_TABS.register(GolemancyArtifice.MODID, () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> ModItems.WOODEN_GOLEM_SPAWN_EGG.get().getDefaultInstance())
            .title(Component.translatable("itemGroup." + GolemancyArtifice.MODID))
            .displayItems((parameters, output) -> output.accept(ModItems.WOODEN_GOLEM_SPAWN_EGG.get()))
            .build());

    private CreativeModeTabRegistry() {
    }
}
