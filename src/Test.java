class Test {
    public static void main(String[] args) {
        TicketSale ticketSale = new TicketSale();
        Thread ticketSale1 = new Thread(ticketSale);
        Thread ticketSale2 = new Thread(ticketSale);
        ticketSale1.start();
        ticketSale2.start();
    }
}

class TicketSale implements Runnable {
    private Object lock1 = new Object();
    private Object lock2 = new Object();

    @Override
    public void run() {
        if (Thread.currentThread().getName().equals("Thread-0")) {
            synchronized (lock1) {
                System.out.println(Thread.currentThread().getName() + "拿到了lock1对象的锁");
                try {
                    Thread.currentThread().wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (lock2) {
                    System.out.println(Thread.currentThread().getName() + "拿到了lock2对象的锁");
                }
            }
        } else {
            synchronized (lock2) {
                System.out.println(Thread.currentThread().getName() + "拿到了lock2对象的锁");
                synchronized (lock1) {
                    System.out.println(Thread.currentThread().getName() + "拿到了lock1对象的锁");
                }
            }
        }
    }
}