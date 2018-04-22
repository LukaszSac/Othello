package GameDevelopments.gui.threads;

import GameDevelopments.gui.frame.MainFrame;
import GameDevelopments.gui.panels.PlainText;
import GameDevelopments.gui.scene.SceneBuilder;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

public class WaitThread implements Runnable {

    private Socket socket = null;
    private Timer timer = null;
    public WaitThread(Socket socket, Timer timer)
    {
        this.socket = socket;
        this.timer = timer;
        timer.start();
        new Thread(this).start();
    }

    @Override
    public void run()
    {
        DataInputStream dIn = null;
        try {
            DataOutputStream out = null;
            out = new DataOutputStream(socket.getOutputStream());
            dIn = new DataInputStream(socket.getInputStream());
            boolean done = false;
            out.writeByte(1);
            out.flush();
            String colour = null;
            String oppNickname = null;
            String yourNickname = null;
            int oppWins = 0;
            int yourWins = 0;
            while(!done) {
                byte messageType = dIn.readByte();
                switch(messageType)
                {
                    case 1: // Type A
                        colour = dIn.readUTF();
                        break;
                    case 2: // Type B
                        oppNickname = dIn.readUTF();
                        break;
                    case 3:
                        oppWins = Integer.parseInt(dIn.readUTF());
                        break;
                    case 4:
                        yourNickname = dIn.readUTF();
                        break;
                    case 5:
                        yourWins = Integer.parseInt(dIn.readUTF());
                        done = true;
                        break;
                    default:
                }
            }
            MainFrame.sceneLoaderSemaphore.acquire();
            MainFrame.instance.changeScene(SceneBuilder.prepareGame(colour,oppNickname,oppWins,yourNickname,yourWins,socket));
            timer.stop();
        } catch (SocketException e)
        {
        } catch (IOException e) {
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
