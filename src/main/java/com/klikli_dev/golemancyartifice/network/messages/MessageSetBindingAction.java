// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.golemancyartifice.network.messages;

import com.klikli_dev.golemancyartifice.GolemancyArtifice;
import com.klikli_dev.golemancyartifice.content.item.GolemBindingToolItem;
import com.klikli_dev.golemancyartifice.network.Message;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

public record MessageSetBindingAction(String action) implements Message {
    public static final Type<MessageSetBindingAction> TYPE = new Type<>(GolemancyArtifice.loc("set_binding_action"));
    public static final StreamCodec<RegistryFriendlyByteBuf, MessageSetBindingAction> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            MessageSetBindingAction::action,
            MessageSetBindingAction::new
    );

    @Override
    public void onServerReceived(MinecraftServer minecraftServer, ServerPlayer player) {
        if (player.getMainHandItem().getItem() instanceof GolemBindingToolItem bindingToolItem) {
            bindingToolItem.setBindingAction(player, player.getMainHandItem(), this.action);
        }
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
