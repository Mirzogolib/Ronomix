package com.example.ronomix.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.ronomix.R;
import com.example.ronomix.fragments.MenuFragment;
import com.example.ronomix.helper.LocaleHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
    @BindView(R.id.byDayRadio)
    RadioButton byDay;
    @BindView(R.id.byWeightRadio)
    RadioButton byWeight;
    @BindView(R.id.nextButton)
    Button next;
    @BindView(R.id.dayOrWeight)
    EditText dayOrWeight;
    @BindView(R.id.quantity)
    EditText quantity;
    @BindView(R.id.dayOrWeightLabel)
    TextView dayOrWeightLabel;

    @BindView(R.id.uzbFlag)
    CardView uzbFlagView;
    @BindView(R.id.rusFlag)
    CardView rusFlagView;

    MenuFragment menuFragment;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        uzbFlagView.setOnClickListener(this);
        rusFlagView.setOnClickListener(this);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedID = radioGroup.getCheckedRadioButtonId();
                if (quantity.getText().toString().equals("") || dayOrWeight.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Insert all needed data", Toast.LENGTH_SHORT).show();
                } else {
                    if (selectedID == R.id.byDayRadio) {
                        int day = Integer.parseInt(dayOrWeight.getText().toString());
                        if (day > 70) {
                            Toast.makeText(getApplicationContext(), "Enter valid day", Toast.LENGTH_SHORT).show();
                        } else {
                            loadFragment("DAY");
                        }
                    } else if (selectedID == R.id.byWeightRadio) {
                        //call menu fragment
                        loadFragment("WEIGHT");

                    } else {
                        Toast.makeText(getApplicationContext(), "Select from Radio Button", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.byDayRadio) {
                    dayOrWeight.setHint(getString(R.string.hintDay));
                    dayOrWeightLabel.setText(getString(R.string.day));
                } else if (checkedId == R.id.byWeightRadio) {
                    dayOrWeight.setHint(getString(R.string.hintWeight));
                    dayOrWeightLabel.setText(getString(R.string.weight));
                }
            }
        });


    }

    private void loadFragment(String type) {
        Bundle bundle = new Bundle();
        bundle.putString("TYPE", type);
        bundle.putInt("D_OR_W", Integer.parseInt(dayOrWeight.getText().toString()));
        bundle.putInt("QUANTITY", Integer.parseInt(quantity.getText().toString()));
        menuFragment = MenuFragment.newInstance();
        menuFragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mainView, menuFragment).addToBackStack(null)
                .commit();


    }

    @Override
    public void onClick(View view) {
     if (view.getId()==R.id.uzbFlag){
         Log.d("CLICKED", "UZB");
         LocaleHelper.setLocale(this, "en");
         Intent intent = new Intent(MainActivity.this, MainActivity.class);
         intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
         startActivity(intent);
     }  else if (view.getId()==R.id.rusFlag){
         Log.d("CLICKED", "RUS");
         LocaleHelper.setLocale(this, "ru");
         Intent intent = new Intent(MainActivity.this, MainActivity.class);
         intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
         startActivity(intent);
     }
    }

}
