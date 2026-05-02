// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.golemancyartifice.content.menu;

import com.klikli_dev.golemancyartifice.registry.MenuRegistry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class GolemBindingToolMenu extends AbstractContainerMenu {
    @Nullable
    private final UUID boundGolemId;
    private final List<String> actions;
    private final String initialSelectedAction;

    public static GolemBindingToolMenu create(int containerId, Inventory inventory, RegistryFriendlyByteBuf extraData) {
        UUID boundGolemId = extraData.readUUID();
        List<String> actions = java.util.stream.IntStream.range(0, extraData.readVarInt())
                .mapToObj(index -> extraData.readUtf())
                .toList();
        String initialSelectedAction = extraData.readUtf();
        return new GolemBindingToolMenu(containerId, inventory, boundGolemId, actions, initialSelectedAction);
    }

    public GolemBindingToolMenu(int containerId, Inventory inventory, @Nullable UUID boundGolemId, List<String> actions, String initialSelectedAction) {
        super(MenuRegistry.GOLEM_BINDING_TOOL.get(), containerId);
        this.boundGolemId = boundGolemId;
        this.actions = List.copyOf(actions);
        this.initialSelectedAction = initialSelectedAction;
    }

    public Optional<UUID> boundGolemId() {
        return Optional.ofNullable(this.boundGolemId);
    }

    public List<String> actions() {
        return this.actions;
    }

    public String initialSelectedAction() {
        return this.initialSelectedAction;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
