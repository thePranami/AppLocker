package com.webrication.smartapplocker.Activity;

import android.content.Intent;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.webrication.smartapplocker.fragment.ForgetDialogFragment;
import com.webrication.smartapplocker.other.PrefrancesUtils;
import com.webrication.smartapplocker.R;

import java.io.File;
import java.util.ArrayList;

public class GestureUnlockActivity extends AppCompatActivity implements ForgetDialogFragment.OnCallbackReceived
{
    private GestureLibrary library;
    private String mPackage;
    private boolean unlockSelf;
    private boolean changeLock;
    ForgetDialogFragment dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_gesture_unlock);
        GestureOverlayView view = (GestureOverlayView) findViewById(R.id.gestureOverlayView);
        view.addOnGestureListener(new GestureProcessor());
        File gestureFile = new File(Environment.getDataDirectory() + "/data/" + getPackageName(),"gesture");
        library = GestureLibraries.fromFile(gestureFile);
    }

    @Override
    protected void onStart() {
        super.onStart();
        findViewById(R.id.id_btn_forgot_gesture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (PrefrancesUtils.getAnswer()!=null)
                {
                    showDialog();
                }
                else
                {
                    Toast.makeText(GestureUnlockActivity.this, "Security Question Not Set", Toast.LENGTH_SHORT).show();
                }
            }
        });
        library.load();
    }

    @Override
    public void Update(String answer) {

        if (answer !=null && !answer.isEmpty())
        {
            if (answer.equalsIgnoreCase(PrefrancesUtils.getAnswer()))
            {
                PrefrancesUtils.setgesture(null);
                PrefrancesUtils.setQuestin(null);
                PrefrancesUtils.setAnswer(null);
                Intent intent=new Intent(GestureUnlockActivity.this,GestureLockActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                dialog.dismiss();
                finish();
            }
            else
            {
                Toast.makeText(this, "Wrong Answer", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            dialog.dismiss();
        }
    }

    private class GestureProcessor implements GestureOverlayView.OnGestureListener {
        @Override
        public void onGestureStarted(GestureOverlayView overlay, MotionEvent event) {
            findViewById(R.id.tv_gesture_desc).setVisibility(View.INVISIBLE);
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
                Toast.makeText(GestureUnlockActivity.this,"Invalid Gesture", Toast.LENGTH_SHORT).show();
            } else {
                // gestures match
                    Intent intent=new Intent(GestureUnlockActivity.this,MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    finish();
            }
        }

        @Override
        public void onGestureCancelled(GestureOverlayView overlay, MotionEvent event) {

        }
    }

    public void showDialog()
    {
        FragmentManager manager=getSupportFragmentManager();
        dialog=new ForgetDialogFragment();
        dialog.show(manager,"Dialog");
    }

    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
        super.onBackPressed();
    }
}
