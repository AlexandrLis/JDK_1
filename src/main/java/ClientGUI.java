import org.w3c.dom.Text;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

public class ClientGUI extends JFrame {
    private static final String pathFile = "File.txt";

    private JTextField IP = new JTextField("127.0.0.1");
    private JTextField port = new JTextField("8189");
    private JTextField userName = new JTextField("Ivan");
    private JPasswordField password = new JPasswordField("123456");
    private JButton btnLogin = new JButton("login");



    private JTextField sendMessage = new JTextField();
    private JButton btnSendMessage = new JButton("send");


    JTextArea yourMessagesArea = new JTextArea();


    private JTextField isRegistration = new JTextField("New user is registration");
    private JTextField notRegistration = new JTextField("Enter the user name, please");

    public ClientGUI(ServerWindow serverWindow){
        setLocation(100, 150);
        setSize(400, 300);
        setTitle("Client");


        JPanel userInfoPanel = new JPanel(new GridLayout(2, 3));
        userInfoPanel.add(IP);
        userInfoPanel.add(port);
        userInfoPanel.add(userName);
        userInfoPanel.add(password);
        userInfoPanel.add(btnLogin);
        add(userInfoPanel, BorderLayout.NORTH);


        JPanel sendMessagePanel = new JPanel(new GridLayout(1, 2));
        sendMessagePanel.add(sendMessage);
        sendMessagePanel.add(btnSendMessage);
        add(sendMessagePanel, BorderLayout.SOUTH);


        JPanel yourMessagesPanel = new JPanel();
        yourMessagesPanel.add(yourMessagesArea);
        add(yourMessagesPanel, BorderLayout.WEST);



        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(registration() && serverWindow.getIsServerWorking()){
                    textWriter(isRegistration.getText() + ": " + userName.getText() + "\n");
                    yourMessagesArea.setText(textReader());
                    serverWindow.messagesArea.setText(textReader());
                    userInfoPanel.setVisible(false);
                    serverWindow.addClient(ClientGUI.this);
                }
                else if(!(registration()) && serverWindow.getIsServerWorking()){
                    textWriter(notRegistration.getText() + "\n");
                    yourMessagesArea.setText(textReader());
                    serverWindow.messagesArea.setText(textReader());
                }
            }
        });

        btnSendMessage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(userName.getText());
                if(serverWindow.getIsServerWorking() && userName.getText().length() != 0){
                    textWriter(userName.getText() + ": " + sendMessage.getText() + "\n");
                    yourMessagesArea.setText(textReader());
                    serverWindow.messagesArea.setText(textReader());
                    serverWindow.sendMessageToClient();
                }
            }
        });




        setVisible(true);
    }


    public boolean registration(){
        if(IP.getText().length() != 0 && port.getText().length() != 0 && userName.getText().length() != 0 && password.getPassword().length != 0){
            return true;
        }
        return false;
    }

    public void textWriter(String text){
        try(FileOutputStream writer = new FileOutputStream(pathFile, true)){
            writer.write(text.getBytes(StandardCharsets.UTF_8));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void textReWriter(){
        try(FileOutputStream writer = new FileOutputStream(pathFile)){
            writer.write("".getBytes(StandardCharsets.UTF_8));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public String textReader(){
        String text = "";
        try(FileInputStream reader = new FileInputStream(pathFile)){
            int line;
            while ((line = reader.read()) != -1){
                System.out.print((char)line);
                text += (char)line;
            }

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return text;
    }


}
