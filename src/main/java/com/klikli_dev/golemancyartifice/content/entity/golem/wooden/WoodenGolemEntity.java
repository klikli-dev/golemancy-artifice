// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.golemancyartifice.content.entity.golem.wooden;

import com.geckolib.animatable.GeoEntity;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.animation.AnimationController;
import com.geckolib.animation.RawAnimation;
import com.geckolib.util.GeckoLibUtil;
import com.klikli_dev.golemancyartifice.content.golem.core.host.GolemCoreHost;
import com.klikli_dev.golemancyartifice.content.golem.core.runtime.ActiveCoreRuntime;
import com.klikli_dev.golemancyartifice.content.golem.core.runtime.NoCoreRuntime;
import com.klikli_dev.golemancyartifice.content.golem.core.slot.GolemCoreSlot;
import java.util.List;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

public class WoodenGolemEntity extends PathfinderMob implements GeoEntity, GolemCoreHost {
    private static final RawAnimation IDLE_ANIMATION = RawAnimation.begin().thenLoop("idle");
    private static final RawAnimation WALK_ANIMATION = RawAnimation.begin().thenLoop("walk");
    private static final Brain.Provider<WoodenGolemEntity> BRAIN_PROVIDER = Brain.provider(
            List.of(SensorType.NEAREST_LIVING_ENTITIES),
            entity -> WoodenGolemAi.getActivities()
    );

    private final AnimatableInstanceCache animationCache = GeckoLibUtil.createInstanceCache(this);
    private final GolemCoreSlot coreSlot = new GolemCoreSlot();

    public WoodenGolemEntity(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
        this.getNavigation().setCanFloat(true);
        this.setPathfindingMalus(PathType.WATER, 0.0F);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.MOVEMENT_SPEED, 0.2)
                .add(Attributes.FOLLOW_RANGE, 16.0);
    }

    @Override
    public ItemStack installedCore() {
        return this.coreSlot.get();
    }

    @Override
    public ActiveCoreRuntime activeCoreRuntime() {
        return NoCoreRuntime.INSTANCE;
    }

    public void installCore(ItemStack stack) {
        if (!this.coreSlot.mayPlace(stack)) {
            throw new IllegalArgumentException("Only CoreItem stacks may be installed");
        }

        this.coreSlot.set(stack);
    }

    @Override
    protected @NonNull Brain<WoodenGolemEntity> makeBrain(Brain.@NonNull Packed packedBrain) {
        return BRAIN_PROVIDER.makeBrain(this, packedBrain);
    }

    @Override
    public @NonNull Brain<WoodenGolemEntity> getBrain() {
        //noinspection unchecked
        return (Brain<WoodenGolemEntity>) super.getBrain();
    }

    @Override
    protected void customServerAiStep(@NonNull ServerLevel level) {
        this.getBrain().tick(level, this);
        WoodenGolemAi.updateActivity(this);
        super.customServerAiStep(level);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<WoodenGolemEntity>("walk_idle", 5, state -> state.setAndContinue(state.isMoving() ? WALK_ANIMATION : IDLE_ANIMATION)));
    }

    @Override
    public @NonNull AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.animationCache;
    }
}
