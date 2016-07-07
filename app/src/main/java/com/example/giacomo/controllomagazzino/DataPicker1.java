package com.example.giacomo.controllomagazzino;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by Giacomo on 28/06/2016.
 */
public class DataPicker1 extends DialogFragment implements DatePickerDialog.OnDateSetListener{
    String data;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dpd = new DatePickerDialog(getActivity(), this, year, month, day);
        dpd.setTitle("Seleziona Disponibilità");
        return dpd;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        EditText tv = (EditText) getActivity().findViewById(R.id.editTextCal);
        monthOfYear = monthOfYear+1;
        String stringOfDate = dayOfMonth + "/" + monthOfYear + "/" + year;
        tv.setText(stringOfDate);
    }
}
