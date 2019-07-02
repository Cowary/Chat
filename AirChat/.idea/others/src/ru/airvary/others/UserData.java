package ru.airvary.others;

import java.io.Serializable;
import java.util.ArrayList;


public class UserData implements Serializable {
    private static final long serialVersionUID = 2329199035760607390L;
    private String login;
    private String password;
    //private Map<String, String> chatHistoryMap = new HashMap<>();
    private ArrayList<String> chatHistory;
    private ArrayList<String> friendsList;
    //private ArrayList<String> groups;
    //private ArrayList<String> groupsHistory;

    public UserData(String login, String password){
        this.login = login;
        this.password = SecurityChat.md5(password);
        chatHistory = new ArrayList<>();
        friendsList = new ArrayList<>();
        //groups = new ArrayList<>();
        //groupsHistory = new ArrayList<>();
    }
    //public List getChatHistory(String name){
    //    return chatHistoryMap;
    //}
    public ArrayList getAllChatHistory(){return chatHistory;}
    public ArrayList getFriends(){return friendsList;}
    public boolean check(String pass){
        return password.equals(pass);
    }
    public String getLogin() {return login;}
    public String getPassword() {return password;}
    //public ArrayList getGroups() {return groups;}
    //public ArrayList getGroupsHistory() {return groupsHistory;}

    /*
    public void addMessageToGroupHistory(String nameUser, String nameGroup, String message){
        String mess = nameUser + ": " + ": " + message;
        String ag = groupsHistory.get(groups.indexOf(nameGroup));
        ag += mess;
        ag += "<br>";
        groupsHistory.set(groups.indexOf(nameGroup), ag);
        System.out.println("Message = " + message);
    }*/

    public void addReciviedMessageToChatHistory(String name, String message){
        String mess = name + ": " + message;
        String ag = chatHistory.get(friendsList.indexOf(name));
        ag += mess;
        ag += "<br>";
        chatHistory.set(friendsList.indexOf(name), ag);
    }
    public void addSendMessageToChatHistory(String name, String message){
        String mess = login + " => " + name + ": " + message;
        String ag = chatHistory.get(friendsList.indexOf(name));
        ag += mess;
        ag += "<br>";
        chatHistory.set(friendsList.indexOf(name), ag);
    }

    public void addFriend(String name){
        System.out.println("addFrineList " + name);
        System.out.println(friendsList.toString());
        friendsList.add(name);
        chatHistory.add("");
    }
    /*
    public void addGroup(String name){
        groups.add(name);
        groupsHistory.add("");
    }
    */
    @Override
    public String toString(){
        String los = "login = " + login + " password = " + password;
        for (String st : friendsList
             ) {
            System.out.println(st);
        }
        return los;
    }


}
