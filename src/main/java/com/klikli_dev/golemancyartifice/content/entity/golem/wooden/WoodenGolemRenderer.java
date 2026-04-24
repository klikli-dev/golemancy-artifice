// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.golemancyartifice.content.entity.golem.wooden;

import com.geckolib.renderer.GeoEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;

public class WoodenGolemRenderer extends GeoEntityRenderer<WoodenGolemEntity, EntityRenderState> {
    public WoodenGolemRenderer(EntityRendererProvider.Context context) {
        super(context, new WoodenGolemModel());
        this.shadowRadius = 0.6F;
    }
}
