package ru.airvary.server;

import ru.airvary.network.NetworkAction;
import ru.airvary.network.NetworkLogic;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.swing.*;
import java.io.*;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.*;

import ru.airvary.others.SecurityChat;
import ru.airvary.others.UserData;


public class ServerChat implements NetworkAction {

    //Хранение всех соедененй
    private final ArrayList<NetworkLogic> connections = new ArrayList<>();
    //Временное соединение
    private final ArrayList<NetworkLogic> temporaryConnections = new ArrayList<>();
    //Именя пользователя
    private final ArrayList<String> namesConn = new ArrayList<>();
    //private final Map<NetworkLogic, String> userConnections = new HashMap<>();
    //ID пользователей
    private final ArrayList<Integer> idConn = new ArrayList<>();

    private final ArrayList<Key> publicKeys = new ArrayList<>();

    private final ArrayList<SecretKey> secretKeys = new ArrayList<>();
    //Временный секретный ключ
    private final ArrayList<SecretKey> temporayKeys = new ArrayList<>();

    private final List<String> userList = new ArrayList<>();
    private final List<UserData> collectionUserData = new ArrayList<>();
    private final List<String> userFullName = new ArrayList<>();
    private final List<String> userInfo = new ArrayList<>();
    private String beginSt = "<html>";
    private String finishSt = "<html>";
    private String logi = "";

    InetAddress thisIP;
    //private final List<String> logi = new ArrayList<>();
    //private final Map<String, UserData>  collectionUserData = new HashMap<>();
    //private final Map<String, ArrayList<String>> groupsMap = new HashMap<>();

    private final Thread rxThread;

    private SecurityChat securityChat;
    public ServerData serverData;
    ServerGui serverGui;




    //KeyPairGenerator parigen;
    //KeyPair keyPair;
    //Key serverPublicKey;
    //Key serverPrivateKey;
    //Cipher cipher;
    //Cipher decriptCipher;



    public static void main(String[] args) {
        new ServerChat();
    }

