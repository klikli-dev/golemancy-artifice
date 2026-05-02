// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.golemancyartifice.network;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.fml.LogicalSide;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class MessageHandler {
    public static <T extends Message> void handle(T message, IPayloadContext context) {
        if (context.flow().getReceptionSide() == LogicalSide.SERVER) {
            handleServer(message, context);
        } else {
            ClientMessageHandler.handleClient(message, context);
        }
    }

    public static <T extends Message> void handleServer(T message, IPayloadContext context) {
        context.enqueueWork(() -> {
            MinecraftServer server = context.player().level().getServer();
            message.onServerReceived(server, (ServerPlayer) context.player());
        });
    }

    public static class ClientMessageHandler {
        public static <T extends Message> void handleClient(T message, IPayloadContext context) {
            context.enqueueWork(() -> {
                Minecraft minecraft = Minecraft.getInstance();
                message.onClientReceived(minecraft, minecraft.player);
            });
        }
    }
}
