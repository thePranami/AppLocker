package com.webrication.smartapplocker.Activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.multidex.MultiDex;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.SupportMenuInflater;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.webrication.smartapplocker.Adapter.AddLockerAdpter;
import com.webrication.smartapplocker.other.Constants;
import com.webrication.smartapplocker.Model.AppDetailsModel;
import com.webrication.smartapplocker.Service.AppLaunchDectionService;
import com.webrication.smartapplocker.Database.DatabaseHandler;
import com.webrication.smartapplocker.Database.LockPackageDatabase;
import com.webrication.smartapplocker.fragment.MyDialogFragment;
import com.webrication.smartapplocker.other.PrefrancesUtils;
import com.webrication.smartapplocker.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity implements MyDialogFragment.OnCallbackReceived
{
   private static final int SYSTEM_ALERT_WINDOW_PERMISSION = 2084;
   ImageView change,plus;
   CardView Add;
   ArrayList<String> list = new ArrayList();
    ArrayList<AppDetailsModel> list_details= new ArrayList();
    DatabaseHandler databaseHandler;
    LockPackageDatabase packageDatabase;
   RecyclerView recyclerView;
   AddLockerAdpter addLockerAdpter;
   RecyclerView.LayoutManager manager;
   MyDialogFragment myDialogFragment;
   Dialog dialog,dialog_over;
   Button cancel,allow,cancel_over,allow_over;
    ProgressBar progressBar;
    String permission="";
    boolean flag;
    boolean dialogShown=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       change=(ImageView)findViewById(R.id.plus);
        plus=(ImageView)findViewById(R.id.back);
       Add=(CardView)findViewById(R.id.add);
        recyclerView=(RecyclerView)findViewById(R.id.recycler);
        databaseHandler=new DatabaseHandler(MainActivity.this);
        progressBar=(ProgressBar)findViewById(R.id.progress);
        manager=new LinearLayoutManager(MainActivity.this,LinearLayoutManager.VERTICAL,false);
        dialog=new Dialog(this);
        dialog_over=new Dialog(this);
        dialog_over.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_over.setContentView(R.layout.permission_dialog_over);
        cancel_over=(Button)dialog_over.findViewById(R.id.cancel);
        allow_over=(Button)dialog_over.findViewById(R.id.allow);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.permission_dialog);
        cancel=(Button)dialog.findViewById(R.id.cancel);
        allow=(Button)dialog.findViewById(R.id.allow);

        ClickEvent();
        DatabaseAllData();
        packageDatabase=new LockPackageDatabase(this);
       flag=false;
    }
// reset lock from menu
    public void ClickEvent()
    {
        change.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                PopupMenu pm=new PopupMenu(MainActivity.this,v);
                SupportMenuInflater inflater=(SupportMenuInflater)pm.getMenuInflater();
                inflater.inflate(R.menu.popup_menu,pm.getMenu());
                pm.show();

                pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item)
                    {
                        Intent intent=new Intent(MainActivity.this,UnlockMetodActivity.class);
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

        plus.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {

                if (!isAccessGranted())
                {
                    dialog.show();
                }
                else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(MainActivity.this))
                    {
                     dialog_over.show();
                    }
                    else {

                        if (dialogShown)
                        {
                            return;
                        }
                        else
                        {
                            FragmentManager manager = getSupportFragmentManager();
                            myDialogFragment = new MyDialogFragment();
                            myDialogFragment.show(manager, "Dialog");
                            dialogShown=true;
                           Log.e("dialog", String.valueOf(myDialogFragment.isAdded()));
                        }
                    }
                }
            }
        });

        Add.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                if (!isAccessGranted())
                {
                    dialog.show();
                }
                else {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(MainActivity.this))
                    {
                        dialog_over.show();
                    }
                    else {

                        if (dialogShown)
                        {
                           return;
                        }
                        else
                        {
                            FragmentManager manager = getSupportFragmentManager();
                            myDialogFragment = new MyDialogFragment();
                            myDialogFragment.show(manager, "Dialog");
                            dialogShown=true;
                        }
                    }
                }
            }
        });

      cancel.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              dialog.dismiss();
          }
      });

      allow.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              flag=false;
              Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
              startActivity(intent);
              dialog.dismiss();
          }
      });

      cancel_over.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

             dialog_over.dismiss();
          }
      });

      allow_over.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              flag=false;
              askPermission();
              dialog_over.dismiss();
          }
      });
    }

    public  void  DatabaseAllData()
    {
        list_details.clear();

        if (list.size()>0)
        {
            list.clear();

            Cursor cursor=databaseHandler.getAllData();

            if (cursor!=null && cursor.moveToFirst())
            {
                while(!cursor.isAfterLast())
                {
                    list.add(cursor.getString(1));
                    cursor.moveToNext();
                }
            }
        }
        else
        {
            Cursor cursor=databaseHandler.getAllData();

            if (cursor!=null && cursor.moveToFirst())
            {
                while(!cursor.isAfterLast())
                {
                    list.add(cursor.getString(1));
                    cursor.moveToNext();
                }
            }
        }
        if (!list.isEmpty())
        {
            Add.setVisibility(View.GONE);
        }

        Log.e("size_of", String.valueOf(list.size()));

        for (int i=0;i<list.size();i++)
        {
            Drawable icon = null;
            try {
                icon = getPackageManager().getApplicationIcon(list.get(i));
                String appName = (String)getPackageManager().getApplicationLabel(getPackageManager().getApplicationInfo(list.get(i), PackageManager.GET_META_DATA));
                list_details.add(new AppDetailsModel(appName,icon,list.get(i)));

            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }

        Collections.sort(list_details, new Comparator<AppDetailsModel>() {

            @Override
            public int compare(AppDetailsModel a1, AppDetailsModel a2) {
                return (a1.appName.toString()).compareTo(a2.appName.toString());
            }
        });

        recyclerView.setLayoutManager(manager);
        addLockerAdpter=new AddLockerAdpter(MainActivity.this,list_details);
        recyclerView.setAdapter(addLockerAdpter);

        addLockerAdpter.SetOnItemClickListener(new AddLockerAdpter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int Position)
            {
                AppDetailsModel model=list_details.get(Position);

                Boolean result=databaseHandler.getpack_name(model.getPackge_name());
                boolean lock_result=packageDatabase.getpack_name(model.getPackge_name());
                if (lock_result)
                {
                    packageDatabase.delete(model.getPackge_name());
                }

                if (result)
                {
                    databaseHandler.delete(model.getPackge_name());
                }
                list_details.remove(list_details.get(Position));
                addLockerAdpter.notifyDataSetChanged();

                if (list_details.size()<1)
                {
                    Add.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void Update()
    {
        DatabaseAllData();
        myDialogFragment.dismiss();
        Intent intent=new Intent(MainActivity.this,AppLaunchDectionService.class);
        intent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }

        dialogShown=false;

        Log.e("dialog", String.valueOf(myDialogFragment.isAdded()));
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private boolean isAccessGranted()
    {
        try {
            PackageManager packageManager = getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) getSystemService(MainActivity.this.APP_OPS_SERVICE);
            int mode = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                        applicationInfo.uid, applicationInfo.packageName);
            }
            return (mode == AppOpsManager.MODE_ALLOWED);

        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private void askPermission() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, SYSTEM_ALERT_WINDOW_PERMISSION);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        if (flag )
        {
            startActivity(new Intent(MainActivity.this,SpalashActivity.class));
            finish();
            flag=true;
        }
        else
        {
            flag = true;
        }
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
