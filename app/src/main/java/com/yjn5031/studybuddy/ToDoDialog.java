package com.yjn5031.studybuddy;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class ToDoDialog extends AppCompatDialogFragment {

    private EditText editTextToDoName;

    private ToDoDialogListen listenr = null;



    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_todo_dialog, null);
        editTextToDoName = view.findViewById(R.id.todo_name);

        builder.setView(view)
                .setTitle("Add To-Do")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = editTextToDoName.getText().toString();

                        TodoFragment fragment = (TodoFragment) getActivity().getSupportFragmentManager().findFragmentByTag("TODOFragment");

                        Log.d("Test", name);

                        fragment.applyTexts(name);
                    }
                });


        return builder.create();
    }

    public  interface ToDoDialogListen{
        void applyTexts(String name);
    }
}
