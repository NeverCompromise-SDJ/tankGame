package tankGame;

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
     * 将地图上存在的所有敌方坦克作为敌方坦克对象的一个属性。以该坦克作为参照，根据参照坦克的不同方向来分成八种情况。
     * 如果参照坦克与其他坦克的边界相碰，就停止参照坦克向此方向移动。
     *
     * @param enemyTanks 地图上存在的所有敌方坦克
     */
    public void setEnemyTanks(Vector<EnemyTank> enemyTanks) {
        this.enemyTanks = enemyTanks;
    }

    public boolean isTouch() {

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
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            move();
        }
    }

    //敌人坦克如果需要自由移动，就需要把敌人坦克做成线程
    @Override
    public void run() {
        System.out.println("敌方坦克线程开始");
        //当坦克被摧毁时，坦克线程退出
        while (isLive()) {
            randomMove();
            //每一个敌方坦克在场上存在的子弹数不能超过五颗
            if (getBulletList().size() < 5) {
                shotBullet();
            }
        }
        System.out.println("敌方坦克线程结束");
    }
}
