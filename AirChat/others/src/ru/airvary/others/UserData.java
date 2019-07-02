package ru.airvary.others;

import com.sun.istack.internal.Nullable;

import java.io.Serializable;
import java.util.ArrayList;


public class UserData implements Serializable {
    private static final long serialVersionUID = 2329299035760607390L;
    private String login;
    private String password;
    private String fullName;
    private String info;
    //private Map<String, String> chatHistoryMap = new HashMap<>();
    private ArrayList<String> chatHistory;
    private ArrayList<String> userList;
    private ArrayList<String> friendsList;
    //private ArrayList<String> groups;
    //private ArrayList<String> groupsHistory;


    public UserData(String login, String password){
        this.login = login;
        this.password = SecurityChat.md5(password);
        this.fullName = "";
        this.info = "";
        chatHistory = new ArrayList<>();
        friendsList = new ArrayList<>();
        userList = new ArrayList<>();
        //groups = new ArrayList<>();
        //groupsHistory = new ArrayList<>();
    }
    //public List getChatHistory(String name){
    //    return chatHistoryMap;
    //}
    public ArrayList getAllChatHistory(){return chatHistory;}
    public String getChatHistory  (String userLogin) throws ArrayIndexOutOfBoundsException{return chatHistory.get(userList.indexOf(userLogin));}
    public ArrayList getFriends(){return friendsList;}
    public boolean check(String pass){
        return password.equals(pass);
    }
    public String getLogin() {return login;}
    public String getPassword() {return password;}
    public String getFullName() {return fullName;}
    public String getInfo(){return info;}


    public void addUserList(String userLogin){
        userList.add(userLogin);
        chatHistory.add("");
    }
    public void deleteUserList(String userLogin){
        chatHistory.remove(userList.indexOf(userLogin));
        if(friendsList.contains(userLogin)){
            friendsList.remove(userLogin);
        }
        userList.remove(userLogin);
    }

    public void addReciviedMessageToChatHistory(String name, String message){
        try{
            String mess = name + ": " + message;
            //String ag = chatHistory.get(friendsList.indexOf(name));
            String ag = chatHistory.get(userList.indexOf(name));
            ag += mess;
            ag += "<br>";
            chatHistory.set(userList.indexOf(name), ag);
        }catch (ArrayIndexOutOfBoundsException e){
            addUserList(name);
            addReciviedMessageToChatHistory(name, message);
        }

    }
    public void addSendMessageToChatHistory(String name, String message){
        try{
            String mess = login + ": " + message;
            //String ag = chatHistory.get(friendsList.indexOf(name));
            String ag = chatHistory.get(userList.indexOf(name));
            ag += mess;
            ag += "<br>";
            chatHistory.set(userList.indexOf(name), ag);
        }catch (ArrayIndexOutOfBoundsException e){
            addUserList(name);
            addSendMessageToChatHistory(name, message);
        }
    }

    public void addFriend(String name){
        System.out.println("addFriendList " + name);
        System.out.println(friendsList.toString());
        friendsList.add(name);
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
    public void updateLogin(String login){
        this.login = login;
    }
    public void updatePassword(String password){
        this.password = password;
    }
    public void updateFullName(String fullName){
        this.fullName = fullName;
        System.out.println("Обновление успешно: " + fullName);
    }
    public void updateInfo(String info){
        this.info = info;
    }


}
