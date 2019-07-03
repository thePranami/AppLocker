package com.webrication.smartapplocker.Activity;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.webrication.smartapplocker.fragment.MydialogFragment_security;
import com.webrication.smartapplocker.other.PrefrancesUtils;
import com.webrication.smartapplocker.R;

public class PinLockActivity extends AppCompatActivity implements View.OnClickListener,MydialogFragment_security.OnCallbackReceived
{
    private String mPassword;
    private String mPasswordConfirm;
    private int count;
    private boolean first;
    private boolean changeLock;
    ImageButton confirm_btn;
    ImageView lockmethod, back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       if (PrefrancesUtils.getPIN()!=null)
       {
           Intent intent=new Intent(PinLockActivity.this,PinUnlockActivity.class);
           startActivity(intent);
           finish();
       }
        setContentView(R.layout.activity_pin_lock);
        back=(ImageView)findViewById(R.id.back);
        confirm_btn=(ImageButton)findViewById(R.id.right);
        count = 0;
        mPassword = "";
        mPasswordConfirm = "";
        first = true;

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

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PinLockActivity.this,UnlockMetodActivity.class);
                PrefrancesUtils.setQuestin(null);
                PrefrancesUtils.setgesture(null);
                PrefrancesUtils.setPatternfirst(null);
                PrefrancesUtils.setAnswer(null);
                PrefrancesUtils.setPIN(null);
                PrefrancesUtils.setPattern(null);
                PrefrancesUtils.setLockType(null);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                finish();
            }
        });

        /*

        lockmethod.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                PopupMenu pm=new PopupMenu(PinLockActivity.this,v);
                SupportMenuInflater inflater=(SupportMenuInflater)pm.getMenuInflater();
                inflater.inflate(R.menu.popup_menu,pm.getMenu());
                pm.show();

                pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item)
                    {
                        Intent intent=new Intent(PinLockActivity.this,UnlockMetodActivity.class);
                        PrefrancesUtils.setQuestin(null);
                        PrefrancesUtils.setgesture(null);
                        PrefrancesUtils.setPatternfirst(null);
                        PrefrancesUtils.setAnswer(null);
                        PrefrancesUtils.setPIN(null);
                        PrefrancesUtils.setPattern(null);
                        PrefrancesUtils.setLockType(null);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                        finish();
                        return false;

                    }
                });
            }
        });

        */

        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (PrefrancesUtils.getPIN()!=null) {
                    FragmentManager manager = getSupportFragmentManager();
                    MydialogFragment_security myDialogFragment = new MydialogFragment_security();
                    myDialogFragment.show(manager, "Dialog");
                }
                else
                {
                    Toast.makeText(PinLockActivity.this, "Pin Not Set", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
                if (first) {
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
                } else {
                    if(count <1){
                        break;
                    }
                    if(count == 1){
                        mPasswordConfirm = "";
                        ((TextView)findViewById(R.id.pass)).setText(mPasswordConfirm);
                        count = 0;
                        break;
                    }
                    char [] pass = mPasswordConfirm.toCharArray();
                    mPasswordConfirm = "";
                    for(int i=0;i<(count -1);++i){
                        mPasswordConfirm += pass[i];
                    }
                    count--;
                    ((TextView)findViewById(R.id.pass)).setText(mPasswordConfirm);
                }
            default:
                break;
        }
    }

    private void formPassword(int num){
        if(first){
            mPassword += num;
            ((TextView)findViewById(R.id.pass)).setText(mPassword);
        } else {
            mPasswordConfirm += num;
            ((TextView)findViewById(R.id.pass)).setText(mPasswordConfirm);
        }
    }

    private void validate()
    {
        if(first)
        {
            ((TextView)findViewById(R.id.pass)).setHint("");
            ((TextView)findViewById(R.id.pass)).setText(mPasswordConfirm);
             Log.e("pin",mPassword+"null");
            ((TextView)findViewById(R.id.tv_unlock_message)).setText(getString(R.string.pin_confirm));
            count = 0;
            first = false;
        }
        else
        {
            if(mPasswordConfirm.equals(mPassword))
            {
                Toast.makeText(this,getString(R.string.pin_set),Toast.LENGTH_SHORT).show();
                PrefrancesUtils.setPIN(mPassword);
//                Intent intent=new Intent(PinLockActivity.this,MainActivity.class);
//                startActivity(intent);
//                finish();
            }
            else
            {
               // Toast.makeText(this,getString(R.string.pin_do_not_match),Toast.LENGTH_SHORT).show();
                ((TextView)findViewById(R.id.tv_unlock_message)).setText(getString(R.string.pin_do_not_match_message));
                ((TextView)findViewById(R.id.pass)).setText("");
                mPassword="";
                mPasswordConfirm="";
                count = 0;
                first = true;
            }
        }
    }

    @Override
    public void Update(String answer) {

        if (answer!=null && !answer.isEmpty())
        {
            PrefrancesUtils.setAnswer(answer);
            Intent intent=new Intent(PinLockActivity.this,MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            finish();
        }
        else
        {
            Intent intent=new Intent(PinLockActivity.this,MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            finish();
        }
    }
}
