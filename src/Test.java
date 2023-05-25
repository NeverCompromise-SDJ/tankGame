import java.io.*;
import java.util.Properties;

class Test {
    public static void main(String[] args) throws IOException {
        Properties properties = new Properties();
        //将FileInputStream流指定的配置文件的键值对，加载到Properties对象中
//        properties.load(new FileInputStream("./testFolder/test.properties"));

        //将FileReader流指定的配置文件的键值对，加载到Properties对象中
        properties.load(new FileReader("./testFolder/test.properties"));

        //将Properties对象中保存的键值对通过标准输出流的println方法输出到显示器。
        properties.list(System.out);

        //将Properties对象中保存的键值对通过PrintWriter流的println方法输出到显示器。(因为PrintWriter包装了标准输出流，所以底层调用的还是标准
        // 输出流的println方法)。
//        PrintWriter printWriter = new PrintWriter(System.out);
//        properties.list(printWriter);
//        如果之后还有内容输出到显示器上，就不要关闭标准输出流
//        printWriter.close();

        //得到key=user的value,不存在则返回null
        System.out.println(properties.getProperty("user"));
        //得到key=haha的value,不存在则返回指定的默认值
//        System.out.println(properties.getProperty("haha", "no exist"));
        //修改key=user1的value，key不存在则新增该key-value对
        System.out.println(properties.setProperty("user", "sdj5哈"));

        //将Properties中的键值对存储到OutPutStream流指定的配置文件中。
//        properties.store(new FileOutputStream("./testFolder/test.properties"), "进行存储");
        //将Properties中的键值对存储到Writer流指定的配置文件中，并不写注释。
        properties.store(new FileWriter("./testFolder/test.properties"), null);
    }
}