package com.webrication.smartapplocker.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.webrication.smartapplocker.other.PrefrancesUtils;
import com.webrication.smartapplocker.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by pc on 1/30/2018.
 */

public class MydialogFragment_security extends DialogFragment
{
    String[] question = new String[]{
            "Select a question...",
            "what's your father's name ?",
            "what's your mother's name ?",
            "what's your best friends's name ?",
            "who is your favorite actor ?",
            "which is your fovorite city ?",
            "what's your birthday ?",
            "what's your pet's name ?",
            "what's your dream job ?",
            "where were you born ?"
    };

    Spinner spinner;
    OnCallbackReceived mCallback;
    Button save,skip;
    EditText answer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.dialog_security,container,false);
        spinner=(Spinner)view.findViewById(R.id.spinner);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        answer=(EditText)view.findViewById(R.id.nick);
        save=(Button)view.findViewById(R.id.Save);
       // skip=(Button)view.findViewById(R.id.skip);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        List<String> questionList = new ArrayList<>(Arrays.asList(question));
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(),R.layout.spinner_item,questionList)
        {
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(spinnerArrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selectedItemText = (String) parent.getItemAtPosition(position);

                if(position > 0){
                    // Notify the selected item text

                    PrefrancesUtils.setQuestin(selectedItemText);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               String ans=answer.getText().toString().trim();
               if (ans !=null && !ans.isEmpty())
               {
                   if (PrefrancesUtils.getQuestion()!=null)
                   {
                       mCallback.Update(ans);
                   }
                   else
                   {
                       Toast.makeText(getActivity(), "Select a Question", Toast.LENGTH_SHORT).show();
                   }
               }
               else
               {
                   Toast.makeText(getActivity(), "Enter Answer", Toast.LENGTH_SHORT).show();
               }
            }
        });

      /*  skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mCallback.Update("");
            }
        });
        */
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