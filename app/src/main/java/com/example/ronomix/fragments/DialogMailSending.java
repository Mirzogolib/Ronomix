package com.example.ronomix.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.ronomix.R;
import com.example.ronomix.helper.JavaMailAPI;

import butterknife.BindView;
import butterknife.ButterKnife;

import static androidx.core.content.ContextCompat.getSystemService;

public class DialogMailSending extends DialogFragment {

    @BindView(R.id.mobilePhone)
    EditText mobilePhone;
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.orderDialogButton)
    Button order;
    @BindView(R.id.backDialog)
    Button back;


    public boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if ((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting()))
                return true;
            else return false;
        } else
            return false;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_view, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        String message = getArguments().getString("MESSAGE");
        String mail = "premixmobile@gmail.com";
        Log.d("INTERNET", isConnected(getActivity()) + "");
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isConnected(getActivity())) {
                    if (!mobilePhone.getText().toString().equals("") && !name.getText().toString().equals("")) {
                        if (mobilePhone.getText().toString().length() == 9) {
                            String subject = getResources().getString(R.string.clientName) + " " + name.getText().toString() + " " + getResources().getString(R.string.phone) + " " + mobilePhone.getText().toString();
                            JavaMailAPI javaMailAPI = new JavaMailAPI(getActivity(), mail, subject, message);
                            javaMailAPI.execute();
                            getDialog().dismiss();
                        } else {
                            Toast.makeText(getContext(), getResources().getString(R.string.properNumber), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), getResources().getString(R.string.allData), Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(getContext(), getResources().getString(R.string.notWorking), Toast.LENGTH_SHORT).show();
                }

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

    }


}
