// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.golemancyartifice.registry;

import com.klikli_dev.golemancyartifice.GolemancyArtifice;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(GolemancyArtifice.MODID);

    public static final DeferredItem<Item> WOODEN_GOLEM_SPAWN_EGG = ITEMS.registerItem(
            "spawn_egg/wooden_golem",
            SpawnEggItem::new,
            () -> new Item.Properties().spawnEgg(ModEntities.WOODEN_GOLEM.get()));

    private ModItems() {
    }
}
