// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.golemancyartifice.content.golem.core;

import net.minecraft.world.item.Item;

public class CoreItem extends Item {
    private final CoreDefinition<?> definition;

    public CoreItem(Properties properties, CoreDefinition<?> definition) {
        super(properties);
        this.definition = definition;
    }

    public CoreDefinition<?> definition() {
        return this.definition;
    }
}
