// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.golemancyartifice.gametest;

import com.klikli_dev.golemancyartifice.GolemancyArtifice;
import com.klikli_dev.golemancyartifice.content.golem.core.CoreItem;
import com.klikli_dev.golemancyartifice.registry.ItemRegistry;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestFunctionLoader;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

public final class GolemancyGameTestFunctions extends TestFunctionLoader {
    public static final ResourceKey<Consumer<GameTestHelper>> CORE_ITEM_EXPOSES_DEFINITION = ResourceKey.create(
            Registries.TEST_FUNCTION,
            Identifier.fromNamespaceAndPath(GolemancyArtifice.MODID, "core_item_exposes_definition")
    );

    @Override
    public void load(BiConsumer<ResourceKey<Consumer<GameTestHelper>>, Consumer<GameTestHelper>> register) {
        register.accept(CORE_ITEM_EXPOSES_DEFINITION, helper -> {
            if (!(ItemRegistry.INVENTORY_TRANSFER_CORE.get() instanceof CoreItem coreItem)) {
                helper.fail("Inventory transfer core item must extend CoreItem");
                return;
            }

            helper.assertTrue(coreItem.definition() != null, "CoreItem must expose a CoreDefinition");
            helper.succeed();
        });
    }
}
