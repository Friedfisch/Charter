package log.charter.song;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import log.charter.data.Config;
import log.charter.gui.SelectionManager.Selectable;
import log.charter.io.rs.xml.vocals.ArrangementVocal;

@XStreamAlias("vocal")
public class Vocal extends Position implements Selectable {
	public int length;
	public String lyric;

	public Vocal(final int position) {
		super(position);
	}

	public Vocal(final ArrangementVocal arrangementVocal) {
		super(arrangementVocal.time);
		length = arrangementVocal.length == null ? 0 : arrangementVocal.length;
		lyric = arrangementVocal.lyric;
	}

	public Vocal(final int time, final String text, final boolean wordPart, final boolean phraseEnd) {
		super(time);
		lyric = text;

		if (wordPart) {
			lyric += "-";
		}
		length = Config.minTailLength;
	}

	public boolean isWordPart() {
		return lyric.endsWith("-");
	}

	public void setWordPart(final boolean wordPart) {
		if (wordPart != isWordPart()) {
			if (wordPart) {
				lyric += "-";
			} else {
				lyric = lyric.substring(0, lyric.length() - 1);
			}
		}
	}

	public boolean isPhraseEnd() {
		return lyric.endsWith("+");
	}

	public void setPhraseEnd(final boolean phraseEnd) {
		if (phraseEnd != isPhraseEnd()) {
			if (phraseEnd) {
				lyric += "+";
			} else {
				lyric = lyric.substring(0, lyric.length() - 1);
			}
		}
	}

	public String getText() {
		String text = lyric.replace("+", "");
		if (text.endsWith("-")) {
			text = text.substring(0, text.length() - 1);
		}

		return text;
	}

	@Override
	public String getSignature() {
		return position + "";
	}
}