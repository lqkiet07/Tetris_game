package main;

import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineEvent.Type;
import javax.sound.sampled.LineListener;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author smth
 */
public class Sound {

    Clip musicClip;
    URL url[] = new URL[10];

    public Sound() {

        url[0] = getClass().getResource("/delete line.wav");
        url[1] = getClass().getResource("/rotation.wav");
        url[2] = getClass().getResource("/gameover.wav");
        url[3] = getClass().getResource("/touch floor.wav");
        url[4] = getClass().getResource("/Original Tetris theme (Tetris Soundtrack).wav");
    }

    public void play(int i, boolean music) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(url[i]);
            Clip clip = AudioSystem.getClip();

            if (music) {
                musicClip = clip;
            }

            clip.open(ais);
            clip.addLineListener(new LineListener() {
                @Override
                public void update(LineEvent event) {
                    if (event.getType() == Type.STOP) {
                        clip.close();
                    }
                }
            });
            ais.close();

            // obj set volume before playing
            if (music) {
                setVolume(clip, 0.1f); // obj music background volume: 0.0f (mute) -> 1.0f (max)
            }

            clip.start();
        } catch (Exception e) {

        }
    }

    // obj convert 0.0-1.0 linear scale to dB gain and apply to clip
    public void setVolume(Clip clip, float volume) {
        try {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            // obj formula: dB = 20 * log10(volume)
            float dB = (float) (Math.log10(Math.max(volume, 0.0001f)) * 20.0);
            // obj clamp to supported range
            dB = Math.max(gainControl.getMinimum(), Math.min(dB, gainControl.getMaximum()));
            gainControl.setValue(dB);
        } catch (Exception e) {

        }
    }

    public void loop() {
        if (musicClip != null) {
            musicClip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void stop() {
        if (musicClip != null) {
            musicClip.stop();
            musicClip.close();
        }
    }
}
