package com.siuli.andr.whitebird.syncadapter;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by william on 2/4/2016.
 */
public class NoteSyncService extends Service {

    private static final Object sSyncAdapterLock = new Object();
    private static NoteSyncAdapter sSyncAdapter = null;

    @Override
    public void onCreate() {
        synchronized (sSyncAdapterLock){
            if(sSyncAdapter == null){
                sSyncAdapter = new NoteSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return sSyncAdapter.getSyncAdapterBinder();
    }
}
