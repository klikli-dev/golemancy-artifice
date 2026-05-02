// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.golemancyartifice.content.item;

import com.klikli_dev.golemancyartifice.content.entity.golem.wooden.WoodenGolemEntity;
import com.klikli_dev.golemancyartifice.content.golem.core.CoreItem;
import com.klikli_dev.golemancyartifice.content.menu.GolemBindingToolMenu;
import com.klikli_dev.golemancyartifice.registry.DataComponentTypeRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class GolemBindingToolItem extends Item {
    public static final String SCREEN_TITLE_KEY = "screen.golemancyartifice.golem_binding_tool";

    public GolemBindingToolItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity interactionTarget, InteractionHand usedHand) {
        if (!(interactionTarget instanceof WoodenGolemEntity golem) || !(golem.installedCore().getItem() instanceof CoreItem coreItem)) {
            return InteractionResult.PASS;
        }

        stack.set(DataComponentTypeRegistry.BOUND_GOLEM.get(), golem.getUUID());
        List<String> actions = coreItem.definition().bindingActions();
        String selectedAction = actions.contains(stack.get(DataComponentTypeRegistry.BINDING_ACTION.get()))
                ? stack.get(DataComponentTypeRegistry.BINDING_ACTION.get())
                : actions.isEmpty() ? "" : actions.getFirst();

        if (!selectedAction.isBlank()) {
            stack.set(DataComponentTypeRegistry.BINDING_ACTION.get(), selectedAction);
        }

        if (!player.level().isClientSide()) {
            player.openMenu(new SimpleMenuProvider(
                    (containerId, inventory, menuPlayer) -> new GolemBindingToolMenu(containerId, inventory, golem.getUUID(), actions, selectedAction),
                    Component.translatable(SCREEN_TITLE_KEY)
            ), buffer -> {
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

    public void setBindingAction(ServerPlayer player, ItemStack stack, String action) {
        if (!(player.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        if (action.isBlank()) {
            return;
        }

        this.resolveBoundGolem(stack, serverLevel)
                .filter(golem -> this.bindingActions(golem).contains(action))
                .ifPresent(golem -> stack.set(DataComponentTypeRegistry.BINDING_ACTION.get(), action));
    }

    public void applyBindingTarget(ServerPlayer player, ItemStack stack, BlockPos target) {
        if (!(player.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        String action = stack.get(DataComponentTypeRegistry.BINDING_ACTION.get());
        if (action == null || action.isBlank()) {
            return;
        }

        this.resolveBoundGolem(stack, serverLevel)
                .filter(golem -> golem.installedCore().getItem() instanceof CoreItem)
                .ifPresent(golem -> {
                    CoreItem coreItem = (CoreItem) golem.installedCore().getItem();
                    if (this.bindingActions(golem).contains(action)) {
                        golem.reconfigureInstalledCore(coreStack -> coreItem.definition().applyBindingAction(coreStack, action, target, golem));
                    }
                });
    }

    private List<String> bindingActions(WoodenGolemEntity golem) {
        if (golem.installedCore().getItem() instanceof CoreItem coreItem) {
            return coreItem.definition().bindingActions();
        }

        return List.of();
    }

    private Optional<WoodenGolemEntity> resolveBoundGolem(ItemStack stack, ServerLevel serverLevel) {
        UUID boundGolemId = stack.get(DataComponentTypeRegistry.BOUND_GOLEM.get());
        if (boundGolemId == null) {
            return Optional.empty();
        }

        if (serverLevel.getEntities().get(boundGolemId) instanceof WoodenGolemEntity golem) {
            return Optional.of(golem);
        }

        return Optional.empty();
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
