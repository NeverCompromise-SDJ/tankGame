package tankGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.BreakIterator;
import java.time.temporal.TemporalAmount;
import java.util.Iterator;
import java.util.Vector;

/**
 * @author SongDongJie
 * @create 2023/4/16 - 22:54
 * Map类说明：坦克地图的中间容器，游戏展示的地方
 */
/*为了让Map不停地重绘子弹，需要将Map做成线程
子弹绘制机制：通过遍历所有坦克各自的子弹集合来绘制子弹。
子弹线程生命周期机制：子弹不存在时(isLive=false)，Bullet的run方法就结束了。
子弹添加进集合，并启动线程的时机：友军坦克：按下J键时（Map的keyPressed方法->Tank的shotBullet方法）最多同时存在五颗  敌方坦克：直接添加（Map的构造器）
子弹从集合中移出的时机：1.子弹与坦克碰撞时（Map的hitTank方法）。2.绘制单个坦克所有子弹的时候（Map的drawAllBullet方法），这里是将到达边界的子弹移除子弹集合。
子弹束线程的时机： 1.子弹与坦克碰撞时（Map的hitTank方法）。2.子弹到达边界时（Bullet的run方法）
坦克爆炸动画机制：坦克被子弹击中后，创建一个Bomb对象并添加到bomb集合中以便进行绘画，同时启动bomb线程来更新爆炸的效果。
paint方法检查bombList集合中是否有元素，有的话根据bomb对象的生命周期，来进行爆炸效果绘制，爆炸结束后将bomb移出集合以提高遍历效率，并结束bomb线程。
敌方坦克移动机制：当坦克对象创建时，同时开启坦克线程，使得坦克可以随机移动。敌方坦克被击中时，坦克状态为死亡，坦克线程结束。坦克
也被移除坦克集合。
防止敌方坦克重叠思路：以一个坦克作为参照，根据参照坦克的不同方向来分成八种情况。如果参照坦克与其他坦克的边界相碰，就停止参照坦克向此方向移动。
情况：
当参照坦克为北朝向时,其他坦克为分别为南北、东西朝向时：参照坦克的左上角和右上角不得与其他坦克重合。
当参照坦克为南朝向时,其他坦克为分别为南北、东西朝向时：参照坦克的左下角和右下角不得与其他坦克重合。
当参照坦克为东朝向时,其他坦克为分别为南北、东西朝向时：参照坦克的右上角和右下角不得与其他坦克重合。
当参照坦克为北朝向时,其他坦克为分别为南北、东西朝向时：参照坦克的左上角和左下角不得与其他坦克重合。
*/
class Map extends JPanel implements KeyListener, Runnable {
    //友军坦克
    private Hero hero;
    //敌军坦克
    final private Vector<EnemyTank> enemyTankList = new Vector<>();//敌人的坦克较多，因此放入vector集合中（线程安全）
    private int enemyTankNumber = 3;
    //Bomb对象集合
    final private Vector<Bomb> bombList = new Vector<>();

    //初始化背景颜色、坦克方位、坦克速度
    Map() {
        //将背景设为黑色
        this.setBackground(Color.BLACK);
        //添加友方坦克
        hero = new Hero(300, 500);
        //修改友方坦克速度
        hero.setSpeed(10);
        //添加敌方坦克
        for (int i = 0; i < enemyTankNumber; i++) {
            //初始化敌方坦克位置
            EnemyTank enemyTank = new EnemyTank((i + 1) * 100, 100);
            //启动敌方坦克线程，使得坦克可以随机移动
            new Thread(enemyTank).start();
            //使得敌方坦克开始时，炮筒向下
            enemyTank.setDirection(2);
            //修改敌方坦克速度
            enemyTank.setSpeed(3);
            enemyTankList.add(enemyTank);
        }
    }

