package ru.airvary.client;

import ru.airvary.network.NetworkLogic;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;

public class Gui extends JFrame {
    //Окно входа
    private JFrame frame_1 = new JFrame();
    private JPanel panel_1 = new JPanel();
    private JTextField input_login = new JTextField(12);
    private JTextField input_password = new JTextField(12);
    private JButton button_1 = new JButton("Start");
    //Основное окно
    private JFrame frame_2 = new JFrame();
    private JPanel leftPanel = new JPanel();
    private JPanel northLeftPanel = new JPanel();
    private JPanel flowNorthLeftPanel = new JPanel();
    private JLabel userName = new JLabel("Имя пользователя");
    private JTextField fieldFriendName = new JTextField(12);
    private JButton addFriend = new JButton("Добавить");
    public String[] dataUser = {"All"};
    public String[] dataGroup = {"all"};
    //private JLabel nameList = new JLabel("Пользователи в сети");
    public JList<String> list = new JList<String>(dataUser);
    public JList<String> listGroup = new JList<>(dataGroup);
    private JPanel southLeftPanel = new JPanel();
    private JButton usersButton = new JButton("Пользователи");
    private JButton groupButton = new JButton("Группы");

    private JPanel centerPanel = new JPanel();
    private JLabel addressName = new JLabel("Адресант");
    private String BeginSt = "<html>";
    private String FinishSt = "<html>";
    private String MiddleSt = "";
    public JLabel mainChat = new JLabel(BeginSt + MiddleSt + FinishSt);
    private JPanel southCenterPanel = new JPanel();
    private JTextField messageInput = new JTextField(20);
    private JPanel buttonPanel = new JPanel();
    private JButton sendButton = new JButton("Отправить");
    private JButton shareButton = new JButton("Поделиться");

    //private JPanel smallEnterPanel = new JPanel();
    //private JPanel buttonSmallEnterPanel = new JPanel();
    //private JLabel addresName = new JLabel("  User");
    //private String BeginSt = "<html>";
    //private String FinishSt = "<html>";
    //private String MiddleSt = "";
    //public JLabel mainChat = new JLabel(BeginSt + MiddleSt + FinishSt);
    //private JTextField messageInput = new JTextField(20);
    //private JLabel namesList = new JLabel("Пользователи в сети");
    //private JButton shareButton = new JButton("Share");
    //private JButton sendButton = new JButton("Send");
    private String loginUser;
    private int sendButtonMode = 0;
    private File file;
    String hintString = "Введите сообщение";
    Color color = messageInput.getCaretColor();


    Client client;

    public Gui(Client client){
        this.client = client;
    }

    public void entry(){
        //Окно входа
        frame_1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame_1.getContentPane().add(BorderLayout.CENTER, panel_1);
        panel_1.setLayout(new FlowLayout());
        panel_1.add(input_login);
        panel_1.add(input_password);
        panel_1.add(button_1);

        frame_1.setSize(200,130);
        frame_1.setVisible(true);
        frame_1.setLocationRelativeTo(null);
        button_1.addActionListener(new ButtonLoginListener());
    }
    public void mainWin(String login){

        System.out.println("login = " + login);
        frame_2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame_2.setSize(800,600);
        frame_2.setLocationRelativeTo(null);
        frame_1.setVisible(false);
        frame_2.setVisible(true);

        frame_2.add(BorderLayout.WEST, leftPanel);
        frame_2.add(BorderLayout.CENTER, centerPanel);

        leftPanel.setLayout(new BorderLayout());
        leftPanel.add(northLeftPanel, BorderLayout.NORTH);
        northLeftPanel.setLayout(new BorderLayout());
        northLeftPanel.add(BorderLayout.NORTH, userName);
        northLeftPanel.add(BorderLayout.CENTER, flowNorthLeftPanel);
        flowNorthLeftPanel.setLayout(new FlowLayout());
        flowNorthLeftPanel.add(fieldFriendName);
        flowNorthLeftPanel.add(addFriend);
        leftPanel.add(list, BorderLayout.CENTER);
        //leftPanel.add(listGroup, BorderLayout.CENTER);
        //listGroup.setVisible(false);

        leftPanel.add(southLeftPanel, BorderLayout.SOUTH);
        southLeftPanel.setLayout(new FlowLayout());
        southLeftPanel.add(usersButton);
        southLeftPanel.add(groupButton);

        messageInput.setForeground(Color.LIGHT_GRAY);
        messageInput.setText(hintString);
        messageInput.addFocusListener(new MessageInputFocusListener());

        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.addListSelectionListener(new ListListener());

        client.sendMessage("CodeGetAllName");
        client.sendMessage(login);
        loginUser = login;

        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(addressName, BorderLayout.NORTH);
        centerPanel.add(mainChat, BorderLayout.CENTER);
        centerPanel.add(southCenterPanel, BorderLayout.SOUTH);

        southCenterPanel.setLayout(new BorderLayout());
        southCenterPanel.add(messageInput, BorderLayout.CENTER);
        southCenterPanel.add(buttonPanel, BorderLayout.EAST);
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(sendButton);
        buttonPanel.add(shareButton);

        mainChat.setHorizontalAlignment(JLabel.LEFT);
        mainChat.setVerticalAlignment(JLabel.TOP);

        //bigChatPanel.add(smallEnterPanel, BorderLayout.SOUTH);

        sendButton.addActionListener(new ButtonSendListener());
        shareButton.addActionListener(new ButtonFileChooseListener());
        addFriend.addActionListener(new ButtonAddFriendListener());
        usersButton.addActionListener(new ButtonFriendListener());
        groupButton.addActionListener(new ButtonGroupListener());
    }


