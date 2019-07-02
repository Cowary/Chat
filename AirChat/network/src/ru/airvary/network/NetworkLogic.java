package ru.airvary.network;

import java.io.*;
import java.net.Socket;

public class NetworkLogic {

    private final Socket socket;
    private final Thread rxThread;
    private final NetworkAction netAct;
   // private final DataInputStream inData;
    private final DataInputStream dataInputStream;
    private final DataOutputStream dataOutputStream;

    private FileInputStream inStreamFile;
    private FileOutputStream outStreamFile;
    //private final DataOutputStream outData;
    private final ObjectInputStream inData;
    private final ObjectOutputStream outData;
    public byte[] buffer = new byte[4096];
    public String codeIn;
    public String codeDo;
    //p
    //ublic String FileToSend = "Futaba.jpg";

    //От клиента
    public NetworkLogic(NetworkAction netAct, String ipAddr, int port) throws IOException{
        this(netAct, new Socket(ipAddr, port));
    }

    public NetworkLogic(NetworkAction netAct, Socket socket) throws IOException{
        this.netAct = netAct;
        this.socket = socket;
        OutputStream sout = socket.getOutputStream();
        outData = new ObjectOutputStream(sout);
        InputStream sin = socket.getInputStream();
        //inData = new DataInputStream(sin);
        //outData = new DataOutputStream(sout);
        inData = new ObjectInputStream(sin);
        dataInputStream = new DataInputStream(sin);
        dataOutputStream = new DataOutputStream(sout);

        //Запуск потока
        rxThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //Cообщение о подключение клиента
                    netAct.onConnectionReady(inData.readUTF().trim(), NetworkLogic.this);
                    //Получение строки и передача функции onReceiveString
                    while(!rxThread.isInterrupted()){
                        System.out.println("готов к получению данных ");
                        //Получение управляющего сообщения
                        codeIn = inData.readUTF().trim();
                        System.out.println("codeIn =" + codeIn);
                        if(codeIn == null){
                            System.out.println("НУль ");
                            disconnect();
                        } else {
                            switch (codeIn) {
                                //Получение имени клиента и запись его в коллекцию
                                case "CodeOnFullConnReady":
                                    netAct.onFullConnReady(inData.readUTF().trim(), NetworkLogic.this);
                                    break;
                                //Получение системного сообщений
                                case "CodeReceiveString":
                                    netAct.onReceiveString(NetworkLogic.this, inData.readUTF().trim());
                                    break;
                                 //Обновление базы пользователей и сохранение информации на сервере
                                case "CodeGetAllNameAndInformation":
                                    netAct.getAllNameUserAndInformation(NetworkLogic.this);
                                    break;
                                //Получение сообщений от другого пользователя
                                case "CodeGetSendOneUser":
                                    netAct.getSendOneUser(inData.readUTF().trim(), inData.readUTF().trim(), inData.readUTF().trim(), NetworkLogic.this);
                                    break;
                                /*case "CodeFileSendAllUser":
                                    netAct.sendFileAllUser(NetworkLogic.this, inData.readUTF().trim());
                                    break;
                                */
                                //Получение файла
                                case "CodeFileSendOneUser":
                                    netAct.sendFileToOneUser(NetworkLogic.this, inData.readUTF().trim(), inData.readUTF().trim(), inData.readUTF().trim());
                                    break;
                                //Получение зашифрованного сообщения  от другого пользователя
                                case "CodeGetEncryptedMessage":
                                    netAct.getEncryptsMessage(inData.readUTF().trim(), inData.readUTF().trim(), NetworkLogic.this);
                                    break;
                                //Получение сообщения в канале
                                case "CodeGetEncryptedMessageForAllUSer":
                                    netAct.getEncryptsMessageForAllUser(NetworkLogic.this, inData.readUTF().trim());
                                    break;
                                //Добавление пользователя
                                case "CodeAddFriendToUserData":
                                    netAct.addFriend(NetworkLogic.this, inData.readUTF().trim());
                                    break;
                                //Регистрация пользователя
                                case "CodeRegistrationUserToServer":
                                    netAct.registrationUser(NetworkLogic.this);
                                    break;
                                default:
                                    break;
                            }
                            }
                    }

                } catch (IOException e){
                    netAct.onException(NetworkLogic.this , e);
                    //Закрытие всего и вся обязательно
                }  finally {
                    netAct.onDisconnect(NetworkLogic.this);
                }
            }
        });
        //Запуск потока
        rxThread.start();
    }
    //Отправка сообщения
    public synchronized void sendString(String value) {
        try {
            outData.writeUTF(value + "\r\n");
            outData.flush();
            System.out.println("value отправлено = " + value);
        } catch (IOException e) {
            netAct.onException(NetworkLogic.this, e);
            disconnect();
            e.printStackTrace();
        }

    }
    public synchronized void sendLong(long value){
        try{
            outData.writeLong(value);
            outData.flush();
        } catch (IOException e){
            netAct.onException(NetworkLogic.this, e);
            disconnect();
            e.printStackTrace();
        }
    }
    public synchronized void sendObject(Object obj){
        try {
            outData.writeObject(obj);
            outData.flush();
            System.out.println("Object send");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public synchronized Object saveObject(){
        try {
            return inData.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    //Отправка файла
    public synchronized void sendFile(String FileToSend, String name) {
        try {
            sendString(name);
            inStreamFile = new FileInputStream(FileToSend);
            File f = new File(FileToSend);
            System.out.println("Путь = " + f.getAbsolutePath());
            long len = f.length();
            System.out.println("Len = " + len);
            sendLong(len);
            try {
                while (inStreamFile.read(buffer) > 0) {
                    //outData.write(buffer);
                    //outData.flush();
                    dataOutputStream.write(buffer);
                    dataOutputStream.flush();
                }
            } catch (IOException e) {
                netAct.onException(NetworkLogic.this, e);
                disconnect();
            }
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            try {
                inStreamFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public synchronized void sendByte(byte[] message){
        long len = message.length;
        sendLong(len);
        System.out.println("Len = " + len);
        try {
            outData.write(message);
            outData.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public synchronized byte[] saveByte(){
        try {
            long len = inData.readLong();
            byte[] message = new byte[(int) len];
            inData.readFully(message);
            return message;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }
    public synchronized String saveString(){
        try {
            return inData.readUTF().trim();
        } catch (IOException e) {
            netAct.onException(NetworkLogic.this, e);
            disconnect();
            e.printStackTrace();
        }
        return null;
    }
    //Сохранения файла
    public synchronized void saveFile(String name) {
        long filesize = 0;
        int read = 0;
        long totalRead = 0;
        long remaining;
        System.out.println("Код был saveFile");
        try {
            outStreamFile = new FileOutputStream(name);

            try {

                System.out.println("Name = " + name);
                filesize = inData.readLong();
                //filesize = dataInputStream.readLong();
                remaining = filesize;
                System.out.println("Filesize = " + filesize);
                /*while ((read = inData.read(buffer, 0, (int)Math.min(buffer.length, remaining))) > 0) {
                    totalRead += read;
                    remaining -= read;
                    System.out.println("read " + totalRead + " bytes.");
                    outStreamFile.write(buffer, 0, read);
                    outStreamFile.flush();

                }
                read = inData.read(buffer);
                System.out.println("остатки read  " + read + " bytes.");
                */
                while ((read = dataInputStream.read(buffer, 0, (int)Math.min(buffer.length, remaining))) > 0) {
                    totalRead += read;
                    remaining -= read;
                    System.out.println("read " + totalRead + " bytes.");
                    outStreamFile.write(buffer, 0, read);
                    outStreamFile.flush();
                }
                read = dataInputStream.read(buffer);
                System.out.println("остатки read  " + read + " bytes.");
            } catch (IOException e) {
                netAct.onException(NetworkLogic.this, e);
                disconnect();
                e.printStackTrace();
        }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                outStreamFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //Закрытие сокета
    public synchronized void disconnect() {
        rxThread.interrupt();
        try {
            socket.close();
        } catch (IOException e) {
            netAct.onException(NetworkLogic.this, e);
        }
    }
    //Вывод адреса и порта соета с помощью toString
    @Override
    public String toString() {
        return "NetworkLogic: " + socket.getInetAddress() + ": " + socket.getPort();
    }

}