    //绘制主方法
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        //绘制存活的友军坦克
        if (hero.isLive()) {
            drawTank(hero, g);
        }
        //绘制友军坦克所有子弹
        drawAllBullet(hero, g);
        //绘制敌方坦克和敌方坦克所有子弹
        Iterator<EnemyTank> enemyTankIterator = enemyTankList.iterator();
        while (enemyTankIterator.hasNext()) {
            EnemyTank enemyTank = enemyTankIterator.next();
            //如果坦克不存在了，即坦克被打爆了，则把该坦克从集合中移除，并不予绘画
            if (!enemyTank.isLive()) {
                enemyTankIterator.remove();
                continue;
            }
            //绘制坦克
            drawTank(enemyTank, g);
            //绘制子弹
            drawAllBullet(enemyTank, g);
        }
        //绘制所有坦克的爆炸效果，当bombList中有对象时，说明已经有坦克爆炸了。
        drawAllExplode(bombList, g);

//        System.out.println(hero.getBulletList().size());  //测试子弹消失后，友方子弹集合的长度是否减少了
//        System.out.println(bombList.size());  //测试爆炸结束后，bomb集合的长度是否减少了
//        System.out.println(enemyTankList.size());       //测试坦克被击中后，enemyTankList长度是否减少了

    }


    /**
     * 绘制单个坦克
     *
     * @param tank 坦克对象
     * @param g    画笔
     */
    public void drawTank(Tank tank, Graphics g) {
        //根据坦克类型，确定坦克颜色
        if (tank.isEnemy()) {
            g.setColor(Color.YELLOW);
        } else {
            g.setColor(Color.cyan);
        }
        //根据坦克朝向，以坦克为左上角的坐标始终为(0,0)的方法，确定坦克坐标。
        switch (tank.getDirection()) {
            case 0:
                g.fill3DRect(tank.getX(), tank.getY(), 10, 70, false);//左边轮子
                g.fill3DRect(tank.getX() + 10, tank.getY() + 10, 30, 50, false);//身子
                g.fill3DRect(tank.getX() + 40, tank.getY(), 10, 70, false);//右边轮子
                g.fillOval(tank.getX() + 10, tank.getY() + 20, 30, 30);//炮台
                g.drawLine(tank.getX() + 25, tank.getY(), tank.getX() + 25, tank.getY() + 45);//炮管
                break;
            case 1:
                g.fill3DRect(tank.getX(), tank.getY(), 70, 10, false);
                g.fill3DRect(tank.getX() + 10, tank.getY() + 10, 50, 30, false);
                g.fill3DRect(tank.getX(), tank.getY() + 40, 70, 10, false);
                g.fillOval(tank.getX() + 20, tank.getY() + 10, 30, 30);
                g.drawLine(tank.getX() + 35, tank.getY() + 25, tank.getX() + 70, tank.getY() + 25);
                break;
            case 2:
                g.fill3DRect(tank.getX(), tank.getY(), 10, 70, false);
                g.fill3DRect(tank.getX() + 10, tank.getY() + 10, 30, 50, false);
                g.fill3DRect(tank.getX() + 40, tank.getY(), 10, 70, false);
                g.fillOval(tank.getX() + 10, tank.getY() + 20, 30, 30);
                g.drawLine(tank.getX() + 25, tank.getY() + 45, tank.getX() + 25, tank.getY() + 70);
                break;
            case 3:
                g.fill3DRect(tank.getX(), tank.getY(), 70, 10, false);
                g.fill3DRect(tank.getX() + 10, tank.getY() + 10, 50, 30, false);
                g.fill3DRect(tank.getX(), tank.getY() + 40, 70, 10, false);
                g.fillOval(tank.getX() + 20, tank.getY() + 10, 30, 30);
                g.drawLine(tank.getX() + 35, tank.getY() + 25, tank.getX(), tank.getY() + 25);
                break;
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
            //当子弹不存在时（超出界外/击中坦克），需要将子弹从子弹集合中移除，提高遍历效率。并且不予绘制该子弹。
            if (!bullet.isLive()) {
                heroBullets.remove();
                continue;
            }
            //绘制单颗子弹
            drawBullet(bullet, g);
        }
    }

    /**
     * 绘制所有坦克爆炸效果
     *
     * @param bombList 爆炸对象的集合
     * @param g        画笔
     */
    public void drawAllExplode(Vector<Bomb> bombList, Graphics g) {
        Iterator<Bomb> bombIterator = bombList.iterator();
        while (bombIterator.hasNext()) {
            Bomb bomb = bombIterator.next();
            //绘制单个坦克的爆炸效果
            drawExplode(bomb, g);
            //爆炸结束后将bomb对象从bombList中移除。使得之后绘制爆炸效果，遍历bombList时能提高效率
            if (bomb.getLifePeriod() > 3) {
                bombIterator.remove();
            }
        }

    }

    /**
     * 根据bomb对象的生命周期，绘制单个坦克爆炸效果
     *
     * @param bomb bomb对象
     * @param g    画笔
     */
    public void drawExplode(Bomb bomb, Graphics g) {
        Image imageTankExplode1 = Toolkit.getDefaultToolkit().getImage("./resource/坦克爆炸图/bomb_1.gif");
        Image imageTankExplode2 = Toolkit.getDefaultToolkit().getImage("./resource/坦克爆炸图/bomb_2.gif");
        Image imageTankExplode3 = Toolkit.getDefaultToolkit().getImage("./resource/坦克爆炸图/bomb_3.gif");
        //当bomb对象生命周期为1、2、3时，分别处于爆炸的前中后期，爆炸的范围也从大到小
        if (bomb.getLifePeriod() == 1) {
            g.drawImage(imageTankExplode1, bomb.getX(), bomb.getY(), 60, 60, this);
        } else if (bomb.getLifePeriod() == 2) {
            g.drawImage(imageTankExplode2, bomb.getX(), bomb.getY(), 60, 60, this);
        } else if (bomb.getLifePeriod() == 3) {
            g.drawImage(imageTankExplode3, bomb.getX(), bomb.getY(), 60, 60, this);
        }

    }

    /**
     * 判断场上所有子弹是否击中敌对方的坦克
     *
     * @param hero          友方坦克
     * @param enemyTankList 敌方坦克
     */
    public void hitTank(Hero hero, Vector<EnemyTank> enemyTankList) {
        //先判断友方子弹是否击中敌方坦克，如果场上没有友方子弹或者敌方坦克全部被消灭，则无需判断
        if (enemyTankList.size() > 0 && hero.getBulletList().size() > 0) {
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
                    isHit(enemyTank, bullet);
                }
            }
        }
