// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.golemancyartifice.content.entity.golem.wooden;

import com.geckolib.model.DefaultedGeoModel;
import com.klikli_dev.golemancyartifice.GolemancyArtifice;
import net.minecraft.resources.Identifier;

public class WoodenGolemModel extends DefaultedGeoModel<WoodenGolemEntity> {
    public WoodenGolemModel() {
        super(Identifier.fromNamespaceAndPath(GolemancyArtifice.MODID, "wooden_golem"));
    }

    @Override
    protected String subtype() {
        return "entity";
    }
}
