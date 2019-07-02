package ru.airvary.server;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
    //Окно настройки
    private JFrame frame_setting = new JFrame();
    private JPanel panel_setting = new JPanel();
    private JPanel leftPanel_set = new JPanel();
    private JPanel cenetralPanel_set = new JPanel();
    private JButton addUserButton = new JButton("Добавать пользователя ");
    //Окно добавление пользователя
    JFrame addUserFrame = new JFrame();
    TextField fieldnewLoginUser = new TextField(12);
    TextField fieldnewPasswordUser = new TextField(12);
    //Окно добавление канала
    JFrame addGroupFrame = new JFrame();
    TextField fieldNewNameGroup = new TextField(12);

    private JButton tunningUserButton = new JButton("Настройка пользователя");
    private JButton addGroup = new JButton("   Добавить группу   ");

    JPanel mainSetPanel = new JPanel();

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
        leftPanel.add(list, BorderLayout.CENTER);

        leftPanel.add(southLeftPanel, BorderLayout.SOUTH);
        southLeftPanel.setLayout(new FlowLayout());
        southLeftPanel.add(usersButton);
        southLeftPanel.add(groupButton);

        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.addListSelectionListener(new ListListener());

        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(addressName, BorderLayout.NORTH);
        centerPanel.add(mainChat, BorderLayout.CENTER);
        centerPanel.add(southCenterPanel, BorderLayout.SOUTH);

        southCenterPanel.setLayout(new BorderLayout());
        southCenterPanel.add(messageInput, BorderLayout.CENTER);

        mainChat.setHorizontalAlignment(JLabel.LEFT);
        mainChat.setVerticalAlignment(JLabel.TOP);

        usersButton.addActionListener(new ButtonUser());
        groupButton.addActionListener(new ButtonGroup());
        //serverChat.createNetwork();

    }
    public void setting(){
        JPanel flowCenPan_1 = new JPanel();
        flowCenPan_1.setLayout(new FlowLayout());

        mainSetPanel.setLayout(new GridLayout(2, 6));

        frame_setting.setSize(200,115);
        frame_setting.setLocationRelativeTo(null);
        frame_setting.setVisible(true);
        frame_setting.getContentPane().add(BorderLayout.CENTER, panel_setting);
        panel_setting.setLayout(new BorderLayout());
        panel_setting.add(BorderLayout.NORTH, leftPanel_set);
        leftPanel_set.setLayout(new BorderLayout());
        leftPanel_set.add(BorderLayout.NORTH, addUserButton);
        leftPanel_set.add(BorderLayout.CENTER, tunningUserButton);
        leftPanel_set.add(BorderLayout.SOUTH, addGroup);

        addUserButton.addActionListener(new ButtonAddUser());
        addGroup.addActionListener(new ButtonAddGroup());
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
    public void addGroup() {
        addGroupFrame = new JFrame();
        addGroupFrame.setSize(340, 90);
        addGroupFrame.setVisible(true);
        addGroupFrame.setLocationRelativeTo(null);

        JLabel newNameGroup = new JLabel("Введите название группы");
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
    class ButtonTunningUserButton implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
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
}

