package GameDevelopments.gui.scene;

import GameDevelopments.dictionary.CustomDictionary;
import GameDevelopments.game.OthelloGame;
import GameDevelopments.game.elements.Stone;
import GameDevelopments.gui.actions.*;
import GameDevelopments.gui.actions.customActions.CustomListenerAction;
import GameDevelopments.gui.panels.ButtonText;
import GameDevelopments.gui.panels.ImagePanel;
import GameDevelopments.gui.panels.PlainText;
import GameDevelopments.gui.frame.MainFrame;
import GameDevelopments.gui.panels.gamePanels.FieldPanel;
import GameDevelopments.gui.threads.BuildSceneThread;
import GameDevelopments.gui.threads.WaitThread;
import GameDevelopments.utilities.Resolution;
import GameDevelopments.settings.Settings;
import GameDevelopments.utilities.FileLoader;
import GameDevelopments.utilities.ImageHolder;
import GameDevelopments.utilities.Painter;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;

public class SceneBuilder
{

    private static Calendar time = null;
    private static Dimension getPercent(double widthPercent, double heightPercent)
    {
        Dimension res = MainFrame.res;
        double width = res.getWidth()*widthPercent;
        double height = res.getHeight()*heightPercent;
        return new Dimension((int)width,(int)height);
    }

    private static Dimension getExact(int width, int height)
    {
        return new Dimension(width,height);
    }
    public static Scene buildMenu()
    {
        Scene menu = new Scene();
        menu.setSize(MainFrame.res);
        ImagePanel othelloString = new ImagePanel(0.5,0.15,Painter.CENTER,ImageHolder.OTHELLO_STRING);
        menu.addCompon(othelloString);
        ButtonText game = new ButtonText(7,20,Color.BLACK,0.5,0.3, Painter.CENTER);
        game.addActionListener(new OpenWindowAction(SceneManager.GAME_LOAD));
        menu.addCompon(game);

        ButtonText settings = new ButtonText(3,20,Color.BLACK,0.5,0.36, Painter.CENTER);
        settings.addActionListener(new OpenWindowAction(SceneManager.SETTINGS));
        menu.addCompon(settings);

        ButtonText exit = new ButtonText(2,20,Color.BLACK,0.5,0.42, Painter.CENTER);
        exit.addActionListener(new ExitAppAction());
        menu.addCompon(exit);

        return menu;
    }

    public static Scene buildSettings()
    {
        Scene settings = new Scene();
        settings.setSize(MainFrame.res);
        PlainText language = new PlainText(4,20, Color.BLACK,0.2,0.15, Painter.LEFT);
        settings.addCompon(language);

        double leftArrowX = 0.6;
        double rightArrowX = 0.8;
        double valueMid = (rightArrowX-leftArrowX)/2.0+leftArrowX;

        PlainText languageName = new PlainText(CustomDictionary.getLangName(CustomDictionary.currentLanguage),20,Color.BLACK,valueMid,0.15, Painter.CENTER);
        settings.addCompon(languageName);

        ButtonText leftArrowLang = new ButtonText(FileLoader.loadImage("resources/leftArrow.png"),leftArrowX,0.15, Painter.CENTER);
        leftArrowLang.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        settings.addCompon(leftArrowLang);

        ButtonText rightArrowLang = new ButtonText(FileLoader.loadImage("resources/rightArrow.png"),rightArrowX,0.15, Painter.CENTER);
        rightArrowLang.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        settings.addCompon(rightArrowLang);

        ChangeLanguageAction arrowLang = new ChangeLanguageAction(-1);
        arrowLang.addCustomAction(new CustomListenerAction() {
            @Override
            public void performAction() {
                int langId = CustomDictionary.getLangID(-1);
                languageName.changeText(CustomDictionary.getLangName(langId));
            }
        });
        leftArrowLang.addActionListener(arrowLang);
        arrowLang = new ChangeLanguageAction(1);
        arrowLang.addCustomAction(new CustomListenerAction() {
            @Override
            public void performAction() {
                int langId = CustomDictionary.getLangID(1);
                languageName.changeText(CustomDictionary.getLangName(langId));
            }
        });
        rightArrowLang.addActionListener(arrowLang);


        PlainText resolutionText = new PlainText(6,20,Color.BLACK,0.2,0.2,Painter.LEFT);
        settings.addCompon(resolutionText);

        PlainText resolutionValue = new PlainText(Settings.getInstance().getValue("Window_resolution"),20,Color.BLACK,valueMid,0.2,Painter.CENTER);
        settings.addCompon(resolutionValue);

        ButtonText leftArrowRes = new ButtonText(FileLoader.loadImage("resources/leftArrow.png"),leftArrowX,0.2, Painter.CENTER);
        leftArrowRes.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        settings.addCompon(leftArrowRes);

        ButtonText rightArrowRes = new ButtonText(FileLoader.loadImage("resources/rightArrow.png"),rightArrowX,0.2, Painter.CENTER);
        rightArrowRes.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        settings.addCompon(rightArrowRes);

        ChangeResAction arrowRes = new ChangeResAction(-1);
        arrowRes.addCustomAction(new CustomListenerAction() {
            @Override
            public void performAction() {

                String res = Resolution.getRes(-1);
                resolutionValue.changeText(res);
            }
        });
        leftArrowRes.addActionListener(arrowRes);
        arrowRes = new ChangeResAction(1);
        arrowRes.addCustomAction(new CustomListenerAction() {
            @Override
            public void performAction() {

                String res = Resolution.getRes(1);
                resolutionValue.changeText(res);
            }
        });
        rightArrowRes.addActionListener(arrowRes);

        ButtonText exit = new ButtonText(5,20,Color.BLACK,0.5,0.8,Painter.CENTER);
        settings.addCompon(exit);
        exit.addActionListener(new OpenWindowAction(SceneManager.MENU));
        return settings;
    }

