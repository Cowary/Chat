package ru.airvary.client;

import ru.airvary.network.NetworkAction;
import ru.airvary.network.NetworkLogic;
import ru.airvary.others.SecurityChat;
import ru.airvary.others.UserData;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.*;

public class Client implements NetworkAction {

    //private static final String IP_ADDR = "localhost";
    public static String IP_ADDR;
    private static final int PORT = 8189;
    public String name;
    public String login;
    public String password;
    public String registrationPassword;
    private int id;

    private String BeginSt = "<html>";
    private String FinishSt = "<html>";

    SecurityChat security;
    UserData userData;

    KeyPairGenerator parigen;
    KeyPair keyPair;
    Key publicKey;
    Key privateKey;
    Cipher cipher;
    Cipher decriptCipher;
    Key serverPublicKey;
    SecretKey secretKey;

    public static void main(String[] args){
        new Client();
    }

    public NetworkLogic conn;
    Gui gui = new Gui(this);
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    private ArrayList<String> userNameList = new ArrayList<>();
    List<String> userList = new ArrayList<>();
    List<String> userFullname = new ArrayList<>();
    List<String> userInfo = new ArrayList<>();
    ArrayList<String> friendList = new ArrayList<>();
    //ArrayList<String> historyMess = new ArrayList<>();
    //ArrayList<String> groups = new ArrayList<>();
    //ArrayList<String> groupsHistory = new ArrayList<>();
    String historyMessage = "";

    Map<String, String> channel;

    private Client() {
            MainLogic();

        /*{
            try {
                try {
                    cipher = Cipher.getInstance("RSA");
                    decriptCipher = Cipher.getInstance("RSA");
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                }
                parigen = KeyPairGenerator.getInstance("RSA");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        keyPair = parigen.generateKeyPair();
        publicKey = keyPair.getPublic();
        privateKey = keyPair.getPrivate();
        System.out.println(publicKey.toString());
        System.out.println(privateKey.toString()); */
    }


