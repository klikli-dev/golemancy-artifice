// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.golemancyartifice.content.golem.core;

import com.klikli_dev.golemancyartifice.content.golem.core.host.GolemCoreHost;
import com.klikli_dev.golemancyartifice.content.golem.core.runtime.ActiveCoreRuntime;
import net.minecraft.world.item.ItemStack;

public interface CoreDefinition<R extends ActiveCoreRuntime> {
    R createRuntime(ItemStack stack, GolemCoreHost host);
}
