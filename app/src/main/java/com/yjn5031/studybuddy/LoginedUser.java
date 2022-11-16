package com.yjn5031.studybuddy;


import java.util.List;

public class LoginedUser {
    public static User user = null;
    public static List<ToDo> userToDos = null;
    public static  List<EventElement> userEvents = null;


    public static void Logout(){
        user = null;
        userToDos = null;
        userEvents = null;
    }


}
