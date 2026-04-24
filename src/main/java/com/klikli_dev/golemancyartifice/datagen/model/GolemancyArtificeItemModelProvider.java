// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.golemancyartifice.datagen.model;

import com.google.gson.JsonObject;
import com.klikli_dev.golemancyartifice.GolemancyArtifice;
import com.klikli_dev.golemancyartifice.registry.ItemRegistry;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

import java.util.stream.Stream;

public class GolemancyArtificeItemModelProvider {
    private static final int WOODEN_GOLEM_PRIMARY_SPAWN_EGG_COLOR = 0x8B6B47;
    private static final int WOODEN_GOLEM_SECONDARY_SPAWN_EGG_COLOR = 0x4F341D;

    public Stream<Item> getKnownItems() {
        return Stream.of(ItemRegistry.WOODEN_GOLEM_SPAWN_EGG.get());
    }

    public void registerModels(ItemModelGenerators itemModels) {
        this.registerSpawnEggTemplate(itemModels);
        this.registerSpawnEgg(itemModels, ItemRegistry.WOODEN_GOLEM_SPAWN_EGG.get(), WOODEN_GOLEM_PRIMARY_SPAWN_EGG_COLOR, WOODEN_GOLEM_SECONDARY_SPAWN_EGG_COLOR);
    }

    private void registerSpawnEgg(ItemModelGenerators itemModels, Item item, int primaryColor, int secondaryColor) {
        var modelId = this.modLoc("item/template_spawn_egg");
        itemModels.itemModelOutput.accept(item, ItemModelUtils.tintedModel(modelId,
                ItemModelUtils.constantTint(opaque(primaryColor)),
                ItemModelUtils.constantTint(opaque(secondaryColor))));
    }

    private void registerSpawnEggTemplate(ItemModelGenerators itemModels) {
        Identifier modelId = this.modLoc("item/template_spawn_egg");
        itemModels.modelOutput.accept(modelId, () -> {
            var json = new JsonObject();
            json.addProperty("parent", "item/generated");

            var textures = new JsonObject();
            textures.addProperty("layer0", this.modLoc("item/spawn_egg").toString());
            textures.addProperty("layer1", this.modLoc("item/spawn_egg_overlay").toString());
            json.add("textures", textures);
            return json;
        });
    }

    private Identifier modLoc(String path) {
        return Identifier.fromNamespaceAndPath(GolemancyArtifice.MODID, path);
    }

    private static int opaque(int color) {
        return (color & 0xFF000000) == 0 ? color | 0xFF000000 : color;
    }
}
