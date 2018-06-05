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

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import ru.touchin.roboswag.core.log.Lc;
import ru.touchin.roboswag.core.observables.RxAndroidUtils;

/**
 * Utility class that is providing common methods related to android device.
 */
public final class DeviceUtils {

    private static final String BUGGED_DEVICE_ID = "9774d56d682e549c";
    private static final String DEVICE_ID_PREFS = "device_id_prefs";
    private static final String DEVICE_ID = "device_id";

    /**
     * Returns a unique UUID for the current android device. As with all UUIDs,
     * this unique ID is "very highly likely" to be unique across all Android
     * devices. Much more so than ANDROID_ID is.
     * <p/>
     * The UUID is generated by using ANDROID_ID as the base key if appropriate,
     * falling back on TelephonyManager.getDeviceID() if ANDROID_ID is known to
     * be incorrect, and finally falling back on a random UUID that's persisted
     * to SharedPreferences if getDeviceID() does not return a usable value.
     * <p/>
     * In some rare circumstances, this ID may change. In particular, if the
     * device is factory reset a new device ID may be generated. In addition, if
     * a user upgrades their phone from certain buggy implementations of Android
     * 2.2 to a newer, non-buggy version of Android, the device ID may change.
     * Or, if a user uninstalls your app on a device that has neither a proper
     * Android ID nor a Device ID, this ID may change on reinstallation.
     * <p/>
     * Note that if the code falls back on using TelephonyManager.getDeviceId(),
     * the resulting ID will NOT change after a factory reset. Something to be
     * aware of.
     * <p/>
     * Works around a bug in Android 2.2 for many devices when using ANDROID_ID
     * directly.
     *
     * @return a UUID that may be used to uniquely identify your device for most purposes.
     * @see "http://code.google.com/p/android/issues/detail?id=10603"
     */
    @NonNull
    public static UUID getDeviceUuid(@NonNull final Context context) {
        final SharedPreferences sharedPreferences = context.getSharedPreferences(DEVICE_ID_PREFS, Context.MODE_PRIVATE);
        if (sharedPreferences.getString(DEVICE_ID, null) != null) {
            return UUID.fromString(sharedPreferences.getString(DEVICE_ID, null));
        }
        final String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        try {
            final UUID uuid;
            if (!TextUtils.equals(androidId, BUGGED_DEVICE_ID)) {
                uuid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8"));
            } else {
                @SuppressLint("MissingPermission")
                final String deviceId = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
                uuid = deviceId != null ? UUID.nameUUIDFromBytes(deviceId.getBytes("utf8")) : UUID.randomUUID();
            }
            sharedPreferences.edit().putString(DEVICE_ID, uuid.toString()).apply();
            return uuid;
        } catch (final UnsupportedEncodingException error) {
            Lc.assertion(error);
            final UUID randomUuid = UUID.randomUUID();
            sharedPreferences.edit().putString(DEVICE_ID, randomUuid.toString()).apply();
            return randomUuid;
        }
    }

