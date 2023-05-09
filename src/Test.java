class Test {
    public static void main(String[] args) throws InterruptedException {
        Account account = new Account(1000000);
        Thread thread1 = new Thread(account);
        Thread thread2 = new Thread(account);
        thread1.start();
        thread2.start();
    }
}

class Account implements Runnable {
    private double money;

    public Account(double money) {
        this.money = money;
    }

    public void takeMoney() {
        while (true) {
            synchronized (this) {
                if ((money - 1000) < 0) {
                    System.out.println("账户余额不足");
                    break;
                }
                money -= 1000;
                System.out.println(Thread.currentThread().getName() + "取了1000元，还剩：" + money + "元");
            }
        }
    }

    @Override
    public void run() {
        takeMoney();
        System.out.println(Thread.currentThread().getName() + "退出");
        System.out.println(money);
    }
}