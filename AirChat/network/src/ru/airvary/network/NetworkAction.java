package ru.airvary.network;

public interface NetworkAction {

    void onConnectionReady(String name, NetworkLogic conn);
    void onFullConnReady(String answer, NetworkLogic conn);
    void getAllNameUserAndInformation(NetworkLogic conn);
    void getSendOneUser(String nameThis, String nameTo,String value, NetworkLogic conn);
    void onReceiveString(NetworkLogic conn, String value);
    void sendFileAllUser(NetworkLogic conn, String nameFile);
    void sendFileToOneUser(NetworkLogic conn, String toUser, String thisUser, String nameFile);
    void onDisconnect(NetworkLogic conn);
    void onException(NetworkLogic conn, Exception e);
    void getEncryptsMessage(String nameThis, String nameTo, NetworkLogic conn);
    void getEncryptsMessageForAllUser(NetworkLogic conn, String nameThis);
    void addFriend(NetworkLogic conn, String friendName);
    void registrationUser(NetworkLogic conn);



}
