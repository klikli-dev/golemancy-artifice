// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.golemancyartifice.content.golem.core.transfer;

import com.klikli_dev.golemancyartifice.content.golem.core.status.CoreDiagnostic;
import java.util.List;

public final class InventoryTransferDiagnostics {
    public List<CoreDiagnostic> diagnostics(InventoryTransferRuntime runtime) {
        return runtime.diagnostics();
    }
}
