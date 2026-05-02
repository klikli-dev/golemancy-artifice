// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.golemancyartifice.content.golem.core.host;

import com.klikli_dev.golemancyartifice.content.golem.core.runtime.ActiveCoreRuntime;
import net.minecraft.world.item.ItemStack;

public interface GolemCoreHost {
    ItemStack installedCore();

    ActiveCoreRuntime activeCoreRuntime();
}
