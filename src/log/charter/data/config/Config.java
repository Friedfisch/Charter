package log.charter.data.config;

import static log.charter.io.Logger.error;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;

import log.charter.util.RW;

public class Config {
	private static final String configName = "config.ini";

	public static String language = "English";
	public static String lastPath = "C:/";
	public static String musicPath = System.getProperty("user.home") + File.separator + "Music";
	public static String songsPath = System.getProperty("user.home") + File.separator + "Documents";

	public static int minNoteDistance = 5;
	public static int minTailLength = 30;
	public static int delay = 15;
	public static int markerOffset = 300;

	public static boolean invertStrings = false;
	public static boolean showChordIds = false;
	public static int frets = 28;
	public static int maxStrings = 6;

	public static int windowPosX = 100;
	public static int windowPosY = 100;
	public static int windowWidth = 800;
	public static int windowHeight = 600;
	public static int zoomLvl = 0;

	public static boolean debugLogging = false;

	private static boolean changed = false;

	private static final Map<String, Consumer<String>> setters = new HashMap<>();

	public static void init() {
		setters.put("language", val -> language = val);
		setters.put("lastPath", val -> lastPath = val);
		setters.put("musicPath", val -> musicPath = val);
		setters.put("songsPath", val -> songsPath = val);

		setters.put("minNoteDistance", val -> minNoteDistance = Integer.valueOf(val));
		setters.put("minTailLength", val -> minTailLength = Integer.valueOf(val));
		setters.put("delay", val -> delay = Integer.valueOf(val));
		setters.put("markerOffset", val -> markerOffset = Integer.valueOf(val));

		setters.put("invertStrings", val -> invertStrings = Boolean.valueOf(val));
		setters.put("showChordIds", val -> showChordIds = Boolean.valueOf(val));
		setters.put("frets", val -> frets = Integer.valueOf(val));

		setters.put("windowPosX", val -> windowPosX = Integer.valueOf(val));
		setters.put("windowPosY", val -> windowPosY = Integer.valueOf(val));
		setters.put("windowWidth", val -> windowWidth = Integer.valueOf(val));
		setters.put("windowHeight", val -> windowHeight = Integer.valueOf(val));
		setters.put("zoomLvl", val -> zoomLvl = Integer.valueOf(val));

		setters.put("debugLogging", val -> debugLogging = Boolean.valueOf(val));

		read();
		save();

		Localization.init();
	}

	public static void read() {
		for (final Entry<String, String> configVal : RW.readConfig(configName).entrySet()) {
			try {
				setters.getOrDefault(configVal.getKey(), val -> {
				}).accept(configVal.getValue());
			} catch (final Exception e) {
				error("wrong config line " + configVal.getKey() + "=" + configVal.getValue(), e);
			}
		}

		markChanged();
	}

	public static void save() {
		if (!changed) {
			return;
		}

		final Map<String, String> config = new HashMap<>();
		config.put("language", language);
		config.put("lastPath", lastPath);
		config.put("musicPath", musicPath);
		config.put("songsPath", songsPath);

		config.put("minNoteDistance", minNoteDistance + "");
		config.put("minTailLength", minTailLength + "");
		config.put("delay", delay + "");
		config.put("markerOffset", markerOffset + "");

		config.put("invertStrings", invertStrings + "");
		config.put("showChordIds", showChordIds + "");
		config.put("frets", frets + "");

		config.put("windowPosX", windowPosX + "");
		config.put("windowPosY", windowPosY + "");
		config.put("windowWidth", windowWidth + "");
		config.put("windowHeight", windowHeight + "");
		config.put("zoomLvl", zoomLvl + "");

		config.put("debugLogging", debugLogging + "");

		RW.writeConfig(configName, config);

		changed = false;
	}

	public static void markChanged() {
		changed = true;
	}
}