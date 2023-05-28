package tankGame;

import com.sun.jndi.ldap.Ber;

import java.text.BreakIterator;
import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

/**
 * @author SongDongJie
 * @create 2023/4/19 - 23:38
 * 敌人坦克类
 */
public class EnemyTank extends Tank implements Runnable {
    //地图上存在的所有敌方坦克
    private Vector<EnemyTank> enemyTanks;

    public EnemyTank(int x, int y) {
        super(x, y);
        //初始化EnemyTank对象是敌方坦克
        setEnemy(true);
    }

    /**
     * 将场上所有的敌方坦克作为敌方坦克对象的属性
     *
     * @param enemyTanks 地图上存在的所有敌方坦克
     */
    public void setEnemyTanks(Vector<EnemyTank> enemyTanks) {
        this.enemyTanks = enemyTanks;
    }


    /**
     * 防止敌方坦克重叠思路：以每一个敌方坦克本身作为参照坦克，根据参照坦克的不同方向来分成八种情况。
     * 如果参照坦克将要进入其他坦克的区域，就停止参照坦克向此方向移动。
     *
     * @return 重合了就返回true，没有重合返回false
     */
    public boolean isTouch() {
        for (EnemyTank enemyTank : enemyTanks) {
            //如果遍历到了参考坦克，则无需判断，跳过
            if (this == enemyTank) {
                continue;
            }
            //以本坦克为参照，根据参照坦克的四个方向来分成八种情况。
            switch (getDirection()) {
                //当参照坦克为北朝向时。参照坦克的北边（即左上角和右上角）不得与其他坦克的区域重合。
                case 0:
                    //情况1，其他坦克为分别为南北朝向时
                    if (enemyTank.getDirection() == 0 || enemyTank.getDirection() == 2) {
                        //参照坦克的左上角不得与其他坦克的区域重合
                        if (this.getX() >= enemyTank.getX() && this.getX() <= enemyTank.getX() + 50 &&
                                this.getY() >= enemyTank.getY() && this.getY() <= enemyTank.getY() + 70) {
                            return true;
                        }
                        //参照坦克的右上角不得与其他坦克的区域重合
                        if (this.getX() + 50 >= enemyTank.getX() && this.getX() + 50 <= enemyTank.getX() + 50 &&
                                this.getY() >= enemyTank.getY() && this.getY() <= enemyTank.getY() + 70) {
                            return true;
                        }
                    } //情况2，其他坦克为分别为东西朝向时
                    else if (enemyTank.getDirection() == 1 || enemyTank.getDirection() == 3) {
                        //参照坦克的左上角不得与其他坦克的区域重合
                        if (this.getX() >= enemyTank.getX() && this.getX() <= enemyTank.getX() + 70 &&
                                this.getY() >= enemyTank.getY() && this.getY() <= enemyTank.getY() + 50) {
                            return true;
                        }
                        //参照坦克的右上角不得与其他坦克的区域重合
                        if (this.getX() + 50 >= enemyTank.getX() && this.getX() + 50 <= enemyTank.getX() + 70 &&
                                this.getY() >= enemyTank.getY() && this.getY() <= enemyTank.getY() + 50) {
                            return true;
                        }
                    }
                    break;
                //当参照坦克为东朝向时。参照坦克的东边（即右上角和右下角）不得与其他坦克的区域重合。
                case 1:
                    //情况3，其他坦克为分别为南北朝向时
                    if (enemyTank.getDirection() == 0 || enemyTank.getDirection() == 2) {
                        //参照坦克的右上角不得与其他坦克的区域重合
                        if (this.getX() + 70 >= enemyTank.getX() && this.getX() + 70 <= enemyTank.getX() + 50 &&
                                this.getY() >= enemyTank.getY() && this.getY() <= enemyTank.getY() + 70) {
                            return true;
                        }
                        //参照坦克的右下角不得与其他坦克的区域重合
                        if (this.getX() + 70 >= enemyTank.getX() && this.getX() + 70 <= enemyTank.getX() + 50 &&
                                this.getY() + 50 >= enemyTank.getY() && this.getY() + 50 <= enemyTank.getY() + 70) {
                            return true;
                        }
                    } //情况4，其他坦克为分别为东西朝向时
                    else if (enemyTank.getDirection() == 1 || enemyTank.getDirection() == 3) {
                        //参照坦克的右上角不得与其他坦克的区域重合
                        if (this.getX() + 70 >= enemyTank.getX() && this.getX() + 70 <= enemyTank.getX() + 70 &&
                                this.getY() >= enemyTank.getY() && this.getY() <= enemyTank.getY() + 50) {
                            return true;
                        }
                        //参照坦克的右下角不得与其他坦克的区域重合
                        if (this.getX() + 70 >= enemyTank.getX() && this.getX() + 70 <= enemyTank.getX() + 70 &&
                                this.getY() + 50 >= enemyTank.getY() && this.getY() + 50 <= enemyTank.getY() + 50) {
                            return true;
                        }
                    }
                    break;
                //当参照坦克为南朝向时。参照坦克的南边（即左上角和右下角）不得与其他坦克的区域重合。
                case 2:
                    //情况5，其他坦克为分别为南北朝向时
                    if (enemyTank.getDirection() == 0 || enemyTank.getDirection() == 2) {
                        //参照坦克的左下角不得与其他坦克的区域重合
                        if (this.getX() >= enemyTank.getX() && this.getX() <= enemyTank.getX() + 50 &&
                                this.getY() + 70 >= enemyTank.getY() && this.getY() + 70 <= enemyTank.getY() + 70) {
                            return true;
                        }
                        //参照坦克的右下角不得与其他坦克的区域重合
                        if (this.getX() + 50 >= enemyTank.getX() && this.getX() + 50 <= enemyTank.getX() + 50 &&
                                this.getY() + 70 >= enemyTank.getY() && this.getY() + 70 <= enemyTank.getY() + 70) {
                            return true;
                        }
                    } //情况6，其他坦克为分别为东西朝向时
                    else if (enemyTank.getDirection() == 1 || enemyTank.getDirection() == 3) {
                        //参照坦克的左下角不得与其他坦克的区域重合
                        if (this.getX() >= enemyTank.getX() && this.getX() <= enemyTank.getX() + 70 &&
                                this.getY() + 70 >= enemyTank.getY() && this.getY() + 70 <= enemyTank.getY() + 50) {
                            return true;
                        }
                        //参照坦克的右下角不得与其他坦克的区域重合
                        if (this.getX() + 50 >= enemyTank.getX() && this.getX() + 50 <= enemyTank.getX() + 70 &&
                                this.getY() + 70 >= enemyTank.getY() && this.getY() + 70 <= enemyTank.getY() + 50) {
                            return true;
                        }
                    }
                    break;
                //当参照坦克为西朝向时。参照坦克的西边（即左上角和左下角）不得与其他坦克的区域重合。
                case 3:
                    //情况7，其他坦克为分别为南北朝向时
                    if (enemyTank.getDirection() == 0 || enemyTank.getDirection() == 2) {
                        //参照坦克的左上角不得与其他坦克的区域重合
                        if (this.getX() >= enemyTank.getX() && this.getX() <= enemyTank.getX() + 50 &&
                                this.getY() >= enemyTank.getY() && this.getY() <= enemyTank.getY() + 70) {
                            return true;
                        }
                        //参照坦克的左下角不得与其他坦克的区域重合
                        if (this.getX() >= enemyTank.getX() && this.getX() <= enemyTank.getX() + 50 &&
                                this.getY() + 70 >= enemyTank.getY() && this.getY() + 70 <= enemyTank.getY() + 70) {
                            return true;
                        }
                    } //情况8，其他坦克为分别为东西朝向时
                    else if (enemyTank.getDirection() == 1 || enemyTank.getDirection() == 3) {
                        //参照坦克的左上角不得与其他坦克的区域重合
                        if (this.getX() >= enemyTank.getX() && this.getX() <= enemyTank.getX() + 70 &&
                                this.getY() >= enemyTank.getY() && this.getY() <= enemyTank.getY() + 50) {
                            return true;
                        }
                        //参照坦克的左下角不得与其他坦克的区域重合
                        if (this.getX() >= enemyTank.getX() && this.getX() <= enemyTank.getX() + 70 &&
                                this.getY() + 70 >= enemyTank.getY() && this.getY() + 70 <= enemyTank.getY() + 50) {
                            return true;
                        }
                    }
                    break;
            }
        }
        //以上都不符合，则说明该坦克边界与其他坦克都不重合
        return false;
    }

    /**
     * 敌方坦克随机移动
     */
    public void randomMove() {
        int direction = new Random().nextInt(4);
        setDirection(direction);
        //让敌人朝着某个方向走十五个像素后，再重新随机敌人走的方向，这样符合现实情况。
        for (int i = 0; i < 15; i++) {
            try {
                //每隔50ms走一个像素
                Thread.sleep(50);
                //注意，每走一步，都得进行是否碰撞的判断。如果该坦克已经碰到其他坦克了，那么这一步该坦克就不走了。
                //切记，不能放到for循环外，放到for循环外就是每15步判断一次，会造成坦克重合
                if (!isTouch()) {
                    move();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //敌人坦克如果需要自由移动，就需要把敌人坦克做成线程
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + "敌方坦克线程开始");
        //当坦克被摧毁时，坦克线程退出
        while (isLive()) {
            randomMove();
            //每一个敌方坦克在场上存在的子弹数不能超过五颗
            if (getBulletList().size() < 5) {
                shotBullet();
            }
        }
        System.out.println(Thread.currentThread().getName() + "敌方坦克线程结束");
    }
}
