// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.golemancyartifice.content.item;

import com.klikli_dev.golemancyartifice.content.entity.golem.wooden.WoodenGolemEntity;
import com.klikli_dev.golemancyartifice.content.golem.core.CoreItem;
import com.klikli_dev.golemancyartifice.content.menu.GolemBindingToolMenu;
import com.klikli_dev.golemancyartifice.registry.DataComponentTypeRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;

import java.util.List;

public class GolemBindingToolItem extends Item {
    public GolemBindingToolItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, net.minecraft.world.entity.LivingEntity interactionTarget, net.minecraft.world.InteractionHand usedHand) {
        if (!(interactionTarget instanceof WoodenGolemEntity golem) || !(golem.installedCore().getItem() instanceof CoreItem coreItem)) {
            return InteractionResult.PASS;
        }

        stack.set(DataComponentTypeRegistry.BOUND_GOLEM.get(), golem.getUUID());
        List<String> actions = coreItem.definition().bindingActions();
        String selectedAction = stack.getOrDefault(DataComponentTypeRegistry.BINDING_ACTION.get(), actions.isEmpty() ? "" : actions.getFirst());

        if (!player.level().isClientSide()) {
            player.openMenu(new SimpleMenuProvider(
                    (containerId, inventory, menuPlayer) -> new GolemBindingToolMenu(containerId, inventory),
                    Component.literal("Golem Binding Tool")
            ), buffer -> {
                buffer.writeBoolean(true);
                buffer.writeUUID(golem.getUUID());
                buffer.writeVarInt(actions.size());
                for (String action : actions) {
                    buffer.writeUtf(action);
                }
                buffer.writeUtf(selectedAction);
            });
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        ItemStack stack = context.getItemInHand();
        if (stack.has(DataComponentTypeRegistry.BOUND_GOLEM.get()) && stack.has(DataComponentTypeRegistry.BINDING_ACTION.get())) {
            return InteractionResult.SUCCESS;
        }

        return super.useOn(context);
    }
}
