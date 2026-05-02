// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.golemancyartifice.content.menu;

import com.klikli_dev.golemancyartifice.registry.MenuRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class GolemBindingToolMenu extends AbstractContainerMenu {
    private UUID boundGolemId;
    private List<String> actions = List.of();
    private String selectedAction = "";

    public GolemBindingToolMenu(int containerId, Inventory inventory, RegistryFriendlyByteBuf buffer) {
        this(containerId, inventory);
        this.readBuffer(buffer);
    }

    public GolemBindingToolMenu(int containerId, Inventory inventory) {
        super(MenuRegistry.GOLEM_BINDING_TOOL.get(), containerId);
    }

    public Optional<UUID> boundGolemId() {
        return Optional.ofNullable(this.boundGolemId);
    }

    public List<String> actions() {
        return this.actions;
    }

    public String selectedAction() {
        return this.selectedAction;
    }

    public void setSelectedAction(String selectedAction) {
        this.selectedAction = selectedAction;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    private void readBuffer(FriendlyByteBuf buffer) {
        if (buffer.readBoolean()) {
            this.boundGolemId = buffer.readUUID();
        }

        this.actions = java.util.stream.IntStream.range(0, buffer.readVarInt())
                .mapToObj(index -> buffer.readUtf())
                .toList();
        this.selectedAction = buffer.readUtf();
    }
}
