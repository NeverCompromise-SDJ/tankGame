import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

class Server {
    public static void main(String[] args) throws IOException {
        //根据端口号创建一个ServerSocket对象
        ServerSocket serverSocket = new ServerSocket(10000);
        //监听客户端的连接请求，连接成功后返回Socket对象。
        Socket socket = serverSocket.accept();
        System.out.println("与客户端成功建立连接");
        //根据Socket对象创建对应的InputStream 输入流，通过输入流接收客户端发送的信息。
        InputStream inputStream = socket.getInputStream();
        //从输入流中读取客户端发送的信息
        int readLen;
        byte[] content = new byte[1024];
        while ((readLen = inputStream.read(content)) != -1) {
            System.out.println(new String(content, 0, readLen));
        }
        //关闭输入流及socket、serverSocket，释放资源
        serverSocket.close();
        socket.close();
        inputStream.close();
    }
}