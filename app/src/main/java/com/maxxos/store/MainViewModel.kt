/*
 * SPDX-FileCopyrightText: 2025 The Calyx Institute
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.maxxos.store

import androidx.lifecycle.ViewModel
import com.maxxos.store.data.helper.UpdateHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val updateHelper: UpdateHelper
) : ViewModel()
