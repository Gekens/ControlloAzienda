package com.example.giacomo.controllomagazzino;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Aggiungi extends AppCompatActivity {
    Spinner pro, qua;
    ImageView ordini, aggiungi;
    Button aggiungipro;
    EditText cal;
    String spro, squa, scal, image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aggiungi);
        pro = (Spinner) findViewById(R.id.spinnerPro);
        qua = (Spinner) findViewById(R.id.spinnerQua);
        ordini = (ImageView) findViewById(R.id.imageViewOrdini);
        aggiungi = (ImageView) findViewById(R.id.imageViewAggiungi);
        aggiungi.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        cal = (EditText) findViewById(R.id.editTextCal);
        aggiungipro = (Button) findViewById(R.id.buttonAggiungi);

        ArrayAdapter<CharSequence> adapterpro = ArrayAdapter.createFromResource(this, R.array.pro, android.R.layout.simple_spinner_item);
        adapterpro.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pro.setAdapter(adapterpro);
        image = "http://serverdatizancan.esy.es/ciliegieicon.png";


        ordini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent vIntent = new Intent(Aggiungi.this, MainActivity.class);
                startActivity(vIntent);
            }
        });

        pro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (pro.getSelectedItemPosition() == 0) {
                    image = "http://serverdatizancan.esy.es/ciliegieicon.png";
                    ArrayAdapter<CharSequence> adaptercqua = ArrayAdapter.createFromResource(getBaseContext(), R.array.cqua, android.R.layout.simple_spinner_item);
                    adaptercqua.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    qua.setAdapter(adaptercqua);
                }
                else if (pro.getSelectedItemPosition() == 1){
                    image = "http://serverdatizancan.esy.es/peas.png";
                    ArrayAdapter<CharSequence> adapterpqua = ArrayAdapter.createFromResource(getBaseContext(), R.array.pqua, android.R.layout.simple_spinner_item);
                    adapterpqua.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    qua.setAdapter(adapterpqua);
                }
                else {
                    image = "http://serverdatizancan.esy.es/peach.png";
                    ArrayAdapter<CharSequence> adapterequa = ArrayAdapter.createFromResource(getBaseContext(), R.array.equa, android.R.layout.simple_spinner_item);
                    adapterequa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    qua.setAdapter(adapterequa);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        cal.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    DialogFragment newFragment1 = new DataPicker1();
                    newFragment1.show(getFragmentManager(), "Date Picker");
                }
            }
        });

        aggiungipro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spro = pro.getSelectedItem().toString();
                squa = qua.getSelectedItem().toString();
                scal = cal.getText().toString();

                if (spro == "Ciliegie") {
                    image = "http://serverdatizancan.esy.es/ciliegieicon.png";
                }
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

                StrictMode.setThreadPolicy(policy);

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                nameValuePairs.add(new BasicNameValuePair("image", image));
                nameValuePairs.add(new BasicNameValuePair("qua", squa));
                nameValuePairs.add(new BasicNameValuePair("cal", scal));

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost("http://serverdatizancan.esy.es/setCustomer.php");
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse httpResponse = httpClient.execute(httpPost);
                    HttpEntity httpEntity = httpResponse.getEntity();

                    Toast.makeText(Aggiungi.this, "Database", Toast.LENGTH_SHORT).show();
                } catch (ClientProtocolException e){
                    Log.d("Log tag", e + "");
                }
                catch (IOException e){
                    Log.d("Log tag", e + "");
                }
            }
        });
    }

    @Override
    protected void onResume() {
        if (pro.getSelectedItemPosition() == 0) {
            ArrayAdapter<CharSequence> adaptercqua = ArrayAdapter.createFromResource(this, R.array.cqua, android.R.layout.simple_spinner_item);
            adaptercqua.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            qua.setAdapter(adaptercqua);
        }
        else {
            ArrayAdapter<CharSequence> adapterpqua = ArrayAdapter.createFromResource(this, R.array.pqua, android.R.layout.simple_spinner_item);
            adapterpqua.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            qua.setAdapter(adapterpqua);
        }
        super.onResume();
    }
}
