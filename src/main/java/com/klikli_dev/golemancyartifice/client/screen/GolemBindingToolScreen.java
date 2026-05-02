// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.golemancyartifice.client.screen;

import com.klikli_dev.golemancyartifice.content.menu.GolemBindingToolMenu;
import com.klikli_dev.golemancyartifice.network.Networking;
import com.klikli_dev.golemancyartifice.network.messages.MessageApplyBindingTarget;
import com.klikli_dev.golemancyartifice.network.messages.MessageSelectBindingAction;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GolemBindingToolScreen extends AbstractContainerScreen<GolemBindingToolMenu> {
    private EditBox xField;
    private EditBox yField;
    private EditBox zField;

    public GolemBindingToolScreen(GolemBindingToolMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, 176, 120);
        this.inventoryLabelY = 1000;
    }

    @Override
    protected void init() {
        super.init();
        this.clearWidgets();

        int x = this.leftPos + 10;
        int y = this.topPos + 18;
        for (String action : this.menu.actions()) {
            this.addRenderableWidget(Button.builder(Component.literal(action), button -> {
                this.menu.setSelectedAction(action);
                Networking.sendToServer(new MessageSelectBindingAction(action));
            }).pos(x, y).size(120, 20).build());
            y += 24;
        }

        this.xField = this.addCoordinateBox(this.leftPos + 10, this.topPos + 70, "X");
        this.yField = this.addCoordinateBox(this.leftPos + 62, this.topPos + 70, "Y");
        this.zField = this.addCoordinateBox(this.leftPos + 114, this.topPos + 70, "Z");

        this.addRenderableWidget(Button.builder(Component.literal("Apply"), button -> this.applyTarget())
                .pos(this.leftPos + 10, this.topPos + 95)
                .size(80, 20)
                .build());
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (event.key() == 256) {
            this.onClose();
            return true;
        }

        return super.keyPressed(event);
    }

    private EditBox addCoordinateBox(int x, int y, String hint) {
        EditBox box = new EditBox(this.font, x, y, 44, 18, Component.literal(hint));
        box.setHint(Component.literal(hint));
        box.setMaxLength(10);
        box.setFilter(value -> value.isEmpty() || value.matches("-?\\d*"));
        this.addRenderableWidget(box);
        return box;
    }

    private void applyTarget() {
        if (this.menu.selectedAction().isBlank()) {
            return;
        }

        int x = this.parseCoordinate(this.xField.getValue());
        int y = this.parseCoordinate(this.yField.getValue());
        int z = this.parseCoordinate(this.zField.getValue());
        Networking.sendToServer(new MessageApplyBindingTarget(new BlockPos(x, y, z)));
    }

    private int parseCoordinate(String value) {
        return value.isBlank() ? 0 : Integer.parseInt(value);
    }
}
