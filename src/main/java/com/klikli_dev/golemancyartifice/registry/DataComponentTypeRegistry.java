// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.golemancyartifice.registry;

import com.klikli_dev.golemancyartifice.GolemancyArtifice;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.UUID;

public class DataComponentTypeRegistry {
    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, GolemancyArtifice.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<GlobalPos>> TRANSFER_SOURCE = DATA_COMPONENTS.registerComponentType(
            "transfer_source",
            builder -> builder.persistent(GlobalPos.CODEC).networkSynchronized(GlobalPos.STREAM_CODEC)
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<GlobalPos>> TRANSFER_DESTINATION = DATA_COMPONENTS.registerComponentType(
            "transfer_destination",
            builder -> builder.persistent(GlobalPos.CODEC).networkSynchronized(GlobalPos.STREAM_CODEC)
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<UUID>> BOUND_GOLEM = DATA_COMPONENTS.registerComponentType(
            "bound_golem",
            builder -> builder.persistent(UUIDUtil.CODEC).networkSynchronized(UUIDUtil.STREAM_CODEC)
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> BINDING_ACTION = DATA_COMPONENTS.registerComponentType(
            "binding_action",
            builder -> builder.persistent(com.mojang.serialization.Codec.STRING).networkSynchronized(net.minecraft.network.codec.ByteBufCodecs.STRING_UTF8)
    );

    private DataComponentTypeRegistry() {
    }
}
