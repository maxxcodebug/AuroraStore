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
 * SPDX-FileCopyrightText: 2021 Aurora OSS
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.aurora.extensions

import android.content.Context
import android.content.DialogInterface
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.maxxos.store.R

fun Context.showDialog(@StringRes titleId: Int, @StringRes messageId: Int) {
    showDialog(getString(titleId), getString(messageId), null, null)
}

fun Context.showDialog(title: String?, message: String?) {
    showDialog(title, message, null, null)
}

fun Context.showDialog(
    title: String?,
    message: String?,
    positiveListener: DialogInterface.OnClickListener?,
    negativeListener: DialogInterface.OnClickListener?
) {
    runOnUiThread {
        val builder = MaterialAlertDialogBuilder(this).apply {
            setTitle(title)
            setMessage(message)

            if (positiveListener != null) {
                setPositiveButton(android.R.string.ok, positiveListener)
            } else {
                setPositiveButton(android.R.string.ok) { dialog, _ -> dialog.dismiss() }
            }

            negativeListener?.let {
                setNegativeButton(android.R.string.cancel, negativeListener)
            }
        }.create()

        builder.show()
    }
}

fun Fragment.showDialog(@StringRes titleId: Int, @StringRes messageId: Int) {
    requireContext().showDialog(titleId, messageId)
}