    public static Scene prepareGame(String colour, String opponentName, int opponentWins, String yourName, int yourWins, Socket connection)
    {
        Scene game = new Scene();
        game.setSize(MainFrame.res);
        double width = ImageHolder.getTileWidth();
        double x, y;
        double xOff = width/2.9, yOff = width/2.9;
        PlainText turnText = new PlainText(8,20,Color.BLACK,0.8,0.3,Painter.LEFT);
        PlainText blackCountText = null,whiteCountText = null;
        game.addCompon(turnText);
        if(colour.matches("White"))
        {
            blackCountText = new PlainText(16,": 0",20,Color.BLACK,0.75,0.2,Painter.LEFT);
            whiteCountText = new PlainText(17,": 0",20,Color.BLACK,0.55,0.2,Painter.LEFT);
        } else if(colour.matches("Black"))
        {
            whiteCountText = new PlainText(17,20,Color.BLACK,0.75,0.2,Painter.LEFT);
            blackCountText = new PlainText(16,20,Color.BLACK,0.55,0.2,Painter.LEFT);
        }
        PlainText result = new PlainText(20,20,Color.BLACK,0.7,0.4,Painter.CENTER);
        result.setVisible(false);
        OthelloGame gameInstance = new OthelloGame(colour,connection,turnText,blackCountText,whiteCountText,result);
        for(int i=0;i<8;i++)
        {
            y = 0 + width*i+yOff;
            for(int j=0;j<8;j++)
            {
                x = 0 + width*j + xOff;
                FieldPanel tile = new FieldPanel(x,y,Painter.EXACT,j,i,gameInstance);
                game.addCompon(tile);
                gameInstance.addListenerToTile(j,i,tile);
            }
        }
        PlainText oppColour;
        PlainText myColour;
        if(gameInstance.getOpponentColour()==Stone.BLACK)
        {
            oppColour = new PlainText(15,20,Color.BLACK,0.75,0.16,Painter.LEFT);
            myColour = new PlainText(14,20,Color.BLACK,0.55,0.16,Painter.LEFT);
        }
        else
        {
            oppColour = new PlainText(14,20,Color.BLACK,0.75,0.16,Painter.LEFT);
            myColour = new PlainText(15,20,Color.BLACK,0.55,0.16,Painter.LEFT);
        }
        gameInstance.forcePiece(3,3, Stone.WHITE);
        gameInstance.forcePiece(3,4, Stone.BLACK);
        gameInstance.forcePiece(4,4, Stone.WHITE);
        gameInstance.forcePiece(4,3, Stone.BLACK);
        gameInstance.changeNumber();
        PlainText oppName = new PlainText(13, ": "+ opponentName,20,Color.BLACK,0.75,0.1,Painter.LEFT);
        PlainText oppWins = new PlainText(12, ": "+ String.valueOf(opponentWins),20,Color.BLACK,0.75,0.13,Painter.LEFT);
        PlainText nickName = new PlainText(10,": "+ yourName,20,Color.BLACK,0.55,0.10,Painter.LEFT);
        PlainText wins = new PlainText(11, ": "+ String.valueOf(yourWins),20,Color.BLACK,0.55,0.13,Painter.LEFT);
        game.addCompon(oppName);
        game.addCompon(oppWins);
        game.addCompon(oppColour);
        game.addCompon(wins);
        game.addCompon(nickName);
        game.addCompon(myColour);
        game.addCompon(blackCountText);
        game.addCompon(whiteCountText);
        game.addCompon(result);
        ButtonText exit = new ButtonText(5,20,Color.BLACK,0.8,0.9,Painter.CENTER);
        OpenWindowAction exitAction = new OpenWindowAction(SceneManager.MENU);
        exitAction.addCustomAction(new CustomListenerAction() {
            @Override
            public void performAction() {
                gameInstance.forfeit();
                gameInstance.endConnection();
            }
        });
        exit.addActionListener(exitAction);
        game.addCompon(exit);
        ButtonText settings = new ButtonText(3,20,Color.BLACK,0.8,0.85,Painter.CENTER);
        settings.addActionListener(new OpenFrameSettings());
        game.addCompon(settings);
        return game;
    }


