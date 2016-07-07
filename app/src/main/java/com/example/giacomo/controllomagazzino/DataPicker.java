package com.example.giacomo.controllomagazzino;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TextView;
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
import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Giacomo on 24/06/2016.
 */
public class DataPicker extends DialogFragment implements DatePickerDialog.OnDateSetListener{
    String data;
    private Mail m;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        m = new Mail("giacomozancan@gmail.com", "95642108");
        String nome, cognome, mail;
        monthOfYear = monthOfYear+1;
        String stringOfDate = dayOfMonth + "/" + monthOfYear + "/" + year;
        Bundle vBundle = this.getArguments();
        nome = vBundle.getString("name");
        cognome = vBundle.getString("surname");
        mail = vBundle.getString("mail");


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);


        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("name", nome));
        nameValuePairs.add(new BasicNameValuePair("surname", cognome));
        nameValuePairs.add(new BasicNameValuePair("mail", mail));
        nameValuePairs.add(new BasicNameValuePair("disp", stringOfDate));


        String[] toArr = {mail}; // This is an array, you can add more emails, just separate them with a coma
        m.setTo(toArr); // load array to setTo function
        m.setFrom("fromEmail@domain.tld"); // who is sending the email
        m.setSubject("Ordine 'Prime Giant'");
        m.setBody("Il tuo ordine sarà pronto il: " + stringOfDate);

        try {
            if (m.send()) {
                // success
                Intent vIntent = new Intent(getActivity(), MainActivity.class);
                startActivity(vIntent);
            }
            else {
                // failure
                Toast.makeText(getActivity(), "Email was not sent.", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            // some other problem
            Toast.makeText(getActivity(), "There was a problem sending the email.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://serverdatizancan.esy.es/removeCherry.php");
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            Toast.makeText(getActivity(), "Mail inviata a: " + nome + " " + cognome, Toast.LENGTH_SHORT).show();
        } catch (ClientProtocolException e) {
            Log.d("Log tag", e + "");
        } catch (IOException e) {
            Log.d("Log tag", e + "");
        }

        Intent vIntent = new Intent(getActivity(), MainActivity.class);
        startActivity(vIntent);

    }

}