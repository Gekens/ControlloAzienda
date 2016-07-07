package com.example.giacomo.controllomagazzino;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

/**
 * Created by Giacomo on 27/06/2016.
 */
public class StringFragment extends DialogFragment {
    EditText text;
    String stext, nome, cognome, mail, sdisp;
    Button invia;
    private Mail m;


    @Nullable
    @Override



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.stringfragment, container, false);
        m = new Mail("giacomozancan@gmail.com", "95642108");
        text = (EditText) view.findViewById(R.id.editText);
        invia = (Button) view.findViewById(R.id.button);

        Bundle vBundle = this.getArguments();
        nome = vBundle.getString("name");
        cognome = vBundle.getString("surname");
        mail = vBundle.getString("mail");

        invia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stext = text.getText().toString();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

                StrictMode.setThreadPolicy(policy);

                sdisp = "Non disponibile";

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                nameValuePairs.add(new BasicNameValuePair("name", nome));
                nameValuePairs.add(new BasicNameValuePair("surname", cognome));
                nameValuePairs.add(new BasicNameValuePair("disp", sdisp));


                String[] toArr = {mail}; // This is an array, you can add more emails, just separate them with a coma
                m.setTo(toArr); // load array to setTo function
                m.setFrom("fromEmail@domain.tld"); // who is sending the email
                m.setSubject("Ordine 'Prime Giant'");
                m.setBody(stext);

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
                    Toast.makeText(getActivity(), "Eliminato: " + nome + " " + cognome, Toast.LENGTH_SHORT).show();
                } catch (ClientProtocolException e) {
                    Log.d("Log tag", e + "");
                } catch (IOException e) {
                    Log.d("Log tag", e + "");
                }

                Intent vIntent = new Intent(getActivity(), MainActivity.class);
                startActivity(vIntent);
            }
        });
        return view;
    }
}
