package tankGame;

import java.io.*;

/**
 * @author SongDongJie
 * @create 2023/5/28 - 22:25
 * 该类用于记录文件信息，以及文件交互
 */
public class Recorder {
    //记录消灭敌方坦克的数量
    private static int destroyEnemyNum;
    //记录写的文件路径
    private static BufferedWriter bufferedWriter = null;
    private static String filePath = "./resource/gameMsg/gameMsg.txt";

    public static int getDestroyEnemyNum() {
        return destroyEnemyNum;
    }

    public static void setDestroyEnemyNum(int destroyEnemyNum) {
        Recorder.destroyEnemyNum = destroyEnemyNum;
    }

    /**
     * 消灭敌方坦克时，增加消灭敌方坦克的数量
     */
    public static void destroyEnemyNumAdd() {
        destroyEnemyNum++;
    }

    /**
     * 保持当前的游戏信息
     */
    public static void storeMsg() {
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(filePath));
            bufferedWriter.write(destroyEnemyNum);
            bufferedWriter.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
