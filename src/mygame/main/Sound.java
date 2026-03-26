package mygame.main;

import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound {

    private Clip clip;
    private URL soundURL;

    public void setFile(String path) {
        try {
            if (clip != null) {
                clip.stop();
                clip.close();
            }

            soundURL = getClass().getResource(path);

            if (soundURL == null) {
                System.out.println("Khong tim thấy file am thanh: " + path);
                clip = null;
                return;
            }

            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL);
            clip = AudioSystem.getClip();
            clip.open(ais);

            System.out.println("Da duyet am thanh: " + path);

        } catch (Exception e) {
            System.out.println("Loi chay am thanh: " + path);
            e.printStackTrace();
            clip = null;
        }
    }

    public void play() {
        try {
            if (clip != null) {
                clip.stop();
                clip.setFramePosition(0);
                clip.start();
                System.out.println("Dang phat am thanh...");
            } else {
                System.out.println("Clip đang null, khong phat duoc.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loop() {
        try {
            if (clip != null) {
                clip.stop();
                clip.setFramePosition(0);
                clip.loop(Clip.LOOP_CONTINUOUSLY);
                System.out.println("Dang lap am thanh...");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        try {
            if (clip != null && clip.isRunning()) {
                clip.stop();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reset() {
        try {
            if (clip != null) {
                clip.setFramePosition(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isLoaded() {
        return clip != null;
    }

    public boolean isRunning() {
        return clip != null && clip.isRunning();
    }
}