    private ServerChat() {
        System.out.println("Сервер запущен...");

        serverGui = new ServerGui(this);
        rxThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try (ServerSocket server = new ServerSocket(8189)) {
                    while (true) {
                        try {
                            Socket socket = server.accept();
                            createNetwork(socket);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        //Запуск потока
        rxThread.start();

        //serverGui.entry();
    }
    public synchronized void  createNetwork(Socket socket){
        try {
            new NetworkLogic(this, socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void loadingServerData(){
        try {
            FileInputStream fis = new FileInputStream("tempServerData.txt");
            ObjectInputStream oin = new ObjectInputStream(fis);
            serverData = (ServerData) oin.readObject();
        } catch (FileNotFoundException e) {
            serverGui.makeErrorMessage("Требуется сначала создать учетную запись");
        e.printStackTrace();
    } catch (IOException | ClassNotFoundException e) {
        e.printStackTrace();
    }
    }
    public void createServerData(String login, String password){
        serverData = new ServerData(login, password);
        saveServerData();
    }
    public void entryAndLoadingUserData(String login, String password){
        securityChat = new SecurityChat();
        try {
            thisIP = InetAddress.getLocalHost();
            addLogi("Сервер запущен...");
            System.out.println("IP: " + thisIP.getHostAddress());
            addLogi("IP: " + thisIP.getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        if(serverData.check(login, SecurityChat.md5(password))){
            try {
                //serverGui.mainWindows();
                serverGui.dataChannel = serverData.getAllNameChannel().toArray(new String[serverData.getAllNameChannel().size()]);
                FileInputStream fis = new FileInputStream("temp.txt");
                ObjectInputStream oin = new ObjectInputStream(fis);
                while (true){
                    try {
                        System.out.println("Дичь");
                        userList.add((String) oin.readObject());
                        collectionUserData.add((UserData) oin.readObject());
                        userFullName.add((String) oin.readObject());
                        userInfo.add((String) oin.readObject());
                        serverGui.dataUser = userList.toArray(new String[userList.size()]);
                    }catch (EOFException e){
                        oin.close();
                        break;
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }




            } catch (IOException | NullPointerException e ) {
                e.printStackTrace();
            }
            for (String user : userList){
                System.out.println(user);
            }
            for (UserData user : collectionUserData){
                System.out.println(user.toString());
            }
            for (String channel: serverData.getAllNameChannel()){
                System.out.print(channel);
            }


        }else {
            serverGui.makeErrorMessage("Неправильный логин или пароль");
        }
    }

    public void addUser(String login, String password){
        UserData user = new UserData(login, password);
        userList.add(user.getLogin());
        collectionUserData.add(user);
        userFullName.add("null");
        userInfo.add("null");
        saveUserData();
        serverGui.dataUser = userList.toArray(new String[userList.size()]);
        System.out.println("Пользователь добавлен: " + login);
        addLogi("Пользователь добавлен: " + login);
    }
    public void deleteUser(String login){
        if(namesConn.contains(login)){
            onDisconnect(connections.get(namesConn.indexOf(login)));
        }
        collectionUserData.remove(userList.indexOf(login));
        userFullName.remove(userList.indexOf(login));
        userInfo.remove(userList.indexOf(login));
        userList.remove(login);
        saveUserData();
        serverGui.dataUser = userList.toArray(new String[userList.size()]);
        System.out.println("Пользователь удален: " + login);
        addLogi("Пользователь удален: " + login);

    }
    public void addChannel(String name){
        serverData.addChannel(name);
        saveServerData();
        serverGui.dataChannel = serverData.getAllNameChannel().toArray(new String[serverData.getAllNameChannel().size()]);
        System.out.print("Добавлен канал: " + name);
        addLogi("Канал добавлен: " + name);
    }
    public void deleteChannel(String name){
        serverData.deleteChannel(name);
        saveServerData();
        serverGui.dataChannel = serverData.getAllNameChannel().toArray(new String[serverData.getAllNameChannel().size()]);
        System.out.println("Канал удален: " + name);
        addLogi("Канал удален: " + name);
    }
    public void changeLogin(String login){
        serverData.updateLogin(login);
        addLogi("Логин измене: " + login);
        saveServerData();
    }
    public void changePassword(String password){
        serverData.updatePassword(password);
        addLogi("Пароль изменен");
        saveServerData();
    }
    public void addLogi(String st){
        logi += st;
        logi += "<br>";
        String logiNew = beginSt + logi + finishSt;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                serverGui.mainChat.setText(logiNew);
                //gui.list.setListData(array);
            }
        });
    }
    /*
    public void addGroup(String name){
        serverData.addGroup(name, new ArrayList<>());
        saveServerData();
        serverGui.dataGroup = serverData.getAllNameGroup().toArray(new String[serverData.getAllNameGroup().size()]);
        System.out.println("Группа добавлена: " + name);
    }
    */
    /*public void setSwingFriendList(ArrayList<String> List){
        String[] array = userList.toArray(new String[List.size()]);
        serverGui.dataUser = array;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                //Обновлеие списка
                serverGui.list.setListData(array);
            }
        });
    }*/
    /*
    public void setChat(String diolog){
        String diologNew = BeginSt + diolog + FinishSt;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                gui.mainChat.setText(diologNew);
                //gui.list.setListData(array);
            }
        });
    }
    */
    /*public void setSwingGroupList(ArrayList<String> list){
        String[] array = serverData.getAllNameChannel().toArray(new String[list.size()]);
        serverGui.dataChannel = array;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                //Обновлеие списка
                serverGui.list.setListData(array);
            }
        });
    }*/

    //Добавка в коллекцию соединение и сообщение о подключение пользователя
    @Override
    public void onConnectionReady(String name, NetworkLogic conn) {
        BigInteger modules = (BigInteger) conn.saveObject();
        BigInteger exponent = (BigInteger) conn.saveObject();
        System.out.println("modules = " + modules);
        System.out.println("exponent = " + exponent);
        for (String st: namesConn
             ) {
            if(st.compareTo(name) == 0){
                conn.sendString(name);
                conn.sendString("CodeOnFullConnReady");
                conn.sendString("NO");
                System.out.println("NO");
                return;
            }
        }
        Key k = null;
        try {
            KeyFactory fact = KeyFactory.getInstance("RSA");
            k = fact.generatePublic(new RSAPublicKeySpec(modules, exponent));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        if(name.compareTo("CodeRegUserToServer1632") == 0){
            //conn.sendString(name);
            conn.sendString("CodeRegUserToServer1632");
            temporaryConnections.add(conn);
            int i = temporaryConnections.indexOf(conn);
            temporayKeys.add(i, securityChat.genAESsecurity());
            String encodedKey = Base64.getEncoder().encodeToString(temporayKeys.get(i).getEncoded());
            byte[] secretKey = securityChat.EncryptMessageRSA(encodedKey, k);
            conn.sendByte(secretKey);
            return;

        }
        connections.add(conn);
        int i = connections.indexOf(conn);
        System.out.println(name + " явлется номером = " + i);
        idConn.add(i);
        namesConn.add(name);
        secretKeys.add(i, securityChat.genAESsecurity());
        System.out.println("SecretKey = " + secretKeys.get(i));
        String encodedKey = Base64.getEncoder().encodeToString(secretKeys.get(i).getEncoded());
        System.out.println("SecretKey = " + encodedKey);



        publicKeys.add(i, k);
        //Потверждение подключения пользователя и отправка секретного ключа
        System.out.println(name + " public key = " + k.toString());
        //System.out.println("serverPublicKey = " + securityChat.publicKey.toString());
        conn.sendString(name);
        conn.sendByte(encryptMessage(encodedKey, i));
        //sendServerPublicKey(conn);

        //conn.sendByte(encryptMessage(encodedKey, i));
        sendToAllConnections("Client connected: " + conn +  " ID = " + idConn.get(i));
    }

    @Override
    public void onFullConnReady(String name, NetworkLogic conn) {
        IvParameterSpec iv = new IvParameterSpec(conn.saveByte());
        securityChat.DecryptCipherAES(secretKeys.get(namesConn.indexOf(name)), iv);
        String login = securityChat.DecryptMessageAES(conn.saveByte());
        iv = new IvParameterSpec(conn.saveByte());
        securityChat.DecryptCipherAES(secretKeys.get(namesConn.indexOf(name)), iv);
        String password = securityChat.DecryptMessageAES(conn.saveByte());
        UserData userData = collectionUserData.get(userList.indexOf(login));
        conn.sendString("CodeOnFullConnReady");
        System.out.println("password = " + password);
        System.out.println(" ");
        System.out.println("passwordUserData = " + userData.getPassword());
        if(userData.check(password)){
            conn.sendString("YES");
            iv = securityChat.CipherAES(secretKeys.get(namesConn.indexOf(name)));
            conn.sendByte(iv.getIV());
            conn.sendByte(securityChat.EncryptObjectAES(userData));
            conn.sendByte(securityChat.EncryptObjectAES(serverData.getChannelMap()));
            addLogi("Добавлен пользователь: " + name + " connection: " + conn + " ID = " + idConn.get(namesConn.indexOf(name)));
            System.out.println("YES");
            sendNameList();
        }else {
            conn.sendString("NO");
            System.out.println("NO");
        }
    }
    @Override
    public void registrationUser(NetworkLogic conn) {
        System.out.println("Регистрация 2 этап");
        IvParameterSpec iv = new IvParameterSpec(conn.saveByte());
        securityChat.DecryptCipherAES(temporayKeys.get(temporaryConnections.indexOf(conn)), iv);
        String registrationPassword = securityChat.DecryptMessageAES(conn.saveByte());
        iv = new IvParameterSpec(conn.saveByte());
        securityChat.DecryptCipherAES(temporayKeys.get(temporaryConnections.indexOf(conn)), iv);
        String login = securityChat.DecryptMessageAES(conn.saveByte());
        iv = new IvParameterSpec(conn.saveByte());
        securityChat.DecryptCipherAES(temporayKeys.get(temporaryConnections.indexOf(conn)), iv);
        String password = securityChat.DecryptMessageAES(conn.saveByte());
        conn.sendString("CodeRegistrationUserToServer");
        if(serverData.checkRegPassword(registrationPassword) ){
            for (String st: userList
            ) {
                if(st.compareTo(login) == 0){
                    conn.sendString("NO");
                    System.out.println("NO");
                    int i = temporaryConnections.indexOf(conn);
                    conn.disconnect();
                    temporayKeys.remove(i);
                    temporaryConnections.remove(conn);
                    return;
                }
            }
            conn.sendString("YES");
            addUser(login, password);
            int i = temporaryConnections.indexOf(conn);
            conn.disconnect();
            temporayKeys.remove(i);
            temporaryConnections.remove(conn);

        }else {
            conn.sendString("NO");
            System.out.println("NO");
            int i = temporaryConnections.indexOf(conn);
            conn.disconnect();
            temporayKeys.remove(i);
            temporaryConnections.remove(conn);
        }

    }
    public void sendServerPublicKey(NetworkLogic conn){
        try {
            KeyFactory fact = KeyFactory.getInstance("RSA");
            RSAPublicKeySpec pub = fact.getKeySpec(securityChat.publicKey, RSAPublicKeySpec.class);
            BigInteger modulus = pub.getModulus();
            BigInteger exponent = pub.getPublicExponent();
            conn.sendObject(modulus);
            conn.sendObject(exponent);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void getAllNameUserAndInformation(NetworkLogic conn) {
        String user = conn.saveString();
        IvParameterSpec iv = new IvParameterSpec(conn.saveByte());
        securityChat.DecryptCipherAES(secretKeys.get(namesConn.indexOf(user)), iv);
        //securityChat.DecryptCipherAES(secretKeys.get(connections.indexOf(conn)), iv);
        UserData userData = (UserData) securityChat.DecryptObjectAES(conn.saveByte());
        collectionUserData.set(userList.indexOf(user), userData);
        userFullName.set(userList.indexOf(user), userData.getFullName());
        userInfo.set(userList.indexOf(user), userData.getInfo());
        saveUserData();
        sendNameList();
    }

    @Override
    public void getSendOneUser(String nameThis, String nameTo, String value, NetworkLogic conn) {
        System.out.println("Пользователь (" + nameThis + ") отправил сообщение пользователю (" + nameTo + ")");
        System.out.println("Сообщение: " + value);
        connections.get(namesConn.indexOf(nameTo)).sendString("CodeGetSendOneUser");
        connections.get(namesConn.indexOf(nameTo)).sendString(nameThis);
        connections.get(namesConn.indexOf(nameTo)).sendString(nameTo);
        connections.get(namesConn.indexOf(nameTo)).sendString(nameThis + ": " + value);
    }

    //Перадача полученного сообщения функции отправляюее её всем клиентам
    @Override
    public void onReceiveString(NetworkLogic conn, String value) {
        sendToAllConnections(value);
    }

    @Override
    public void sendFileAllUser(NetworkLogic conn, String FileName) {
        System.out.println("Началось получение и запись файлов на сервер");
        conn.saveFile(FileName);
        sendFileToAll(conn, FileName);
    }

    @Override
    public void sendFileToOneUser(NetworkLogic conn, String toUser, String thisUser, String nameFile) {
        System.out.println("Пользователь (" + thisUser + ") отправил файл " + nameFile + " пользователю (" + toUser + ")");
        addLogi("Пользователь " + thisUser + " отправил файл " + nameFile + " пользователю " + toUser);
        conn.saveFile(nameFile);
        int index = namesConn.indexOf(toUser);
        System.out.println("index = " + index);
        connections.get(index).sendString("CodeFileSendOneUser");
        connections.get(index).sendString(toUser);
        connections.get(index).sendString(thisUser);
        connections.get(index).sendFile(nameFile, nameFile);
    }
    //Удаление всех соеденений из коллекции
    @Override
    public void onDisconnect(NetworkLogic conn) {
        int i = connections.indexOf(conn);
        addLogi("Пользователь " + namesConn.get(i) + " : " + conn);
        connections.get(i).disconnect();
        connections.remove(i);
        namesConn.remove(i);
        idConn.remove(i);
        sendToAllConnections("Client disconnected: " + conn );
        sendNameList();
    }
    //Обработка ошибок
    @Override
    public void onException(NetworkLogic conn, Exception e) {
        System.out.println("NetworkLogic exception: " + e);
    }


    @Override
    public void getEncryptsMessage(String nameThis, String nameTo, NetworkLogic conn) {
        IvParameterSpec iv = new IvParameterSpec(conn.saveByte());
        securityChat.DecryptCipherAES(secretKeys.get(namesConn.indexOf(nameThis)), iv);
        byte[] encryptedBytes = conn.saveByte();
        String message = "Error";
        message = securityChat.DecryptMessageAES(encryptedBytes);

        System.out.println("Message = " + message);
        System.out.println("Пользователь (" + nameThis + ") отправил зашифрованное сообщение пользователю (" + nameTo + ")");
        addLogi("Пользователь " + nameThis + " отправил зашифрованное сообщение пользователю " + nameTo);
        System.out.println("Сообщение: " + message);
        System.out.println("connection = "+ namesConn.indexOf(nameTo));
        UserData userData = collectionUserData.get(namesConn.indexOf(nameThis));
        userData.addSendMessageToChatHistory(nameTo, message);
        collectionUserData.set(namesConn.indexOf(nameThis), userData);
        userData = collectionUserData.get(namesConn.indexOf(nameTo));
        userData.addReciviedMessageToChatHistory(nameThis, message);
        collectionUserData.set(namesConn.indexOf(nameTo), userData);
        saveUserData();
        saveServerData();

        connections.get(namesConn.indexOf(nameTo)).sendString("CodeGetEncryptedMessage");
        connections.get(namesConn.indexOf(nameTo)).sendString(nameThis);
        connections.get(namesConn.indexOf(nameTo)).sendString(nameTo);
        iv = securityChat.CipherAES(secretKeys.get(namesConn.indexOf(nameTo)));
        byte[] encryptMessage = securityChat.EncryptMessageAES(message);
        connections.get(namesConn.indexOf(nameTo)).sendByte(iv.getIV());
        connections.get(namesConn.indexOf(nameTo)).sendByte(encryptMessage);
    }
    @Override
    public void getEncryptsMessageForAllUser(NetworkLogic conn, String nameThis) {
        String group = conn.saveString();
        IvParameterSpec iv = new IvParameterSpec(conn.saveByte());
        securityChat.DecryptCipherAES(secretKeys.get(namesConn.indexOf(nameThis)), iv);
        byte[] encryptedBytes = conn.saveByte();
        String message = "Errorrr";
        message = securityChat.DecryptMessageAES(encryptedBytes);
        System.out.println("Message = " + message);
        serverData.addMessageToGroup(group, nameThis, message);
        //ArrayList<String> groupList = serverData.getListGroup(group);
        //ArrayList<String> groupList = groupsMap.get(group);
        for (String name : namesConn
             ) {
            //UserData user = collectionUserData.get(namesConn.indexOf(name));
            //user.addMessageToGroupHistory(nameThis, group, message);
            NetworkLogic con = connections.get(namesConn.indexOf(name));
            con.sendString("CodeGetEncryptedMessageForAllUSer");
            con.sendString(nameThis);
            con.sendString(group);
            iv = securityChat.CipherAES(secretKeys.get(namesConn.indexOf(name)));
            con.sendByte(iv.getIV());
            byte[] encryptMessage = securityChat.EncryptMessageAES(message);
            con.sendByte(encryptMessage);
        }
        saveUserData();
        saveServerData();
        /*
        for(int i = 0; i < connections.size(); i++){
            connections.get(i).sendString("CodeGetEncryptedMessageForAllUSer");
            connections.get(i).sendString(nameThis);
            iv = securityChat.CipherAES(secretKeys.get(i));
            connections.get(i).sendByte(iv.getIV());
            byte[] encryptMessage = securityChat.EncryptMessageAES(message);
            connections.get(i).sendByte(encryptMessage);
        }
        */

    }

    @Override
    public void addFriend(NetworkLogic conn, String friendName) {
        System.out.println("Пользователь " + namesConn.get(connections.indexOf(conn)) + " добавли в друзья " + friendName);
        String userLogin = namesConn.get(connections.indexOf(conn));
        //UserData userData = collectionUserData.get(namesConn.get(connections.indexOf(conn)));
        UserData userData = collectionUserData.get(userList.indexOf(userLogin));
        if(namesConn.contains(friendName)){
            userData.addFriend(friendName);
            conn.sendString("CodeAddFriendToUserData");
            conn.sendString(friendName);
            conn.sendString("yes");
            saveUserData();
            saveServerData();
        }else{
            conn.sendString("CodeAddFriendToUserData");
            conn.sendString(friendName);
            conn.sendString("no");
        }
    }

    public byte[] encryptMessage(String message, int numberNameTo) {
        return securityChat.EncryptMessageRSA(message, publicKeys.get(numberNameTo));
    }

    //Отправка сообщения всем клиентам
    private void sendToAllConnections(String value){
        System.out.println(value);
        final int cnt = connections.size();
        for(int i = 0; i < connections.size(); i++){
            connections.get(i).sendString("CodeReceiveString");
            connections.get(i).sendString(value);
        }
    }
    private void sendFileToAll(NetworkLogic conn, String FileName){
        for(int i = 0; i < connections.size(); i++){
            if(conn.equals(connections.get(i))){
                System.out.println("Соединение пропущено ");
                continue;
            }
            connections.get(i).sendString("CodeFileSendAllUser");
            //connections.get(i).sendString(FileName);
            connections.get(i).sendFile(FileName, FileName);
        }
    }

    private void sendNameList(){
        IvParameterSpec iv;
        for (NetworkLogic co: connections
                ) {
           /* for (String nameList: namesConn
                    ) {
                co.sendString("CodeGetAllName");
                co
                //co.sendString(nameList);
            }*/
            //co.sendString("CodeGetAllName");
            //co.sendString("StopGetAllNameUserSend");
            co.sendString("CodeGetAllNameAndInformation");
            System.out.println("Отправка обновленной базы пользователей");
            System.out.println(namesConn.toString());
            iv = securityChat.CipherAES(secretKeys.get(connections.indexOf(co)));
            co.sendByte(iv.getIV());
            //co.sendObject(namesConn);
            co.sendByte(securityChat.EncryptObjectAES(namesConn));
            co.sendByte(securityChat.EncryptObjectAES(userList));
            co.sendByte(securityChat.EncryptObjectAES(userFullName));
            co.sendByte(securityChat.EncryptObjectAES(userInfo));
        }
    }
    private void saveUserData(){
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream("temp.txt");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            System.out.println(collectionUserData.size());
            for(int i = 0; i < collectionUserData.size(); i++){
                oos.writeObject(userList.get(i));
                oos.writeObject(collectionUserData.get(i));
                oos.writeObject(userFullName.get(i));
                oos.writeObject(userInfo.get(i));
            }
            oos.flush();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void saveServerData(){
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream("tempServerData.txt");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(serverData);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
   /*
        try {
            FileInputStream fis = new FileInputStream("tempServerData.txt");
            ObjectInputStream oin = new ObjectInputStream(fis);
            serverData = (ServerData) oin.readObject();



        } catch (FileNotFoundException e) {
            System.out.println("Создание учетной записи");
            System.out.println("Введите логин");
            String login = sc.nextLine();
            System.out.println("Введите пароль");
            String password = sc.nextLine();

            serverData = new ServerData(login, password);
            e.printStackTrace();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }


        try {
            FileInputStream fis = new FileInputStream("temp.txt");
            ObjectInputStream oin = new ObjectInputStream(fis);

            while (true){
                try {
                    userList.add((String) oin.readObject());
                    collectionUserData.add((UserData) oin.readObject());
                }catch (EOFException e){
                    oin.close();
                    break;
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String user : userList){
            System.out.println(user);
        }
        for (UserData user : collectionUserData){
            System.out.println(user.toString());
        }
        //ArrayList<String> groupUser = new ArrayList<>();
        //groupUser.add("kiki");
        //groupUser.add("jojo");
        //groupUser.add("atla");

        //groupsMap.put("alluser", groupUser);


        while (true) {
            System.out.println("Желаете добавить новую группу ");
            if (sc.nextLine().equals("да")) {
                System.out.println("Введите названи ");
                String name = sc.nextLine();
                //groupsMap.put(name, new ArrayList<>());
                serverData.addGroup(name, new ArrayList<>());
            } else {
                break;
            }
        }
        while (true){
            System.out.println("Желаете добавить новых пользователей?");
            if(sc.nextLine().equals("да")){
                System.out.println("Введите имя пользователя");
                String login = sc.nextLine();
                System.out.println("Введите пароль пользователя");
                String password = sc.nextLine();
                UserData user = new UserData(login, password);
                userList.add(user.getLogin());
                collectionUserData.add(user);
                while (true){
                    System.out.println("Желаете добавить пользователя в группу?");
                    if(sc.nextLine().equals("да")){
                        Map<String, ArrayList<String>> groupsMap = serverData.getGroupsMap();
                        Set<String> keys = groupsMap.keySet();
                        System.out.println(keys.toString());
                        String group = sc.nextLine();
                        //ArrayList<String> list = groupsMap.get(group);
                        //list.add(login);
                        //groupsMap.put(group, list);
                        serverData.addUserToGroup(group, login);
                        user.addGroup(group);
                    }else {
                        break;
                    }
                }
                //collectionUserData.put(user.getLogin(), user);
            }else {
                break;
            }
        }
        while (true){
            System.out.println("Желаете добавить пользователей в группы");
            if(sc.nextLine().equals("да")){
                System.out.println("Введите название группы");
                String nameGroup = sc.nextLine();
                System.out.println("Введите нимя пользователя");
                String nameUser = sc.nextLine();
                serverData.addUserToGroup(nameGroup, nameUser);
            }else {
                break;
            }
        }

        try {
            FileOutputStream fos = new FileOutputStream("temp.txt");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            System.out.println(collectionUserData.size());
            for(int i = 0; i < collectionUserData.size(); i++){
                oos.writeObject(userList.get(i));
                oos.writeObject(collectionUserData.get(i));
            }
            oos.flush();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        securityChat = new SecurityChat();

        try (ServerSocket server = new ServerSocket(8189)) {
            while (true) {
                try {
                    new NetworkLogic(this, server.accept());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    } */
