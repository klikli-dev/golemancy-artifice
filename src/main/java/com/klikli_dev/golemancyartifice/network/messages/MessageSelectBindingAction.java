// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.golemancyartifice.network.messages;

import com.klikli_dev.golemancyartifice.GolemancyArtifice;
import com.klikli_dev.golemancyartifice.network.Message;
import com.klikli_dev.golemancyartifice.registry.DataComponentTypeRegistry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

public record MessageSelectBindingAction(String action) implements Message {
    public static final Type<MessageSelectBindingAction> TYPE = new Type<>(GolemancyArtifice.loc("select_binding_action"));
    public static final StreamCodec<RegistryFriendlyByteBuf, MessageSelectBindingAction> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            MessageSelectBindingAction::action,
            MessageSelectBindingAction::new
    );

    @Override
    public void onServerReceived(MinecraftServer minecraftServer, ServerPlayer player) {
        player.getMainHandItem().set(DataComponentTypeRegistry.BINDING_ACTION.get(), this.action);
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
