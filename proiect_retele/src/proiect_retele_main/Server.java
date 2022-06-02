package proiect_retele_main;



import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class Server extends JFrame implements ActionListener
{
    public static void main(String[] args)
    {

        Server myServer = new Server();
        myServer.setVisible(true);
    }

    private JButton startServerButton;
    private JButton stopServerButton;
    private JTextArea messagesTextArea;
    private ServerThread server;



    public Server()
    {

        super("Server");

                setSize(new Dimension(400,400));

                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


                startServerButton = new JButton("Start Server");
                startServerButton.addActionListener(this);


                stopServerButton = new JButton("Stop Server");
                stopServerButton.addActionListener(this);


                JPanel serverPanel = new JPanel();
                serverPanel.setLayout(new GridLayout(1, 2, 5, 5));

                serverPanel.add(startServerButton);
                serverPanel.add(stopServerButton);

                messagesTextArea = new JTextArea(10 , 20);


                getContentPane().add(serverPanel, BorderLayout.NORTH);
                getContentPane().add(new JScrollPane(messagesTextArea),
                      BorderLayout.CENTER);


                pack();


                setLocationRelativeTo(null);
    }

    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource().equals(startServerButton))
        {
            if (server !=null)
            {
                server.stopServer();
            }

                server = new ServerThread(messagesTextArea);
                server.startServer();
        }
        else if (e.getSource().equals(stopServerButton))
        {
            if (server !=null)
            {
                server.stopServer();
                server = null;
            }
        }
    }
}