package com.example.ronomix.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.ronomix.R;
import com.example.ronomix.models.Feeding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DayFragment extends Fragment implements View.OnClickListener {


    double cum_all;
    JSONObject jsonObject;
    Feeding[] feeding;
    int day, quantity, spinnerPosition;
    String startType, premix;
    String startC, rostC, finishC, finish2C;
    private static final double SUPER_START = 6.0;
    private static final double START = 3.0;
    private static final double ROST = 2.5;
    private static final double FINISH = 2.5;
    private static final double FINISH_2 = 2.0;
    BigDecimal rostVal, finishVal, finishVal2;

    DecimalFormat df = new DecimalFormat("#,###");
    String[] arrayOfPremix;

    @BindView(R.id.premixAll)
    TextView premixAll;
    @BindView(R.id.premixStart)
    TextView startPremix;
    @BindView(R.id.premixRost)
    TextView rostPremix;
    @BindView(R.id.premixFinish)
    TextView finishPremix;
    @BindView(R.id.premixFinish2)
    TextView finish2Premix;

    @BindView(R.id.cormAll)
    TextView cormAll;
    @BindView(R.id.cormStart)
    TextView startCorm;
    @BindView(R.id.cormRost)
    TextView rostCorm;
    @BindView(R.id.cormFinish)
    TextView finishCorm;
    @BindView(R.id.cormFinish2)
    TextView finish2Corm;
    @BindView(R.id.dayDayF)
    TextView dayTextView;
    @BindView(R.id.quantityDayF)
    TextView quantityTextView;
    @BindView(R.id.startType)
    TextView startTypeText;

    @BindView(R.id.rowStart)
    TableRow rowStart;
    @BindView(R.id.rowRost)
    TableRow rowRost;
    @BindView(R.id.rowFinish)
    TableRow rowFinish;
    @BindView(R.id.rowFinish2)
    TableRow rowFinish2;
    @BindView(R.id.backButtonDayF)
    Button backButton;
    @BindView(R.id.orderButton)
    Button orderButton;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.stringArray)
    TextView stringArray;

    String emailStart, emailRost, emailFinish, emailFinish2;
    BigDecimal priceVal;


    public static DayFragment newInstance() {
        DayFragment dayFragment = new DayFragment();
        return dayFragment;
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.table_view_day, container, false);
        ButterKnife.bind(this, view);
        day = getArguments().getInt("D_OR_W");
        quantity = getArguments().getInt("QUANTITY");
        spinnerPosition = getArguments().getInt("SPINNER");
        startType = getArguments().getString("START");
        Log.d("MYTAG", startType + " " + day + " " + quantity + " " + spinnerPosition);
        backButton.setOnClickListener(this);
        orderButton.setOnClickListener(this);

        arrayOfPremix = getResources().getStringArray(R.array.typeOfMix);
        try {
            jsonObject = new JSONObject(loadJSONFromAsset(getContext()));
            JSONArray array = jsonObject.getJSONArray("Result");
            feeding = new Feeding[array.length()];
            for (int i = 0; i < array.length(); i++) {
                Feeding feed = new Feeding();
                String day = array.getJSONObject(i).getString("DAY");
                String weight = array.getJSONObject(i).getString("WEIGHT");
                String cum_all = array.getJSONObject(i).getString("CUM_ALL");
                String cum_period = array.getJSONObject(i).getString("CUM_PERIOD");
                feed.setDay(day);
                feed.setWeight(weight);
                feed.setCum_all(cum_all);
                feed.setCum_period(cum_period);
                feeding[i] = feed;
            }
            calculate(day, quantity, startType, feeding);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("JSON", "ERROR");
        }
        return view;
    }

    private void calculate(int day, int quantity, String startType, Feeding[] feeding) {
        cum_all = (Double.parseDouble(feeding[day].getCum_all()) / 1000.0) * quantity;
        String allText = df.format(cum_all).replace(",", " ") + " " + getResources().getString(R.string.kg);
        cormAll.setText(allText);
        String daysText = getString(R.string.dayDayF) + " " + day;
        String quantityText = getString(R.string.quantityDayF) + " " + df.format(quantity).replace(",", " ");
        dayTextView.setText(daysText);
        quantityTextView.setText(quantityText);
        stringArray.setText(arrayOfPremix[spinnerPosition]);

        Log.d("MYTAG", "All: " + cum_all);
        if (startType.equals("START")) {
            finalCalculation(START, getResources().getString(R.string.start), feeding);
        } else if (startType.equals("SUPER")) {
            finalCalculation(SUPER_START, getResources().getString(R.string.superStart), feeding);
        }
    }

    private void finalCalculation(double startPercentage, String startType, Feeding[] feeding) {
        String startLabel, rostLabel, finishLabel, finish2Label;
        if (day >= 0 && day < 11) {
            //START
            rowStart.setVisibility(View.VISIBLE);
            double cumStart = getFromObject(day);
            double firstVal = (cumStart * quantity) / 1000;
            startTypeText.setText(startType);
            BigDecimal bigDecimal = BigDecimal.valueOf(firstVal).multiply(new BigDecimal(startPercentage)).multiply(new BigDecimal(10));
//            startVal = firstVal * startPercentage * 10;
            startLabel = df.format(bigDecimal) + " " + getResources().getString(R.string.kg);
            startPremix.setText(startLabel);
            premixAll.setText(startLabel);
            startC = df.format((Float.parseFloat(feeding[day].getCum_period()) / 1000) * quantity).replace(",", " ") + " " + getResources().getString(R.string.kg);
            startCorm.setText(startC);
            calculatePrice(startType, bigDecimal, null, spinnerPosition);
            emailStart = getResources().getString(R.string.start) + " " + df.format(bigDecimal) + " " + getResources().getString(R.string.kg);
        } else if (day >= 11 && day <= 24) {
            //ROST
            rowStart.setVisibility(View.VISIBLE);
            rowRost.setVisibility(View.VISIBLE);
            double cumStart = getFromObject(10);
            double cumRost = getFromObject(day);
            double firstVal = (cumStart * quantity) / 1000;
            startTypeText.setText(startType);
            BigDecimal bigDecimal = BigDecimal.valueOf(firstVal).multiply(new BigDecimal(startPercentage)).multiply(new BigDecimal(10));
//            startVal = firstVal * startPercentage * 10;
            rostVal = calculateValue(cumRost, quantity, ROST);
            premix = df.format((bigDecimal.add(rostVal))) + " " + getResources().getString(R.string.kg);
            startLabel = df.format(bigDecimal) + " " + getResources().getString(R.string.kg);
            rostLabel = df.format(rostVal) + " " + getResources().getString(R.string.kg);
            startPremix.setText(startLabel);
            rostPremix.setText(rostLabel);
            premixAll.setText(premix);
            startC = df.format((Float.parseFloat(feeding[10].getCum_period()) / 1000) * quantity).replace(",", " ") + " " + getResources().getString(R.string.kg);
            startCorm.setText(startC);
            rostC = df.format((Float.parseFloat(feeding[day].getCum_period()) / 1000) * quantity).replace(",", " ") + " " + getResources().getString(R.string.kg);
            rostCorm.setText(rostC);
            calculatePrice(startType, bigDecimal, rostVal, spinnerPosition);
            emailStart = getResources().getString(R.string.start) + " " + df.format(bigDecimal) + " " + getResources().getString(R.string.kg);
            emailRost = getResources().getString(R.string.rost) + " " + rostLabel;

        } else if (day >= 25 && day <= 39) {
            //FINISH
            rowStart.setVisibility(View.VISIBLE);
            rowRost.setVisibility(View.VISIBLE);
            rowFinish.setVisibility(View.VISIBLE);
            double cumStart = getFromObject(10);
            double cumRost = getFromObject(24);
            double cumFinish = getFromObject(day);
            double firstVal = (cumStart * quantity) / 1000;
//            startVal = firstVal * startPercentage * 10;
            BigDecimal bigDecimal = BigDecimal.valueOf(firstVal).multiply(new BigDecimal(startPercentage)).multiply(new BigDecimal(10));
            rostVal = calculateValue(cumRost, quantity, ROST);
            Log.d("ALLTAG", rostVal + " " + cumRost);
            finishVal = calculateValue(cumFinish, quantity, FINISH);
            startTypeText.setText(startType);
            startLabel = df.format(bigDecimal) + " " + getResources().getString(R.string.kg);
            rostLabel = df.format(rostVal) + " " + getResources().getString(R.string.kg);
            finishLabel = df.format(finishVal) + " " + getResources().getString(R.string.kg);
            premix = df.format((bigDecimal.add(rostVal).add(finishVal))) + " " + getResources().getString(R.string.kg);
            startPremix.setText(startLabel);
            rostPremix.setText(rostLabel);
            finishPremix.setText(finishLabel);
            premixAll.setText(premix);
            startC = df.format((Float.parseFloat(feeding[10].getCum_period()) / 1000) * quantity).replace(",", " ") + " " + getResources().getString(R.string.kg);
            startCorm.setText(startC);
            rostC = df.format((Float.parseFloat(feeding[24].getCum_period()) / 1000) * quantity).replace(",", " ") + " " + getResources().getString(R.string.kg);
            rostCorm.setText(rostC);
            finishC = df.format((Float.parseFloat(feeding[day].getCum_period()) / 1000) * quantity).replace(",", " ") + " " + getResources().getString(R.string.kg);
            finishCorm.setText(finishC);
            calculatePrice(startType, bigDecimal, rostVal.add(finishVal), spinnerPosition);

            emailStart = getResources().getString(R.string.start) + " " + df.format(bigDecimal) + " " + getResources().getString(R.string.kg);
            emailRost = getResources().getString(R.string.rost) + " " + rostLabel;
            emailFinish = getResources().getString(R.string.finish) + " " + finishLabel;

        } else if (day >= 40 && day <= 70) {
            //FINISH 2
            rowStart.setVisibility(View.VISIBLE);
            rowRost.setVisibility(View.VISIBLE);
            rowFinish.setVisibility(View.VISIBLE);
            rowFinish2.setVisibility(View.VISIBLE);
            double cumStart = getFromObject(10);
            double cumRost = getFromObject(24);
            double cumFinish = getFromObject(39);
            double cunFinish2 = getFromObject(day);
            double firstVal = (cumStart * quantity) / 1000;
//            startVal = firstVal * startPercentage * 10;
            BigDecimal bigDecimal = BigDecimal.valueOf(firstVal).multiply(new BigDecimal(startPercentage)).multiply(new BigDecimal(10));
            rostVal = calculateValue(cumRost, quantity, ROST);
            finishVal = calculateValue(cumFinish, quantity, FINISH);
            finishVal2 = calculateValue(cunFinish2, quantity, FINISH_2);
            startTypeText.setText(startType);
            startLabel = df.format(bigDecimal) + " " + getResources().getString(R.string.kg);
            rostLabel = df.format(rostVal) + " " + getResources().getString(R.string.kg);
            finishLabel = df.format(finishVal) + " " + getResources().getString(R.string.kg);
            finish2Label = df.format(finishVal2) + " " + getResources().getString(R.string.kg);
            premix = df.format((bigDecimal.add(rostVal).add(finishVal).add(finishVal2))) + " " + getResources().getString(R.string.kg);
            startPremix.setText(startLabel);
            rostPremix.setText(rostLabel);
            finishPremix.setText(finishLabel);
            finishPremix.setText(finishLabel);
            finish2Premix.setText(finish2Label);
            premixAll.setText(premix);
            startC = df.format((Float.parseFloat(feeding[10].getCum_period()) / 1000) * quantity).replace(",", " ") + " " + getResources().getString(R.string.kg);
            startCorm.setText(startC);
            rostC = df.format((Float.parseFloat(feeding[24].getCum_period()) / 1000) * quantity).replace(",", " ") + " " + getResources().getString(R.string.kg);
            rostCorm.setText(rostC);
            finishC = df.format((Float.parseFloat(feeding[39].getCum_period()) / 1000) * quantity).replace(",", " ") + " " + getResources().getString(R.string.kg);
            finishCorm.setText(finishC);
            finish2C = df.format((Float.parseFloat(feeding[day].getCum_period()) / 1000) * quantity).replace(",", " ") + " " + getResources().getString(R.string.kg);
            finish2Corm.setText(finish2C);
            calculatePrice(startType, bigDecimal, rostVal.add(finishVal).add(finishVal2), spinnerPosition);
            emailStart = getResources().getString(R.string.start) + " " + df.format(bigDecimal) + " " + getResources().getString(R.string.kg);
            emailRost = getResources().getString(R.string.rost) + " " + rostLabel;
            emailFinish = getResources().getString(R.string.finish) + " " + finishLabel;
            emailFinish2 = getResources().getString(R.string.finish_2) + " " + finish2Label;
        }
    }

    private BigDecimal calculateValue(double cumulative, int quantity, double percentage) {
        BigDecimal a = BigDecimal.valueOf(cumulative).multiply(new BigDecimal(quantity)).multiply(new BigDecimal(percentage)).divide(new BigDecimal(100));
        return a;
    }

    private double getFromObject(int i) {
        return Double.parseDouble(feeding[i].getCum_period()) / 1000;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.backButtonDayF) {
            getActivity().onBackPressed();
        } else if (view.getId() == R.id.orderButton) {
            sendMail();

        }
    }

    private void sendMail() {
        String message = messageBody();
        DialogMailSending dialogFragment = new DialogMailSending();
        Bundle bundle = new Bundle();
        bundle.putBoolean("notAlertDialog", true);
        bundle.putString("MESSAGE", message);
        dialogFragment.setArguments(bundle);
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        dialogFragment.show(ft, "dialog");

    }

    private void calculatePrice(String startType, BigDecimal bigDecimal, BigDecimal allValue, int spinnerPosition) {

        if (allValue == null) {
            if (startType.equals("START")) {
                priceVal = bigDecimal.multiply(new BigDecimal(23575));
            } else {
                priceVal = bigDecimal.multiply(new BigDecimal(20760));
            }
        } else {
            BigDecimal priceFirst, priceSecond = null;
            if (startType.equals("START")) {
                priceFirst = bigDecimal.multiply(new BigDecimal(23575));
            } else {
                priceFirst = bigDecimal.multiply(new BigDecimal(20760));
            }
            if (spinnerPosition == 0) {
                priceSecond = allValue.multiply(new BigDecimal(24725));
            } else if (spinnerPosition == 1) {
                priceSecond = allValue.multiply(new BigDecimal(24725));
            } else if (spinnerPosition == 2) {
                priceSecond = allValue.multiply(new BigDecimal(25875));
            } else if (spinnerPosition == 3) {
                priceSecond = allValue.multiply(new BigDecimal(25875));
            }
            priceVal = priceFirst.add(priceSecond);
        }


        price.setText(df.format(priceVal).replace(",", " ") + " " + getResources().getString(R.string.sum));

    }


    public String messageBody() {
        String text = getResources().getString(R.string.calculatedFor) + "\n" + dayTextView.getText().toString() + "\n" + quantityTextView.getText().toString()
                + "\n" + stringArray.getText().toString() + "\n" + getResources().getString(R.string.totalPrice) + " " + df.format(priceVal).replace(",", " ") + " "
                + getResources().getString(R.string.sum) + "\n" + emailStart + "\n" + emailRost + "\n" + emailFinish + "\n" + emailFinish2;
        return text;
    }

    public String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("data.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }


}
