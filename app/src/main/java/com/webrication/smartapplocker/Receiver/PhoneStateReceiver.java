package com.webrication.smartapplocker.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.webrication.smartapplocker.other.PrefrancesUtils;
import com.webrication.smartapplocker.Service.PinLockService;

/**
 * Created by pc on 2/5/2018.
 */

public class PhoneStateReceiver extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent) {


        String type = PrefrancesUtils.getLockType();
        if (type.equalsIgnoreCase("pin"))
        {
            context.stopService(new Intent(context,PinLockService.class));
        }
        else if (type.equalsIgnoreCase("pattern"))
        {
            context.stopService(new Intent(context,PinLockService.class));
        }
        else if (type.equalsIgnoreCase("gesture"))
        {
            context.stopService(new Intent(context,PinLockService.class));
        }
    }
}
