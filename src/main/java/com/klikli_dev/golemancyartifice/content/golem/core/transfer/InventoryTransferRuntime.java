// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.golemancyartifice.content.golem.core.transfer;

import com.klikli_dev.golemancyartifice.content.golem.core.runtime.ActiveCoreRuntime;
import com.klikli_dev.golemancyartifice.content.golem.core.status.CoreDiagnostic;
import com.klikli_dev.golemancyartifice.content.golem.core.status.CoreRunState;
import java.util.List;
import net.minecraft.core.GlobalPos;

public final class InventoryTransferRuntime implements ActiveCoreRuntime {
    private final GlobalPos source;
    private final GlobalPos destination;
    private final CoreRunState runState;
    private final List<CoreDiagnostic> diagnostics;

    public InventoryTransferRuntime(GlobalPos source, GlobalPos destination, CoreRunState runState, List<CoreDiagnostic> diagnostics) {
        this.source = source;
        this.destination = destination;
        this.runState = runState;
        this.diagnostics = diagnostics;
    }

    @Override
    public CoreRunState runState() {
        return this.runState;
    }

    @Override
    public List<CoreDiagnostic> diagnostics() {
        return this.diagnostics;
    }

    public GlobalPos source() {
        return this.source;
    }

    public GlobalPos destination() {
        return this.destination;
    }
}
