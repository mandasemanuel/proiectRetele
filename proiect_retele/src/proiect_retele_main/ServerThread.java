package proiect_retele_main;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import javax.swing.JTextArea;

class ServerThread extends Thread
    {
        private final int CONNECTION_PORT = 4444;

        private BufferedReader input;
        private PrintWriter output;
        private ServerSocket serverSocket;
        private Socket clientSocket;
        public static JTextArea messages;
        private boolean stopped;

    @SuppressWarnings("static-access")
        public ServerThread(JTextArea messages)
        {
            this.messages = messages;
            stopped = true;
        }

        @Override
	public void run()
        {
		logMessage(">>>>> Server running...");

		while (!stopped)
        {

                clientSocket = null;

                try
                {

                    clientSocket = serverSocket.accept();
                } catch (IOException e)
                {
                    errorMessage("Error Message" + e.getMessage());
                }


                if (clientSocket != null)
                {

                    try
                    {

                        logMessage("Connected: "
                                + clientSocket.getRemoteSocketAddress());


                        input = new BufferedReader(new InputStreamReader(
                                clientSocket.getInputStream()));


                        try
                        {
                            String line = input.readLine();
                            logMessage("<<<<<<File requested: " + line);


                            output = new PrintWriter(
                                    clientSocket.getOutputStream(), true);


                            File file = new File(line);


                            if (file.exists())
                            {
                                logMessage(">>>>>Send file to client");


                                Scanner reader = new Scanner(file);


                                while (reader.hasNextLine())
                                {

                                    line = reader.nextLine();


                                    output.println(line);
                                }

                                logMessage("File sent");


                                reader.close();

                            }

                            else
                            {
                              output.write("File not found or opened.");
                            }
                        } 

                        finally
                        {
                            
                            output.close();
                            input.close();
                            clientSocket.close();
                        }
                    } 
                    catch (IOException e)
                    {
                        logError("Generic IO Exception. Details: " + e.getMessage());
                    }
                }
            }


            try
            {
                serverSocket.close();
            }
            catch (IOException e)
            {
                errorMessage("Error Message: " + e.getMessage());
            }

            logMessage("Server Shutdown");
        }

	public void startServer()
        {
		serverSocket = null;
		try
                {
                    serverSocket = new ServerSocket(CONNECTION_PORT);
                    serverSocket.setSoTimeout(10000);
                    stopped = false;
                    start();
                }
                catch (IOException e)
                {
                    logError("Generic IO Exception. ServerThread.startServer() Details: "
                        + e.getMessage());
		}
	}


	public void stopServer()
        {
            stopped = true;
            logMessage("Stop the server");
	}


	public void logMessage(String mssg)
        {
            messages.setText(messages.getText() + mssg + "\n");
	}


	private void logError(String error)
        {
            logMessage("logError()error" + error);
	}

        private void errorMessage(String error)
        {
            logMessage("errorMessage()" + error);
        }
}