package dev.badbird.voicechanger;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;

public class App {
    public static void main(String[] args) throws Exception {
        AudioFormat format = buildAudioFormatInstance();

        VoiceCaptureImpl voiceCapture = new VoiceCaptureImpl();
        voiceCapture.build(format);

        System.out.println("Start recording ....");
        voiceCapture.start();
        Thread.sleep(20000);
        voiceCapture.stop();

        WaveDataUtil wd = new WaveDataUtil();
        Thread.sleep(3000);
        wd.saveToFile("/SoundClip", AudioFileFormat.Type.WAVE, voiceCapture.getAudioInputStream());
    }

    public static AudioFormat buildAudioFormatInstance() {
        ApplicationProperties aConstants = new ApplicationProperties();
        AudioFormat.Encoding encoding = aConstants.ENCODING;
        float rate = aConstants.RATE;
        int channels = aConstants.CHANNELS;
        int sampleSize = aConstants.SAMPLE_SIZE;
        boolean bigEndian = aConstants.BIG_ENDIAN;

        return new AudioFormat(encoding, rate, sampleSize, channels, (sampleSize / 8) * channels, rate, bigEndian);
    }
}
