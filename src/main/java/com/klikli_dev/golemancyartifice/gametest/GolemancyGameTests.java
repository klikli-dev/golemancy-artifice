// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.golemancyartifice.gametest;

import com.klikli_dev.golemancyartifice.GolemancyArtifice;
import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;

public final class GolemancyGameTests {
    private GolemancyGameTests() {
    }

    public static void onRegisterGameTests(RegisterGameTestsEvent event) {
        Holder<TestEnvironmentDefinition<?>> environment = event.registerEnvironment(
                Identifier.fromNamespaceAndPath(GolemancyArtifice.MODID, "default")
        );

        event.registerTest(
                Identifier.fromNamespaceAndPath(GolemancyArtifice.MODID, "core_item_exposes_definition"),
                new DirectGameTestInstance(
                        new TestData<>(environment, Identifier.withDefaultNamespace("empty"), 20, 0, true)
                        , GolemancyGameTestFunctions.CORE_ITEM_EXPOSES_DEFINITION_FUNCTION
                )
        );

        event.registerTest(
                Identifier.fromNamespaceAndPath(GolemancyArtifice.MODID, "golem_accepts_transfer_core"),
                new DirectGameTestInstance(
                        new TestData<>(environment, Identifier.withDefaultNamespace("empty"), 40, 0, true)
                        , GolemancyGameTestFunctions.GOLEM_ACCEPTS_TRANSFER_CORE_FUNCTION
                )
        );
    }
}
