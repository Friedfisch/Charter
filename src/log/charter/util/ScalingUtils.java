package log.charter.util;

import log.charter.data.Config;
import log.charter.data.Zoom;

public class ScalingUtils {
	public static int xToTime(final int x, final int t) {
		return (int) (((x - Config.markerOffset) / Zoom.zoom) + t);
	}

	public static int xToTimeLength(final int x) {
		return (int) (x / Zoom.zoom);
	}

	public static int timeToX(final int pos, final int t) {
		return (int) ((pos - t) * Zoom.zoom) + Config.markerOffset;
	}

	public static int timeToXLength(final int length) {
		return (int) (length * Zoom.zoom);
	}
}