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

package com.maxxos.store.compose.ui.about

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import com.maxxos.store.R
import com.maxxos.store.compose.preview.ThemePreviewProvider

/**
 * Dialog for displaying information about Aurora Store
 * @param onDismiss Callback on dismiss
 */
@Composable
fun AboutDialog(onDismiss: () -> Unit = {}) {
    AlertDialog(
        title = { Text(text = stringResource(R.string.about_aurora_store_title)) },
        text = {
            Text(text = stringResource(R.string.about_aurora_store_summary))
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(android.R.string.ok))
            }
        }
    )
}

@PreviewWrapper(ThemePreviewProvider::class)
@Preview
@Composable
private fun AboutDialogPreview() {
    AboutDialog()
}
