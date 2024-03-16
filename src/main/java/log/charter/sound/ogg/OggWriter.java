package log.charter.sound.ogg;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import log.charter.data.config.Config;
import log.charter.io.Logger;
import log.charter.sound.data.AudioDataShort;
import log.charter.sound.wav.WavWriter;

public class OggWriter {

	public static void write(final AudioDataShort musicData, final File output) {
		final File wav = new File(output.getAbsolutePath() + "_tmp_" + System.currentTimeMillis() + ".wav");
		wav.deleteOnExit();

		WavWriter.write(musicData, wav);
		runOggEnc(wav, output);
		wav.delete();
	}

	private static void runOggEnc(final File input, final File output) {
		final String exe = Config.oggEncPath;
		final String inputPath = input.getAbsolutePath();
		final String outputPath = output.getAbsolutePath();

		final String[] cmd = { exe, "--quiet", "-q 10", "-s", "0", inputPath, "--output=" + outputPath };

		try {
			runCmd(cmd);
		} catch (final IOException e) {
			Logger.error("Couldn't run oggenc!", e);
			e.printStackTrace();
		}
	}

	private static void runCmd(final String[] cmd) throws IOException {
		final Process process = Runtime.getRuntime().exec(cmd);
		final InputStream in = process.getInputStream();
		final InputStream err = process.getErrorStream();

		final byte[] buffer = new byte[1024];
		while (process.isAlive()) {
			if (in.available() > 0) {
				in.read(buffer);
			}
			if (err.available() > 0) {
				err.read(buffer);
			}

			try {
				Thread.sleep(1);
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
