// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.golemancyartifice.network;

import com.klikli_dev.golemancyartifice.GolemancyArtifice;
import com.klikli_dev.golemancyartifice.network.messages.MessageApplyBindingTarget;
import com.klikli_dev.golemancyartifice.network.messages.MessageSelectBindingAction;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

public class Networking {
    public static void register(RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar(GolemancyArtifice.MODID);

        registrar.playToServer(MessageSelectBindingAction.TYPE, MessageSelectBindingAction.STREAM_CODEC, MessageHandler::handle);
        registrar.playToServer(MessageApplyBindingTarget.TYPE, MessageApplyBindingTarget.STREAM_CODEC, MessageHandler::handle);
    }

    public static <T extends Message> void sendTo(ServerPlayer player, T message) {
        PacketDistributor.sendToPlayer(player, message);
    }

    public static <T extends Message> void sendToServer(T message) {
        ClientPacketDistributor.sendToServer(message);
    }
}
