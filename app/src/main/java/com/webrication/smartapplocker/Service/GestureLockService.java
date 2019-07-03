package com.webrication.smartapplocker.Service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.webrication.smartapplocker.Receiver.PhoneStateReceiver;
import com.webrication.smartapplocker.R;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by pc on 2/2/2018.
 */

public class GestureLockService extends Service implements View.OnClickListener
{
    private WindowManager mWindowManager;
    private View mFloatingView;
    ImageView imageView;
    private GestureLibrary library;
    GestureOverlayView view;
    TextView textView;
    WindowManager.LayoutParams params;

    public GestureLockService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        registerReceiver(new PhoneStateReceiver(),new IntentFilter("android.intent.action.PHONE_STATE"));
        //getting the widget layout from xml using layout inflater
        mFloatingView = LayoutInflater.from(this).inflate(R.layout.custom_gesture_service, null);
        imageView=(ImageView)mFloatingView.findViewById(R.id.back);
        view = (GestureOverlayView)mFloatingView.findViewById(R.id.gestureOverlayView);
        textView=(TextView)mFloatingView.findViewById(R.id.tv_gesture_desc);
        //setting the layout parameters
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

        //getting windows services and adding the floating view to it
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mFloatingView, params);
        imageView.setOnClickListener(this);
        GestureProcessor gestureProcessor=new GestureProcessor();
        view.addOnGestureListener(gestureProcessor);
        File gestureFile = new File(Environment.getDataDirectory() + "/data/" + getPackageName(),"gesture");
        library = GestureLibraries.fromFile(gestureFile);
        library.load();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatingView != null) mWindowManager.removeView(mFloatingView);
    }

    @Override
    public void onClick(View v) {

        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
        stopSelf();
    }
    private class GestureProcessor implements GestureOverlayView.OnGestureListener
    {
        @Override
        public void onGestureStarted(GestureOverlayView overlay, MotionEvent event) {
            textView.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onGesture(GestureOverlayView overlay, MotionEvent event) {

        }

        @Override
        public void onGestureEnded(GestureOverlayView overlay, MotionEvent event)
        {
            ArrayList<Prediction> list = library.recognize(overlay.getGesture());
            float score = (float) list.get(0).score;
            if(Float.isNaN(score) || score < 2)
            {
                // gesture do not match
                Toast.makeText(GestureLockService.this,"Invalid Gesture", Toast.LENGTH_SHORT).show();
            } else {
                // gestures match

                stopSelf();
            }
        }

        @Override
        public void onGestureCancelled(GestureOverlayView overlay, MotionEvent event) {

        }
    }
}
