package tankGame;

import java.io.*;
import java.util.Vector;

/**
 * @author SongDongJie
 * @create 2023/5/28 - 22:25
 * 该类用于记录文件信息，以及文件交互
 */
public class Recorder {
    //记录消灭敌方坦克的数量
    private static int destroyEnemyNum;
    //记录友方坦克
    private static Hero hero = null;
    //记录敌方坦克
    private static Vector<EnemyTank> enemyTanks = null;
    //游戏信息文件路径
    private final static String filePath = "./resource/gameMsg/gameMsg.dat";
    //输出流
    private static ObjectOutputStream oos = null;
    //写入流
    private static ObjectInputStream ois = null;

    public static int getDestroyEnemyNum() {
        return destroyEnemyNum;
    }

    public static void setDestroyEnemyNum(int destroyEnemyNum) {
        Recorder.destroyEnemyNum = destroyEnemyNum;
    }

    public static Hero getHero() {
        return hero;
    }

    public static void setHero(Hero hero) {
        Recorder.hero = hero;
    }

    public static Vector<EnemyTank> getEnemyTanks() {
        return enemyTanks;
    }

    public static void setEnemyTanks(Vector<EnemyTank> enemyTanks) {
        Recorder.enemyTanks = enemyTanks;
    }

    /**
     * 消灭敌方坦克时，增加消灭敌方坦克的数量
     */
    public static void destroyEnemyNumAdd() {
        destroyEnemyNum++;
    }

    /**
     * 保存当前的游戏信息
     */
    public static void storeMsg() {
        try {
            oos = new ObjectOutputStream(new FileOutputStream(filePath));
            //记录消灭敌方坦克的数量
            oos.writeInt(destroyEnemyNum);
            //记录友方坦克
            oos.writeObject(hero);
            //记录敌方坦克
            for (EnemyTank enemyTank : enemyTanks) {
                oos.writeObject(enemyTank);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 加载上次的游戏信息
     *
     * @return 返回上局游戏数据是否为空，有数据返回true，否则返回false
     */
    public static boolean loadMsg() {
        File file = new File(filePath);
        //如果指定路径的文件不存在，则创建一个新文件
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
        try {
            enemyTanks = new Vector<>();
            ois = new ObjectInputStream(new FileInputStream(filePath));
            //读取的顺序要与写入的顺序一致
            //读取消灭敌方坦克的数量
            destroyEnemyNum = ois.readInt();
            //读取友方坦克
            hero = (Hero) ois.readObject();
            //读取敌方坦克
            for (int i = 0; i < Map.getEnemyTankNumber() - destroyEnemyNum; i++) {
                enemyTanks.add((EnemyTank) ois.readObject());
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                ois.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * 清空游戏数据
     */
    public static void deleteMsg() {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }
}
