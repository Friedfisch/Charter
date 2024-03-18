package log.charter.gui.chartPanelDrawers.data;

import static java.lang.Math.max;
import static log.charter.util.ScalingUtils.timeToXLength;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map.Entry;

import log.charter.data.song.BendValue;
import log.charter.data.song.ChordTemplate;
import log.charter.data.song.enums.BassPickingTechnique;
import log.charter.data.song.enums.HOPO;
import log.charter.data.song.enums.Harmonic;
import log.charter.data.song.enums.Mute;
import log.charter.data.song.notes.Chord;
import log.charter.data.song.notes.ChordNote;
import log.charter.data.song.notes.Note;
import log.charter.util.collections.ArrayList2;

public class EditorNoteDrawingData {
	private static class EditorBendValueDrawingData {
		public final int position;
		public final int x;
		public final BigDecimal bendValue;

		public EditorBendValueDrawingData(final int position, final int x, final BigDecimal bendValue) {
			this.position = position;
			this.x = x;
			this.bendValue = bendValue;
		}

	}

	public static EditorNoteDrawingData fromNote(final int x, final Note note, final boolean selected,
			final boolean highlighted, final boolean lastWasLinkNext, final boolean wrongLinkNext) {
		return new EditorNoteDrawingData(x, timeToXLength(note.position(), note.length()), note, selected, highlighted,
				lastWasLinkNext, wrongLinkNext);
	}

	public static EditorNoteDrawingData fromChordNote(final int x, final Chord chord, final ChordTemplate chordTemplate,
			final int string, final ChordNote chordNote, final boolean selected, final boolean highlighted,
			final boolean lastWasLinkNext, final boolean wrongLinkNext, final boolean ctrl) {
		final int fret = chordTemplate.frets.get(string);
		final Integer finger = chordTemplate.fingers.get(string);
		final String fretDescription = fret
				+ (ctrl && finger != null ? "(" + (finger == 0 ? "T" : finger.toString()) + ")" : "");

		return new EditorNoteDrawingData(x, timeToXLength(chord.position(), chordNote.length), string, fret,
				fretDescription, chord, chordNote, selected, highlighted, lastWasLinkNext, wrongLinkNext);
	}

	public static ArrayList2<EditorNoteDrawingData> fromChord(final Chord chord, final ChordTemplate chordTemplate,
			final int x, final boolean selected, final int highlightedString, final boolean lastWasLinkNext,
			final boolean wrongLinkNext, final boolean ctrl) {
		final ArrayList2<EditorNoteDrawingData> notes = new ArrayList2<>();

		for (final Entry<Integer, ChordNote> chordNoteEntry : chord.chordNotes.entrySet()) {
			final int string = chordNoteEntry.getKey();
			notes.add(fromChordNote(x, chord, chordTemplate, string, chordNoteEntry.getValue(), selected,
					highlightedString == string, lastWasLinkNext, wrongLinkNext, ctrl));
		}

		return notes;
	}

	public int position;
	public final int x;
	public final int length;

	public final int string;
	public final int fretNumber;
	public final String fret;
	public final boolean accent;
	public final Mute mute;
	public final HOPO hopo;
	public final Harmonic harmonic;
	public final BassPickingTechnique bassPickingTech;
	public final List<BendValue> bendValues;
	public final Integer slideTo;
	public final boolean unpitchedSlide;
	public final boolean vibrato;
	public final boolean tremolo;

	public final boolean selected;
	public final boolean highlighted;
	public final boolean linkPrevious;
	public final boolean wrongLink;
	public final double prebend;

	private EditorNoteDrawingData(final int x, final int length, final Note note, final boolean selected,
			final boolean highlighted, final boolean lastWasLinkNext, final boolean wrongLink) {
		this(note.position(), x, length, //
				note.string, note.fret, note.fret + "", //
				note.accent, note.mute, note.hopo, note.harmonic, note.bassPicking, note.bendValues, note.slideTo,
				note.unpitchedSlide, note.vibrato, note.tremolo, //
				selected, highlighted, lastWasLinkNext, wrongLink);
	}

	private EditorNoteDrawingData(final int x, final int length, final int string, final int fret,
			final String fretDescription, final Chord chord, final ChordNote chordNote, final boolean selected,
			final boolean highlighted, final boolean lastWasLinkNext, final boolean wrongLink) {
		this(chord.position(), x, length, //
				string, fret, fretDescription, //
				chord.accent, chordNote.mute, chordNote.hopo, chordNote.harmonic, BassPickingTechnique.NONE,
				chordNote.bendValues, chordNote.slideTo, chordNote.unpitchedSlide, chordNote.vibrato, chordNote.tremolo, //
				selected, highlighted, lastWasLinkNext, wrongLink);
	}

	private EditorNoteDrawingData(final int position, final int x, final int length, //
			final int string, final int fretNumber, final String fret, //
			final boolean accent, final Mute mute, final HOPO hopo, final Harmonic harmonic,
			final BassPickingTechnique bassPickingTech, final List<BendValue> bendValues, final Integer slideTo,
			final boolean unpitchedSlide, final boolean vibrato, final boolean tremolo, final boolean selected,
			final boolean highlighted, final boolean lastWasLinkNext, final boolean wrongLink) {
		this.position = position;
		this.x = x;
		this.length = lastWasLinkNext ? max(5, length) : length;

		this.string = string;
		this.fretNumber = fretNumber;
		this.fret = fret;
		this.accent = accent;
		this.mute = mute;
		this.hopo = hopo;
		this.harmonic = harmonic;
		this.bassPickingTech = bassPickingTech;
		this.bendValues = bendValues == null ? new ArrayList2<>() : bendValues;
		this.slideTo = slideTo;
		this.unpitchedSlide = unpitchedSlide;
		this.vibrato = vibrato;
		this.tremolo = tremolo;

		this.selected = selected;
		this.highlighted = highlighted;
		linkPrevious = lastWasLinkNext;
		this.wrongLink = wrongLink;

		prebend = !this.bendValues.isEmpty() && this.bendValues.get(0).position() == 0
				? this.bendValues.get(0).bendValue.doubleValue()
				: 0;
	}
}