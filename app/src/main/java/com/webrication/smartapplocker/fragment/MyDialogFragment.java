package com.webrication.smartapplocker.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckedTextView;

import com.webrication.smartapplocker.Adapter.ApplicationAdapter;
import com.webrication.smartapplocker.Database.DatabaseHandler;
import com.webrication.smartapplocker.Database.LockPackageDatabase;
import com.webrication.smartapplocker.Model.AllAppListModel;
import com.webrication.smartapplocker.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by pc on 1/26/2018.
 */

public class MyDialogFragment extends DialogFragment
{
    RecyclerView recyclerView;
    ApplicationAdapter adapter;
    Context context;
    private PackageManager packageManager = null;
    private List<ApplicationInfo> applist = null;
    RecyclerView.LayoutManager manager;
    Button protect;
    LockPackageDatabase lockPackageDatabase;
    DatabaseHandler databaseHandler;
    ArrayList<AllAppListModel> app_list=new ArrayList<>();

    private static final String SYSTEM_PACKAGE_NAME = "android";
    private static  PackageManager mPackageManager = null;

    OnCallbackReceived mCallback;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.app_list_dialog,container,false);
        recyclerView=(RecyclerView)view.findViewById(R.id.recycler);
        protect=(Button)view.findViewById(R.id.button);
        packageManager = getActivity().getPackageManager();
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        LoadApplications();
        manager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        databaseHandler=new DatabaseHandler(getContext());
        lockPackageDatabase=new LockPackageDatabase(getContext());
        return  view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        mPackageManager=getActivity().getPackageManager();

        Log.e("time","onActivity");

        for (int i=0;i<applist.size();i++)
        {
            ApplicationInfo info = applist.get(i);
            String p_name = info.packageName;
            boolean result = databaseHandler.getpack_name(p_name);
            if (!result && !p_name.equalsIgnoreCase("com.google.android.googlequicksearchbox")&&!p_name.equalsIgnoreCase("com.vspl.smartapplocker"))
            {
                String app_name= (String) info.loadLabel(mPackageManager);
                Drawable icon=info.loadIcon(mPackageManager);
                app_list.add(new AllAppListModel(app_name,icon,p_name));
            }
        }

        Collections.sort(app_list, new Comparator<AllAppListModel>()
        {
            @Override
            public int compare(AllAppListModel a1, AllAppListModel a2) {
                return (a1.appName.toString()).compareTo(a2.appName.toString());
            }
        });

        Log.e("applist", String.valueOf(app_list.size()));
        adapter=new ApplicationAdapter(getContext(),app_list);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        adapter.SetOnItemClickListener(new ApplicationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, String name) {
                CheckedTextView checkbox=(CheckedTextView)view.findViewById(R.id.app_name);

                    if (checkbox.isChecked())
                    {
                        databaseHandler.delete(name);
                        lockPackageDatabase.delete(name);
                        checkbox.setChecked(false);
                    } else
                        {
                        databaseHandler.addPackage(name);
                        lockPackageDatabase.addPackage(name);
                        checkbox.setChecked(true);
                    }


            }
        });


        protect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mCallback.Update();

            }
        });

    }

    private List<ApplicationInfo> checkForLaunchIntent(List<ApplicationInfo> list) {
        ArrayList<ApplicationInfo> applist = new ArrayList<ApplicationInfo>();
        for (ApplicationInfo info : list) {
            try {
                if (null != packageManager.getLaunchIntentForPackage(info.packageName)) {
                    applist.add(info);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return applist;
    }

    public void LoadApplications()
    {
        applist = checkForLaunchIntent(packageManager.getInstalledApplications(PackageManager.GET_META_DATA));
        Log.d("appinstall", String.valueOf(applist));

    }

    public interface OnCallbackReceived {
        public void Update();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallback= (OnCallbackReceived) context;
    }

    public  boolean isSystemApp(String packageName) {
        try {
            // Get packageinfo for target application
            PackageInfo targetPkgInfo = mPackageManager.getPackageInfo(
                    packageName, PackageManager.GET_SIGNATURES);
            // Get packageinfo for system package
            PackageInfo sys = mPackageManager.getPackageInfo(
                    SYSTEM_PACKAGE_NAME, PackageManager.GET_SIGNATURES);
            // Match both packageinfo for there signatures
            return (targetPkgInfo != null && targetPkgInfo.signatures != null && sys.signatures[0]
                    .equals(targetPkgInfo.signatures[0]));
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        return new Dialog(getActivity(), getTheme()){
            @Override
            public void onBackPressed() {
               mCallback.Update();
            }
        };
    }
}
