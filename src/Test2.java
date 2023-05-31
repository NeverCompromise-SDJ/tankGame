import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

class Client {
    public static void main(String[] args) throws IOException {
        //根据InetAddress对象和端口号创建一个Socket对象，并与服务端建立连接。
        Socket socket = new Socket(InetAddress.getLocalHost(), 10000);
        System.out.println("与服务端成功建立连接");
        //根据Socket对象创建对应的OutputStream 输出流，通过输出流向服务端发送数据。
        OutputStream outputStream = socket.getOutputStream();
        //向服务器发送数据
        outputStream.write("hello".getBytes());
        //关闭socket和相关的流，释放资源
        socket.close();
        outputStream.close();
    }
}