    public static Scene buildLoadingGameScene(String name)
    {
        time = Calendar.getInstance();
        time.set(Calendar.HOUR,0);
        time.set(Calendar.MINUTE, 0);
        time.set(Calendar.SECOND, 0);
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        PlainText queueTime = new PlainText(21,": " +sdf.format(time.getTime()),20,Color.BLACK,0.5,0.4,Painter.CENTER);
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                time.add(Calendar.SECOND,1);
                queueTime.changeStatic(": " + sdf.format(time.getTime()));
            }
        });
        CustomListenerAction exied = null;
        try {
            final Socket connection = new Socket("localhost",1337);
            exied = new CustomListenerAction() {
                @Override
                public void performAction() {
                    try {
                        timer.stop();
                        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                        out.writeByte(9);
                        out.writeUTF("");
                        out.flush();
                        connection.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            new WaitThread(connection,timer);

            try {
                DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                out.writeByte(1);
                out.writeUTF(name);
                out.flush();
            } catch (IOException e) {
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene loading = buildLoading(SceneManager.MENU, exied);
        loading.addCompon(queueTime);
        ImagePanel plaseWait = new ImagePanel(0.5,0.3,Painter.CENTER,ImageHolder.PLEASE_WAIT);
        loading.add(plaseWait);
        return loading;
    }

    public static Scene buildGame()
    {
        Scene login = new Scene();
        PlainText logIn = new PlainText(22,20,Color.BLACK,0.52,0.44,Painter.CENTER);
        login.addCompon(logIn);
        JTextField nameArea = new JTextField();
        int areaWidth = 200;
        nameArea.setHorizontalAlignment(JTextField.CENTER);
        nameArea.setBounds((int)(MainFrame.WIDTH/2-areaWidth/2),(int)(MainFrame.HEIGHT/2-20),areaWidth,50);
        nameArea.setFont(nameArea.getFont().deriveFont(30f));
        login.add(nameArea);
        ButtonText ok = new ButtonText("Login",20,Color.BLACK,0.5,0.6,Painter.CENTER);
        ok.setMnemonic(KeyEvent.VK_ENTER);
        login.addCompon(ok);
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    MainFrame.sceneLoaderSemaphore.acquire();
                    Method method = SceneBuilder.class.getMethod("buildLoadingGameScene", String.class);
                    new BuildSceneThread(method,new Object[]{nameArea.getText()}).start();
                } catch (NoSuchMethodException e1) {
                    e1.printStackTrace();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        });
        ButtonText exit = new ButtonText(5,20,Color.BLACK,0.5,0.65,Painter.CENTER);
        exit.addActionListener(new OpenWindowAction(SceneManager.MENU));

        login.addCompon(exit);
        return login;
    }



    public static Scene buildLoading(int backScene, CustomListenerAction action)
    {
        Scene loading = new Scene();
        loading.setSize(MainFrame.res);
        ButtonText exit = new ButtonText(5,20,Color.BLACK,0.5,0.5,Painter.CENTER);
        OpenWindowAction exitAction = new OpenWindowAction(backScene);
        if(action!=null)
            exitAction.addCustomAction(action);
        exit.addActionListener(exitAction);
        loading.addCompon(exit);
        return loading;
    }
}
