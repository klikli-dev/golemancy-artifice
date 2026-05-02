// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.golemancyartifice.gametest;

import com.klikli_dev.golemancyartifice.content.golem.core.CoreItem;
import com.klikli_dev.golemancyartifice.registry.EntityRegistry;
import com.klikli_dev.golemancyartifice.registry.ItemRegistry;
import java.util.function.Consumer;
import net.minecraft.gametest.framework.GameTestHelper;

public final class GolemancyGameTestFunctions {
    public static final Consumer<GameTestHelper> CORE_ITEM_EXPOSES_DEFINITION_FUNCTION = helper -> {
        if (!(ItemRegistry.INVENTORY_TRANSFER_CORE.get() instanceof CoreItem coreItem)) {
            helper.fail("Inventory transfer core item must extend CoreItem");
            return;
        }

        helper.assertTrue(coreItem.definition() != null, "CoreItem must expose a CoreDefinition");
        helper.succeed();
    };
    public static final Consumer<GameTestHelper> GOLEM_ACCEPTS_TRANSFER_CORE_FUNCTION = helper -> {
        var golem = helper.spawn(EntityRegistry.WOODEN_GOLEM.get(), 1, 2, 1);
        var coreStack = ItemRegistry.INVENTORY_TRANSFER_CORE.get().getDefaultInstance();

        golem.installCore(coreStack);

        helper.assertTrue(golem.installedCore().is(ItemRegistry.INVENTORY_TRANSFER_CORE.get()), "Installed core stack must match");
        helper.succeed();
    };

    private GolemancyGameTestFunctions() {
    }
}
