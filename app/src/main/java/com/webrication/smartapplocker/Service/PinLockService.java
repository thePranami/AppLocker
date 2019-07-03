package com.webrication.smartapplocker.Service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.webrication.smartapplocker.Adapter.ApplicationAdapter;
import com.webrication.smartapplocker.Model.AllAppListModel;
import com.webrication.smartapplocker.Model.AppDetailsModel;
import com.webrication.smartapplocker.Receiver.PhoneStateReceiver;
import com.webrication.smartapplocker.other.PrefrancesUtils;
import com.webrication.smartapplocker.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pc on 2/2/2018.
 */

public class PinLockService extends Service implements View.OnClickListener
{
    private static final int MAX = 3;
    private int count;
    private String mPassword="";

    private WindowManager mWindowManager;
    WindowManager.LayoutParams params;
    private View mFloatingView;
    ImageView imageView, appicon;
    TextView pass_text;
    RelativeLayout layout;
    AllAppListModel detailsModel;
    ApplicationAdapter adapter;
    Context context;
    private List<ActivityManager.RunningAppProcessInfo> process;
    private ActivityManager activityMan;
    PackageManager packageManager;

    public PinLockService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        registerReceiver(new PhoneStateReceiver(),new IntentFilter("android.intent.action.PHONE_STATE"));

        mFloatingView = LayoutInflater.from(this).inflate(R.layout.custom_pin_service, null);
        imageView=(ImageView)mFloatingView.findViewById(R.id.back);
        pass_text=(TextView)mFloatingView.findViewById(R.id.pass);
        layout=(RelativeLayout)mFloatingView.findViewById(R.id.rl);
        appicon=(ImageView)mFloatingView.findViewById(R.id.imge222);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {

              params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.TYPE_PHONE,WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    PixelFormat.TRANSLUCENT);
        }
        else {
             params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
        }

        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mFloatingView, params);
        imageView.setOnClickListener(this);

       mFloatingView.findViewById(R.id.zero).setOnClickListener(this);
      mFloatingView.findViewById(R.id.one).setOnClickListener(this);
      mFloatingView.findViewById(R.id.two).setOnClickListener(this);
       mFloatingView.findViewById(R.id.three).setOnClickListener(this);
       mFloatingView.findViewById(R.id.four).setOnClickListener(this);
       mFloatingView.findViewById(R.id.five).setOnClickListener(this);
      mFloatingView.findViewById(R.id.six).setOnClickListener(this);
      mFloatingView.findViewById(R.id.seven).setOnClickListener(this);
      mFloatingView.findViewById(R.id.eight).setOnClickListener(this);
       mFloatingView.findViewById(R.id.nine).setOnClickListener(this);
      mFloatingView.findViewById(R.id.exitImageButton_id).setOnClickListener(this);
      mFloatingView.findViewById(R.id.backspace).setOnClickListener(this);

        activityMan = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        process = activityMan.getRunningAppProcesses();
      Drawable icon = getPackageManager().getApplicationIcon(getApplicationInfo());
        //Drawable ico=getApplicationInfo(process, PackageManager.GET_META_DATA).loadIcon(getPackageManager());
      appicon.setImageDrawable(icon);
       // appicon.setImageDrawable(detailsModel.getAppIcon());
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (mFloatingView != null) mWindowManager.removeView(mFloatingView);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId()) {
            case R.id.zero:
                count++;
                formPassword(0);
                if(count == 4){
                    validate();
                }
                break;
            case R.id.one:
                count++;
                formPassword(1);
                if(count == 4){
                    validate();
                }
                break;
            case R.id.two:
                count++;
                formPassword(2);
                if(count == 4){
                    validate();
                }
                break;
            case R.id.three:
                count++;
                formPassword(3);
                if(count == 4){
                    validate();
                }
                break;
            case R.id.four:
                count++;
                formPassword(4);
                if(count == 4){
                    validate();
                }
                break;
            case R.id.five:
                count++;
                formPassword(5);
                if(count == 4){
                    validate();
                }
                break;
            case R.id.six:
                count++;
                formPassword(6);
                if(count == 4){
                    validate();
                }
                break;
            case R.id.seven:
                count++;
                formPassword(7);
                if(count == 4){
                    validate();
                }
                break;
            case R.id.eight:
                count++;
                formPassword(8);
                if(count == 4){
                    validate();
                }
                break;
            case R.id.nine:
                count++;
                formPassword(9);
                if(count == 4){
                    validate();
                }
                break;
            case R.id.backspace:
                if(count <1){
                    break;
                }
                if(count == 1){
                    mPassword = "";
                    pass_text.setText(mPassword);
                    count = 0;
                    break;
                }
                char [] pass = mPassword.toCharArray();
                mPassword = "";
                for(int i=0;i<(count -1);++i){
                    mPassword += pass[i];
                }
                count--;
               pass_text.setText(mPassword);
                break;

            case R.id.exitImageButton_id:
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                stopSelf();
                break;

            case R.id.back:
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
                stopSelf();
                break;
            default:
                break;
        }
    }

    private void formPassword(int num){
        mPassword += num;
        ((TextView)mFloatingView.findViewById(R.id.pass)).setText(mPassword);
    }

    private void validate()
    {
        if(PrefrancesUtils.getPIN().equalsIgnoreCase(mPassword))
        {
            // finish this activity
            stopSelf();

        } else {
            Toast.makeText(this, "Invalid PIN", Toast.LENGTH_SHORT).show();
            mPassword = "";
            pass_text.setText(mPassword);
            count = 0;
        }
    }
}
