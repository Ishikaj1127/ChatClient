import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ChatClient {
    private static PrintWriter out;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Chat Client");
        JTextArea textArea = new JTextArea(20, 50);
        JTextField inputField = new JTextField(40);
        JButton sendButton = new JButton("Send");
        JPanel panel = new JPanel();

        textArea.setEditable(false);
        panel.add(inputField);
        panel.add(sendButton);

        frame.add(new JScrollPane(textArea), BorderLayout.CENTER);
        frame.add(panel, BorderLayout.SOUTH);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        try {
            Socket socket = new Socket("localhost", 12345); // Replace "localhost" with server IP on LAN
            out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Sending message
            sendButton.addActionListener(e -> {
                String msg = inputField.getText();
                out.println(msg);
                inputField.setText("");
            });

            inputField.addActionListener(e -> {
                String msg = inputField.getText();
                out.println(msg);
                inputField.setText("");
            });

            // Receiving message
            new Thread(() -> {
                String message;
                try {
                    while ((message = in.readLine()) != null) {
                        textArea.append("Server: " + message + "\n");
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
