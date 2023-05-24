import java.io.*;

class Test {
    public static void main(String[] args) throws IOException {
        //使用指定的文件地址，创建一个PrintStream流。
        PrintStream printStream = new PrintStream("./testFolder/test.txt");
        //将字符数组的内容写入到文件中
        printStream.write("sdj".getBytes());
        //        将标准输出流输出的目标从显示器重定向为文件
        System.setOut(new PrintStream("./testFolder/test.txt"));
        //        这句话将输出到PrintStream流指定的文件中，而不是显示器上
        System.out.println("123");
        printStream.close();
    }
}