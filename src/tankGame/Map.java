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
 * Map类说明：坦克地图的中间容器，游戏展示的地方
 */
//为了让Map不停地重绘子弹，需要将Map做成线程
//子弹绘制机制：通过遍历所有坦克各自的子弹集合来绘制子弹。
//子弹线程生命周期机制：子弹不存在时(isLive=false)，Bullet的run方法就结束了。
//子弹添加进集合，并启动线程的时机：友军坦克：按下J键时（Map的keyPressed方法->Tank的shotBullet方法）  敌方坦克：直接添加（Map的构造器）
//子弹从集合中移出的时机：1.子弹与坦克碰撞时（Map的hitTank方法）。2.绘制单个坦克所有子弹的时候（Map的drawAllBullet方法），这里是将到达边界的子弹移除子弹集合。
//子弹束线程的时机： 1.子弹与坦克碰撞时（Map的hitTank方法）。2.子弹到达边界时（Bullet的run方法）

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
//            enemyTank.shotBullet();
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
     * 绘制单个坦克
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
     * 通过遍历一个坦克的子弹集合，来绘制一个坦克发射的所有子弹。同时将不存在的子弹（isLive=false）移出子弹集合中，提高遍历的效率
     *
     * @param g 画笔
     */
    public void drawAllBullet(Tank tank, Graphics g) {
        //坦克子弹的集合
        Iterator<Bullet> heroBullets = tank.getBulletList().iterator();
        while (heroBullets.hasNext()) {
            Bullet bullet = heroBullets.next();
            //当子弹超出界外而不存在时，需要将子弹从子弹集合中移除，来保证发射了很多子弹后不会降低遍历效率
            if (!bullet.isLive()) {
                heroBullets.remove();
            }
            //绘制单颗子弹
            drawBullet(bullet, g);
        }
    }

    /**
     * 判断友方子弹是否击中敌方坦克，如果击中了则销毁坦克和子弹。
     *
     * @param hero          友方坦克
     * @param enemyTankList 敌方坦克
     */
    public void hitTank(Hero hero, Vector<EnemyTank> enemyTankList) {
        //优化，如果友方坦克在地图上没有子弹或者地图上不存在敌方坦克了，即直接返回。无需再判断友方子弹是否击中敌方坦克
        if (enemyTankList.size() <= 0 || hero.getBulletList().size() <= 0) {
            return;
        }
        /*当友方坦克在地图上存在子弹，且敌方坦克在地图上也存在时。遍历友方坦克的子弹，再针对每颗子弹遍历敌方所有坦克，如果某颗子弹
        和敌方坦克的矩形范围重叠，则说明子弹集中了敌方坦克。将对应的子弹和敌方坦克销毁。*/
        Iterator<Bullet> bulletIterator = hero.getBulletList().iterator();
        //遍历每颗子弹
        while (bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();
            Iterator<EnemyTank> enemyTankIterator = enemyTankList.iterator();
            //针对每颗子弹遍历敌方所有坦克
            while (enemyTankIterator.hasNext()) {
                EnemyTank enemyTank = enemyTankIterator.next();
                //判断子弹是否击中敌方坦克
                switch (enemyTank.getDirection()) {
                    //当坦克为南北朝向时
                    case 0:
                    case 2:
                        if ((bullet.getX() > enemyTank.getX()) && (bullet.getX() < enemyTank.getX() + 50) && (bullet.getY() > enemyTank.getY()) && (bullet.getY() < enemyTank.getY() + 70)) {
                            //将击中坦克的子弹的状态改为不存在，使该子弹线程退出
                            bullet.setLive(false);
                            //将击中坦克的子弹从集合中移除：1.提高效率 2.将该子弹从地图上移除。
                            bulletIterator.remove();
                            //将被击中的坦克从集合中移除：1.提高效率 2.将该子弹从地图上移除。
                            enemyTankIterator.remove();
                        }
                        break;
                    //当坦克为东西朝向时
                    case 1:
                    case 3:
                        if ((bullet.getX() > enemyTank.getX()) && (bullet.getX() < enemyTank.getX() + 70) && (bullet.getY() > enemyTank.getY()) && (bullet.getY() < enemyTank.getY() + 50)) {
                            bullet.setLive(false);
                            bulletIterator.remove();
                            enemyTankIterator.remove();
                        }
                }
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
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //判断友方子弹是否击中敌方坦克，因为要实时判断所以需要放在run方法的while循环中不停执行
            hitTank(hero, enemyTankList);
            //需要不断绘制地图，来更新战场情况
            this.repaint();
        }
    }
}