    /**
     * Detects active network type.
     *
     * @param context Application context
     * @return Active network type {@link NetworkType}
     */
    @NonNull
    public static NetworkType getNetworkType(@NonNull final Context context) {
        final ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission")
        final NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null || !info.isConnected()) {
            return NetworkType.NONE;
        }
        if (info.getType() == ConnectivityManager.TYPE_WIFI) {
            return NetworkType.WI_FI;
        }
        if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
            final int networkType = info.getSubtype();
            switch (networkType) {
                case TelephonyManager.NETWORK_TYPE_GPRS:
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_CDMA:
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_IDEN:
                    return NetworkType.MOBILE_2G;
                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                case TelephonyManager.NETWORK_TYPE_EHRPD:
                case TelephonyManager.NETWORK_TYPE_HSPAP:
                    return NetworkType.MOBILE_3G;
                case TelephonyManager.NETWORK_TYPE_LTE:
                case 19: // NETWORK_TYPE_LTE_CA is hide
                    return NetworkType.MOBILE_LTE;
                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                default:
                    return NetworkType.UNKNOWN;
            }
        }
        return NetworkType.UNKNOWN;
    }

    /**
     * Detects if some network connected.
     *
     * @param context Application context
     * @return true if network connected, false otherwise.
     */
    public static boolean isNetworkConnected(@NonNull final Context context) {
        return getNetworkType(context) != NetworkType.NONE;
    }


    /**
     * Returns observable to observe is device connected to Wi-Fi network.
     *
     * @param context Context to register BroadcastReceiver to check network state;
     * @return Observable of Wi-Fi connection status.
     */
    @NonNull
    public static Observable<Boolean> observeIsConnectedToWifi(@NonNull final Context context) {
        return RxAndroidUtils.observeBroadcastEvent(context, new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION))
                .map(intent -> {
                    final NetworkInfo networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                    return networkInfo != null && networkInfo.isConnected();
                })
                .distinctUntilChanged();
    }

    /**
     * Returns observable to observe is device connected to the internet.
     *
     * @param context Context to register BroadcastReceiver to check connection to the internet;
     * @return Observable of internet connection status.
     */
    @NonNull
    public static Observable<Boolean> observeIsNetworkConnected(@NonNull final Context context) {
        return Observable.switchOnNext(Observable.fromCallable(() -> {
            final NetworkStateReceiver networkStateReceiver = new NetworkStateReceiver();
            return Observable
                    .<Boolean>create(subscriber -> {
                        context.registerReceiver(networkStateReceiver, NetworkStateReceiver.INTENT_FILTER);
                        subscriber.onNext(isNetworkConnected(context));
                        networkStateReceiver.setEmitter(subscriber);
                    })
                    .doOnDispose(() -> context.unregisterReceiver(networkStateReceiver))
                    .onErrorReturnItem(false)
                    .distinctUntilChanged();
        }));
    }

    /**
     * Create an Observable that depends on network connection.
     *
     * @param processObservable - Observable to which we subscribe in the availability of the Internet;
     */
    @NonNull
    public static Observable<?> createNetworkDependentObservable(@NonNull final Context context, @NonNull final Observable<?> processObservable) {
        return DeviceUtils.observeIsNetworkConnected(context)
                .debounce(100, TimeUnit.MILLISECONDS)
                .switchMap(connected -> !connected
                        ? Observable.empty()
                        : processObservable);
    }

    /**
     * Create an Observable that depends on network connection.
     *
     * @param processObservable - Observable to which we subscribe in the availability of the Internet;
     */
    @NonNull
    public static Observable<?> createNetworkDependentObservable(@NonNull final Context context, @NonNull final Completable processObservable) {
        return createNetworkDependentObservable(context, processObservable.toObservable());
    }

    private DeviceUtils() {
    }

    /**
     * Available network types.
     */
    public enum NetworkType {
        /**
         * Mobile 2G network.
         */
        MOBILE_2G("2g"),
        /**
         * Mobile 3G network.
         */
        MOBILE_3G("3g"),
        /**
         * Mobile LTE network.
         */
        MOBILE_LTE("lte"),
        /**
         * Wi-Fi network.
         */
        WI_FI("Wi-Fi"),
        /**
         * Unknown network type.
         */
        UNKNOWN("unknown"),
        /**
         * No network.
         */
        NONE("none");

        @NonNull
        private final String name;

        NetworkType(@NonNull final String name) {
            this.name = name;
        }

        /**
         * @return Network type readable name.
         */
        @NonNull
        public String getName() {
            return name;
        }

    }

    private static class NetworkStateReceiver extends BroadcastReceiver {

        private static final IntentFilter INTENT_FILTER = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        @Nullable
        private ConnectivityManager connectivityManager;

        @Nullable
        private ObservableEmitter<? super Boolean> emitter;

        public void setEmitter(@Nullable final ObservableEmitter<? super Boolean> emitter) {
            this.emitter = emitter;
        }

        public void onReceive(@NonNull final Context context, @NonNull final Intent intent) {
            if (connectivityManager == null) {
                connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            }
            if (emitter != null) {
                emitter.onNext(isNetworkConnected(context));
            }
        }
    }

}