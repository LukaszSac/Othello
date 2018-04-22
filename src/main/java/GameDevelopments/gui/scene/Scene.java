package GameDevelopments.gui.scene;

import GameDevelopments.gui.frame.MainFrame;
import GameDevelopments.gui.panels.OthelloComponent;
import GameDevelopments.gui.panels.PlainText;
import GameDevelopments.utilities.Painter;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Scene extends JPanel
{
    private Timer timer = null;
    public List<OthelloComponent> texts = new ArrayList<OthelloComponent>();
    private static LocalTime localTime = null;
    public Scene()
    {
        super(null);
        setBackground(Color.WHITE);
        setSize(MainFrame.res);
        try {

            JSONObject downloaded = readJsonFromUrl("https://www.amdoren.com/api/timezone.php?api_key=tqdigwAY2imrUbE7DxTjw8YBzQsCHr&loc=Warsaw");
            String time = downloaded.getString("time");
            localTime = LocalTime.parse(("20:00:01"), DateTimeFormatter.ofPattern("HH:mm:ss"));
            //localTime = LocalTime.parse(time.substring(time.length()-8,time.length()), DateTimeFormatter.ofPattern("HH:mm:ss"));
            PlainText timeText = new PlainText(localTime.toString(),13, Color.BLACK,0.9,0.9, Painter.LEFT);
            timer = new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    localTime = localTime.plusSeconds(1);
                    timeText.changeText(localTime.toString());
                }
            });
            timer.start();
            addCompon(timeText);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }

    public void addCompon(OthelloComponent c)
    {
        add(c.getComponent());
        texts.add(c);
    }

    public void unregister()
    {
        timer.stop();
        for(OthelloComponent text : texts)
        {
            text.unregister();
        }
    }


}
