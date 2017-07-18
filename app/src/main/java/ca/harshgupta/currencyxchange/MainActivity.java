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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    ListView listCurrency;
    Set<Currency> availableCurrenciesSet;
    List<Currency> availableCurrenciesList;
    ArrayAdapter<Currency> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize spinner
        Spinner spinner1 = (Spinner) findViewById(R.id.cur_id_1);
        Spinner spinner2 = (Spinner) findViewById(R.id.cur_id_2);

        //available from API Level 19
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

        String usd = "USD";
        String cad = "CAD";

        int usdpos = adapter.getPosition(Currency.getInstance(usd));
        int cadpos = adapter.getPosition(Currency.getInstance(cad));

        spinner1.setSelection(usdpos);
        spinner2.setSelection(cadpos);
    }
}
