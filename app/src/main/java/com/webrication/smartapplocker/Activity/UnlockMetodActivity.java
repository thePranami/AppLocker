package com.webrication.smartapplocker.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.webrication.smartapplocker.other.PrefrancesUtils;
import com.webrication.smartapplocker.R;

public class UnlockMetodActivity extends AppCompatActivity
{
    Button pin,gesture,pattern;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_unlock_metod);
        pin=(Button)findViewById(R.id.pin);
        pattern=(Button)findViewById(R.id.pattern);
        gesture=(Button)findViewById(R.id.gesture);

        pin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrefrancesUtils.setLockType("pin");
                Intent intent=new Intent(UnlockMetodActivity.this,PinLockActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                finish();
            }
        });

        gesture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrefrancesUtils.setLockType("gesture");
                Intent intent=new Intent(UnlockMetodActivity.this,GestureLockActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                finish();
            }
        });

        pattern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrefrancesUtils.setLockType("pattern");
                Intent intent=new Intent(UnlockMetodActivity.this,PatternSetUpActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                finish();
            }
        });
    }
}
