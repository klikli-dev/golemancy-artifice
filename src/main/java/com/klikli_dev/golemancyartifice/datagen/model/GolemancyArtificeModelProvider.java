// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.golemancyartifice.datagen.model;

import com.klikli_dev.golemancyartifice.GolemancyArtifice;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

public class GolemancyArtificeModelProvider extends ModelProvider {
    private final GolemancyArtificeItemModelProvider itemModels = new GolemancyArtificeItemModelProvider();

    public GolemancyArtificeModelProvider(PackOutput packOutput) {
        super(packOutput, GolemancyArtifice.MODID);
    }

    @Override
    public @NotNull String getName() {
        return "Model Definitions - " + this.modId;
    }

    @Override
    protected void registerModels(@NotNull BlockModelGenerators blockModels, @NotNull ItemModelGenerators itemModels) {
        this.itemModels.registerModels(itemModels);
    }

    @Override
    protected Stream<? extends Holder<Block>> getKnownBlocks() {
        return Stream.empty();
    }

    @Override
    protected Stream<? extends Holder<Item>> getKnownItems() {
        return this.itemModels.getKnownItems().map(Item::builtInRegistryHolder);
    }
}
