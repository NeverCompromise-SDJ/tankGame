import java.io.*;
import java.net.*;

class Server {
    public static void main(String[] args) throws IOException {
        //读客户端发送的文件请求
        ServerSocket serverSocket = new ServerSocket(9999);
        Socket socket = serverSocket.accept();
        InputStream inputStream = socket.getInputStream();
        int readLen;
        byte[] readContentOfBytes = new byte[1024];
        String readContentOfString = "";
        while ((readLen = inputStream.read(readContentOfBytes)) != -1) {
            readContentOfString += new String(readContentOfBytes, 0, readLen);
        }

        //根据客户端的所要，拿到对应的文件
        String responseFilePath;
        if (readContentOfString.equals("pic")) {
            responseFilePath = "./testFolder/pic.webp";
        } else {
            responseFilePath = "./testFolder/test.txt";
        }

        FileInputStream fileInputStream = new FileInputStream(responseFilePath);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int writeLenOfFile;
        byte[] writeContentOfBytes = new byte[1024];
        while ((writeLenOfFile = fileInputStream.read(writeContentOfBytes)) != -1) {
            byteArrayOutputStream.write(writeContentOfBytes, 0, writeLenOfFile);
        }
        byte[] file = byteArrayOutputStream.toByteArray();

        //给客户端发送文件
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write(file);
        socket.shutdownOutput();

        //释放资源
        inputStream.close();
        outputStream.close();
        byteArrayOutputStream.close();
        fileInputStream.close();
        serverSocket.close();
        socket.close();
    }
}