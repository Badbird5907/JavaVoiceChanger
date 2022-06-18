package dev.badbird.voicechanger;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.TargetDataLine;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public interface VoiceCapture {
    void start();

    void stop();

    void buildByteOutputStream(final ByteArrayOutputStream out, final TargetDataLine line, int frameSizeInBytes, final int bufferLengthInBytes) throws IOException;

    AudioInputStream convertToAudioIStream(final ByteArrayOutputStream out, int frameSizeInBytes);

    TargetDataLine getTargetDataLineForRecord();

    AudioInputStream getAudioInputStream();

    AudioFormat getFormat();

    void setAudioInputStream(AudioInputStream audioInputStream);

    void setFormat(AudioFormat format);

    double getDuration();

    Thread getThread();
}
