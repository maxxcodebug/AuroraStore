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

package com.huawei.appmarket.service.externalservice.distribution.thirdsilentinstall;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Keep;

import com.huawei.appgallery.coreservice.internal.framework.ipc.transport.data.BaseIPCRequest;
import com.huawei.appgallery.coreservice.internal.support.parcelable.AutoParcelable;
import com.huawei.appgallery.coreservice.internal.support.parcelable.EnableAutoParcel;

@Keep
public class SilentInstallRequest extends BaseIPCRequest {
    public static final Parcelable.Creator<SilentInstallRequest> CREATOR = new AutoParcelable.AutoCreator<>(SilentInstallRequest.class);

    public static final String METHOD = "method.requestSilentInstall";
    @EnableAutoParcel(1)
    private int sessionId;

    @Override
    public String getMethod() {
        return METHOD;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeInt(sessionId);
    }

    public void readFromParcel(Parcel source) {
        this.sessionId = source.readInt();
    }

    public SilentInstallRequest() {
    }

    protected SilentInstallRequest(Parcel in) {
        this.sessionId = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
