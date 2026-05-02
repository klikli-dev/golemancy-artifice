// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.golemancyartifice.network.messages;

import com.klikli_dev.golemancyartifice.GolemancyArtifice;
import com.klikli_dev.golemancyartifice.content.entity.golem.wooden.WoodenGolemEntity;
import com.klikli_dev.golemancyartifice.content.golem.core.CoreItem;
import com.klikli_dev.golemancyartifice.network.Message;
import com.klikli_dev.golemancyartifice.registry.DataComponentTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
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
        if (!(player.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        ItemStack tool = player.getMainHandItem();
        var boundGolemId = tool.get(DataComponentTypeRegistry.BOUND_GOLEM.get());
        var action = tool.get(DataComponentTypeRegistry.BINDING_ACTION.get());
        if (boundGolemId == null || action == null) {
            return;
        }

        if (!(serverLevel.getEntities().get(boundGolemId) instanceof WoodenGolemEntity golem)) {
            return;
        }

        if (!(golem.installedCore().getItem() instanceof CoreItem coreItem)) {
            return;
        }

        golem.reconfigureInstalledCore(stack -> coreItem.definition().applyBindingAction(stack, action, this.target, golem));
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
