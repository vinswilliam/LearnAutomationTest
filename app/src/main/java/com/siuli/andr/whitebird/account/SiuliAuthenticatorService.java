package com.siuli.andr.whitebird.account;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by william on 1/28/2016.
 */
public class SiuliAuthenticatorService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        SiuliAuthenticator authenticator = new SiuliAuthenticator(this);
        return authenticator.getIBinder();
    }
}
