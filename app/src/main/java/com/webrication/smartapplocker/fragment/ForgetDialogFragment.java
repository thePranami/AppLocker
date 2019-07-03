package com.webrication.smartapplocker.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.webrication.smartapplocker.other.PrefrancesUtils;
import com.webrication.smartapplocker.R;

/**
 * Created by pc on 1/30/2018.
 */

public class ForgetDialogFragment extends DialogFragment
{
    TextView question;
    EditText answer;
    Button cancel,submit;
    OnCallbackReceived mCallback;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.forget_dialog_security,container,false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        question=(TextView)view.findViewById(R.id.question);
        answer=(EditText)view.findViewById(R.id.answer);
        cancel=(Button)view.findViewById(R.id.cancel);
        submit=(Button)view.findViewById(R.id.submit);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

       question.setText(PrefrancesUtils.getQuestion());

       submit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
              String result=answer.getText().toString().trim();
              if (result !=null && !result.isEmpty())
              {
                  mCallback.Update(result);
              }
              else
              {
                  Toast.makeText(getActivity(), "Enter Answer", Toast.LENGTH_SHORT).show();
              }

           }
       });

       cancel.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               mCallback.Update("");
           }
       });

    }

    public interface OnCallbackReceived {
        public void Update(String answer);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallback= (OnCallbackReceived) context;
    }

}
