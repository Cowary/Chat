package ru.airvary.client;

import ru.airvary.network.NetworkLogic;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class Gui extends JFrame {
    //Окно входа
    private JFrame frame_1 = new JFrame();
    private JPanel panel_1 = new JPanel();
    //private JPanel panel_button = new JPanel();
    private JTextField input_IP = new JTextField(12);
    private JTextField input_login = new JTextField(12);
    private JTextField input_password = new JTextField(12);
    private JButton button_regUser = new JButton("Регистрация");
    private JButton button_1 = new JButton("Вход");
    //Окно регистрации
    private JFrame frame_reg = new JFrame();
    private JPanel panel_reg = new JPanel();
    //private JPanel panel_button = new JPanel();
    private JTextField reg_input_IP = new JTextField(12);
    private JTextField reg_input_login = new JTextField(12);
    private JTextField reg_input_password = new JTextField(12);
    private JTextField reg_input_regPassword = new JTextField(12);
    private JButton button_finalRegUser = new JButton("Регистрация");

    //Основное окно
    private JFrame frame_2 = new JFrame();
    private JPanel leftPanel = new JPanel();
    private JPanel northLeftPanel = new JPanel();
    private JPanel flowNorthLeftPanel = new JPanel();
    private JLabel userName = new JLabel("Имя пользователя");
    private JTextField fieldFriendName = new JTextField(12);
    private JButton settingButton = new JButton("Профиль");
    private JButton addFriend = new JButton("В избранные");

    public String[] dataUser = {"All"};
    public String[] dataFavorites = {"all"};
    public String[] dataGroup = {"all"};
    //private JLabel nameList = new JLabel("Пользователи в сети");
    public JList<String> list = new JList<String>(dataUser);
    public JScrollPane listScrollPane = new JScrollPane(list);
    public JList<String> listGroup = new JList<>(dataGroup);
    private JPanel southLeftPanel = new JPanel();
    private JButton usersButton = new JButton("В сети");
    private JButton favoritesButton = new JButton("Избранные");
    private JButton groupButton = new JButton("Каналы");

    private JPanel centerPanel = new JPanel();
    private JLabel addressName = new JLabel("Адресант");
    private String BeginSt = "<html>";
    private String FinishSt = "<html>";
    private String MiddleSt = "";
    public JLabel mainChat = new JLabel(BeginSt + MiddleSt + FinishSt);
    public JScrollPane mainChatScrollPane = new JScrollPane(mainChat);
    private JPanel southCenterPanel = new JPanel();
    private JTextField messageInput = new JTextField(20);
    private JPanel buttonPanel = new JPanel();
    private JButton sendButton = new JButton("Отправить");
    private JButton shareButton = new JButton("Поделиться");

    //Окно профиля
    private JFrame frame_profile = new JFrame();
    private JPanel panelProfile_mane = new JPanel();
    private JPanel centerPanelProfile = new JPanel();
    private JPanel southPanelProfile = new JPanel();
    private JLabel loginLabelProfile = new JLabel("Логин:");
    private JTextField loginTextFieldProfile = new JTextField(12);
    private JLabel passwordLabelProfile = new JLabel("Пароль:");
    private JTextField passwordTextFieldProfile = new JTextField(12);
    private JLabel fullnameLabelProfile = new JLabel("Полное имя:");
    public JTextField fullnameTextFieldProfile = new JTextField(12);
    private JLabel infoLabelProfile = new JLabel("Дополнительная информация:");
    //private JTextField infoTextFieldProfile = new JTextField(30);
    private JTextArea infoTextAreaProfile = new JTextArea(8, 20);
    public JScrollPane infoScrollPane = new JScrollPane(infoTextAreaProfile);
    private JButton saveProfile = new JButton("Сохранить");
    //Маленькое окно профиля
    private JFrame miniProfileFrame = new JFrame();



    private JPanel cenetralPanel_set = new JPanel();

    JPopupMenu popup = new JPopupMenu();
    JMenuItem item = new JMenuItem("Профиль");
    //item.addActionListener(listener);
    //popup.add(item);

    //private JButton addUserButton = new JButton("Добавать пользователя");
    private JButton changeLoginUserButton = new JButton("Изменить логин");
    private JButton changePasswordUserButton = new JButton("Изменить пароль");


    private String loginUser;
    private int sendButtonMode = 0;
    private File file;
    String hintString = "Введите сообщение";
    String hintIpString = "Введите IP-адрес";
    String hintLoginString = "Введите логин";
    String hintPasswordString = "Введите пароль";
    String hintRegPasswordString = "Пароль-регистрации";
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
        panel_1.add(input_IP);
        panel_1.add(input_login);
        panel_1.add(input_password);
        panel_1.add(button_regUser);
        panel_1.add(button_1);


        frame_1.setSize(200,150);
        frame_1.setVisible(true);
        frame_1.setLocationRelativeTo(null);
        button_1.addActionListener(new ButtonLoginListener());
        button_regUser.addActionListener(new ButtonRegUserListener());

        input_IP.setForeground(Color.LIGHT_GRAY);
        input_IP.setText(hintIpString);
        input_IP.addFocusListener(new IpInputFocusListener());
        input_login.setForeground(Color.LIGHT_GRAY);
        input_login.setText(hintLoginString);
        input_login.addFocusListener(new LoginInputFocusListener());
        input_password.setForeground(Color.LIGHT_GRAY);
        input_password.setText(hintPasswordString);
        input_password.addFocusListener(new PasswordInputFocusListener());
    }
    public void registration(){
        frame_reg.getContentPane().add(BorderLayout.CENTER, panel_reg);
        frame_reg.setSize(200, 180);
        frame_reg.setVisible(true);
        frame_reg.setLocationRelativeTo(null);
        panel_reg.setLayout(new FlowLayout());
        panel_reg.add(reg_input_IP);
        panel_reg.add(reg_input_login);
        panel_reg.add(reg_input_password);
        panel_reg.add(reg_input_regPassword);
        panel_reg.add(button_finalRegUser);

        reg_input_IP.setForeground(Color.LIGHT_GRAY);
        reg_input_IP.setText(hintIpString);
        reg_input_IP.addFocusListener(new Reg_IpInputFocusListener());
        reg_input_login.setForeground(Color.LIGHT_GRAY);
        reg_input_login.setText(hintLoginString);
        reg_input_login.addFocusListener(new Reg_LoginInputFocusListener());
        reg_input_password.setForeground(Color.LIGHT_GRAY);
        reg_input_password.setText(hintPasswordString);
        reg_input_password.addFocusListener(new Reg_PasswordInputFocusListener());
        reg_input_regPassword.setForeground(Color.LIGHT_GRAY);
        reg_input_regPassword.setText(hintRegPasswordString);
        reg_input_regPassword.addFocusListener(new Reg_RegPasswordInputFocusListener());


        button_finalRegUser.addActionListener(new ButtonFinalRegUserListener());
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
        userName.setText("Логин: " + login);
        northLeftPanel.add(BorderLayout.CENTER, flowNorthLeftPanel);
        flowNorthLeftPanel.setLayout(new FlowLayout());
        flowNorthLeftPanel.add(settingButton);
        flowNorthLeftPanel.add(addFriend);
        //leftPanel.add(list, BorderLayout.CENTER);
        leftPanel.add(listScrollPane, BorderLayout.CENTER);
        //leftPanel.add(listGroup, BorderLayout.CENTER);
        //listGroup.setVisible(false);

        leftPanel.add(southLeftPanel, BorderLayout.SOUTH);
        southLeftPanel.setLayout(new FlowLayout());
        southLeftPanel.add(usersButton);
        southLeftPanel.add(favoritesButton);
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

        //centerPanel.add(mainChat, BorderLayout.CENTER);
        centerPanel.add(mainChatScrollPane, BorderLayout.CENTER);
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
        favoritesButton.addActionListener(new ButtonFriendListener());
        groupButton.addActionListener(new ButtonGroupListener());
        usersButton.addActionListener(new ButtonUserListener());
        settingButton.addActionListener(new ButtonProfileListener());

        list.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent e)  {check(e);}
            public void mouseReleased(MouseEvent e) {check(e);}



            public void check(MouseEvent e) {
                if (e.isPopupTrigger()) { //if the event shows the menu
                    list.setSelectedIndex(list.locationToIndex(e.getPoint())); //select the item
                    popup.show(list, e.getX(), e.getY()); //and show the menu
                }
            }

        });
        popup.add(item);
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String el = list.getSelectedValue().trim();
                miniProfileWin(el);
            }
        });
    }
    public void profileWin(){
        //frame_profile.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame_profile.setSize(500,300);
        frame_profile.setLocationRelativeTo(null);
        frame_profile.setVisible(true);

        frame_profile.add(BorderLayout.NORTH, panelProfile_mane);
        panelProfile_mane.setLayout(new GridLayout(3, 2));
        //panelProfile_mane.setLayout(new FlowLayout());
        frame_profile.add(BorderLayout.CENTER, centerPanelProfile);
        frame_profile.add(BorderLayout.SOUTH, saveProfile);
        centerPanelProfile.setLayout(new GridLayout(1, 2));
        infoTextAreaProfile.setLineWrap(true);
        infoTextAreaProfile.setWrapStyleWord(true);

        panelProfile_mane.add(loginLabelProfile);
        panelProfile_mane.add(loginTextFieldProfile);
        panelProfile_mane.add(passwordLabelProfile);
        panelProfile_mane.add(passwordTextFieldProfile);
        panelProfile_mane.add(fullnameLabelProfile);
        panelProfile_mane.add(fullnameTextFieldProfile);
        //panelProfile_mane.add(infoLabelProfile);
        //panelProfile_mane.add(infoTextFieldProfile);
        //panelProfile_mane.add(infoTextAreaProfile)

        centerPanelProfile.add(infoLabelProfile);
        //centerPanelProfile.add(infoTextAreaProfile);
        centerPanelProfile.add(infoScrollPane);
        //centerPanelProfile.add(infoTextFieldProfile);

        loginTextFieldProfile.setText(loginUser);
        if(client.userData.getFullName() != null && !client.userData.getFullName().isEmpty()){
            fullnameTextFieldProfile.setText(client.userData.getFullName());
        }
        if(client.userData.getInfo() != null && !client.userData.getInfo().isEmpty()){
            infoTextAreaProfile.setText(client.userData.getInfo());
        }
        saveProfile.addActionListener(new ButtonSaveProfileListener());

    }
    public void miniProfileWin(String loginProfile){
        miniProfileFrame.setSize(500, 300);
        miniProfileFrame.setLocationRelativeTo(null);
        miniProfileFrame.setVisible(true);
        JPanel miniProfilePanel = new JPanel();
        miniProfileFrame.getContentPane().add(BorderLayout.NORTH, miniProfilePanel);
        miniProfilePanel.setLayout(new GridLayout(3, 2));
        JLabel loginMiniProfileLabel_1 = new JLabel("Логин: ");
        JLabel loginMiniProfileLabel_2 = new JLabel(loginProfile);
        JLabel fullNameMiniProfileLabel_1 = new JLabel("Полное имя: ");
        JLabel fullNameMiniProfileLabel_2 = new JLabel(client.userFullname.get(client.userList.indexOf(loginProfile)));
        JLabel infoMiniProfileLabel_1 = new JLabel("Дополнительная информация: ");
        JLabel infoMiniProfileLabel_2 = new JLabel(client.userInfo.get(client.userList.indexOf(loginProfile)));
        miniProfilePanel.add(loginMiniProfileLabel_1);
        miniProfilePanel.add(loginMiniProfileLabel_2);
        miniProfilePanel.add(fullNameMiniProfileLabel_1);
        miniProfilePanel.add(fullNameMiniProfileLabel_2);
        miniProfilePanel.add(infoMiniProfileLabel_1);
        miniProfilePanel.add(infoMiniProfileLabel_2);

    }



    class ButtonSaveProfileListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(!loginTextFieldProfile.getText().trim().isEmpty()){
                client.userData.updateLogin(loginTextFieldProfile.getText().trim());
                //System.out.println(fullnameTextFieldProfile.getText().trim());
            }
            if(passwordTextFieldProfile.getText().trim() != null && !passwordTextFieldProfile.getText().trim().isEmpty()){
                client.userData.updatePassword(passwordTextFieldProfile.getText().trim());
                //System.out.println(fullnameTextFieldProfile.getText().trim());
            }
            if(fullnameTextFieldProfile.getText().trim() != null && !fullnameTextFieldProfile.getText().trim().isEmpty()){
                client.userData.updateFullName(fullnameTextFieldProfile.getText().trim());
                System.out.println(fullnameTextFieldProfile.getText().trim());
            }
            if(infoTextAreaProfile.getText().trim() != null && !infoTextAreaProfile.getText().trim().isEmpty()){
                client.userData.updateInfo(infoTextAreaProfile.getText().trim());
                //System.out.println(fullnameTextFieldProfile.getText().trim());
            }
            client.saveUserData();
            frame_profile.setVisible(false);
        }
    }

    class ButtonLoginListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(input_login.getText().trim().length() > 0){
                client.IP_ADDR = input_IP.getText().trim();
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
    class ButtonRegUserListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            registration();
        }
    }
    class ButtonFinalRegUserListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(reg_input_login.getText().trim().length() > 0){
                client.IP_ADDR = reg_input_IP.getText().trim();
                client.createNetwork();
                client.name = reg_input_login.getText().trim();
                client.login = reg_input_login.getText().trim();
                client.password = reg_input_password.getText().trim();
                client.registrationPassword = reg_input_regPassword.getText().trim();
                client.sendMessage("CodeRegUserToServer1632");
                client.sendPublicKey();
                frame_reg.setVisible(false);
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
            addressName.setText("Кому: " + el);
            if(sendButtonMode == 2){
                //mainChat.setText(BeginSt + client.groupsHistory.get(client.groups.indexOf(el)) + FinishSt);
                mainChat.setText(BeginSt + client.channel.get(el) + FinishSt);
            }else{
                try{
                    //mainChat.setText(BeginSt + client.historyMess.get(client.friendList.indexOf(el)) + FinishSt);
                    mainChat.setText(BeginSt + client.userData.getChatHistory(el) + FinishSt);
                } catch (ArrayIndexOutOfBoundsException ex){
                    mainChat.setText(BeginSt + "Сообщений нет" + FinishSt);
                }
            }
            System.out.println("element = " + el);
        }
    }
    class ButtonSendListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            //String st = addressName.getText().trim();
            String st = list.getSelectedValue().trim();
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
                    //String mess = loginUser + ": " + messageInput.getText().trim();
                    //String ag = client.historyMess.get(client.friendList.indexOf(st));
                    //ag += mess;
                    //ag += "<br>";
                    //client.historyMess.set(client.friendList.indexOf(st), ag);
                    //mainChat.setText(BeginSt + client.historyMess.get(client.friendList.indexOf(st)) + FinishSt);
                    client.userData.addSendMessageToChatHistory(st, messageInput.getText().trim());
                    mainChat.setText(BeginSt + client.userData.getChatHistory(st) + FinishSt);

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





    class ButtonProfileListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            profileWin();
        }
    }

    class ButtonAddFriendListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            client.sendMessage("CodeAddFriendToUserData");
            client.sendMessage(list.getSelectedValue().trim());
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
    class IpInputFocusListener implements FocusListener {
        @Override
        public void focusGained(FocusEvent e) {
            input_IP.setForeground(color);
            if (input_IP.getText().equals(hintIpString)) {
                input_IP.setText("");
            }
        }
        @Override
        public void focusLost(FocusEvent e) {
            if (input_IP.getText().isEmpty()) {
                input_IP.setForeground(Color.LIGHT_GRAY);
                input_IP.setText(hintIpString);
            }
        }
    }
    class LoginInputFocusListener implements FocusListener {
        @Override
        public void focusGained(FocusEvent e) {
            input_login.setForeground(color);
            if (input_login.getText().equals(hintLoginString)) {
                input_login.setText("");
            }
        }
        @Override
        public void focusLost(FocusEvent e) {
            if (input_login.getText().isEmpty()) {
                input_login.setForeground(Color.LIGHT_GRAY);
                input_login.setText(hintLoginString);
            }
        }
    }
    class PasswordInputFocusListener implements FocusListener {
        @Override
        public void focusGained(FocusEvent e) {
            input_password.setForeground(color);
            if (input_password.getText().equals(hintPasswordString)) {
                input_password.setText("");
            }
        }
        @Override
        public void focusLost(FocusEvent e) {
            if (input_password.getText().isEmpty()) {
                input_password.setForeground(Color.LIGHT_GRAY);
                input_password.setText(hintPasswordString);
            }
        }
    }
    class Reg_IpInputFocusListener implements FocusListener {
        @Override
        public void focusGained(FocusEvent e) {
            reg_input_IP.setForeground(color);
            if (reg_input_IP.getText().equals(hintIpString)) {
                reg_input_IP.setText("");
            }
        }
        @Override
        public void focusLost(FocusEvent e) {
            if (reg_input_IP.getText().isEmpty()) {
                reg_input_IP.setForeground(Color.LIGHT_GRAY);
                reg_input_IP.setText(hintIpString);
            }
        }
    }
    class Reg_LoginInputFocusListener implements FocusListener {
        @Override
        public void focusGained(FocusEvent e) {
            reg_input_login.setForeground(color);
            if (reg_input_login.getText().equals(hintLoginString)) {
                reg_input_login.setText("");
            }
        }
        @Override
        public void focusLost(FocusEvent e) {
            if (reg_input_login.getText().isEmpty()) {
                reg_input_login.setForeground(Color.LIGHT_GRAY);
                reg_input_login.setText(hintLoginString);
            }
        }
    }
    class Reg_PasswordInputFocusListener implements FocusListener {
        @Override
        public void focusGained(FocusEvent e) {
            reg_input_password.setForeground(color);
            if (reg_input_password.getText().equals(hintPasswordString)) {
                reg_input_password.setText("");
            }
        }
        @Override
        public void focusLost(FocusEvent e) {
            if (reg_input_password.getText().isEmpty()) {
                reg_input_password.setForeground(Color.LIGHT_GRAY);
                reg_input_password.setText(hintPasswordString);
            }
        }
    }
    class Reg_RegPasswordInputFocusListener implements FocusListener {
        @Override
        public void focusGained(FocusEvent e) {
            reg_input_regPassword.setForeground(color);
            if (reg_input_regPassword.getText().equals(hintRegPasswordString)) {
                reg_input_regPassword.setText("");
            }
        }
        @Override
        public void focusLost(FocusEvent e) {
            if (reg_input_regPassword.getText().isEmpty()) {
                reg_input_regPassword.setForeground(Color.LIGHT_GRAY);
                reg_input_regPassword.setText(hintRegPasswordString);
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
            list.setListData(dataFavorites);
        }
    }
    class ButtonUserListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            sendButtonMode = 0;
            list.setListData(dataUser);
        }
    }

}