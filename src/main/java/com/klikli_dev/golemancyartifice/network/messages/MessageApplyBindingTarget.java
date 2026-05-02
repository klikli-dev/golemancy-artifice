// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.golemancyartifice.network.messages;

import com.klikli_dev.golemancyartifice.GolemancyArtifice;
import com.klikli_dev.golemancyartifice.content.item.GolemBindingToolItem;
import com.klikli_dev.golemancyartifice.network.Message;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

public record MessageApplyBindingTarget(BlockPos target) implements Message {
    public static final Type<MessageApplyBindingTarget> TYPE = new Type<>(GolemancyArtifice.loc("apply_binding_target"));
    public static final StreamCodec<RegistryFriendlyByteBuf, MessageApplyBindingTarget> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            MessageApplyBindingTarget::target,
            MessageApplyBindingTarget::new
    );

    @Override
    public void onServerReceived(MinecraftServer minecraftServer, ServerPlayer player) {
        if (player.getMainHandItem().getItem() instanceof GolemBindingToolItem bindingToolItem) {
            bindingToolItem.applyBindingTarget(player, player.getMainHandItem(), this.target);
        }
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
