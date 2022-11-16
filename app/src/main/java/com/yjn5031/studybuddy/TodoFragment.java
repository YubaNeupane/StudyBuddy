package com.yjn5031.studybuddy;

import android.app.Activity;
import android.app.FragmentManager;
import android.icu.text.SimpleDateFormat;
import android.net.ParseException;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


interface MyClickListener extends View.OnClickListener {

    @Override
    public void onClick(View view);
}

public class TodoFragment extends Fragment {

    AppDatabase db;
    ToDoDao toDoDao;
    FloatingActionButton addToDOButton;
    RecyclerView recyclerView;
    List<ToDoModel> toDos = new ArrayList<>();
    private ToDoAdapter toDoAdapter;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_todo, container, false);
        addToDOButton = view.findViewById(R.id.addToDoButton);
        addToDOButton.setOnClickListener(new MyClickListener() {
            @Override
            public void onClick(View view) {
                openAddDialog();
            }
        });


//        View.OnClickListener onClickListener = itemView -> {
//
//            // Create fragment arguments containing the selected band ID
//            int selectedBandId = (int) itemView.getTag();
//            Bundle args = new Bundle();
//            args.putInt(DetailFragment.ARG_BAND_ID, selectedBandId);
//
//            // Replace list with details
//            Navigation.findNavController(itemView).navigate(R.id.show_item_detail, args);
//        };


        recyclerView = view.findViewById(R.id.toDoList);
        db = AppDatabase.getDatabase(getActivity().getApplicationContext());

        toDoDao = db.toDoDao();


        LoginedUser.userToDos = toDoDao.getAllOfUserToDos(LoginedUser.user.mId);
        toDoAdapter = new ToDoAdapter(LoginedUser.userToDos);
        recyclerView.setAdapter(toDoAdapter);


        return view;
    }

    public void openAddDialog(){
        ToDoDialog toDoDialog = new ToDoDialog();
        toDoDialog.onAttach(getContext());
        toDoDialog.show(getActivity().getSupportFragmentManager(), "Example");
    }

    public void applyTexts(String name) {
        if(name.equals("")){
            Toast.makeText(getActivity().getApplicationContext(), "Invalid Input!", Toast.LENGTH_SHORT).show();
            return;
        }

        ToDo newToDo = new ToDo();
        newToDo.mCreatedAt = System.currentTimeMillis();
        newToDo.mDueAt = System.currentTimeMillis();
        newToDo.mIsComplected = false;
        newToDo.mName = name;
        newToDo.mUserId = LoginedUser.user.mId;

        toDoDao.addToDO(newToDo);
        LoginedUser.userToDos = toDoDao.getAllOfUserToDos(LoginedUser.user.mId);
        toDoAdapter.mTodo = toDoDao.getAllOfUserToDos(LoginedUser.user.mId);
        toDoAdapter.notifyDataSetChanged();
       // recyclerView.getAdapter().notify();
    }

    private class ToDoAdapter extends RecyclerView.Adapter<ToDoHolder> {

        public List<ToDo> mTodo;
     //   private final View.OnClickListener mOnClickListener;

        public ToDoAdapter(List<ToDo> mTodo) {
            this.mTodo = mTodo;
//            mOnClickListener = onClickListener;
        }

        @NonNull
        @Override
        public ToDoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new ToDoHolder(layoutInflater, parent);
        }






        @Override
        public void onBindViewHolder(@NonNull ToDoHolder holder, int position) {
            ToDo todo = mTodo.get(position);
            int temp = position;
            holder.bind(todo, new MyClickListener() {
                @Override
                public void onClick(View view) {
                    todo.mIsComplected = !todo.mIsComplected;
                    toDoDao.updateToDo(todo);
                    toDoDao.deleteToDo(todo);
                    removeItem(todo);

                }
            });

           // holder.itemView.setTag(band.getId());
          //  holder.itemView.setOnClickListener(mOnClickListener);
        }

        public void removeItem(ToDo todo) {
            mTodo.remove(todo);
//            Toast.makeText(getActivity().getApplicationContext(), position, Toast.LENGTH_LONG).show();
            notifyDataSetChanged ();
        }




        @Override
        public int getItemCount() {
            return mTodo.size();
        }
    }

    private class ToDoHolder extends RecyclerView.ViewHolder {

        private final TextView mNameTextView;
        private final TextView mTodoDate;
        private final Button completedButton;
        ToDoDao toDoDao;

        public ToDoHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_todo, parent, false));
            mNameTextView = itemView.findViewById(R.id.todo_name);
            mTodoDate = itemView.findViewById(R.id.todo_date);
            completedButton = itemView.findViewById(R.id.compleButton);
            toDoDao = AppDatabase.getDatabase(parent.getContext().getApplicationContext()).toDoDao();
        }

        public void bind(ToDo todo, View.OnClickListener mOnClickListener) {
            mNameTextView.setText(todo.mName);
            String convTime = null;

            String prefix = "";
            String suffix = "Ago";
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
           try {
               Date pasTime = new Date(todo.mCreatedAt);

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
           mTodoDate.setText(convTime);

            completedButton.setOnClickListener(mOnClickListener);

        }
    }
}




