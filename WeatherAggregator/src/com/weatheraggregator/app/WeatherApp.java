package com.weatheraggregator.app;

import com.activeandroid.ActiveAndroid;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class WeatherApp extends com.activeandroid.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);

        // Use a quarter of VMs available memory, this is a good approach
        long maxMemory = Runtime.getRuntime().maxMemory() / 4;
        // Don't use less than 4MB of memory cache
        // You may vary this value depnding on the average bitmap size
        maxMemory = Math.max(1024L * 1024L * 4L, maxMemory);

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).denyCacheImageMultipleSizesInMemory().memoryCache(new LruMemoryCache((int) maxMemory)).writeDebugLogs().build();
        ImageLoader.getInstance().init(config);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ActiveAndroid.dispose();
    }
}