//        判断敌方子弹是否击中友方坦克，如果友方已经被消灭，则无需判断
        if (hero.isLive()) {
            Iterator<EnemyTank> iteratorEnemyTank = enemyTankList.iterator();
            while (iteratorEnemyTank.hasNext()) {
                EnemyTank enemyTank = iteratorEnemyTank.next();
                //如果该敌方坦克在场上没有子弹，则无需判断
                if (enemyTank.getBulletList().size() <= 0) {
                    continue;
                }
                Iterator<Bullet> bulletIterator = enemyTank.getBulletList().iterator();
                while (bulletIterator.hasNext()) {
                    Bullet bullet = bulletIterator.next();
                    //判断子弹是否击中友方坦克
                    isHit(hero, bullet);
                }
            }
        }
    }

    /**
     * 判断当前该子弹是否击中敌对方的坦克,如果击中了则销毁坦克和子弹。
     *
     * @param tank   遍历的坦克对象
     * @param bullet 遍历的子弹对象
     */
    public void isHit(Tank tank, Bullet bullet) {
        switch (tank.getDirection()) {
            //当坦克为南北朝向时
            case 0:
            case 2:
                if ((bullet.getX() > tank.getX()) && (bullet.getX() < tank.getX() + 50) && (bullet.getY() > tank.getY()) && (bullet.getY() < tank.getY() + 70)) {
                    //将击中坦克的子弹的状态改为不存在，使该子弹线程退出，并且从地图上消失
                    bullet.setLive(false);
                    //将被击中的坦克的状态设置为死亡，如果是敌方坦克，能够使得坦克线程退出，并且从地图上消失。
                    tank.setLive(false);
                    //被子弹击中后，创建一个Bomb对象并添加到bomb集合中以便进行绘画，同时启动bomb线程来更新爆炸的效果
                    Bomb bomb = new Bomb(tank.getX(), tank.getY());
                    bombList.add(bomb);
                    new Thread(bomb).start();
                }
                break;
            //当坦克为东西朝向时
            case 1:
            case 3:
                if ((bullet.getX() > tank.getX()) && (bullet.getX() < tank.getX() + 70) && (bullet.getY() > tank.getY()) && (bullet.getY() < tank.getY() + 50)) {
                    bullet.setLive(false);
                    tank.setLive(false);
                    Bomb bomb = new Bomb(tank.getX(), tank.getY());
                    bombList.add(bomb);
                    new Thread(bomb).start();
                }
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        //如果友方坦克已经爆炸了，则友方坦克不能再进行操作了
        if (!hero.isLive()) {
            return;
        }
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
        //按下J键时，坦克发射子弹，场上友方子弹最多存在五个
        if (e.getKeyCode() == KeyEvent.VK_J) {
            if (hero.getBulletList().size() < 5) {
                hero.shotBullet();
            }
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
            //判断子弹是否敌对方坦克，因为要实时判断所以需要放在run方法的while循环中不停执行
            hitTank(hero, enemyTankList);
            //需要不断绘制地图，来更新战场情况
            this.repaint();
        }
    }
}
