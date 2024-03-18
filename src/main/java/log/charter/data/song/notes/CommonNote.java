package log.charter.data.song.notes;

import java.util.List;

import log.charter.data.song.BeatsMap.ImmutableBeatsMap;
import log.charter.data.song.BendValue;
import log.charter.data.song.enums.BassPickingTechnique;
import log.charter.data.song.enums.HOPO;
import log.charter.data.song.enums.Harmonic;
import log.charter.data.song.enums.Mute;
import log.charter.data.song.position.FractionalPosition;
import log.charter.data.song.position.fractional.IConstantFractionalPositionWithEnd;

public class CommonNote implements NoteInterface {
	private final int string;
	private final boolean passOtherNotes;
	private final NoteInterface note;

	public CommonNote(final Chord chord, final int string) {
		this.string = string;
		passOtherNotes = chord.passOtherNotes;
		note = chord.chordNotes.get(string);
	}

	public CommonNote(final Note note) {
		string = note.string;
		passOtherNotes = note.passOtherNotes;
		this.note = note;
	}

	@Override
	public FractionalPosition position() {
		return note.position();
	}

	@Override
	public void endPosition(final FractionalPosition newEndPosition) {
		note.endPosition(newEndPosition);
	}

	@Override
	public FractionalPosition endPosition() {
		return note.endPosition();
	}

	public int string() {
		return string;
	}

	public boolean passOtherNotes() {
		return passOtherNotes;
	}

	@Override
	public BassPickingTechnique bassPicking() {
		return note.bassPicking();
	}

	@Override
	public void bassPicking(final BassPickingTechnique value) {
		note.bassPicking(value);
	}

	@Override
	public Mute mute() {
		return note.mute();
	}

	@Override
	public void mute(final Mute value) {
		note.mute(value);
	}

	@Override
	public HOPO hopo() {
		return note.hopo();
	}

	@Override
	public void hopo(final HOPO value) {
		note.hopo(value);
	}

	@Override
	public Harmonic harmonic() {
		return note.harmonic();
	}

	@Override
	public void harmonic(final Harmonic value) {
		note.harmonic(value);
	}

	@Override
	public boolean vibrato() {
		return note.vibrato();
	}

	@Override
	public void vibrato(final boolean value) {
		note.vibrato(value);
	}

	@Override
	public boolean tremolo() {
		return note.tremolo();
	}

	@Override
	public void tremolo(final boolean value) {
		note.tremolo(value);
	}

	@Override
	public boolean linkNext() {
		return note.linkNext();
	}

	@Override
	public void linkNext(final boolean value) {
		note.linkNext(value);
	}

	@Override
	public Integer slideTo() {
		return note.slideTo();
	}

	@Override
	public void slideTo(final Integer value) {
		note.slideTo(value);
	}

	@Override
	public boolean unpitchedSlide() {
		return note.unpitchedSlide();
	}

	@Override
	public void unpitchedSlide(final boolean value) {
		note.unpitchedSlide(value);
	}

	@Override
	public List<BendValue> bendValues() {
		return note.bendValues();
	}

	@Override
	public void bendValues(final List<BendValue> value) {
		note.bendValues(value);
	}

	@Override
	public IConstantFractionalPositionWithEnd toFraction(final ImmutableBeatsMap beats) {
		return this;
	}

}
