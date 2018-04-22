package GameDevelopments.server;

import GameDevelopments.server.db.Player;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class ClientThread extends Thread {

    private static final Logger logger = LogManager.getLogger(ClientThread.class);
    private static Semaphore queueSem = new Semaphore(1);
    private static List<ClientThread> queued = new ArrayList<>();
    private static String white = "White";
    private static String black = "Black";
    protected Socket socket;
    private ClientThread opponent = null;
    private String name = null;
    private int wins;
    private boolean done;
    public ClientThread(Socket clientSocket) {
        this.socket = clientSocket;
        boolean done = false;
        DataInputStream dIn = null;
        try {
            while(!done)
            {
                dIn = new DataInputStream(socket.getInputStream());
                byte messageType = dIn.readByte();
                switch (messageType) {
                    case 1:
                        name = dIn.readUTF();
                        done = true;
                }
            }
        }catch( IOException e)
        {

        }
        EntityManager em = Server.emf.createEntityManager();
        em.getTransaction().begin();
        Player player = null;
        try {
            player = em.createQuery("SELECT p FROM Player p WHERE p.name = '" + name + "'", Player.class).getSingleResult();
        } catch (NoResultException e)
        {
            player = new Player();
            player.setName(name);
            player.setWins(0);
            em.persist(player);
            logger.info("Added new player to Database: " + name);
        }
        em.getTransaction().commit();
        wins = player.getWins();
        try {
            queueSem.acquire();
            queued.add(this);
            if(queued.size()>=2)
            {
                String oppC = black;
                String myC = white;
                queued.get(0).interTwine(this,oppC);

                this.opponent = queued.get(0);
                send(1,myC);
                send(2,opponent.getPlayerName());
                send(3,String.valueOf(opponent.getPlayerWins()));
                send(4,player.getName());
                send(5,String.valueOf(player.getWins()));
                queued.remove(1);
                queued.remove(0);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            queueSem.release();
        }
    }

    private void interTwine(ClientThread opponent, String colour)
    {
        this.opponent = opponent;
        send(1,colour);
        send(2,opponent.getPlayerName());
        send(3,String.valueOf(opponent.getPlayerWins()));
        send(4,this.getPlayerName());
        send(5,String.valueOf(this.getPlayerWins()));
    }

    private synchronized void send(int value, String text)
    {
        try {
            DataOutputStream  out = new DataOutputStream(socket.getOutputStream());
            out.writeByte(value);
            out.writeUTF(text);
            out.flush();
        } catch (IOException e) {

        }
    }

    public void run() {
        DataOutputStream out = null;
            DataInputStream dIn = null;
            try {
            dIn = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            out.writeByte(-1);
            done = false;
            while(!done) {
                byte messageType = dIn.readByte();
                switch(messageType)
                {
                    case 9:
                        ended();
                        break;
                    case 4:
                        opponent.send(4,"");
                        break;
                    case 5:
                        opponent.send(3,dIn.readUTF());
                        break;
                    case 6:
                        String who = dIn.readUTF();
                        if(who.matches("1"))
                            opponent.youWon();
                        else
                            youWon();
                        break;
                    case 7:
                        ended();
                        done = true;
                        break;

                }
            }
        } catch (IOException e) {
                try {
                    if(opponent!=null)
                        opponent.socket.close();
                    socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                ended();
                return;
        }
        ended();

    }

    public void youLost()
    {
        send(5,"You Lost!");
    }

    public void youWon()
    {
        send(5,"You Won!");
        opponent.youLost();
        opponent.done = true;
        done = true;
        EntityManager em = Server.emf.createEntityManager();
        em.getTransaction().begin();
        Player player = null;
        player = em.createQuery("SELECT p FROM Player p WHERE p.name = '" + name + "'", Player.class).getSingleResult();
        player.setWins(player.getWins() + 1);
        em.persist(player);
        em.getTransaction().commit();
    }

    private void ended()
    {
        try {
            queueSem.acquire();
            queued.remove(this);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        } finally {
            queueSem.release();
        }
    }

    public String getPlayerName() {
        return name;
    }


    public int getPlayerWins() {
        return wins;
    }
}
