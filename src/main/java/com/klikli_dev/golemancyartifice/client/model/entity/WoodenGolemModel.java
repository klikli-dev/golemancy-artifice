// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.golemancyartifice.client.model.entity;

import com.geckolib.model.DefaultedEntityGeoModel;
import com.klikli_dev.golemancyartifice.common.entity.WoodenGolemEntity;
import com.klikli_dev.golemancyartifice.registry.ModEntities;

public class WoodenGolemModel extends DefaultedEntityGeoModel<WoodenGolemEntity> {
    public WoodenGolemModel() {
        super(ModEntities.WOODEN_GOLEM.get());
    }
}
