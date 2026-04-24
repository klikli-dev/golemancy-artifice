// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.golemancyartifice.datagen;

import com.klikli_dev.golemancyartifice.datagen.lang.ENUSLanguageProvider;
import com.klikli_dev.golemancyartifice.datagen.model.GolemancyArtificeModelProvider;
import net.minecraft.data.DataGenerator;
import net.neoforged.neoforge.data.event.GatherDataEvent;

public class DataGenerators {

    public static void onGatherData(GatherDataEvent.Client event) {
        DataGenerator generator = event.getGenerator();

        generator.addProvider(true, new GolemancyArtificeModelProvider(generator.getPackOutput()));

        var enUSProvider = new ENUSLanguageProvider(generator.getPackOutput());
        generator.addProvider(true, enUSProvider);
    }
}
