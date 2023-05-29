package tankGame;

import java.io.Serializable;
import java.util.Vector;

/**
 * @author SongDongJie
 * @create 2023/4/16 - 22:55
 * 坦克类
 */
class Tank implements Serializable {
    private int x;//坦克的x坐标
    private int y;//坦克的y坐标
    private int direction;//坦克的朝向，0123分别对应上右下左
    private int speed;//坦克的速度
    private boolean isEnemy;//是否为敌人，true是敌人
    private boolean isLive = true;//是否还存活，true为存活
    //存放子弹的集合，可以让一个坦克在地图上拥有多颗子弹
    private Vector<Bullet> bulletList = new Vector<>();

    public Tank(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public boolean isEnemy() {
        return isEnemy;
    }

    public void setEnemy(boolean enemy) {
        isEnemy = enemy;
    }

    public boolean isLive() {
        return isLive;
    }

    public void setLive(boolean live) {
        isLive = live;
    }

    public Vector<Bullet> getBulletList() {
        return bulletList;
    }

    /**
     * 首先保存移动前的坦克位置坐标，然后根据坦克方向和速度计算移动后的位置坐标，
     * 如果移动后的位置超出了地图边界则直接返回，否则更新坦克的位置坐标。
     */

    public void move() {
        int moveBeforeX = x;
        int moveBeforeY = y;
        //移动的时候，需要保证坦克的四条边界都不超过地图边界。因此需要将坦克本身的长宽计算进去，坦克南北方向和东西的判断是不同的
        switch (direction) {
            case 0:
                moveBeforeY -= speed;
                if (moveBeforeX + 50 > Map.getWidthOfMap() || moveBeforeX < 0 || moveBeforeY + 70 > Map.getHeightOfMap() || moveBeforeY < 0) {
                    return;
                }
                break;
            case 1:
                moveBeforeX += speed;
                if (moveBeforeX + 70 > Map.getWidthOfMap() || moveBeforeX < 0 || moveBeforeY + 50 > Map.getHeightOfMap() || moveBeforeY < 0) {
                    return;
                }
                break;
            case 2:
                moveBeforeY += speed;
                if (moveBeforeX + 50 > Map.getWidthOfMap() || moveBeforeX < 0 || moveBeforeY + 70 > Map.getHeightOfMap() || moveBeforeY < 0) {
                    return;
                }
                break;
            case 3:
                moveBeforeX -= speed;
                if (moveBeforeX + 70 > Map.getWidthOfMap() || moveBeforeX < 0 || moveBeforeY + 50 > Map.getHeightOfMap() || moveBeforeY < 0) {
                    return;
                }
                break;
        }

        x = moveBeforeX;
        y = moveBeforeY;
    }

    /*
        坦克发射子弹
    */
    public void shotBullet() {
        //发射子弹时，子弹的方向和坦克的方向保持一致，子弹的坐标由坦克的坐标、方向决定。0123分别为上右下左
        //创建子弹并初始化子弹位置
        Bullet bullet = null;
        switch (getDirection()) {
            case 0:
                bullet = new Bullet(getX() + 25, getY(), getDirection());
                break;
            case 1:
                bullet = new Bullet(getX() + 70, getY() + 25, getDirection());
                break;
            case 2:
                bullet = new Bullet(getX() + 25, getY() + 70, getDirection());
                break;
            case 3:
                bullet = new Bullet(getX(), getY() + 25, getDirection());
                break;
        }
        //子弹发射
        new Thread(bullet).start();
        //将该子弹添加到该坦克的子弹集合中
        bulletList.add(bullet);

    }
}
