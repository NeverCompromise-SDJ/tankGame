package tankGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author SongDongJie
 * @create 2023/4/16 - 22:55
 * 坦克地图顶级容器，规定了游戏的界面大小
 */
public class MapFrame extends JFrame {
    final static int width = Map.width + 300;
    final static int height = Map.height + 100;
    Map map = null;

    public MapFrame() {
        //创建一个坦克地图的非顶级容器
        map = new Map();
        //启动map线程
        new Thread(map).start();
        this.setBackground(Color.BLACK);
        this.setSize(width, height);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(map);
        this.addKeyListener(map);
        //监听关闭窗口事件,关闭窗口时保存游戏信息
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Recorder.storeMsg();
            }
        });
    }
}
