// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.golemancyartifice.datagen.model;

import com.google.gson.JsonObject;
import com.klikli_dev.golemancyartifice.GolemancyArtifice;
import com.klikli_dev.golemancyartifice.registry.ItemRegistry;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ItemModelOutput;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

import java.lang.reflect.Field;
import java.util.function.BiConsumer;
import java.util.stream.Stream;
import net.minecraft.client.data.models.model.ModelInstance;

public class GolemancyArtificeItemModelProvider {
    private static final int WOODEN_GOLEM_PRIMARY_SPAWN_EGG_COLOR = 0x8B6B47;
    private static final int WOODEN_GOLEM_SECONDARY_SPAWN_EGG_COLOR = 0x4F341D;

    public Stream<Item> getKnownItems() {
        return Stream.of(ItemRegistry.WOODEN_GOLEM_SPAWN_EGG.get(), ItemRegistry.INVENTORY_TRANSFER_CORE.get(), ItemRegistry.GOLEM_BINDING_TOOL.get());
    }

    public void registerModels(ItemModelGenerators itemModels) {
        this.registerSpawnEggTemplate(itemModels);
        this.registerSpawnEgg(itemModels, ItemRegistry.WOODEN_GOLEM_SPAWN_EGG.get(), WOODEN_GOLEM_PRIMARY_SPAWN_EGG_COLOR, WOODEN_GOLEM_SECONDARY_SPAWN_EGG_COLOR);
        this.registerFlatItem(itemModels, ItemRegistry.INVENTORY_TRANSFER_CORE.get());
        this.registerFlatItem(itemModels, ItemRegistry.GOLEM_BINDING_TOOL.get());
    }

    private void registerSpawnEgg(ItemModelGenerators itemModels, Item item, int primaryColor, int secondaryColor) {
        var modelId = this.modLoc("item/template_spawn_egg");
        this.getItemModelOutput(itemModels).accept(item, ItemModelUtils.tintedModel(modelId,
                ItemModelUtils.constantTint(opaque(primaryColor)),
                ItemModelUtils.constantTint(opaque(secondaryColor))));
    }

    private void registerFlatItem(ItemModelGenerators itemModels, Item item) {
        Identifier modelId = ModelTemplates.FLAT_ITEM.create(item, TextureMapping.layer0(item), this.getModelOutput(itemModels));
        this.getItemModelOutput(itemModels).accept(item, ItemModelUtils.plainModel(modelId));
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

    @SuppressWarnings("unchecked")
    private ItemModelOutput getItemModelOutput(ItemModelGenerators itemModels) {
        try {
            Field field = ItemModelGenerators.class.getDeclaredField("itemModelOutput");
            field.setAccessible(true);
            return (ItemModelOutput) field.get(itemModels);
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException("Failed to access item model output", exception);
        }
    }

    @SuppressWarnings("unchecked")
    private BiConsumer<Identifier, ModelInstance> getModelOutput(ItemModelGenerators itemModels) {
        try {
            Field field = ItemModelGenerators.class.getDeclaredField("modelOutput");
            field.setAccessible(true);
            return (BiConsumer<Identifier, ModelInstance>) field.get(itemModels);
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException("Failed to access model output", exception);
        }
    }

    private static int opaque(int color) {
        return (color & 0xFF000000) == 0 ? color | 0xFF000000 : color;
    }
}
