package tankGame;

/**
 * @author SongDongJie
 * @create 2023/4/16 - 22:55
 */
class Tank {
    private int x;//坦克的x坐标
    private int y;//坦克的y坐标
    private int direction;//坦克的朝向，0123分别对应上右下左
    private int speed;//坦克的速度
    private boolean isEnemy;//是否为敌人

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

    public void move() {
        if (direction == 0) {
            y -= speed;
        } else if (direction == 1) {
            x += speed;
        } else if (direction == 2) {
            y += speed;
        } else if (direction == 3) {
            x -= speed;
        }
    }

}
