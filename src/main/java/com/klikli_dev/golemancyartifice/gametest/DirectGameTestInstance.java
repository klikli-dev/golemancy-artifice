// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.golemancyartifice.gametest;

import com.mojang.serialization.MapCodec;
import java.util.function.Consumer;
import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.BlockBasedTestInstance;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.GameTestInstance;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public final class DirectGameTestInstance extends GameTestInstance {
    @SuppressWarnings("unchecked")
    private static final MapCodec<DirectGameTestInstance> CODEC = (MapCodec<DirectGameTestInstance>) (MapCodec<?>) BlockBasedTestInstance.CODEC;

    private final Consumer<GameTestHelper> function;

    public DirectGameTestInstance(TestData<Holder<TestEnvironmentDefinition<?>>> testData, Consumer<GameTestHelper> function) {
        super(testData);
        this.function = function;
    }

    @Override
    public void run(GameTestHelper helper) {
        this.function.accept(helper);
    }

    @Override
    public MapCodec<DirectGameTestInstance> codec() {
        return CODEC;
    }

    @Override
    protected MutableComponent typeDescription() {
        return Component.literal("direct");
    }
}
