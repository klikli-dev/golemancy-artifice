// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.golemancyartifice.content.golem.core.runtime;

import com.klikli_dev.golemancyartifice.content.golem.core.status.CoreDiagnostic;
import com.klikli_dev.golemancyartifice.content.golem.core.status.CoreRunState;
import java.util.List;

public final class NoCoreRuntime implements ActiveCoreRuntime {
    public static final NoCoreRuntime INSTANCE = new NoCoreRuntime();

    private NoCoreRuntime() {
    }

    @Override
    public CoreRunState runState() {
        return CoreRunState.UNCONFIGURED;
    }

    @Override
    public List<CoreDiagnostic> diagnostics() {
        return List.of();
    }
}
