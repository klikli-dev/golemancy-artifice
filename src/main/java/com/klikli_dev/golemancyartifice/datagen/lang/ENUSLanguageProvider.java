// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.golemancyartifice.datagen.lang;

import com.klikli_dev.golemancyartifice.GolemancyArtifice;
import com.klikli_dev.golemancyartifice.registry.EntityRegistry;
import com.klikli_dev.golemancyartifice.registry.ItemRegistry;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class ENUSLanguageProvider extends LanguageProvider {
    public ENUSLanguageProvider(PackOutput output) {
        super(output, GolemancyArtifice.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        this.addConfig();
        this.addCreativeTabs();
        this.addEntities();
        this.addItems();
    }

    protected void addCreativeTabs() {
        this.add("itemGroup." + GolemancyArtifice.MODID, "Golemancy: Artifice");
    }

    protected void addConfig() {
        this.add(GolemancyArtifice.MODID + ".configuration.title", "Golemancy: Artifice Configs");
    }

    protected void addEntities() {
        this.add(EntityRegistry.WOODEN_GOLEM.get(), "Wooden Golem");
    }

    protected void addItems() {
        this.addItem(ItemRegistry.INVENTORY_TRANSFER_CORE, "Inventory Transfer Core");
        this.addItem(ItemRegistry.WOODEN_GOLEM_SPAWN_EGG, "Wooden Golem Spawn Egg");
    }
}