    class ButtonLoginListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(input_login.getText().trim().length() > 0){
                client.createNetwork();
                client.name = input_login.getText().trim();
                client.login = input_login.getText().trim();
                client.password = input_password.getText().trim();
                client.sendMessage(input_login.getText().trim());
                client.sendPublicKey();
                //mainWin(input_1.getText().trim());
            }
        }
    }
    class ButtonFileChooseListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileopen = new JFileChooser();
            int ret = fileopen.showDialog(null, "Выбрать файл");
            if (ret == JFileChooser.APPROVE_OPTION) {
                file = fileopen.getSelectedFile();
                messageInput.setText(file.getName() + " готов к отправке");
                sendButtonMode = 1;
            }
        }
    }

    class ListListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            String el = list.getSelectedValue().trim();
            addressName.setText(el);
            if(sendButtonMode == 2){
                //mainChat.setText(BeginSt + client.groupsHistory.get(client.groups.indexOf(el)) + FinishSt);
                mainChat.setText(BeginSt + client.channel.get(el) + FinishSt);
            }else{
                mainChat.setText(BeginSt + client.historyMess.get(client.friendList.indexOf(el)) + FinishSt);
            }

            System.out.println("element = " + el);
        }
    }
    class ButtonSendListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String st = addressName.getText().trim();
            if(sendButtonMode == 0) {
                if (st.compareTo("All") == 0) {
                    client.historyMessage += (loginUser + " => " + "All" + ": " + messageInput.getText().trim());
                    client.historyMessage += "<br>";
                    mainChat.setText(BeginSt + client.historyMessage + FinishSt);

                    client.sendMessage("CodeGetEncryptedMessageForAllUSer");
                    client.sendMessage(loginUser);
                    //client.sendMessage(messageInput.getText().trim());
                    //client.sendEncryptMessage(messageInput.getText().trim());
                    client.sendEncryptMessageAES(messageInput.getText().trim());
                } else {
                    String mess = loginUser + " => " + st + ": " + messageInput.getText().trim();

                    String ag = client.historyMess.get(client.friendList.indexOf(st));
                    ag += mess;
                    ag += "<br>";
                    client.historyMess.set(client.friendList.indexOf(st), ag);
                    mainChat.setText(BeginSt + client.historyMess.get(client.friendList.indexOf(st)) + FinishSt);
                    //mainChat.setText(BeginSt + client.historyMessage + FinishSt);

                    //client.historyMessage += (loginUser + " => " + st + ": " + messageInput.getText().trim());
                    //client.historyMessage += "<br>";
                    //mainChat.setText(BeginSt + client.historyMessage + FinishSt);

                    client.sendMessage("CodeGetEncryptedMessage");
                    client.sendMessage(loginUser);
                    client.sendMessage(st);
                    client.sendEncryptMessageAES(messageInput.getText().trim());
                    //client.sendEncryptMessage(messageInput.getText().trim());
                    //client.sendMessage(messageInput.getText().trim());
                }
            }else if(sendButtonMode == 1){
                if(st.compareTo("All") == 0) {
                    client.sendMessage("CodeFileSendAllUser");
                    client.sendFile(file.getAbsolutePath(), file.getName());
                } else {
                    client.sendMessage("CodeFileSendOneUser");
                    client.sendMessage(st);
                    client.sendMessage(loginUser);
                    client.sendFile(file.getAbsolutePath(), file.getName());
                    sendButtonMode = 0;
                }
            }else if(sendButtonMode == 2){
                client.sendMessage("CodeGetEncryptedMessageForAllUSer");
                client.sendMessage(loginUser);
                client.sendMessage(st);
                client.sendEncryptMessageAES(messageInput.getText().trim());
            }

        }
    }
    class ButtonAddFriendListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            client.sendMessage("CodeAddFriendToUserData");
            client.sendMessage(fieldFriendName.getText().trim());
        }
    }
    class MessageInputFocusListener implements FocusListener {
        @Override
        public void focusGained(FocusEvent e) {
            messageInput.setForeground(color);
            if (messageInput.getText().equals(hintString)) {
                messageInput.setText("");
            }
        }

        @Override
        public void focusLost(FocusEvent e) {
            if (messageInput.getText().isEmpty()) {
                messageInput.setForeground(Color.LIGHT_GRAY);
                messageInput.setText(hintString);
            }
        }
    }
    public void createDialog(String title, boolean modal){
        System.out.println("DIALOG");
        JOptionPane.showMessageDialog(this,
                new String[] {"Данное имя уже занято",},
                title,
                JOptionPane.ERROR_MESSAGE);
    }
    class ButtonGroupListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            sendButtonMode = 2;
            //list.setVisible(false);
            //listGroup.setVisible(true);
            //leftPanel.add(listGroup, BorderLayout.CENTER);
            list.setListData(dataGroup);
        }
    }
    class ButtonFriendListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            sendButtonMode = 0;
            //listGroup.setVisible(false);
            //list.setVisible(true);
            //leftPanel.add(list, BorderLayout.CENTER);
            list.setListData(dataUser);
        }
    }
}