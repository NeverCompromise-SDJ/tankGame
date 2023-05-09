package tankGame;

/**
 * @author SongDongJie
 * @create 2023/5/9 - 0:55
 * 子弹类
 */
//为了不断改变子弹的坐标，以及子弹的生命周期，因此需要将子弹做成线程
public class Bullet implements Runnable {
    private int x;//子弹的x坐标
    private int y;//子弹的y坐标
    private int direction;//子弹的朝向，0123分别对应上右下左
    private int speed = 2;//子弹的速度
    private boolean isLive = true;//子弹是否存活（可移动），刚发生的子弹状态默认是可移动的

    public Bullet(int x, int y, int direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isLive() {
        return isLive;
    }

    public void bulletMove() {
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

    //子弹被发射出去
    @Override
    public void run() {
        while (true) {
            try {
                //目的是不让子弹移动过快
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //每50毫秒，子弹移动一次
            bulletMove();
            //如果子弹到达边界，则消失，状态变为不可移动
            if (!(x >= 0 && x <= 1200 && y >= 0 && y <= 700)) {
                isLive = false;
                break;
            }
            System.out.println(x + "  " + y);
        }
    }
//TEST
//    public static void main(String[] args) {
//        new Thread(new Bullet(1, 1, 1)).start();
//    }
}