    public void createNetwork(){
        try {
            conn = new NetworkLogic(this, IP_ADDR, PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void MainLogic() {
        gui.entry();
        //conn.sendString("CodeReceiveName");
        }

    /*private void MainLogic() throws IOException{
        conn.sendString("CodeReceiveName");
        //conn.sendInt(131241241);
        conn.sendString(this.name);
        while (true){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Выбирай ");
            System.out.println("1 - Вывести список всех подключенных пользователей ");
            System.out.println("2 - Отправка сообщения всем пользователям ");
            System.out.println("3 - Отправка сообщения определенному пользователю ");
            System.out.println("4 - Отправка файла всем пользователям ");
            System.out.println("5 - Отправка файла определенному пользователю ");
            System.out.println("6 - Выход из программы ");
            System.out.println("");
            System.out.println("Введите номер команды ");
            int b = Integer.parseInt(in.readLine());
            switch (b){
                case 1 : conn.sendString("CodeGetAllName");
                         conn.sendString(name);
                         break;
                case 2 : conn.sendString("CodeReceiveString");
                         System.out.println("Введите сообщение для отправки ");
                         String a = in.readLine();
                         conn.sendString(a);
                         break;
                case 3 : conn.sendString("CodeGetSendOneUser");
                         conn.sendString(this.name);
                         System.out.println("Введите имя пользователя которому хотите отравить сообщение: ");
                         conn.sendString(in.readLine());
                         System.out.println("Введите сообщение ");
                         conn.sendString(in.readLine());
                         break;
                case 4:  System.out.println("Введите название файла для отправки");
                         conn.sendString("CodeFileSendAllUser");
                         conn.sendFile(in.readLine());
                         System.out.println("Отправка файла начилась");
                         break;
                case 5:  System.out.println("Введите название файла для отправки");
                         conn.sendString("CodeFileSendOneUser");
                         String fileName = in.readLine();
                         System.out.println("Введите ник пользователя которому хотите отправить файл: ");
                         conn.sendString(in.readLine());
                         conn.sendString(name);
                         conn.sendFile(fileName);
                         break;
                case 6:  conn.disconnect();
                         break;
                default: System.out.println("Неправильный команда");
                         break;

            }
        }
    } */
    public void sendMessage(String st){
        System.out.println("Отпрака; " + st);
        conn.sendString(st);
    }
    public void sendFile(String st, String name){
        conn.sendFile(st, name);
    }
    //public void sendPublicKey(){conn.sendObject(security.getPublicKey());}
    public void sendPublicKey(){
        security = new SecurityChat();
        try {
            KeyFactory fact = KeyFactory.getInstance("RSA");
            RSAPublicKeySpec pub = fact.getKeySpec(security.genRSA(), RSAPublicKeySpec.class);
            BigInteger modulus = pub.getModulus();
            BigInteger exponent = pub.getPublicExponent();
            conn.sendObject(modulus);
            conn.sendObject(exponent);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }
    //Сообщение о том, что соединение готово
    @Override
    public void onConnectionReady(String id, NetworkLogic conn) {
        //this.id = Integer.parseInt(id);
        System.out.println("id = " + id);
        if(id.compareTo("CodeRegUserToServer1632") == 0){
            System.out.println("Прием начался");
            byte[] decodedKey = Base64.getDecoder().decode(security.DecrytMessageRSA(conn.saveByte()));
            secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
            System.out.println("Секретный ключ сервера = " + secretKey);
            IvParameterSpec iv = security.ivGen();
            security.startAESsecurity();
            conn.sendString("CodeRegistrationUserToServer");
            sendEncryptMessageAES(SecurityChat.md5(registrationPassword));
            sendEncryptMessageAES(login);
            sendEncryptMessageAES(password);
            System.out.println("Все отправлено");
            return;
        }
        //BigInteger modules = (BigInteger) obj_modules;
        //BigInteger exponent = (BigInteger) obj_exponent;
        /*
        Key k = null;
        try {
            KeyFactory fact = KeyFactory.getInstance("RSA");
            k = fact.generatePublic(new RSAPublicKeySpec(modules, exponent));
            System.out.println();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        serverPublicKey = k;
        */
        //security.CipherOn(serverPublicKey);
        //System.out.println("Server public key = " + k.toString());
        System.out.println("Connection ready...");
        System.out.println("Клиент: " + this.name + " с ID = " + this.id);
        byte[] decodedKey = Base64.getDecoder().decode(security.DecrytMessageRSA(conn.saveByte()));
        secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
        System.out.println("Секретный ключ сервера = " + secretKey);
        IvParameterSpec iv = security.ivGen();
        security.startAESsecurity();
        System.out.println("IV = " + iv);
        System.out.println(iv.getIV());
        System.out.println(iv.toString());

        conn.sendString("CodeOnFullConnReady");
        sendMessage(name);
        sendEncryptMessageAES(login);
        sendEncryptMessageAES(SecurityChat.md5(password));

        /*try {
            cipher.init(Cipher.ENCRYPT_MODE, serverPublicKey);
            System.out.println("Where error = " + privateKey.toString());
            decriptCipher.init(Cipher.DECRYPT_MODE, privateKey);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public void onFullConnReady(String answer, NetworkLogic conn) {
        System.out.println("onFullConnReady");
        if(answer.compareTo("YES") == 0){
            IvParameterSpec iv =  new IvParameterSpec(conn.saveByte());
            security.DecryptCipherAES(secretKey, iv);
            userData = (UserData) security.DecryptObjectAES(conn.saveByte());
            channel = (HashMap<String, String>) security.DecryptObjectAES(conn.saveByte());
            friendList =  userData.getFriends();
            //historyMess =  userData.getAllChatHistory();
            //groups = userData.getGroups();
            //groupsHistory = userData.getGroupsHistory();

            try {
                setSwingFriendList(friendList);
                setSwingChannelList();
            }catch (NullPointerException ex){
                System.out.println("Список друзей пуст");
            }

            System.out.println("userData получено ");
            System.out.println("login = " + userData.getLogin());
            System.out.println("password = " + userData.getPassword());
            gui.mainWin(name);
        }else if(answer.compareTo("NO") == 0){
            gui.createDialog("Ошибка", true);
        }

    }
    @Override
    public void registrationUser(NetworkLogic conn) {
        String asnswer = conn.saveString();
        if(asnswer.compareTo("YES") == 0){
            gui.createDialog("Регистрация завершена", true);
        }else{
            gui.createDialog("Ошибка регистрации", true);
        }
    }


    //Получение списка всех пользователей
    @Override
    public void getAllNameUserAndInformation(NetworkLogic conn) {
        //ArrayList<String> arra = (ArrayList<String>) conn.saveObject();
        //conn.sendString(login);
        IvParameterSpec iv = new IvParameterSpec(conn.saveByte());
        security.DecryptCipherAES(secretKey, iv);
        userNameList = (ArrayList<String>) security.DecryptObjectAES(conn.saveByte());
        System.out.println(userNameList.toString());
        userList = (ArrayList<String>) security.DecryptObjectAES(conn.saveByte());
        System.out.println(userList.toString());
        userFullname = (ArrayList<String>) security.DecryptObjectAES(conn.saveByte());
        System.out.println(userFullname.toString());
        userInfo = (ArrayList<String>) security.DecryptObjectAES(conn.saveByte());
        System.out.println(userInfo.toString());
        //System.out.println(userNameList.toString());
        //gui.dataUser = userNameList.toArray(new String[userNameList.size()]);
        System.out.println(userNameList.toString());
        gui.dataUser = userNameList.toArray(new String[userNameList.size()]);

            /*
            userNameList.add(0, "All");
            String[] array = userNameList.toArray(new String[userNameList.size()]);
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    //Обновлеие списка
                    gui.list.setListData(array);
                }
            });
            userNameList.clear();
            nameThis = "";
            */

    }
    //Вывод от кого пришло сообщение и вывод сообщение
    @Override
    public void getSendOneUser(String nameThis, String nameTo, String value, NetworkLogic conn) {
        System.out.println(value);
        historyMessage += value;
        historyMessage += "<br>";
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                gui.mainChat.setText(BeginSt + historyMessage + FinishSt);
            }
        });
    }
    //Получение сообщения и  вывод его на консоль
    @Override
    public void onReceiveString(NetworkLogic conn, String value) {

        System.out.println(value);
    }

    @Override
    public void sendFileAllUser(NetworkLogic conn, String nameFile) {
        System.out.println("Вам прислали файл " + nameFile);
        conn.saveFile(nameFile);
    }

    @Override
    public void sendFileToOneUser(NetworkLogic conn, String toUser , String thisUser, String nameFile) {
        System.out.println("Пользователь " + thisUser + " прислал вам файл");
        conn.saveFile(nameFile);
    }

    //Вывод сообщения о закрытия подключения
    @Override
    public void onDisconnect(NetworkLogic conn) {
        System.out.println("ConnectionClose");
    }
    //Вывод сообщения о ошибке
    @Override
    public void onException(NetworkLogic conn, Exception e) {
        System.out.println("Connection exception: " + e);
    }

    @Override
    public void getEncryptsMessage(String nameThis, String nameTo, NetworkLogic conn) {
        IvParameterSpec iv = new IvParameterSpec(conn.saveByte());
        byte[] encryptedBytes = conn.saveByte();
        String message = "Error";
        security.DecryptCipherAES(secretKey, iv);
        message = security.DecryptMessageAES(encryptedBytes);
        //message = security.DecrytMessage(encryptedBytes);
        /*byte[] decriptedBytes = decriptCipher.doFinal(encryptedBytes);
        message = new String(decriptedBytes, "UTF-8");*/
        //String mess = nameThis + ": " + message;
        //String ag = historyMess.get(friendList.indexOf(nameThis));
        //ag += mess;
        //ag += "<br>";
        //historyMess.set(friendList.indexOf(nameThis), ag);
        userData.addReciviedMessageToChatHistory(nameThis, message);
        setChat(userData.getChatHistory(nameThis));
        /*
        String[] array = friendList.toArray(new String[friendList.size()]);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                //Обновлеие списка
                gui.list.setListData(array);
            }
        });
        */
        //historyMessage += mess;
        //historyMessage += "<br>";
        /*
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                gui.mainChat.setText(BeginSt + historyMessage + FinishSt);
                }
        });
        */
        System.out.println("Message = " + message);
    }

    @Override
    public void getEncryptsMessageForAllUser(NetworkLogic conn, String nameThis) {
        String group = conn.saveString();
        IvParameterSpec iv = new IvParameterSpec(conn.saveByte());
        byte[] encryptedBytes = conn.saveByte();
        String message = "Error";
        security.DecryptCipherAES(secretKey, iv);
        message = security.DecryptMessageAES(encryptedBytes);
        System.out.println(nameThis + ": " + message);
        //message = security.DecrytMessage(encryptedBytes);
        //byte[] decriptedBytes = decriptCipher.doFinal(encryptedBytes);
        //message = new String(decriptedBytes, "UTF-8");
        //userData.addMessageToGroupHistory(nameThis, group, message);
        String mess = nameThis + ": " + ": " + message;
        String ag = channel.get(group);
        ag += mess;
        ag += "<br>";
        channel.put(group, ag);

        setChat(ag);
        System.out.println("Message = " + message);
    }


    @Override
    public void addFriend(NetworkLogic conn, String friendName) {
        String s = conn.saveString();
        System.out.println("дОБАВЛЕНИЕ дурга = " + s);
        if(s.equals("yes")){
            System.out.println("туть");
            friendList.add(friendName);
            //historyMess.add("");
            String[] array = friendList.toArray(new String[friendList.size()]);
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    //Обновлеие списка
                    gui.dataFavorites = array;
                    gui.list.setListData(array);
                }
            });
        }
    }

    public void saveUserData(){
        conn.sendString("CodeGetAllNameAndInformation");
        conn.sendString(login);
        IvParameterSpec iv = security.CipherAES(secretKey);
        conn.sendByte(iv.getIV());
        conn.sendByte(security.EncryptObjectAES(userData));
    }


    /*
    public void sendEncryptMessage(String message){
        byte[] bytes = security.EncryptMessage(message, serverPublicKey);
        conn.sendByte(bytes);

    }
    */

    public void sendEncryptMessageAES(String message){
        IvParameterSpec iv = security.CipherAES(secretKey);
        conn.sendByte(iv.getIV());
        conn.sendByte(security.EncryptMessageAES(message));
    }

    public void setSwingFriendList(ArrayList<String> List){
        String[] array = List.toArray(new String[List.size()]);
        gui.dataFavorites = array;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                //Обновлеие списка
                gui.list.setListData(array);
            }
        });
    }
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
    public void setSwingChannelList(){
        ArrayList<String> channelList = new  ArrayList<String>(channel.keySet());
        String[] array = channelList.toArray(new String[channelList.size()]);
        gui.dataGroup = array;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                //Обновлеие списка
                gui.list.setListData(array);
            }
        });
    }



}