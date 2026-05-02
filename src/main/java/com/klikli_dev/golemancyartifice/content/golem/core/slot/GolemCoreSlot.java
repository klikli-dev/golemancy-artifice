// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.golemancyartifice.content.golem.core.slot;

import com.klikli_dev.golemancyartifice.content.golem.core.CoreItem;
import net.minecraft.world.item.ItemStack;

public final class GolemCoreSlot {
    private ItemStack stack = ItemStack.EMPTY;

    public ItemStack get() {
        return this.stack;
    }

    public boolean mayPlace(ItemStack candidate) {
        return !candidate.isEmpty() && candidate.getItem() instanceof CoreItem;
    }

    public void set(ItemStack stack) {
        this.stack = stack.copy();
    }

    public ItemStack remove() {
        ItemStack removed = this.stack;
        this.stack = ItemStack.EMPTY;
        return removed;
    }
}
