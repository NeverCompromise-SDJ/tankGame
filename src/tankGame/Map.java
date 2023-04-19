package tankGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * @author SongDongJie
 * @create 2023/4/16 - 22:54
 */
class Map extends JPanel implements KeyListener {
    Tank hero = null;
    private int direction = 0;

    //初始化背景颜色和坦克初始坐标
    Map() {
        this.setBackground(Color.BLACK);
        hero = new Hero(100, 100);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        drawTank(hero.getX(), hero.getY(), 0, this.direction, g);
    }

    /**
     * @param x         坦克左上角x坐标
     * @param y         坦克左上角y坐标
     * @param type      坦克的类型，0为友军，1为敌人
     * @param direction 坦克的朝向，0123对应上右下左
     * @param g         画笔
     */
    public void drawTank(int x, int y, int type, int direction, Graphics g) {
        //根据坦克类型，确定坦克颜色
        switch (type) {
            case 0:
                g.setColor(Color.cyan);
                break;
            case 1:
                g.setColor(Color.YELLOW);
                break;
            default:

        }
        //根据坦克朝向，以坦克为左上角的坐标始终为(0,0)的方法，确定坦克坐标。
        switch (direction) {
            case 0:
                g.fill3DRect(x, y, 10, 70, false);//左边轮子
                g.fill3DRect(x + 10, y + 10, 30, 50, false);//身子
                g.fill3DRect(x + 40, y, 10, 70, false);//右边轮子
                g.fillOval(x + 10, y + 20, 30, 30);//炮台
                g.drawLine(x + 25, y, x + 25, y + 45);//炮管
                break;
            case 1:
                g.fill3DRect(x, y, 70, 10, false);
                g.fill3DRect(x + 10, y + 10, 50, 30, false);
                g.fill3DRect(x, y + 40, 70, 10, false);
                g.fillOval(x + 20, y + 10, 30, 30);
                g.drawLine(x + 35, y + 25, x + 70, y + 25);
                break;
            case 2:
                g.fill3DRect(x, y, 10, 70, false);
                g.fill3DRect(x + 10, y + 10, 30, 50, false);
                g.fill3DRect(x + 40, y, 10, 70, false);
                g.fillOval(x + 10, y + 20, 30, 30);
                g.drawLine(x + 25, y + 45, x + 25, y + 70);
                break;
            default:
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int x = hero.getX();
        int y = hero.getY();
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            this.direction = 0;
            hero.setY(--y);
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            this.direction = 1;
            hero.setX(++x);
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            this.direction = 2;
            hero.setY(++y);
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            this.direction = 3;
            hero.setX(--x);
        }
        this.repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
