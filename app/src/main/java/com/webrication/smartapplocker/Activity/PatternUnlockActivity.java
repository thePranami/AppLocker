package com.webrication.smartapplocker.Activity;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.andrognito.patternlockview.utils.ResourceUtils;
import com.bcgdv.asia.lib.connectpattern.ConnectPatternView;
import com.webrication.smartapplocker.fragment.ForgetDialogFragment;
import com.webrication.smartapplocker.other.PrefrancesUtils;
import com.webrication.smartapplocker.R;

import java.util.List;

public class PatternUnlockActivity extends AppCompatActivity implements ForgetDialogFragment.OnCallbackReceived
{
    ConnectPatternView view;
    private PatternLockView mPatternLockView;
    Button forget;
    ForgetDialogFragment dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_unlock_pattern);
        mPatternLockView = (PatternLockView) findViewById(R.id.patter_lock_view);
        forget=(Button)findViewById(R.id.forget);

        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               showDialog();
            }
        });

        mPatternLockView.setDotCount(3);
        mPatternLockView.setDotNormalSize((int) ResourceUtils.getDimensionInPx(this, R.dimen.pattern_lock_dot_size));
        mPatternLockView.setDotSelectedSize((int) ResourceUtils.getDimensionInPx(this, R.dimen.pattern_lock_dot_selected_size));
        mPatternLockView.setPathWidth((int) ResourceUtils.getDimensionInPx(this, R.dimen.pattern_lock_path_width));
        mPatternLockView.setAspectRatioEnabled(true);
        mPatternLockView.setAspectRatio(PatternLockView.AspectRatio.ASPECT_RATIO_HEIGHT_BIAS);
        mPatternLockView.setViewMode(PatternLockView.PatternViewMode.CORRECT);
        mPatternLockView.setDotAnimationDuration(150);
        mPatternLockView.setPathEndAnimationDuration(100);
        mPatternLockView.setCorrectStateColor(ResourceUtils.getColor(this, R.color.white));
        mPatternLockView.setInStealthMode(false);
        mPatternLockView.setTactileFeedbackEnabled(true);
        mPatternLockView.setInputEnabled(true);

        mPatternLockView.addPatternLockListener(new PatternLockViewListener() {
            @Override
            public void onStarted() {
                Log.e("start", "Pattern drawing started");
            }

            @Override
            public void onProgress(List<PatternLockView.Dot> progressPattern) {
                Log.e("progress", "Pattern progress: " +
                        PatternLockUtils.patternToString(mPatternLockView, progressPattern));
            }

            @Override
            public void onComplete(List<PatternLockView.Dot> pattern) {
                Log.e("complete", "Pattern complete: " +
                        PatternLockUtils.patternToString(mPatternLockView, pattern));
                if (PrefrancesUtils.getPattern().equalsIgnoreCase(PatternLockUtils.patternToString(mPatternLockView,pattern)))
                {
                        Intent intent=new Intent(PatternUnlockActivity.this,MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        finish();
                }
                else
                {
                    if (PrefrancesUtils.getAnswer()!=null)
                    {
                        forget.setVisibility(View.VISIBLE);
                    }
                    Toast.makeText(PatternUnlockActivity.this, "Pattern No Matched", Toast.LENGTH_SHORT).show();
                }

                mPatternLockView.clearPattern();
            }
            @Override
            public void onCleared() {
                Log.e("cleared", "Pattern has been cleared");
            }
        });
    }

    @Override
    public void Update(String answer)
    {
        if (answer !=null && !answer.isEmpty())
        {
            if (answer.equalsIgnoreCase(PrefrancesUtils.getAnswer()))
            {
                PrefrancesUtils.setPattern(null);
                PrefrancesUtils.setQuestin(null);
                PrefrancesUtils.setAnswer(null);
                PrefrancesUtils.setPatternfirst(null);
                Intent intent=new Intent(PatternUnlockActivity.this,PatternSetUpActivity.class);
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