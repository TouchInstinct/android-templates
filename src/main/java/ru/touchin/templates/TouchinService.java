/*
 *  Copyright (c) 2016 Touch Instinct
 *
 *  This file is part of RoboSwag library.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package ru.touchin.templates;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;

import ru.touchin.roboswag.components.utils.UiUtils;
import ru.touchin.roboswag.components.utils.destroyable.BaseDestroyable;
import ru.touchin.roboswag.core.log.Lc;
import ru.touchin.roboswag.core.utils.ServiceBinder;

/**
 * Created by Gavriil Sitnikov on 10/01/17.
 * Base class of service to extends for Touch Instinct related projects.
 */
public abstract class TouchinService extends Service {

    @NonNull
    protected final BaseDestroyable destroyable = new BaseDestroyable();

    @Override
    public void onCreate() {
        super.onCreate();
        UiUtils.UI_LIFECYCLE_LC_GROUP.i(Lc.getCodePoint(this));
    }

    @NonNull
    @Override
    public IBinder onBind(@NonNull final Intent intent) {
        return new ServiceBinder<>(this);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        UiUtils.UI_LIFECYCLE_LC_GROUP.i(Lc.getCodePoint(this));
    }

    @Override
    public void onDestroy() {
        UiUtils.UI_LIFECYCLE_LC_GROUP.i(Lc.getCodePoint(this));
        destroyable.onDestroy();
        super.onDestroy();
    }

}

