package tankGame;

import javax.swing.*;

/**
 * @author SongDongJie
 * @create 2023/4/16 - 22:55
 */
public class MapFrame extends JFrame {
    Map map = null;

    public MapFrame() {
        map = new Map();
        this.setSize(1200, 700);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(map);
    }
}
