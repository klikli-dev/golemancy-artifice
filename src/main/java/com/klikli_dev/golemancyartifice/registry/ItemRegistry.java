// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.golemancyartifice.registry;

import com.klikli_dev.golemancyartifice.GolemancyArtifice;
import com.klikli_dev.golemancyartifice.content.golem.core.CoreItem;
import com.klikli_dev.golemancyartifice.content.golem.core.transfer.InventoryTransferCoreDefinition;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ItemRegistry {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(GolemancyArtifice.MODID);

    public static final DeferredItem<Item> INVENTORY_TRANSFER_CORE = ITEMS.registerItem(
            "inventory_transfer_core",
            properties -> new CoreItem(properties.stacksTo(1), new InventoryTransferCoreDefinition())
    );

    public static final DeferredItem<Item> WOODEN_GOLEM_SPAWN_EGG = ITEMS.registerItem(
            "spawn_egg/wooden_golem",
            SpawnEggItem::new,
            () -> new Item.Properties().spawnEgg(EntityRegistry.WOODEN_GOLEM.get()));
}
