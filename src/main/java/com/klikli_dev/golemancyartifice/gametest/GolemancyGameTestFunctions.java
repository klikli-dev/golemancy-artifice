// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.golemancyartifice.gametest;

import com.klikli_dev.golemancyartifice.content.golem.core.status.CoreRunState;
import com.klikli_dev.golemancyartifice.content.golem.core.transfer.InventoryTransferRuntime;
import com.klikli_dev.golemancyartifice.content.golem.core.CoreItem;
import com.klikli_dev.golemancyartifice.registry.EntityRegistry;
import com.klikli_dev.golemancyartifice.registry.DataComponentTypeRegistry;
import com.klikli_dev.golemancyartifice.registry.ItemRegistry;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.ChestBlockEntity;

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
    public static final Consumer<GameTestHelper> GOLEM_REBUILDS_RUNTIME_FOR_TRANSFER_CORE_FUNCTION = helper -> {
        var golem = helper.spawn(EntityRegistry.WOODEN_GOLEM.get(), 1, 2, 1);

        golem.installCore(ItemRegistry.INVENTORY_TRANSFER_CORE.get().getDefaultInstance());
        golem.refreshInstalledCore();

        helper.assertTrue(golem.activeCoreRuntime() instanceof InventoryTransferRuntime, "Runtime must switch to transfer runtime");
        helper.assertValueEqual(golem.activeCoreRuntime().runState(), CoreRunState.UNCONFIGURED, "run state");
        helper.succeed();
    };
    public static final Consumer<GameTestHelper> TRANSFER_CORE_REPORTS_UNCONFIGURED_FUNCTION = helper -> {
        var golem = helper.spawn(EntityRegistry.WOODEN_GOLEM.get(), 2, 2, 2);
        golem.installCore(ItemRegistry.INVENTORY_TRANSFER_CORE.toStack());
        golem.refreshInstalledCore();

        helper.assertValueEqual(golem.activeCoreRuntime().runState(), CoreRunState.UNCONFIGURED, "run state");
        helper.assertFalse(golem.activeCoreRuntime().diagnostics().isEmpty(), "Unconfigured runtime must expose diagnostics");
        helper.succeed();
    };
    public static final Consumer<GameTestHelper> TRANSFER_CORE_MOVES_ITEM_BETWEEN_CHESTS_FUNCTION = helper -> {
        helper.setBlock(1, 1, 1, Blocks.CHEST);
        helper.setBlock(4, 1, 1, Blocks.CHEST);
        helper.getBlockEntity(new BlockPos(1, 1, 1), ChestBlockEntity.class).setItem(0, new ItemStack(Items.COBBLESTONE));

        var coreStack = ItemRegistry.INVENTORY_TRANSFER_CORE.toStack();
        coreStack.set(DataComponentTypeRegistry.TRANSFER_SOURCE.get(), GlobalPos.of(helper.getLevel().dimension(), helper.absolutePos(new BlockPos(1, 1, 1))));
        coreStack.set(DataComponentTypeRegistry.TRANSFER_DESTINATION.get(), GlobalPos.of(helper.getLevel().dimension(), helper.absolutePos(new BlockPos(4, 1, 1))));

        var golem = helper.spawn(EntityRegistry.WOODEN_GOLEM.get(), 2, 2, 3);
        golem.installCore(coreStack);
        golem.refreshInstalledCore();

        helper.succeedWhen(() -> {
            helper.assertContainerEmpty(new BlockPos(1, 1, 1));
            helper.assertContainerContainsSingle(new BlockPos(4, 1, 1), Items.COBBLESTONE);
        });
    };

    private GolemancyGameTestFunctions() {
    }
}
