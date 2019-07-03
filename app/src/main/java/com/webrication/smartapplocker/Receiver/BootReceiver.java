
package com.webrication.smartapplocker.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import com.webrication.smartapplocker.other.Constants;
import com.webrication.smartapplocker.Service.AppLaunchDectionService;

import java.util.Objects;

public class BootReceiver extends BroadcastReceiver
{
    static final String ACTION = "android.intent.action.BOOT_COMPLETED";
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onReceive(Context context, Intent intent) {
        // BOOT_COMPLETED” start Service

        if (Objects.equals(intent.getAction(), ACTION)) {
            //Service
            Intent serviceIntent = new Intent(context, AppLaunchDectionService.class);
            serviceIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
            context.startService(serviceIntent);
            Toast.makeText(context, "Boot completed", Toast.LENGTH_SHORT).show();
        }
    }
}





















































