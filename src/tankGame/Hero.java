package tankGame;

import com.sun.javafx.scene.traversal.Direction;

/**
 * @author SongDongJie
 * @create 2023/4/16 - 22:55
 * 友方坦克类
 */
class Hero extends Tank {
    Bullet bullet = null;

    public Hero(int x, int y) {
        super(x, y);
    }

    /*
        坦克发射子弹
     */
    public void shotBullet() {
        //发射子弹时，子弹的方向和坦克的方向保持一致，子弹的坐标由坦克的坐标、方向决定。0123分别为上右下左
        //创建子弹并初始化子弹位置
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
    }
}
