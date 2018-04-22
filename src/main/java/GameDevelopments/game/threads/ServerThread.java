package GameDevelopments.game.threads;

import GameDevelopments.game.CanMakeMoveException;
import GameDevelopments.game.CannotMakeMoveException;
import GameDevelopments.game.GameEndedException;
import GameDevelopments.game.OthelloGame;
import GameDevelopments.game.utilities.Pos;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerThread extends Thread {

    public static int GAMEPREPARED = 5;
    private Socket socket;
    private OthelloGame gameInstance;

    public ServerThread(OthelloGame gameInstance, Socket clientSocket) {
        this.socket = clientSocket;
        this.gameInstance = gameInstance;
    }

    public void run() {
        DataOutputStream out = null;
        DataInputStream dIn = null;
        try {
            dIn = new DataInputStream(socket.getInputStream());
            boolean done = false;
            while(!done) {
                byte messageType = dIn.readByte();
                switch(messageType)
                {
                    case 5: //game Ended
                        String result = dIn.readUTF();
                        if(result.matches("You Won!"))
                            gameInstance.gameEnded(true);
                        else
                            gameInstance.gameEnded(false);
                        break;
                    case 4: //Your move
                        gameInstance.nextTurn(true);
                        gameInstance.opponentHasNoMoves();
                        break;
                    case 3: // MoveDone
                        Pos pos = Pos.parse(dIn.readUTF());
                        gameInstance.opponentPlaced(pos.getX(),pos.getY());
                        try {
                            gameInstance.checkState();
                        } catch(GameEndedException e)
                        {
                            if(gameInstance.getYourCount()>gameInstance.getOppCount())
                                send(6, "2");
                            else
                                send(6, "1");
                        } catch(CanMakeMoveException e)
                        {

                        } catch(CannotMakeMoveException e)
                        {
                            gameInstance.nextTurn(false);
                            send(4,"");
                        }
                        break;
                    default:
                        break;
                }
            }
        } catch (IOException e) {
            return;
        }
    }


    public synchronized void send(int byteInt, String text)
    {
        try {
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.writeByte(byteInt);
            out.writeUTF(text);
            out.flush();
        } catch (IOException e) {
            return;
        }
    }

    public void endConnection()
    {
        send(7,"");
    }
}