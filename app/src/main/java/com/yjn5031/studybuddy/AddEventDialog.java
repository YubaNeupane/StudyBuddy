package com.yjn5031.studybuddy;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.Calendar;
import java.util.Date;

public class AddEventDialog extends AppCompatDialogFragment {

    private CalendarView calendarView;
    private EditText eventNameText;
    private Calendar calendar;


    //craetes the dialog and makes the refrence to all of the componenrts from the layout page

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_add_event_dialog, null);
        calendarView = view.findViewById(R.id.eventCalander);
        eventNameText = view.findViewById(R.id.event_name);
        calendarView.setMinDate(System.currentTimeMillis() - 1000);
        calendar = Calendar.getInstance();


        //makes the calander view date chaange listener, and sleect the only date in the future
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                calendar.set(year,month, dayOfMonth);
            }
        });

        //makes the fragament for the dialog box to pop up on top on a existing fragement
        builder.setView(view)
                .setTitle("Add Event")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EventFragment eventFragment = (EventFragment) getActivity().getSupportFragmentManager().findFragmentByTag("EVENTTRACKERFragment");
                        String name =   eventNameText.getText().toString();
                        eventFragment.applyText(name, calendar.getTimeInMillis());
//                        Toast.makeText(getActivity().getApplicationContext(), "Date: "+ date,Toast.LENGTH_LONG).show();

                    }
                });

        return builder.create();

    }

    //a interface to get access to the data being passed from the dialog fragament
    public interface AddEventListen{
        void applyText(String name, long date);
    }

}
