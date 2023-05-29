package tankGame;


/**
 * 该类对象在坦克被击中时创建。然后播放爆炸动画。
 *
 * @author SongDongJie
 * @create 2023/5/14 - 0:31
 */
public class Bomb implements Runnable {
    //爆炸图片的左上角x坐标
    private int x;
    //爆炸图片的左上角y坐标
    private int y;
    //爆炸图片的生命周期，不同生命周期段显示不同的爆炸效果。1为爆炸开始，2为爆炸中期，3为爆炸末期。开始-末期，爆炸特效逐渐变小。
    private int lifePeriod = 1;

    public Bomb(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getLifePeriod() {
        return lifePeriod;
    }

    /**
     * 为了保持爆炸的效果，并且在绘制坦克爆炸时，不会由于阻塞Map线程，使得爆炸时整个画面除了坦克爆炸其他地图上的对象都会卡住。
     * 因此需要将bomb对象单独做成一个线程
     */
    @Override
    public void run() {
//        System.out.println("bomb线程开始");
        //当爆炸生命周期>3，即爆炸结束时，bomb线程也结束
        while (lifePeriod <= 3) {
            //每隔300毫秒将爆炸的生命周期+1，也就是每隔300毫秒更换爆炸的效果
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lifePeriod++;
        }
//        System.out.println("bomb线程结束");   //测试爆炸结束后，bomb线程是否结束
    }
}
