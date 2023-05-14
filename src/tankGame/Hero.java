package tankGame;

/**
 * @author SongDongJie
 * @create 2023/4/16 - 22:55
 * 友方坦克类
 */
class Hero extends Tank {

    public Hero(int x, int y) {
        super(x, y);
        //初始化hero对象不是敌方坦克
        setEnemy(false);
    }
}
