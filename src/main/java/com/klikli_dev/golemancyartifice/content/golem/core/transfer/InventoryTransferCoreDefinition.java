// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.golemancyartifice.content.golem.core.transfer;

import com.klikli_dev.golemancyartifice.content.golem.core.CoreDefinition;
import com.klikli_dev.golemancyartifice.content.golem.core.host.GolemCoreHost;
import com.klikli_dev.golemancyartifice.content.golem.core.status.CoreRunState;
import java.util.List;
import net.minecraft.world.item.ItemStack;

public final class InventoryTransferCoreDefinition implements CoreDefinition<InventoryTransferRuntime> {
    @Override
    public InventoryTransferRuntime createRuntime(ItemStack stack, GolemCoreHost host) {
        return new InventoryTransferRuntime(null, null, CoreRunState.UNCONFIGURED, List.of());
    }
}
