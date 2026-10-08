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
 * SPDX-FileCopyrightText: 2025 The Calyx Institute
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.maxxos.store.viewmodel.details

import androidx.lifecycle.ViewModel
import com.maxxos.store.data.ExodusRepository
import com.maxxos.store.data.model.ExodusTracker
import com.maxxos.store.data.model.Report
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ExodusViewModel @Inject constructor(
    private val exodusRepository: ExodusRepository
) : ViewModel() {

    /**
     * Resolves the tracker ids of [report] to their details via the local tracker table,
     * best-effort (ids missing from the table resolve to a `Tracker #<id>` placeholder).
     */
    suspend fun resolveTrackers(report: Report): List<ExodusTracker> =
        exodusRepository.resolveTrackers(report.trackers)
}
