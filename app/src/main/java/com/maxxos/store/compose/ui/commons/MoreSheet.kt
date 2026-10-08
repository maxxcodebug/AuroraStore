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

package com.maxxos.store.compose.ui.commons

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.maxxos.store.R
import com.maxxos.store.compose.navigation.Destination
import com.maxxos.store.viewmodel.commons.MoreViewModel

private data class MoreItem(
    @StringRes val titleRes: Int,
    @DrawableRes val iconRes: Int,
    val onClick: () -> Unit
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreSheet(
    onDismiss: () -> Unit,
    onNavigateTo: (Destination) -> Unit = {},
    viewModel: MoreViewModel = hiltViewModel()
) {
    val mainItems = listOf(
        MoreItem(R.string.title_notifications, R.drawable.ic_notifications) {
            onNavigateTo(Destination.Notifications)
        },
        MoreItem(R.string.title_download_manager, R.drawable.ic_download_manager) {
            onNavigateTo(Destination.Downloads)
        },
        MoreItem(R.string.title_apps_games, R.drawable.ic_apps) {
            onNavigateTo(Destination.Installed)
        },
        MoreItem(R.string.title_blacklist_manager, R.drawable.ic_blacklist) {
            onNavigateTo(Destination.Blacklist)
        },
        MoreItem(R.string.title_favourites_manager, R.drawable.ic_favorite_unchecked) {
            onNavigateTo(Destination.Favourite)
        },
        MoreItem(R.string.title_spoof_manager, R.drawable.ic_spoof) {
            onNavigateTo(Destination.Spoof)
        }
    )
    val extraItems = listOf(
        MoreItem(R.string.title_settings, R.drawable.ic_menu_settings) {
            onNavigateTo(Destination.Settings)
        },
        MoreItem(R.string.title_about, R.drawable.ic_menu_about) { onNavigateTo(Destination.About) }
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(Modifier.fillMaxWidth().navigationBarsPadding().padding(horizontal = 16.dp)) {
            AccountHeader(viewModel, onNavigateToAccounts)
            Spacer(Modifier.height(4.dp))
            mainItems.forEach { item ->
                MaxxMenuItem(item, onDismiss)
            }
            Spacer(Modifier.height(8.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f))
            Spacer(Modifier.height(8.dp))
            extraItems.forEach { item ->
                MaxxMenuItem(item, onDismiss)
            }
            Spacer(Modifier.height(10.dp))
        }
    }
}

@Composable
private fun MaxxMenuItem(item: MoreItem, onDismiss: () -> Unit) {
    Card(
        onClick = { item.onClick(); onDismiss() },
        modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.48f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(42.dp).clip(RoundedCornerShape(14.dp)).background(MaterialTheme.colorScheme.primaryContainer), contentAlignment = Alignment.Center) {
                Icon(painterResource(item.iconRes), contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            }
            Text(stringResource(item.titleRes), modifier = Modifier.weight(1f).padding(start = 14.dp), style = MaterialTheme.typography.titleSmall)
            Icon(painterResource(R.drawable.ic_arrow_right), contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}


@Composable
private fun AccountHeader(viewModel: MoreViewModel, onNavigateToAccounts: () -> Unit) {
    val context = LocalContext.current
    val isAnonymous = viewModel.authProvider.isAnonymous

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(viewModel.authProvider.authData?.userProfile?.artwork?.url)
                .crossfade(true)
                .build(),
            contentDescription = null,
            placeholder = painterResource(R.drawable.ic_account),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .requiredSize(40.dp)
                .clip(CircleShape)
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = dimensionResource(R.dimen.spacing_medium))
        ) {
            Text(
                text = if (isAnonymous) {
                    stringResource(R.string.account_anonymous)
                } else {
                    viewModel.authProvider.authData?.userProfile?.name
                        ?: stringResource(R.string.status_unavailable)
                },
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = viewModel.authProvider.authData?.userProfile?.email
                    ?: stringResource(R.string.status_unavailable),
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        IconButton(onClick = onNavigateToAccounts) {
            Icon(
                painter = painterResource(R.drawable.ic_account_manager),
                contentDescription = stringResource(R.string.manage_account)
            )
        }
    }
}
