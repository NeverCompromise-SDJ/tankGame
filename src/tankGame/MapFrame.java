package tankGame;

import javax.swing.*;

/**
 * @author SongDongJie
 * @create 2023/4/16 - 22:55
 * 坦克地图顶级容器，规定了游戏的界面大小
 */
public class MapFrame extends JFrame {
    final static int width = 1300;
    final static int height = 800;
    Map map = null;

    public MapFrame() {
        //创建一个坦克地图的非顶级容器
        map = new Map();
        //启动map线程
        new Thread(map).start();
        this.setSize(width, height);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(map);
        this.addKeyListener(map);
    }
}
