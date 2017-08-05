package ca.harshgupta.currencyxchange;


import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.List;
import java.util.Set;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.res.Resources;
import android.util.TypedValue;
import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import android.text.Editable;

public class MainActivity extends AppCompatActivity {

    ListView listCurrency;
    Set<Currency> availableCurrenciesSet;
    List<Currency> availableCurrenciesList;
    ArrayAdapter<Currency> adapter;

    String cur_1_selected;
    String cur_2_selected;

    public String url;
    public String new_url;

    public double toEUR;

    private String TAG = MainActivity.class.getSimpleName();

    public double cur_rate_1;
    public double cur_rate_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText cur_val_1 = (EditText) findViewById(R.id.cur_val_1);
        final EditText cur_val_2 = (EditText) findViewById(R.id.cur_val_2);

        //initialize spinner
        Spinner spinner1 = (Spinner) findViewById(R.id.cur_id_1);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                cur_1_selected = parent.getItemAtPosition(pos).toString();

                new_url = url + cur_1_selected + "," + cur_2_selected;
                Log.d("change", new_url);

                new GetRates().execute();

                if (cur_val_1.getText() != null){
                    double val1 = Double.parseDouble(cur_val_1.getText().toString());
                    toEUR = val1 / cur_rate_1;
                    double val2 = toEUR * cur_rate_2;

                    cur_val_2.setText(String.format("%.2f",val2));
                }

            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Spinner spinner2 = (Spinner) findViewById(R.id.cur_id_2);

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                cur_2_selected = parent.getItemAtPosition(pos).toString();
                Log.d("change", "currency 2 changed: " + cur_2_selected);

                new_url = url + cur_1_selected + "," + cur_2_selected;
                new GetRates().execute();
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        availableCurrenciesSet = Currency.getAvailableCurrencies();


        availableCurrenciesList = new ArrayList<Currency>(availableCurrenciesSet);
        //Collections.sort(availableCurrenciesList, new Comparator<Currency>(){
        //    public int compare(Currency c1, Currency c2){
        //        return Currency.getCurrencyCode(c1);
        //    }
        //});

        adapter = new ArrayAdapter<Currency>(this, android.R.layout.simple_spinner_item, availableCurrenciesList);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner1.setAdapter(adapter);
        spinner2.setAdapter(adapter);

        cur_1_selected = "USD";
        cur_2_selected = "CAD";

        int usdpos = adapter.getPosition(Currency.getInstance(cur_1_selected));
        int cadpos = adapter.getPosition(Currency.getInstance(cur_2_selected));

        spinner1.setSelection(usdpos);
        spinner2.setSelection(cadpos);

        url = "http://api.fixer.io/latest?symbols=";
    }

    private class GetRates extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            Log.d("rate", new_url);
            String jsonStr = sh.makeServiceCall(new_url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONObject jsonRates = jsonObj.getJSONObject("rates");

                    cur_rate_1 = Double.parseDouble(jsonRates.getString(cur_1_selected));
                    cur_rate_2 = Double.parseDouble(jsonRates.getString(cur_2_selected));

                    Log.d("rate", jsonRates.getString(cur_1_selected));
                    Log.d("rate", jsonRates.getString(cur_2_selected));

                }
                catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

                }
            }
            else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Couldn't get json from server. Check LogCat for possible errors!", Toast.LENGTH_LONG).show();
                    }
                });

            }

            return null;
        }

    }
}

