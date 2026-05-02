// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.golemancyartifice.registry;

import com.klikli_dev.golemancyartifice.GolemancyArtifice;
import com.klikli_dev.golemancyartifice.content.menu.GolemBindingToolMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class MenuRegistry {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, GolemancyArtifice.MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<GolemBindingToolMenu>> GOLEM_BINDING_TOOL = MENUS.register(
            "golem_binding_tool",
            () -> net.neoforged.neoforge.common.extensions.IMenuTypeExtension.create(GolemBindingToolMenu::new)
    );

    private MenuRegistry() {
    }
}
