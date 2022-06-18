package dev.badbird.voicechanger;

import org.urish.openal.ALException;
import org.urish.openal.OpenAL;
import org.urish.openal.Source;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

public class VoiceChanger implements Runnable{
    private VoiceCapture capture;
    private Thread thread;
    public VoiceChanger(VoiceCapture capture) {
        this.capture = capture;
        this.thread = new Thread(this,"Voice Changer Thread");
        this.thread.start();
    }

    @Override
    public void run() {
        try {
            OpenAL openal = new OpenAL();
            Source source = openal.createSource(capture.getAudioInputStream());
            source.play();
        } catch (ALException | IOException | UnsupportedAudioFileException e) {
            throw new RuntimeException(e);
        }
    }
}
