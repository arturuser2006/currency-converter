package com.example.flashlight;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Spinner spinner;
    Spinner spinner2;
    String[] items = new String[] {"USD", "EUR","AUD", "GBP", "DKK", "KZT", "CAD", "CNY", "NOK", "TRY", "UAH", "CHF", "RUB", "JPY", "PLN", "INR"};
    String[] items2 = new String[] {"USD", "EUR","AUD", "GBP", "DKK", "KZT", "CAD", "CNY", "NOK", "TRY", "UAH", "CHF", "RUB", "JPY", "PLN", "INR"};
    ArrayAdapter<String> adapter;
    String currency;
    public static String to;
    public static String from;
    public static Double amount;
    EditText initial;
    EditText converted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("MyLog", "onCreate");
        //это нужно чтобы не было networkonmainthreadexception
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    @Override
    protected void onStart() {
        super.onStart();

        initial = findViewById(R.id.initialCurrency);
        converted = findViewById(R.id.converterCurrency);
        spinner2 = findViewById(R.id.spinner2);
        spinner = findViewById(R.id.spinner);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, android.R.id.text1, items);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, android.R.id.text1, items2);
        spinner2.setAdapter(adapter2);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                from = parent.getSelectedItem().toString().toLowerCase(Locale.ROOT);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                to = parent.getSelectedItem().toString().toLowerCase(Locale.ROOT);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Log.d("MyLog", "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("MyLog", "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("MyLog", "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("MyLog", "onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("MyLog", "onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("MyLog", "onDestroy");
    }

    public void sendHttpGetRequest(String from, String to, Double amount) throws IOException, JSONException {
        DecimalFormat df = new DecimalFormat("0.00");
        String getUrl = "https://cdn.jsdelivr.net/gh/fawazahmed0/currency-api@1/latest/currencies/" + from+ "/" + to + ".json";
        URL url = new URL(getUrl);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        int responseCode = httpURLConnection.getResponseCode();

        if (responseCode == 200) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String inputLine;
            StringBuilder stringBuffer = new StringBuilder();
            while ((inputLine = reader.readLine()) != null) {
                stringBuffer.append(inputLine);
            }
            reader.close();
            JSONObject jsonObject = new JSONObject(stringBuffer.toString());
            currency = jsonObject.getString(to);
            currency = df.format(Double.parseDouble(currency) * amount);
            Log.d("MyLog", currency);
        } else {
            Log.d("MyLog", "RES" + responseCode);
        }
        converted.setText(currency);
    }

    public void onConvertClick(View view) {
        try {
            amount = Double.parseDouble(initial.getText().toString());
            sendHttpGetRequest(from, to, amount);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Ошибка. Пустое поле ввода", Toast.LENGTH_SHORT).show();
        }
    }
}