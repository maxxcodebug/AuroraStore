// Copyright (C) 2026 MaxxOS. All rights reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
/*
 * SPDX-FileCopyrightText: 2026 Aurora OSS
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.aurora.store.data.model

import androidx.annotation.StringRes
import com.aurora.store.R

enum class DownloadStatus(@StringRes val localized: Int) {
    DOWNLOADING(R.string.status_downloading),
    FAILED(R.string.status_failed),
    CANCELLED(R.string.status_cancelled),
    COMPLETED(R.string.status_completed),
    QUEUED(R.string.status_queued),
    UNAVAILABLE(R.string.status_unavailable),
    VERIFYING(R.string.status_verifying),
    PURCHASING(R.string.preparing_to_install),
    AWAITING_INSTALL(R.string.status_awaiting_install),
    INSTALLING(R.string.status_installing),
    INSTALLED(R.string.status_installed);

    companion object {
        val finished = setOf(FAILED, CANCELLED, COMPLETED, INSTALLED)
        val running = setOf(QUEUED, PURCHASING, DOWNLOADING)

        /**
         * States in which a download worker is actively occupying the (single) download
         * slot — purchasing, transferring bytes or verifying. Used to serialize downloads:
         * the next [QUEUED] item is only started once none of these are in progress, so
         * concurrent workers can't clobber the shared foreground/progress notification.
         */
        val processing = setOf(PURCHASING, DOWNLOADING, VERIFYING)

        /** States reached only once the download was handed off to the installer. */
        val installerStates = setOf(AWAITING_INSTALL, INSTALLING, INSTALLED)

        val installable = setOf(COMPLETED, AWAITING_INSTALL)
    }
}
