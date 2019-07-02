package ru.airvary.server;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ServerGui extends JFrame {
    //Окно входа
    private JFrame frame_1 = new JFrame();
    private JPanel panel_1 = new JPanel();
    private JTextField input_login = new JTextField(12);
    private JTextField input_password = new JTextField(12);
    private JButton button_1 = new JButton("Регистрация");
    private JButton button_2 = new JButton("Вход");
    //Окно регистрации
    private JFrame frame_reg = new JFrame();
    private JPanel panel_reg = new JPanel();
    private JTextField reg_login = new JTextField(12);
    private JTextField reg_password = new JTextField(12);
    private JButton buttonFinishRegistration = new JButton("Регистрация");
    //Главное окно
    private JFrame frame_2 = new JFrame();
    private JPanel leftPanel = new JPanel();
    private JPanel northLeftPanel = new JPanel();
    private JPanel flowNorthLeftPanel = new JPanel();
    private JButton settingBut = new JButton("Настройки");
    public String[] dataUser = {"All"};
    public String[] dataChannel = {"all"};
    public JList<String> list = new JList<String>(dataUser);
    public JScrollPane listScrollPane = new JScrollPane(list);
    private JPanel southLeftPanel = new JPanel();
    private JButton usersButton = new JButton("Пользователи");
    private JButton groupButton = new JButton("Каналы");

    private JPanel centerPanel = new JPanel();
    private JLabel addressName = new JLabel("Логи: ");
    private String BeginSt = "<html>";
    private String FinishSt = "<html>";
    private String MiddleSt = "";

    public JLabel mainChat = new JLabel(BeginSt + MiddleSt + FinishSt);
    public JScrollPane mainChatScrollPane = new JScrollPane(mainChat);
    private JPanel southCenterPanel = new JPanel();
    private JTextField messageInput = new JTextField(20);
    //Окно настройки
    private JFrame frame_setting = new JFrame();
    private JPanel northPanel_setting = new JPanel();
    private JPanel panel_setting = new JPanel();
    private JPanel northPanel_set = new JPanel();
    private JPanel centralPanel_set = new JPanel();

    JLabel loginSetting = new JLabel("Логин: ");
    JLabel ipSetting = new JLabel("IP-адрес: ");
    JLabel registrationPassword = new JLabel("Пароль для регистрации пользователей: ");

    private JButton changeLoginButton  = new JButton("Изменить логин");
    private JButton changePasswordButton = new JButton("Изменить пароль");
    private JButton changeRegPasswordButton = new JButton("Изменить пароль для регистрации пользователей");
    private JButton addUserButton = new JButton("Добавать пользователя ");
    private JButton removeUserButton = new JButton("Удалить пользователя");
    private JButton addGroupButton = new JButton("   Добавить канал   ");
    private JButton removeGroupButton = new JButton("Удалить канал");

    //Окно добавление пользователя
    JFrame addUserFrame = new JFrame();
    TextField fieldnewLoginUser = new TextField(12);
    TextField fieldnewPasswordUser = new TextField(12);
    //Окно удаления пользователя
    JFrame deleteUserFrame = new JFrame();
    TextField fieldDeleteLoginUser = new TextField(12);
    //Окно добавление канала
    JFrame addGroupFrame = new JFrame();
    TextField fieldNewNameGroup = new TextField(12);
    //Окно удаления канала
    JFrame deleteGroupFrame = new JFrame();
    TextField deleteGroupField = new TextField(12);
    //Окно обновления пароля для регистрации пользователей
    JFrame changeRegistrationPasswordFrame = new JFrame();
    TextField fieldchangeRegistrationPassword = new TextField(12);
    //Окно изменения логина
    JFrame changeLoginFrame = new JFrame();
    TextField changeLoginTextField = new TextField(12);
    //Окно изменения пароля
    JFrame changePasswordFrame = new JFrame();
    TextField changePasswordTextField = new TextField(12);
    //private JButton tunningUserButton = new JButton("Настройка пользователя");

    //private JButton setRegistration = new JButton("Изменить пароль регистрации");
    String hintLoginString = "Введите логин";
    String hintPasswordString = "Введите пароль";
    Color color = input_login.getCaretColor();


    ServerChat serverChat;

    public ServerGui(ServerChat serverChat){
        this.serverChat = serverChat;
        entry();
    }


    public void entry() {
        //Окно входа
        frame_1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame_1.getContentPane().add(BorderLayout.CENTER, panel_1);
        panel_1.setLayout(new FlowLayout());
        panel_1.add(input_login);
        panel_1.add(input_password);
        panel_1.add(button_1);
        panel_1.add(button_2);

        frame_1.setSize(200, 130);
        frame_1.setVisible(true);
        frame_1.setLocationRelativeTo(null);
        button_2.addActionListener(new ButtonLoginListener());
        button_1.addActionListener(new ButtonRegistrationListener());

        input_login.setForeground(Color.LIGHT_GRAY);
        input_login.setText(hintLoginString);
        input_login.addFocusListener(new LoginInputFocusListener());
        input_password.setForeground(Color.LIGHT_GRAY);
        input_password.setText(hintPasswordString);
        input_password.addFocusListener(new PasswordInputFocusListener());

        serverChat.loadingServerData();
    }

    public void registration() {
        //Окно регистрации
        frame_reg.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame_reg.getContentPane().add(BorderLayout.CENTER, panel_reg);
        panel_reg.setLayout(new FlowLayout());
        panel_reg.add(reg_login);
        panel_reg.add(reg_password);
        panel_reg.add(buttonFinishRegistration);
        buttonFinishRegistration.addActionListener(new ButtonFinalRegistrationListener());

        reg_login.setForeground(Color.LIGHT_GRAY);
        reg_login.setText(hintLoginString);
        reg_login.addFocusListener(new Reg_LoginInputFocusListener());
        reg_password.setForeground(Color.LIGHT_GRAY);
        reg_password.setText(hintPasswordString);
        reg_password.addFocusListener(new Reg_PasswordInputFocusListener());

        frame_1.setVisible(false);
        frame_reg.setSize(200, 140);
        frame_reg.setVisible(true);
        frame_reg.setLocationRelativeTo(null);
    }
    public void mainWindows(){
        //Осноное окно
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
        northLeftPanel.add(BorderLayout.CENTER, flowNorthLeftPanel);
        flowNorthLeftPanel.setLayout(new BoxLayout(flowNorthLeftPanel, BoxLayout.Y_AXIS));
        flowNorthLeftPanel.setLayout(new FlowLayout());
        flowNorthLeftPanel.add(settingBut);
        settingBut.addActionListener(new ButtonSetting());
        //leftPanel.add(list, BorderLayout.CENTER);
        leftPanel.add(listScrollPane, BorderLayout.CENTER);

        leftPanel.add(southLeftPanel, BorderLayout.SOUTH);
        southLeftPanel.setLayout(new FlowLayout());
        southLeftPanel.add(usersButton);
        southLeftPanel.add(groupButton);

        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.addListSelectionListener(new ListListener());

        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(addressName, BorderLayout.NORTH);
        //centerPanel.add(mainChat, BorderLayout.CENTER);
        centerPanel.add(mainChatScrollPane, BorderLayout.CENTER);
        //centerPanel.add(southCenterPanel, BorderLayout.SOUTH);

        //southCenterPanel.setLayout(new BorderLayout());
        //southCenterPanel.add(messageInput, BorderLayout.CENTER);

        mainChat.setHorizontalAlignment(JLabel.LEFT);
        mainChat.setVerticalAlignment(JLabel.TOP);

        usersButton.addActionListener(new ButtonUser());
        groupButton.addActionListener(new ButtonGroup());
        //serverChat.createNetwork();

    }
    public void setting(){
        //JPanel flowCenPan_1 = new JPanel();
        //flowCenPan_1.setLayout(new FlowLayout());

        //mainSetPanel.setLayout(new GridLayout(2, 6));
        loginSetting.setText("Логин: " + serverChat.serverData.getLogin());
        ipSetting.setText("IP-адрес: " + serverChat.thisIP.getHostAddress());


        if(serverChat.serverData.getRegistrationPassword() != null){
            registrationPassword.setText("Пароль для регистрации пользователей: " + serverChat.serverData.getRegistrationPassword());
        }

        frame_setting.setSize(400,190);
        frame_setting.setLocationRelativeTo(null);
        frame_setting.setVisible(true);
        frame_setting.getContentPane().add(BorderLayout.NORTH, northPanel_setting);
        frame_setting.getContentPane().add(BorderLayout.CENTER, panel_setting);
        frame_setting.getContentPane().add(BorderLayout.SOUTH, changeRegPasswordButton);
        //panel_setting.setLayout(new BorderLayout());
        panel_setting.setLayout(new GridLayout(3, 2));
        northPanel_setting.setLayout(new GridLayout(3, 1));
        northPanel_setting.add(loginSetting);
        northPanel_setting.add(ipSetting);
        northPanel_setting.add(registrationPassword);

        panel_setting.add(changeLoginButton);
        panel_setting.add(changePasswordButton);
        panel_setting.add(addUserButton);
        panel_setting.add(removeUserButton);
        panel_setting.add(addGroupButton);
        panel_setting.add(removeGroupButton);
        //panel_setting.add(changeRegPasswordButton);

        //panel_setting.add(BorderLayout.NORTH, leftPanel_set);
        //leftPanel_set.setLayout(new BorderLayout());
        //leftPanel_set.add(BorderLayout.NORTH, addUserButton);
        //leftPanel_set.add(BorderLayout.CENTER, tunningUserButton);
        //leftPanel_set.add(BorderLayout.SOUTH, addGroup);

        addUserButton.addActionListener(new ButtonAddUser());
        removeUserButton.addActionListener(new ButtonDeleteUser());
        addGroupButton.addActionListener(new ButtonAddGroup());
        removeGroupButton.addActionListener(new ButtonDeleteGroup());
        changeRegPasswordButton.addActionListener(new ButtonChangeRegPasswordListener());
        changeLoginButton.addActionListener(new ButtonChangeLoginListener());
        changePasswordButton.addActionListener(new ButtonChangePasswordListener());
    }


    public void addUser(){
        addUserFrame = new JFrame();
        addUserFrame.setSize(400, 130);
        addUserFrame.setVisible(true);
        addUserFrame.setLocationRelativeTo(null);

        JLabel newLoginUser = new JLabel("Введите логин пользователя  ");
        JLabel newPasswordUser = new JLabel("Введите пароль пользователя");
        fieldnewLoginUser = new TextField(12);
        fieldnewPasswordUser = new TextField(12);
        JButton addFinishAddUser = new JButton("Добавить");

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        addUserFrame.getContentPane().add(BorderLayout.CENTER, mainPanel);

        JPanel northFlowPanel = new JPanel();
        northFlowPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 8, 5));
        JPanel centralFlowPanel = new JPanel();
        centralFlowPanel.setLayout(new FlowLayout());

        mainPanel.add(BorderLayout.NORTH, northFlowPanel);
        mainPanel.add(BorderLayout.CENTER, centralFlowPanel);
        mainPanel.add(BorderLayout.SOUTH, addFinishAddUser);
        northFlowPanel.add(newLoginUser);
        northFlowPanel.add(fieldnewLoginUser);
        centralFlowPanel.add(newPasswordUser);
        centralFlowPanel.add(fieldnewPasswordUser);
        addFinishAddUser.addActionListener(new ButtonFinishAddUser());

    }
    public void deleteUser(){
        deleteUserFrame = new JFrame();
        deleteUserFrame.setSize(400, 100);
        deleteUserFrame.setVisible(true);
        deleteUserFrame.setLocationRelativeTo(null);
        JPanel deleteUserPanel = new JPanel();
        deleteUserFrame.getContentPane().add(BorderLayout.NORTH, deleteUserPanel);
        deleteUserPanel.setLayout(new FlowLayout());
        JLabel deleteUserLabel = new JLabel("Введите логин пользователя ");
        JButton deleteUserFinishButton = new JButton("Удалить");
        deleteUserPanel.add(deleteUserLabel);
        deleteUserPanel.add(fieldDeleteLoginUser);
        deleteUserPanel.add(deleteUserFinishButton);
        deleteUserFinishButton.addActionListener(new ButtonFinishDeleteUser());
    }
    public void addGroup() {
        addGroupFrame = new JFrame();
        addGroupFrame.setSize(340, 90);
        addGroupFrame.setVisible(true);
        addGroupFrame.setLocationRelativeTo(null);

        JLabel newNameGroup = new JLabel("Введите название канала");
        fieldNewNameGroup = new TextField(12);
        JButton addFinishGroupButton = new JButton("Добавить");

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        addGroupFrame.getContentPane().add(BorderLayout.CENTER, mainPanel);
        JPanel centerPanelGroup = new JPanel();
        centerPanelGroup.setLayout(new FlowLayout());
        mainPanel.add(BorderLayout.CENTER, centerPanelGroup);
        centerPanelGroup.add(newNameGroup);
        centerPanelGroup.add(fieldNewNameGroup);
        mainPanel.add(BorderLayout.SOUTH, addFinishGroupButton);
        addFinishGroupButton.addActionListener(new ButtonFinishAddGroup());
    }
    public void deleteGroup(){
        deleteGroupFrame = new JFrame();
        deleteGroupFrame.setSize(400, 100);
        deleteGroupFrame.setVisible(true);
        deleteGroupFrame.setLocationRelativeTo(null);
        JPanel deleteGroupPanel = new JPanel();
        deleteGroupFrame.getContentPane().add(BorderLayout.NORTH, deleteGroupPanel);
        deleteGroupPanel.setLayout(new FlowLayout());
        JLabel deleteGroupLabel = new JLabel("Введите название канала ");
        JButton deleteGroupFinishButton = new JButton("Удалить");
        deleteGroupPanel.add(deleteGroupLabel);
        deleteGroupPanel.add(deleteGroupField);
        deleteGroupPanel.add(deleteGroupFinishButton);
        deleteGroupFinishButton.addActionListener(new ButtonFinishDeleteGroup());
    }
    public void changeLogin(){
        changeLoginFrame = new JFrame();
        changeLoginFrame.setSize(400, 100);
        changeLoginFrame.setVisible(true);
        changeLoginFrame.setLocationRelativeTo(null);
        JPanel changeLoginPanel = new JPanel();
        changeLoginFrame.getContentPane().add(BorderLayout.NORTH, changeLoginPanel);
        changeLoginPanel.setLayout(new FlowLayout());
        JLabel changeLoginLabel = new JLabel("Введите новый логин ");
        JButton changeLoginFinishButton = new JButton("Изменить");
        changeLoginPanel.add(changeLoginLabel);
        changeLoginPanel.add(changeLoginTextField);
        changeLoginPanel.add(changeLoginFinishButton);
        changeLoginFinishButton.addActionListener(new ButtonFinishChangeLoginListener());
    }
    public void changePassword(){
        changePasswordFrame = new JFrame();
        changePasswordFrame.setSize(400, 100);
        changePasswordFrame.setVisible(true);
        changePasswordFrame.setLocationRelativeTo(null);
        JPanel changePasswordPanel = new JPanel();
        changePasswordFrame.getContentPane().add(BorderLayout.NORTH, changePasswordPanel);
        changePasswordPanel.setLayout(new FlowLayout());
        JLabel changePasswordLabel = new JLabel("Введите новый пароль ");
        JButton changePasswordFinishButton = new JButton("Изменить");
        changePasswordPanel.add(changePasswordLabel);
        changePasswordPanel.add(changePasswordTextField);
        changePasswordPanel.add(changePasswordFinishButton);
        changePasswordFinishButton.addActionListener(new ButtonFinishChangePasswordListener());
    }

    public void changeRegistrationPassword(){
        changeRegistrationPasswordFrame = new JFrame();
        changeRegistrationPasswordFrame.setSize(350, 120);
        changeRegistrationPasswordFrame.setVisible(true);
        changeRegistrationPasswordFrame.setLocationRelativeTo(null);

        JLabel changeRegistrationPasswordLabel = new JLabel("Введите новый пароль для регистрации пользователей");
        fieldchangeRegistrationPassword = new TextField(12);
        JButton changeRegistrationPasswordButton = new JButton("Cохранить");
        JPanel centerPanelGroup = new JPanel();
        centerPanelGroup.setLayout(new GridLayout(3,1));
        changeRegistrationPasswordFrame.getContentPane().add(BorderLayout.CENTER, centerPanelGroup);
        centerPanelGroup.add(changeRegistrationPasswordLabel);
        centerPanelGroup.add(fieldchangeRegistrationPassword);
        centerPanelGroup.add(changeRegistrationPasswordButton);
        changeRegistrationPasswordButton.addActionListener(new ButtonChangeRegistrationPassword());

    }
    public void makeErrorMessage(String message){
        JOptionPane.showMessageDialog(ServerGui.this, message,"Ошибка", JOptionPane.ERROR_MESSAGE);
    }

    class ButtonLoginListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String login = input_login.getText().trim();
            String password = input_password.getText().trim();
            serverChat.entryAndLoadingUserData(login, password);
            mainWindows();
        }
    }
    class ButtonRegistrationListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            registration();
        }
    }
    class ButtonFinalRegistrationListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            String login = reg_login.getText().trim();
            String password = reg_password.getText().trim();
            serverChat.createServerData(login, password);
            frame_reg.setVisible(false);
            frame_1.setVisible(true);
        }
    }
    class ButtonSetting implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            setting();
        }
    }
    class ButtonAddUser implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            addUser();
        }
    }
    class ButtonFinishAddUser implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String login = fieldnewLoginUser.getText().trim();
            String password = fieldnewPasswordUser.getText().trim();
            serverChat.addUser(login, password);
            addUserFrame.setVisible(false);
        }
    }
    class ButtonDeleteUser implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            deleteUser();
        }
    }
    class ButtonFinishDeleteUser implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String login = fieldDeleteLoginUser.getText().trim();
            serverChat.deleteUser(login);
            deleteUserFrame.setVisible(false);
        }
    }
    class ButtonChangeLoginListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            changeLogin();
        }
    }
    class ButtonFinishChangeLoginListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            serverChat.changeLogin(changeLoginTextField.getText().trim());
            loginSetting.setText("Логин: " + changeLoginTextField.getText().trim());
            changeLoginFrame.setVisible(false);
        }
    }
    class ButtonChangePasswordListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            changePassword();
        }
    }
    class ButtonFinishChangePasswordListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            serverChat.changePassword(changePasswordTextField.getText().trim());
            changePasswordFrame.setVisible(false);
        }
    }

    class ButtonChangeRegPasswordListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            changeRegistrationPassword();
        }
    }
    class ButtonChangeRegistrationPassword implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            serverChat.serverData.updateRegistrationPassword(fieldchangeRegistrationPassword.getText().trim());
            serverChat.saveServerData();
            changeRegistrationPasswordFrame.setVisible(false);


        }
    }
    class ButtonAddGroup implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            addGroup();
        }
    }
    class ButtonFinishAddGroup implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = fieldNewNameGroup.getText().trim();
            serverChat.addChannel(name);
            addGroupFrame.setVisible(false);
        }
    }
    class ButtonDeleteGroup implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            deleteGroup();
        }
    }
    class ButtonFinishDeleteGroup implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = deleteGroupField.getText().trim();
            serverChat.deleteChannel(name);
            deleteGroupFrame.setVisible(false);
        }
    }
    class ListListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            String el = list.getSelectedValue().trim();
            addressName.setText(el);
            System.out.println("element = " + el);
        }
    }
    class ButtonUser implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            list.setListData(dataUser);
        }
    }
    class ButtonGroup implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            list.setListData(dataChannel);
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
    class Reg_LoginInputFocusListener implements FocusListener {
        @Override
        public void focusGained(FocusEvent e) {
            reg_login.setForeground(color);
            if (reg_login.getText().equals(hintLoginString)) {
                reg_login.setText("");
            }
        }
        @Override
        public void focusLost(FocusEvent e) {
            if (reg_login.getText().isEmpty()) {
                reg_login.setForeground(Color.LIGHT_GRAY);
                reg_login.setText(hintLoginString);
            }
        }
    }
    class Reg_PasswordInputFocusListener implements FocusListener {
        @Override
        public void focusGained(FocusEvent e) {
            reg_password.setForeground(color);
            if (reg_password.getText().equals(hintPasswordString)) {
                reg_password.setText("");
            }
        }
        @Override
        public void focusLost(FocusEvent e) {
            if (reg_password.getText().isEmpty()) {
                reg_password.setForeground(Color.LIGHT_GRAY);
                reg_password.setText(hintPasswordString);
            }
        }
    }
}

