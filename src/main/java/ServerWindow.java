import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ServerWindow extends JFrame {

    private static final String pathFile = "File.txt";

    private static boolean isServerWorking = false;

    JButton start = new JButton("start");
    JButton stop = new JButton("stop");
    JButton clear = new JButton("Clear");

    Label startLabel = new Label("Server start");
    Label stopLabel = new Label("Server stopped");


    JTextArea messagesArea = new JTextArea();

    List<ClientGUI> clientGUIList;


    public ServerWindow(){

        clientGUIList = new ArrayList<>();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocation(500, 150);
        setSize(300, 300);
        setTitle("Server");

        JPanel btnPanel = new JPanel(new GridLayout(1, 3));
        btnPanel.add(start);
        btnPanel.add(stop);
        btnPanel.add(clear);
        add(btnPanel, BorderLayout.SOUTH);

        JPanel messagesPanel = new JPanel();
        messagesPanel.add(messagesArea);
        add(messagesPanel, BorderLayout.WEST);

        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isServerWorking = true;
                textWriter(startLabel.getText() + "\n");
                messagesArea.setText(textReader());
                sendMessageToClient();
//              ******************************
            }
        });


        stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isServerWorking = false;
                textWriter(stopLabel.getText() + "\n");
                messagesArea.setText(textReader());
                sendMessageToClient();
//              ******************************
            }
        });

        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textReWriter();
                messagesArea.setText(textReader());
                sendMessageToClient();
            }
        });



        setVisible(true);
    }

    public void addClient(ClientGUI clientGUI){
        clientGUIList.add(clientGUI);
    }

    public void sendMessageToClient(){
        for (ClientGUI clientGUI : clientGUIList) {
            clientGUI.yourMessagesArea.setText(textReader());
        }
    }

    @Override
    protected void processWindowEvent(WindowEvent e) {
        if(e.getID() == WindowEvent.WINDOW_CLOSING){
            textReWriter();
            messagesArea.setText(textReader());
            sendMessageToClient();
        }
        super.processWindowEvent(e);
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

    public boolean getIsServerWorking(){
        return isServerWorking;
    }

}
