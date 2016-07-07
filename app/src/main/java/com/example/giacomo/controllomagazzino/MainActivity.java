package com.example.giacomo.controllomagazzino;

import android.app.DialogFragment;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    MyCustomAdapter dataAdapter = null;
    ImageButton send;
    ImageView ordini, aggiungi;
    SwipeRefreshLayout swipelayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        send = (ImageButton) findViewById(R.id.imageButton);
        swipelayout = (SwipeRefreshLayout) findViewById(R.id.swipelayout);
        ordini = (ImageView) findViewById(R.id.imageViewOrdini);
        aggiungi = (ImageView) findViewById(R.id.imageViewAggiungi);
        ordini.setColorFilter(0xe0f47521, PorterDuff.Mode.SRC_ATOP);

        swipelayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new GetAllCustomerTask().execute(new ApiConnector());
                isInternetOn();
            }
        });

        aggiungi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent vIntent = new Intent(MainActivity.this, Aggiungi.class);
                startActivity(vIntent);
            }
        });
        isInternetOn();
        new GetAllCustomerTask().execute(new ApiConnector());

    }

    @Override
    protected void onResume() {
        new GetAllCustomerTask().execute(new ApiConnector());
        super.onResume();
    }

    public void setTextToTextView(JSONArray jsonArray) {
        int n = 0;
        int indice = 0;
        int i;
        String code;
        String s = "";
        ArrayList<Cherries> cherriesList = new ArrayList<Cherries>();
        if (jsonArray != null) {
            for (i = 0; i < jsonArray.length(); i++) {
                code = Integer.toString(i);
                JSONObject json = null;
                try {
                    n = jsonArray.length() - 1 - i;
                    json = jsonArray.getJSONObject(n);
                    cherriesList.add(new Cherries(code, json.getString("name"), json.getString("surname"), json.getString("mail"), false));

                    dataAdapter = new MyCustomAdapter(this, R.layout.cell_layout, cherriesList);
                    ListView listView = (ListView) findViewById(R.id.listView);
                    // Assign adapter to ListView
                    listView.setAdapter(dataAdapter);




                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private class GetAllCustomerTask extends AsyncTask<ApiConnector, Long, JSONArray> {
        ProgressDialog progress;

        @Override
        protected void onCancelled(JSONArray jsonArray) {
            Toast.makeText(MainActivity.this, "Controlla la connessione", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPreExecute() {
            progress = new ProgressDialog(MainActivity.this);
            progress .setMessage("loading");
            progress .show();
        }

        @Override
        protected JSONArray doInBackground(ApiConnector... params) {

            // it is executed on Background thread

            return params[0].GetAllCustomers();
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {

            setTextToTextView(jsonArray);
            progress.dismiss();
            swipelayout.setRefreshing(false);

        }
    }

    private class MyCustomAdapter extends ArrayAdapter<Cherries> {

        private ArrayList<Cherries> cherriesList;

        public MyCustomAdapter(Context context, int textViewResourceId, ArrayList<Cherries> cherriesList) {
            super(context, textViewResourceId, cherriesList);
            this.cherriesList = new ArrayList<Cherries>();
            this.cherriesList.addAll(cherriesList);
        }

        private class ViewHolder {
            TextView code;
            CheckBox name;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));
            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.cell_layout, null);

                holder = new ViewHolder();
                holder.code = (TextView) convertView.findViewById(R.id.code);
                holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
                convertView.setTag(holder);

                holder.name.setOnClickListener( new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v ;
                        Cherries cherries = (Cherries) cb.getTag();
                        final String nome = cherries.getConferma();
                        final String cognome = cherries.getConfermaSur();
                        final String mail = cherries.getConfermaMail();
                        if (cb.isChecked() == true) {
                            Toast.makeText(getApplicationContext(), "Hai selezionato: " + cb.getText(), Toast.LENGTH_SHORT).show();
                        }
                        cherries.setSelected(cb.isChecked());

                        send.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //Creating the instance of PopupMenu
                                PopupMenu popup = new PopupMenu(MainActivity.this, send);
                                //Inflating the Popup using xml file
                                popup.getMenuInflater()
                                        .inflate(R.menu.popup_menu, popup.getMenu());

                                //registering popup with OnMenuItemClickListener
                                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                    public boolean onMenuItemClick(MenuItem item) {
                                        if (item.getItemId() == R.id.one) {
                                            Bundle bundle = new Bundle();
                                            bundle.putString("name", nome);
                                            bundle.putString("surname", cognome);
                                            bundle.putString("mail", mail);
                                            DialogFragment fragInfo = new DataPicker();
                                            fragInfo.setArguments(bundle);
                                            fragInfo.show(getFragmentManager(),"Date Picker");
                                        }
                                        else if (item.getItemId() == R.id.two) {
                                            Bundle bundle = new Bundle();
                                            bundle.putString("name", nome);
                                            bundle.putString("surname", cognome);
                                            bundle.putString("mail", mail);
                                            DialogFragment fragInfo = new StringFragment();
                                            fragInfo.setArguments(bundle);
                                            fragInfo.show(getFragmentManager(),"String");
                                        }
                                        return true;
                                    }
                                });

                                popup.show(); //showing popup menu
                            }
                        });
                    }
                });
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            Cherries cherries = cherriesList.get(position);
            holder.name.setText(cherries.getName());
            holder.name.setChecked(cherries.isSelected());
            holder.name.setTag(cherries);

            return convertView;

        }

    }

    public final boolean isInternetOn() {

        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        if ( connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {

            // if connected with internet

            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED  ) {

            Toast.makeText(this, " Connessione Assente ", Toast.LENGTH_LONG).show();
            return false;
        }
        return false;
    }


}