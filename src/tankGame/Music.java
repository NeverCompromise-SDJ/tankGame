package tankGame;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * 声音类，用于播放开局音乐
 */

class Music implements Runnable {
    //音乐的路径
    private String filePath;

    public Music(String filePath) {
        this.filePath = filePath;
    }

    public void run() {
        File musicFile = new File(filePath);

        AudioInputStream audioInputStream;

        try {
            //根据file对象创建一个AudioInputStream流
            audioInputStream = AudioSystem.getAudioInputStream(musicFile);
        } catch (UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
            return;
        }

        //AudioInputStream流返回一个AudioFormat对象
        AudioFormat audioFormat = audioInputStream.getFormat();
        SourceDataLine sdl = null;
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);

        try {
            sdl = (SourceDataLine) AudioSystem.getLine(info);
            sdl.open(audioFormat);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        sdl.start();
        int nBytesRead = 0;
        //缓冲
        byte[] abData = new byte[1024];
        try {
            while (nBytesRead != -1) {
                nBytesRead = audioInputStream.read(abData, 0, abData.length);
                if (nBytesRead >= 0) {
                    sdl.write(abData, 0, nBytesRead);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        } finally {
            sdl.drain();
            sdl.close();
        }
    }
}