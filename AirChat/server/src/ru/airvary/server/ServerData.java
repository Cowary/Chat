package ru.airvary.server;

import ru.airvary.others.SecurityChat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ServerData implements Serializable {

    private static final long serialVersionUID = 7329308235721507390L;

    private String login;
    private String password;
    private String registrationPassword;
    private Map<String, String> channelMap;
    private ArrayList<String> userList;
    //private Map<String, ArrayList<String>> groupsMap;

    public ServerData(String login, String password){
        this.login = login;
        this.password = SecurityChat.md5(password);
        channelMap = new HashMap<>();
        userList = new ArrayList<>();
    }
    public boolean check(String login, String pass){
        if(this.login.equals(login) && this.password.equals(pass)){
            return true;
        }else{
            return false;
        }
    }
    public boolean checkRegPassword(String pass){ return SecurityChat.md5(registrationPassword).equals(pass); }

    public String getLogin() {
        return login;
    }

    public String getRegistrationPassword() {
        return registrationPassword;
    }
    public void updateRegistrationPassword(String registrationPassword){
        this.registrationPassword = registrationPassword;
    }
    public void updateLogin(String login){this.login = login;}
    public void updatePassword(String password){this.password = SecurityChat.md5(password);}
    public void deleteChannel(String name){channelMap.remove(name);}

    public void addChannel(String name){
        channelMap.put(name, "Начало канала<br>");
    }
    public Map<String, String> getChannelMap(){
        return channelMap;
    }
    public void addMessageToGroup(String nameChannel, String nameUser, String message){
        String mess = nameUser + ": " + ": " + message;
        String ag = channelMap.get(nameChannel);
        ag += mess;
        ag += "<br>";
        channelMap.put(nameChannel, ag);
    }
    public ArrayList<String> getAllNameChannel(){
        return new ArrayList<String>(channelMap.keySet());
    }
    public void addUser(String userLogin){
        userList.add(userLogin);
    }
    public ArrayList<String> getUserList(){
        return userList;
    }
    /*public void addGroup(String name, ArrayList<String> array ){
        groupsMap.put(name, array);
    }
    public void addUserToGroup(String nameGroup, String name){
        ArrayList<String> groupUser = groupsMap.get(nameGroup);
        groupUser.add(name);
        groupsMap.put(nameGroup, groupUser);
    }

    public Map<String, ArrayList<String>> getGroupsMap() {
        return groupsMap;
    }
    public ArrayList<String> getAllNameGroup(){
        return new ArrayList<String>(groupsMap.keySet());
    }

    public ArrayList<String> getListGroup(String nameGroup){return groupsMap.get(nameGroup);}
    */
}
