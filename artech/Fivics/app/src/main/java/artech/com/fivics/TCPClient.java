package artech.com.fivics;

/**
 * Created by moon on 2017-09-27.
 */

import android.util.Log;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class TCPClient {

    private String serverMessage;
    public static final String SERVERIP = "192.168.2.169"; //your computer IP address
    public static final int SERVERPORT = 8100;
    private OnMessageReceived mMessageListener = null;
    private boolean mRun = false;

    OutputStream out;
    BufferedReader in;

    /**
     *  Constructor of the class. OnMessagedReceived listens for the messages received from server
     */
    public TCPClient(OnMessageReceived listener) {
        mMessageListener = listener;
    }

    /**
     * Sends the message entered by client to the server
     * @param message text entered by client
     */
    public void sendMessage(String message2){
        if (out != null) {
            Log.d("TCPClient", "sendMessage");
//            out.println(message);
            byte[] message = new byte[12];
            message[0] = (byte) 0x00;
            message[1] = (byte) 0x00;
            message[2] = (byte) 0x00;
            message[3] = (byte) 0x00;
            message[4] = (byte) 0x00;
            message[5] = (byte) 0x00;
            message[6] = (byte) 0x22;
            message[7] = (byte) 0x31;
            message[8] = (byte) 0x03;
            message[9] = (byte) 0x11;
            message[10] = (byte) 0x00;
            message[11] = (byte) 0x01;
            try {
                out.write(message);
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopClient(){
        mRun = false;
    }

    public void run() {

        mRun = true;

        try {
            //here you must put your computer's IP address.
            InetAddress serverAddr = InetAddress.getByName(SERVERIP);

            Log.e("TCP Client", "C: Connecting...");

            //create a socket to make the connection with the server
            Socket socket = new Socket(serverAddr, SERVERPORT);

            try {

                //send the message to the server
                out = socket.getOutputStream();

                Log.e("TCP Client", "C: Sent.");

                Log.e("TCP Client", "C: Done.");

                //receive the message which the server sends back
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                //in this while the client listens for the messages sent by the server
                while (mRun) {
                    serverMessage = in.readLine();

                    if (serverMessage != null && mMessageListener != null) {
                        //call the method messageReceived from MyActivity class
                        mMessageListener.messageReceived(serverMessage);
                    }
                    serverMessage = null;

                }

                Log.e("RESPONSE FROM SERVER", "S: Received Message: '" + serverMessage + "'");

            } catch (Exception e) {

                Log.e("TCP", "S: Error", e);

            } finally {
                //the socket must be closed. It is not possible to reconnect to this socket
                // after it is closed, which means a new socket instance has to be created.
                socket.close();
            }

        } catch (Exception e) {

            Log.e("TCP", "C: Error", e);

        }

    }

    //Declare the interface. The method messageReceived(String message) will must be implemented in the MyActivity
    //class at on asynckTask doInBackground
    public interface OnMessageReceived {
        public void messageReceived(String message);
    }
}