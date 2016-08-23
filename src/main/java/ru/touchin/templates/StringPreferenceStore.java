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

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import ru.touchin.roboswag.core.observables.storable.SafeStore;

/**
 * Created by Gavriil Sitnikov on 23/08/2016.
 * TODO
 */
public class StringPreferenceStore implements SafeStore<String, String> {

    @NonNull
    private final SharedPreferences preferences;

    public StringPreferenceStore(@NonNull final SharedPreferences preferences) {
        this.preferences = preferences;
    }

    @Override
    public boolean contains(@NonNull final String key) {
        return preferences.contains(key);
    }

    @Override
    public void storeObject(@NonNull final Class<String> stringClass, @NonNull final String key, @Nullable final String object) {
        if (object == null) {
            preferences.edit().remove(key).apply();
        } else {
            preferences.edit().putString(key, object).apply();
        }
    }

    @Nullable
    @Override
    public String loadObject(@NonNull final Class<String> stringClass, @NonNull final String key) {
        return preferences.getString(key, null);
    }

}