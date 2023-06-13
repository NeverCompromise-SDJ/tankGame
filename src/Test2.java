import java.io.*;
import java.net.*;
import java.util.Scanner;

class Client {
    public static void main(String[] args) throws IOException {
        //向服务端发送请求
        Socket socket = new Socket(InetAddress.getLocalHost(), 9999);
        OutputStream outputStream = socket.getOutputStream();
        String needFileName = new Scanner(System.in).next();
        outputStream.write(needFileName.getBytes());
        socket.shutdownOutput();

        //读取服务端发送的文件
        InputStream inputStream = socket.getInputStream();
        int readLen;
        byte[] readContentOfBytes = new byte[1024];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        while ((readLen = inputStream.read(readContentOfBytes)) != -1) {
            byteArrayOutputStream.write(readContentOfBytes, 0, readLen);
        }
        byte[] file = byteArrayOutputStream.toByteArray();


        //将读取的文件下载到本地
        String filePath;
        if (needFileName.equals("pic")) {
            filePath = "./newPic.webp";
        } else {
            filePath = "./newTest.txt";
        }
        FileOutputStream fileOutputStream = new FileOutputStream(filePath);
        fileOutputStream.write(file);

        //释放资源
        outputStream.close();
        inputStream.close();
        byteArrayOutputStream.close();
        fileOutputStream.close();
        socket.close();
    }
}
