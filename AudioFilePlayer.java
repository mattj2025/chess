import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine.Info;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import static javax.sound.sampled.AudioSystem.getAudioInputStream;
import static javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED;

public class AudioFilePlayer {

    private volatile boolean fadeOutRequested = false;
    private static final int FADE_OUT_DURATION_MS = 7000;
    private boolean cancel = false;

    public void play(String filePath) 
    {
        if (Main.muted)
            return;

        Thread playThread = new Thread(() -> {

            final File file = new File(filePath);

            try (final AudioInputStream in = getAudioInputStream(file)) {

                final AudioFormat outFormat = getOutFormat(in.getFormat());
                final Info info = new Info(SourceDataLine.class, outFormat);

                try (final SourceDataLine line =
                            (SourceDataLine) AudioSystem.getLine(info)) {

                    if (line != null) {
                        line.open(outFormat);
                        line.start();
                        stream(getAudioInputStream(outFormat, in), line);
                        line.drain();
                        line.stop();
                    }
                }

            } catch (UnsupportedAudioFileException
                    | LineUnavailableException
                    | IOException e) {
                throw new IllegalStateException(e);
            }
        });

        playThread.start();
        fadeOutRequested = false;
    }

    private AudioFormat getOutFormat(AudioFormat inFormat) {
        final int ch = inFormat.getChannels();
        final float rate = inFormat.getSampleRate();
        return new AudioFormat(PCM_SIGNED, rate, 16, ch, ch * 2, rate, false);
    }

    private void stream(AudioInputStream in, SourceDataLine line) 
        throws IOException {
        final byte[] buffer = new byte[4096];
        long totalFramesRead = 0;
        long fadeOutStartFrame = 0;
        long fadeOutDurationFrames = (long)(FADE_OUT_DURATION_MS / 1000.0 * line.getFormat().getFrameRate());
        boolean fadingOut = false;

        for (int n = in.read(buffer, 0, buffer.length); n != -1; n = in.read(buffer, 0, buffer.length)) {
            if (fadeOutRequested && !fadingOut) {
                // Start fading out
                fadeOutStartFrame = totalFramesRead;
                fadingOut = true;
            }

            if (fadingOut) {
                int bytesPerFrame = line.getFormat().getFrameSize();
                for (int i = 0; i < n; i += bytesPerFrame) {
                    long currentFrame = totalFramesRead + (i / bytesPerFrame);
                    double fadeFactor;
                    if (currentFrame >= fadeOutStartFrame) {
                        fadeFactor = 1.0 - (double)(currentFrame - fadeOutStartFrame) / fadeOutDurationFrames;
                        fadeFactor = Math.max(0.0, fadeFactor);
                        applyFade(buffer, i, bytesPerFrame, fadeFactor);
                    }
                }
            }

            if (cancel)
            {
                cancel = false;
                return;
            }

            line.write(buffer, 0, n);
            totalFramesRead += n / line.getFormat().getFrameSize();
        }
    }

    private void applyFade(byte[] buffer, int start, int bytesPerFrame, double fadeFactor) {
        for (int i = 0; i < bytesPerFrame; i += 2) {
            int sample = (buffer[start + i + 1] << 8) | (buffer[start + i] & 0xff);
            sample = (int)(sample * fadeFactor);
            buffer[start + i] = (byte)(sample & 0xff);
            buffer[start + i + 1] = (byte)((sample >> 8) & 0xff);
        }
    }

    public void requestFadeOut() {
        fadeOutRequested = true;
    }

    public void cancel() {
        cancel = true;
    }
}