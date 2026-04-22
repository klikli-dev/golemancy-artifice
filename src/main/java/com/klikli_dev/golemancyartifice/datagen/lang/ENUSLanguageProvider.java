// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.golemancyartifice.datagen.lang;

import com.klikli_dev.golemancyartifice.GolemancyArtifice;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class ENUSLanguageProvider extends LanguageProvider {
    public ENUSLanguageProvider(PackOutput output) {
        super(output, GolemancyArtifice.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        this.addConfig();
    }

    protected void addConfig(){
        this.add(GolemancyArtifice.MODID + ".configuration.title", "Golemancy: Artifice Configs");
    }
}
