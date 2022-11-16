package com.yjn5031.studybuddy;

import android.icu.text.SimpleDateFormat;
import android.net.ParseException;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

interface MyButtonClickListener extends View.OnClickListener {

    @Override
    public void onClick(View view);
}
public class EventFragment extends Fragment {

    AppDatabase db;
    EventDao eventDao;

    RecyclerView recyclerView;

    private EventAdapter eventAdapter;

    FloatingActionButton addEventButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event, container, false);

        addEventButton = view.findViewById(R.id.addEventButton);

        addEventButton.setOnClickListener(new MyButtonClickListener() {
            @Override
            public void onClick(View view) {
                openAddEventDialog();
            }
        });

        recyclerView = view.findViewById(R.id.eventsList);
        db = AppDatabase.getDatabase(getActivity().getApplicationContext());
        eventDao = db.eventDao();

//        EventElement eventElement = new EventElement();
//        eventElement.mCreatedAt = System.currentTimeMillis();
//        eventElement.mUserId = LoginedUser.user.mId;
//        eventElement.mDueAt = System.currentTimeMillis();
//        eventElement.mName = "This is test event";

//        eventDao.addEvent(eventElement);

        LoginedUser.userEvents = eventDao.getAllOfUserEvents(LoginedUser.user.mId);

        eventAdapter = new EventAdapter(LoginedUser.userEvents);
        recyclerView.setAdapter(eventAdapter);

        return view;
    }

    public void applyText(String name, long date){
        if(name.equals((""))){
            Toast.makeText(getActivity().getApplicationContext(), "Invalid Input!", Toast.LENGTH_SHORT).show();
            return;
        }

        EventElement eventElement = new EventElement();
        eventElement.mName = name;
        eventElement.mDueAt = date;
        eventElement.mCreatedAt = System.currentTimeMillis();
        eventElement.mIsComplected = false;
        eventElement.mUserId = LoginedUser.user.mId;

        eventDao.addEvent(eventElement);
        LoginedUser.userEvents = eventDao.getAllOfUserEvents(LoginedUser.user.mId);
        eventAdapter.mEvents = eventDao.getAllOfUserEvents(LoginedUser.user.mId);
        eventAdapter.notifyDataSetChanged();
        Toast.makeText(getActivity().getApplicationContext(), "Event Added!", Toast.LENGTH_SHORT).show();


    }

    public void openAddEventDialog(){
        AddEventDialog addEventDialog = new AddEventDialog();
        addEventDialog.onAttach(getContext());
        addEventDialog.show(getActivity().getSupportFragmentManager(), "AddEventDialog");
    }


    private class EventAdapter extends RecyclerView.Adapter<EventHolder>{
        public List <EventElement> mEvents;

        public EventAdapter(List<EventElement> mEvents){
            this.mEvents =mEvents;
        }


        @NonNull
        @Override
        public EventHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new EventHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull EventHolder holder, int position) {
            EventElement eventElement = mEvents.get(position);
            holder.bind(eventElement, new MyButtonClickListener() {
                @Override
                public void onClick(View view) {
                    eventElement.mIsComplected = !eventElement.mIsComplected;
                    eventDao.updateEvent(eventElement);
                    eventDao.deleteEvent(eventElement);
                    removeItem(eventElement);
                }
            });

        }

        public void removeItem(EventElement eventElement){
            mEvents.remove(eventElement);
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return mEvents.size();
        }
    }

    private class EventHolder extends RecyclerView.ViewHolder{

        private final TextView mNameTextView;
        private final TextView createdAtTextView;
        private final TextView dueAtTextView;
        private final Button completedButton;

        EventDao eventDao;

        private String getCreatedAtString(long pastTime){
            String convTime = null;

            String prefix = "";
            String suffix = "Ago";
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            try {
                Date pasTime = new Date(pastTime);

                Date nowTime = new Date();

                long dateDiff = nowTime.getTime() - pasTime.getTime();

                long second = TimeUnit.MILLISECONDS.toSeconds(dateDiff);
                long minute = TimeUnit.MILLISECONDS.toMinutes(dateDiff);
                long hour   = TimeUnit.MILLISECONDS.toHours(dateDiff);
                long day  = TimeUnit.MILLISECONDS.toDays(dateDiff);


                if (second < 60) {
                    if(second == 0){
                        convTime = "Just Added";
                    }else{
                        convTime = second + " Seconds " + suffix;
                    }
                } else if (minute < 60) {
                    convTime = minute + " Minutes "+suffix;
                } else if (hour < 24) {
                    convTime = hour + " Hours "+suffix;
                } else if (day >= 7) {
                    if (day > 360) {
                        convTime = (day / 360) + " Years " + suffix;
                    } else if (day > 30) {
                        convTime = (day / 30) + " Months " + suffix;
                    } else {
                        convTime = (day / 7) + " Week " + suffix;
                    }
                } else if (day < 7) {
                    convTime = day+" Days "+suffix;
                }
            }catch (ParseException e){
                e.printStackTrace();
                Log.e("ConvTimeE", e.getMessage());
            }
            return convTime;
        }

        public EventHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.event_list_item, parent, false));

            mNameTextView = itemView.findViewById(R.id.eventName);
            createdAtTextView = itemView.findViewById(R.id.eventCreatedAt);
            dueAtTextView = itemView.findViewById(R.id.eventDueAt);
            completedButton = itemView.findViewById(R.id.doneEventButton);

        }

        public void bind(EventElement event, View.OnClickListener myButtonClickListener){
            mNameTextView.setText(event.mName);
            createdAtTextView.setText("Created: " + getCreatedAtString(event.mCreatedAt));
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
            String dueDate = dateFormat.format(new Date(event.mDueAt));
            dueAtTextView.setText("Due: "+dueDate);

            completedButton.setOnClickListener(myButtonClickListener);


        }


    }


}