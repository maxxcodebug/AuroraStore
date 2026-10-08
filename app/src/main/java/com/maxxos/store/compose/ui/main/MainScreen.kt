/*
 * SPDX-FileCopyrightText: 2026 Aurora OSS
 * SPDX-License-Identifier: GPL-3.0-or-later
 */


/*
 * Copyright (C) 2026 MaxxOS. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.maxxos.store.compose.ui.main

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aurora.extensions.requiresObbDir
import com.maxxos.store.MainViewModel
import com.maxxos.store.R
import com.maxxos.store.compose.composable.InsufficientStorageDialog
import com.maxxos.store.compose.composable.MaxxBackground
import com.maxxos.store.compose.composable.maxxGreeting
import com.maxxos.store.compose.composable.MaxxNavigationPill
import com.maxxos.store.compose.composable.TrackerUpdateWarningDialog
import com.maxxos.store.compose.composition.LocalNetworkStatus
import com.maxxos.store.compose.navigation.Destination
import com.maxxos.store.compose.ui.apps.AppsGamesScreen
import com.maxxos.store.compose.ui.commons.MoreSheet
import com.maxxos.store.compose.ui.commons.NetworkScreen
import com.maxxos.store.compose.ui.sheets.AppUpdateSheet
import com.maxxos.store.compose.ui.updates.UpdatesScreen
import com.maxxos.store.data.model.ExodusTracker
import com.maxxos.store.data.model.NetworkStatus
import com.maxxos.store.data.model.PermissionType
import com.maxxos.store.data.model.StorageRequirement
import com.maxxos.store.data.providers.PermissionProvider.Companion.isGranted
import com.maxxos.store.data.room.update.Update
import com.maxxos.store.util.PackageUtil
import com.maxxos.store.util.Preferences
import com.maxxos.store.util.Preferences.PREFERENCE_UPDATES_WARN_TRACKERS
import com.maxxos.store.util.StorageUtil
import com.maxxos.store.viewmodel.all.UpdatesViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

private enum class MainTab(
    @StringRes val labelRes: Int,
    @DrawableRes val iconRes: Int
) {
    APPS(R.string.title_apps, R.drawable.ic_apps),
    GAMES(R.string.title_games, R.drawable.ic_games),
    UPDATES(R.string.title_updates, R.drawable.ic_updates)
}


@Composable
private fun MaxxHomeHeader(
    onMenu: () -> Unit,
    onSearch: () -> Unit
) {
    Surface(
        color = androidx.compose.material3.MaterialTheme.colorScheme.background.copy(alpha = 0.94f),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(start = 20.dp, end = 8.dp, top = 10.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(1.dp)) {
                Text(
                    text = "Maxx Store",
                    style = androidx.compose.material3.MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = maxxGreeting(),
                    style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                    color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            HeaderIconButton(R.drawable.ic_round_search, "Search", onSearch)
            HeaderIconButton(R.drawable.ic_settings_account, "Menu", onMenu)
        }
    }
}

@Composable
private fun HeaderIconButton(icon: Int, description: String, onClick: () -> Unit, badge: Int = 0) {
    Box {
        IconButton(onClick = onClick) {
            Icon(painterResource(icon), contentDescription = description)
        }
        if (badge > 0) {
            Surface(
                modifier = Modifier.align(Alignment.TopEnd).padding(top = 4.dp, end = 3.dp),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp),
                color = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                contentColor = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary
            ) {
                Text(badge.coerceAtMost(99).toString(), modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp), style = androidx.compose.material3.MaterialTheme.typography.labelSmall)
            }
        }
    }
}

@Composable
fun MainScreen(
    initialTab: Int = 0,
    mainViewModel: MainViewModel = hiltViewModel(),
    updatesViewModel: UpdatesViewModel = hiltViewModel(),
    onNavigateTo: (Destination) -> Unit = {}
) {
    val context = LocalContext.current
    val networkStatus = LocalNetworkStatus.current
    val updates by mainViewModel.updateHelper.updates.collectAsStateWithLifecycle(
        initialValue = null
    )
    val updateCount = updates?.size ?: 0
    val downloads by updatesViewModel.downloadsList.collectAsStateWithLifecycle()

    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(
        initialPage = initialTab.coerceIn(
            0,
            MainTab.entries.size - 1
        )
    ) {
        MainTab.entries.size
    }

    var showMoreSheet by remember { mutableStateOf(false) }
    var appUpdateTarget by remember { mutableStateOf<Update?>(null) }
    var trackerWarning by remember {
        mutableStateOf<Pair<Update, List<ExodusTracker>>?>(null)
    }
    var storageWarning by remember { mutableStateOf<StorageRequirement?>(null) }
    val checkingJobs = remember { mutableStateMapOf<String, Job>() }

    // A blocked update never produces a download, so the effect below can't clear its marker.
    LaunchedEffect(Unit) {
        updatesViewModel.storageWarning.collect {
            storageWarning = it
            checkingJobs.clear()
        }
    }

    // Once the download a check kicked off actually appears, drop the "checking" marker so the
    // item's in-progress state is driven purely by the download (no flash back to "Update").
    LaunchedEffect(downloads) {
        checkingJobs.keys.toList().forEach { pkg ->
            if (downloads.any { it.packageName == pkg && !it.isFinished }) {
                checkingJobs.remove(pkg)
            }
        }
    }

    fun handleNavigation(destination: Destination) {
        when (destination) {
            is Destination.AppUpdate -> appUpdateTarget = destination.update
            else -> onNavigateTo(destination)
        }
    }

    fun performUpdate(update: Update) {
        if (update.fileList.requiresObbDir() &&
            !isGranted(context, PermissionType.STORAGE_MANAGER)
        ) {
            checkingJobs.remove(update.packageName)
            onNavigateTo(
                Destination.PermissionRationale(setOf(PermissionType.STORAGE_MANAGER))
            )
        } else {
            updatesViewModel.download(update)
        }
    }

    if (networkStatus == NetworkStatus.UNAVAILABLE) {
        NetworkScreen()
        return
    }

    if (showMoreSheet) {
        MoreSheet(
            onDismiss = { showMoreSheet = false },
            onNavigateTo = { destination ->
                showMoreSheet = false
                onNavigateTo(destination)
            }
        )
    }

    appUpdateTarget?.let { app ->
        AppUpdateSheet(
            update = app,
            onDismiss = { appUpdateTarget = null },
            onNavigateTo = { destination ->
                appUpdateTarget = null
                onNavigateTo(destination)
            }
        )
    }

    Scaffold(
        containerColor = androidx.compose.ui.graphics.Color.Transparent,
        topBar = {
            MaxxHomeHeader(
                onMenu = { showMoreSheet = true },
                onSearch = { onNavigateTo(Destination.Search) }
            )
        },
        bottomBar = {
            MaxxNavigationPill(
                selectedIndex = pagerState.currentPage,
                updateCount = updateCount,
                onSelected = { index ->
                    coroutineScope.launch { pagerState.animateScrollToPage(index) }
                }
            )
        }
    ) { paddingValues ->
        MaxxBackground {
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .consumeWindowInsets(paddingValues)
                    .fillMaxSize()
            ) {
            HorizontalPager(
                state = pagerState,
                userScrollEnabled = false,
                beyondViewportPageCount = MainTab.entries.size - 1,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                when (MainTab.entries[page]) {
                    MainTab.APPS -> AppsGamesScreen(
                        pageType = 0,
                        onNavigateTo = onNavigateTo
                    )
                    MainTab.GAMES -> AppsGamesScreen(
                        pageType = 1,
                        onNavigateTo = ::handleNavigation
                    )
                    MainTab.UPDATES -> {
                        UpdatesScreen(
                            viewModel = updatesViewModel,
                            onNavigateTo = ::handleNavigation,
                            onRequestUpdate = { update ->
                                if (!Preferences.getBoolean(
                                        context,
                                        PREFERENCE_UPDATES_WARN_TRACKERS,
                                        false
                                    )
                                ) {
                                    performUpdate(update)
                                } else {
                                    val job = coroutineScope.launch {
                                        val installedVc = PackageUtil.getInstalledVersionCode(
                                            context,
                                            update.packageName
                                        )
                                        val trackers = updatesViewModel.getNewTrackers(
                                            update.packageName,
                                            installedVc
                                        )
                                        if (trackers.isEmpty()) {
                                            performUpdate(update)
                                        } else {
                                            trackerWarning = update to trackers
                                        }
                                    }
                                    checkingJobs[update.packageName] = job
                                }
                            },
                            onRequestUpdateAll = { selectedUpdates ->
                                val needsObb = selectedUpdates.any {
                                    it.fileList.requiresObbDir()
                                }
                                if (needsObb &&
                                    !isGranted(context, PermissionType.STORAGE_MANAGER)
                                ) {
                                    onNavigateTo(
                                        Destination.PermissionRationale(
                                            setOf(PermissionType.STORAGE_MANAGER)
                                        )
                                    )
                                } else {
                                    updatesViewModel.downloadAll(selectedUpdates)
                                }
                            },
                            onCancelUpdate = { packageName ->
                                if (downloads.any {
                                        it.packageName == packageName && !it.isFinished
                                    }
                                ) {
                                    checkingJobs.remove(packageName)
                                    updatesViewModel.cancelDownload(packageName)
                                } else {
                                    checkingJobs.remove(packageName)?.cancel()
                                }
                            },
                            onCancelAll = { updatesViewModel.cancelAll() },
                            checkingPackages = checkingJobs.keys
                        )
                    }
                }
            }
            }
        }
    }

    trackerWarning?.let { (update, trackers) ->
        TrackerUpdateWarningDialog(
            trackers = trackers,
            onConfirm = {
                val pending = update
                trackerWarning = null
                performUpdate(pending)
            },
            onDismiss = {
                trackerWarning = null
                checkingJobs.remove(update.packageName)
            }
        )
    }

    storageWarning?.let { requirement ->
        InsufficientStorageDialog(
            requirement = requirement,
            onFreeUpSpace = {
                storageWarning = null
                StorageUtil.openFreeUpSpace(context)
            },
            onDismiss = { storageWarning = null }
        )
    }
}
