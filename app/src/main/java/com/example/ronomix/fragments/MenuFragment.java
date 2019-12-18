package com.example.ronomix.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.ronomix.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MenuFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
    @BindView(R.id.calculateButton)
    Button calculate;
    @BindView(R.id.backButton)
    Button back;
    @BindView(R.id.spinner)
    Spinner spinner;




    private String typeOfFragment, typeOfStart;
    private int dayOrWeight, quantity, spinnerPosition;
    DayFragment dayFragment;
    WeightFragment weightFragment;
    FragmentTransaction fragmentTransaction;



    public static MenuFragment newInstance(){
        MenuFragment menuFragment = new MenuFragment();
        return menuFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        ButterKnife.bind(this, view);
        createFragments();
        typeOfFragment = getArguments().getString("TYPE");
        dayOrWeight = getArguments().getInt("D_OR_W");
        quantity = getArguments().getInt("QUANTITY");
        Log.d("TYPE", typeOfFragment+ " "+dayOrWeight+ " "+ quantity);
        calculate.setOnClickListener(this);
        back.setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.calculateButton){
            int selectedID = radioGroup.getCheckedRadioButtonId();
            if (selectedID == -1){
                Toast.makeText(this.getActivity(), "Select from Radio Button", Toast.LENGTH_SHORT).show();
            }else {
                if (selectedID==R.id.startRadio)
                    typeOfStart = "START";
                else if (selectedID == R.id.superStartRadio)
                    typeOfStart = "SUPER";
                spinnerPosition = spinner.getSelectedItemPosition();

                Bundle bundle = new Bundle();
                bundle.putInt("SPINNER", spinnerPosition);
                bundle.putInt("D_OR_W", dayOrWeight);
                bundle.putInt("QUANTITY", quantity);
                bundle.putString("START", typeOfStart);

                if (typeOfFragment.equals("DAY")){
                    //day fragment
                    changeFragment(bundle, dayFragment);
                }else if (typeOfFragment.equals("WEIGHT")){
                    //weight fragment
                    changeFragment(bundle, weightFragment);
                }
            }
        }else if (view.getId() == R.id.backButton){
            getActivity().onBackPressed();
        }
    }

    private void changeFragment(Bundle bundle, Fragment fragment){
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.layoutMenu, fragment).addToBackStack(null)
                .commit();
    }

    private void createFragments(){
        dayFragment = DayFragment.newInstance();
        weightFragment = WeightFragment.newInstance();
    }
}
