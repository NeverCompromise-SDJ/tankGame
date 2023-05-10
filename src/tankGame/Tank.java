package tankGame;

import java.util.Vector;

/**
 * @author SongDongJie
 * @create 2023/4/16 - 22:55
 * 坦克类
 */
class Tank {
    private int x;//坦克的x坐标
    private int y;//坦克的y坐标
    private int direction;//坦克的朝向，0123分别对应上右下左
    private int speed;//坦克的速度
    private boolean isEnemy;//是否为敌人
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

    public Vector<Bullet> getBulletList() {
        return bulletList;
    }


    //根据当前的方向来移动
    public void move() {
        switch (direction) {
            case 0:
                y -= speed;
                break;
            case 1:
                x += speed;
                break;
            case 2:
                y += speed;
                break;
            case 3:
                x -= speed;
                break;
        }
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
