package log.charter.services.data.validation;

import static log.charter.gui.components.utils.ComponentUtils.askYesNoCancel;
import static log.charter.util.CollectionUtils.filter;

import java.util.List;

import log.charter.data.config.Localization.Label;
import log.charter.data.song.Arrangement;
import log.charter.data.song.EventPoint;
import log.charter.gui.CharterFrame;
import log.charter.services.data.ChartTimeHandler;
import log.charter.services.editModes.ModeManager;

public class PhrasesValidator {
	private CharterFrame charterFrame;
	private ChartTimeHandler chartTimeHandler;
	private ModeManager modeManager;

	private void moveToTimeOnArrangement(final int arrangementId, final int time) {
		modeManager.setArrangement(arrangementId);
		chartTimeHandler.nextTime(time);
	}

	private boolean validateCountPhrases(final List<EventPoint> phrases, final int arrangementId,
			final String arrangementName) {
		final List<EventPoint> countPhrases = filter(phrases, phrase -> phrase.phrase.equals("COUNT"));

		if (countPhrases.size() <= 1) {
			return true;
		}

		switch (askYesNoCancel(charterFrame, Label.WARNING, Label.MULTIPLE_COUNT_PHRASES_MOVE_TO_LAST_QUESTION,
				arrangementName)) {
			case YES:
				moveToTimeOnArrangement(arrangementId, countPhrases.get(countPhrases.size() - 1).position());
				return false;
			case NO:
				return true;
			case CANCEL:
			case EXIT:
			default:
				return false;
		}
	}

	private boolean validateEndPhrases(final List<EventPoint> phrases, final int arrangementId,
			final String arrangementName) {
		final List<EventPoint> endPhrases = filter(phrases, phrase -> phrase.phrase.equals("END"));

		if (endPhrases.size() <= 1) {
			return true;
		}

		switch (askYesNoCancel(charterFrame, Label.WARNING, Label.MULTIPLE_END_PHRASES_MOVE_TO_FIRST_QUESTION,
				arrangementName)) {
			case YES:
				moveToTimeOnArrangement(arrangementId, endPhrases.get(0).position());
				return false;
			case NO:
				return true;
			case CANCEL:
			case EXIT:
			default:
				return false;
		}
	}

	private boolean validatePhrasesAmount(final List<EventPoint> phrases, final int arrangementId,
			final String arrangementName) {
		if (!phrases.isEmpty()) {
			return true;
		}

		switch (askYesNoCancel(charterFrame, Label.WARNING, Label.NO_PHRASES_MOVE_TO_ARRANGEMENT_QUESTION,
				arrangementName)) {
			case YES:
				modeManager.setArrangement(arrangementId);
				return false;
			case NO:
				return true;
			case CANCEL:
			case EXIT:
			default:
				return false;
		}
	}

	public boolean validatePhrases(final int arrangementId, final Arrangement arrangement) {
		final List<EventPoint> phrases = filter(arrangement.eventPoints, EventPoint::hasPhrase);
		final String arrangementName = arrangement.getTypeNameLabel(arrangementId);

		if (!validateCountPhrases(phrases, arrangementId, arrangementName)) {
			return false;
		}
		if (!validateEndPhrases(phrases, arrangementId, arrangementName)) {
			return false;
		}
		if (!validatePhrasesAmount(phrases, arrangementId, arrangementName)) {
			return false;
		}

		return true;
	}
}
