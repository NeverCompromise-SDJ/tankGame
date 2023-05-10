package tankGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Iterator;
import java.util.Vector;

/**
 * @author SongDongJie
 * @create 2023/4/16 - 22:54
 * 坦克地图的中间容器，游戏展示的地方
 */
//为了让Map不停地重绘子弹，需要将Map做成线程
class Map extends JPanel implements KeyListener, Runnable {
    //友军坦克
    private Hero hero = null;
    //敌军坦克
    final private Vector<EnemyTank> enemyTankList = new Vector<>();//敌人的坦克较多，因此放入vector集合中（线程安全）
    private int enemyTankNumber = 3;

    //初始化背景颜色、坦克方位、坦克速度
    Map() {
        //将背景设为黑色
        this.setBackground(Color.BLACK);
        //添加友方坦克
        hero = new Hero(300, 300);
        hero.setSpeed(5);
        //添加敌方坦克
        for (int i = 0; i < enemyTankNumber; i++) {
            EnemyTank enemyTank = new EnemyTank((i + 1) * 100, 0);
            //使得敌方坦克开始时，炮筒向下
            enemyTank.setDirection(2);
            enemyTankList.add(enemyTank);
            enemyTank.shotBullet();
        }
    }

    //绘制主方法
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        //绘制友军坦克
        drawTank(hero.getX(), hero.getY(), 0, hero.getDirection(), g);
        //绘制友军坦克所有子弹
        drawAllBullet(hero, g);
        //绘制敌方坦克和敌方坦克所有子弹
        for (EnemyTank enemyTank : enemyTankList) {
            //绘制坦克
            drawTank(enemyTank.getX(), enemyTank.getY(), 1, enemyTank.getDirection(), g);
            //绘制子弹
            drawAllBullet(enemyTank, g);
        }


        System.out.println(hero.getBulletList().size());  //测试子弹消失后，子弹集合的长度是否减少了

    }

    /**
     * 绘制坦克
     *
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
            case 3:
                g.fill3DRect(x, y, 70, 10, false);
                g.fill3DRect(x + 10, y + 10, 50, 30, false);
                g.fill3DRect(x, y + 40, 70, 10, false);
                g.fillOval(x + 20, y + 10, 30, 30);
                g.drawLine(x + 35, y + 25, x, y + 25);
                break;
            default:
        }
    }

    /**
     * 绘制一个坦克的一颗子弹
     *
     * @param bullet 子弹对象
     * @param g      画笔
     */
    public void drawBullet(Bullet bullet, Graphics g) {
        g.draw3DRect(bullet.getX(), bullet.getY(), 1, 1, false);
    }

    /**
     * 绘制一个坦克发射的所有子弹
     *
     * @param g 画笔
     */
    public void drawAllBullet(Tank tank, Graphics g) {
        //坦克子弹的集合
        Iterator<Bullet> heroBullets = tank.getBulletList().iterator();
        while (heroBullets.hasNext()) {
            Bullet bullet = heroBullets.next();
            //绘制单颗子弹
            drawBullet(bullet, g);
            //当子弹不存在时，需要将子弹从子弹集合中移除，来保证发射了很多子弹后不会降低遍历效率
            if (!bullet.isLive()) {
                heroBullets.remove();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        //根据方向键改变坦克的方向,并让坦克动起来
        if (e.getKeyCode() == KeyEvent.VK_UP) {//朝北走
            hero.setDirection(0);
            hero.move();
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {//朝东走
            hero.setDirection(1);
            hero.move();
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {//朝南走
            hero.setDirection(2);
            hero.move();
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {//朝西走
            hero.setDirection(3);
            hero.move();
        }
        //按下J键时，坦克发射子弹
        if (e.getKeyCode() == KeyEvent.VK_J) {
            hero.shotBullet();
        }
        //改变了坦克的位置和方向、射出子弹后，需要重新绘制地图
        this.repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void run() {//每隔100毫秒重绘地图
        while (true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.repaint();
        }
    }
}
