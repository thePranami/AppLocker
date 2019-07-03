package com.webrication.smartapplocker.Adapter;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.webrication.smartapplocker.Model.AllAppListModel;
import com.webrication.smartapplocker.Database.DatabaseHandler;
import com.webrication.smartapplocker.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pc on 12/20/2017.
 */

public class ApplicationAdapter extends RecyclerView.Adapter<ApplicationAdapter.RemaingView>
{
    OnItemClickListener mItemClickListener;

    private List<ApplicationInfo> appsList;
    private Context context;
    private PackageManager packageManager;
    ArrayList<AllAppListModel> list;
    DatabaseHandler databaseHandler;

    public ApplicationAdapter(Context context, ArrayList<AllAppListModel> appsList)
    {
        this.context = context;
        this.list = appsList;
        packageManager = context.getPackageManager();
         databaseHandler=new DatabaseHandler(context);
    }

    @Override
    public RemaingView onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view=LayoutInflater.from(context).inflate(R.layout.row,parent,false);
        RemaingView remaingView=new RemaingView(view);
        return remaingView;
    }

    @Override
    public void onBindViewHolder(RemaingView holder, int position)
    {
        AllAppListModel model=list.get(position);

           holder.imageView.setImageDrawable(model.getAppIcon());
           holder.textView.setText(model.getAppName());

        boolean result=databaseHandler.getpack_name(model.getPackge_name());

        Log.e("result", String.valueOf(result));
        if (result)
        {
          holder.textView.setChecked(true);
        }
        else
        {
            holder.textView.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public  class  RemaingView extends RecyclerView.ViewHolder implements View.OnClickListener
    {   CheckedTextView textView;
        ImageView imageView;
        RelativeLayout layout;

        public RemaingView(View itemView) {
            super(itemView);

            textView=(CheckedTextView)itemView.findViewById(R.id.app_name);
            imageView=(ImageView)itemView.findViewById(R.id.app_icon);
            layout=(RelativeLayout)itemView.findViewById(R.id.rl);
            layout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null)
            {
                AllAppListModel pack_name=list.get(getAdapterPosition());
                mItemClickListener.onItemClick(v,pack_name.getPackge_name());
            }
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, String name);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}