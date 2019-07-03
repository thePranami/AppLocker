package com.webrication.smartapplocker.Activity;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.webrication.smartapplocker.fragment.ForgetDialogFragment;
import com.webrication.smartapplocker.other.PrefrancesUtils;
import com.webrication.smartapplocker.R;

public class PinUnlockActivity extends AppCompatActivity implements View.OnClickListener,ForgetDialogFragment.OnCallbackReceived
{
    private static final int MAX = 3;
    private int count;
    private String mPassword="";
    ForgetDialogFragment dialog;
    TextView forgetTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_pin_unlock);


        findViewById(R.id.zero).setOnClickListener(this);
        findViewById(R.id.one).setOnClickListener(this);
        findViewById(R.id.two).setOnClickListener(this);
        findViewById(R.id.three).setOnClickListener(this);
        findViewById(R.id.four).setOnClickListener(this);
        findViewById(R.id.five).setOnClickListener(this);
        findViewById(R.id.six).setOnClickListener(this);
        findViewById(R.id.seven).setOnClickListener(this);
        findViewById(R.id.eight).setOnClickListener(this);
        findViewById(R.id.nine).setOnClickListener(this);
        findViewById(R.id.backspace).setOnClickListener(this);
        findViewById(R.id.exitImageButton_id).setOnClickListener(this);
         forgetTextView=(TextView)findViewById(R.id.id_btn_forgot_pin);
        forgetTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
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
                    ((TextView)findViewById(R.id.pass)).setText(mPassword);
                    count = 0;
                    break;
                }
                char [] pass = mPassword.toCharArray();
                mPassword = "";
                for(int i=0;i<(count -1);++i){
                    mPassword += pass[i];
                }
                count--;
                ((TextView)findViewById(R.id.pass)).setText(mPassword);
                break;
            case R.id.id_btn_forgot_pin:
                showDialog();
                break;

            case R.id.exitImageButton_id:
                finish();
                break;
            default:
                break;
        }
    }

    private void formPassword(int num){
        mPassword += num;
        ((TextView)findViewById(R.id.pass)).setText(mPassword);
    }

    private void validate()
    {
        if(PrefrancesUtils.getPIN().equalsIgnoreCase(mPassword))
        {
            // finish this activity

                Intent intent=new Intent(PinUnlockActivity.this,MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                finish();

        } else {
            Toast.makeText(this, "Invalid PIN", Toast.LENGTH_SHORT).show();
            mPassword = "";
            ((TextView)findViewById(R.id.pass)).setText(mPassword);
            count = 0;
        }
    }

    @Override
    public void Update(String answer) {

        if (answer !=null && !answer.isEmpty())
        {
           if (answer.equalsIgnoreCase(PrefrancesUtils.getAnswer()))
           {
               PrefrancesUtils.setPIN(null);
               PrefrancesUtils.setQuestin(null);
               PrefrancesUtils.setAnswer(null);
               Intent intent=new Intent(PinUnlockActivity.this,PinLockActivity.class);
               startActivity(intent);
               overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
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
        if (PrefrancesUtils.getAnswer()!=null)
        {
            FragmentManager manager=getSupportFragmentManager();
            dialog=new ForgetDialogFragment();
            dialog.show(manager,"Dialog");
        }
        else
        {
            Toast.makeText(PinUnlockActivity.this, "Security Question Not Set", Toast.LENGTH_SHORT).show();
        }
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
