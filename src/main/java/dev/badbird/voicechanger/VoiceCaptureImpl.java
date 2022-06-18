package dev.badbird.voicechanger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

public class VoiceCaptureImpl implements Runnable, VoiceCapture {
    private AudioInputStream audioInputStream;
    private AudioFormat format;
    public Thread thread;
    private double duration;

    public VoiceCaptureImpl() {
        super();
    }

    public VoiceCaptureImpl(AudioFormat format) {
        this.format = format;
    }

    public VoiceCaptureImpl build(AudioFormat format) {
        this.format = format;
        return this;
    }

    @Override
    public void start() {
        thread = new Thread(this,"Voice Capture Thread");
        thread.start();
    }

    @Override
    public void stop() {
        thread = null;
    }

    @Override
    public void run() {
        duration = 0;

        try (final ByteArrayOutputStream out = new ByteArrayOutputStream(); final TargetDataLine line = getTargetDataLineForRecord();) {
            int frameSizeInBytes = format.getFrameSize();
            int bufferLengthInFrames = line.getBufferSize() / 8;
            final int bufferLengthInBytes = bufferLengthInFrames * frameSizeInBytes;
            buildByteOutputStream(out, line, frameSizeInBytes, bufferLengthInBytes);
            this.audioInputStream = new AudioInputStream(line);
            setAudioInputStream(convertToAudioIStream(out, frameSizeInBytes));
            audioInputStream.reset();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
    }

    @Override
    public void buildByteOutputStream(final ByteArrayOutputStream out, final TargetDataLine line, int frameSizeInBytes, final int bufferLengthInBytes) throws IOException {
        final byte[] data = new byte[bufferLengthInBytes];
        int numBytesRead;

        line.start();
        while (thread != null) {
            if ((numBytesRead = line.read(data, 0, bufferLengthInBytes)) == -1) {
                break;
            }
            out.write(data, 0, numBytesRead);
        }
    }

    @Override
    public void setAudioInputStream(AudioInputStream aStream) {
        this.audioInputStream = aStream;
    }

    @Override
    public AudioInputStream convertToAudioIStream(final ByteArrayOutputStream out, int frameSizeInBytes) {
        byte audioBytes[] = out.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(audioBytes);
        AudioInputStream audioStream = new AudioInputStream(bais, format, audioBytes.length / frameSizeInBytes);
        long milliseconds = (long) ((audioInputStream.getFrameLength() * 1000) / format.getFrameRate());
        duration = milliseconds / 1000.0;
        System.out.println("Recorded duration in seconds:" + duration);
        return audioStream;
    }

    @Override
    public TargetDataLine getTargetDataLineForRecord() {
        TargetDataLine line;
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        if (!AudioSystem.isLineSupported(info)) {
            return null;
        }
        try {
            line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format, line.getBufferSize());
        } catch (final Exception ex) {
            return null;
        }
        return line;
    }

    @Override
    public AudioInputStream getAudioInputStream() {
        return audioInputStream;
    }

    @Override
    public AudioFormat getFormat() {
        return format;
    }

    @Override
    public void setFormat(AudioFormat format) {
        this.format = format;
    }

    @Override
    public Thread getThread() {
        return thread;
    }

    @Override
    public double getDuration() {
        return duration;
    }
}
