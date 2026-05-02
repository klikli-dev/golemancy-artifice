// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.golemancyartifice.client.render;

import com.klikli_dev.golemancyartifice.content.entity.golem.wooden.WoodenGolemEntity;
import com.klikli_dev.golemancyartifice.content.golem.core.status.CoreRunState;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.DustParticleOptions;

public final class GolemCoreStatusParticles {
    private static final DustParticleOptions UNCONFIGURED = new DustParticleOptions(0xFF0000, 1.0F);
    private static final DustParticleOptions BLOCKED = new DustParticleOptions(0xFF8000, 1.0F);

    public void tick(WoodenGolemEntity entity) {
        if (!(entity.level() instanceof ClientLevel level) || entity.tickCount % 10 != 0) {
            return;
        }

        DustParticleOptions particle = switch (entity.currentCoreRunState()) {
            case UNCONFIGURED -> UNCONFIGURED;
            case BLOCKED -> BLOCKED;
            case RUNNABLE -> null;
        };

        if (particle != null) {
            level.addParticle(particle, entity.getX(), entity.getY() + 1.4D, entity.getZ(), 0.0D, 0.02D, 0.0D);
        }
    }
}
