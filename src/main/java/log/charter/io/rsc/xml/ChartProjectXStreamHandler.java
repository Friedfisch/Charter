package log.charter.io.rsc.xml;

import java.util.ArrayList;
import java.util.stream.Collectors;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.collections.CollectionConverter;
import com.thoughtworks.xstream.converters.collections.MapConverter;

import log.charter.data.song.Anchor;
import log.charter.data.song.Arrangement;
import log.charter.data.song.Beat;
import log.charter.data.song.BeatsMap;
import log.charter.data.song.BeatsMap.ImmutableBeatsMap;
import log.charter.data.song.BendValue;
import log.charter.data.song.ChordTemplate;
import log.charter.data.song.EventPoint;
import log.charter.data.song.HandShape;
import log.charter.data.song.Level;
import log.charter.data.song.Phrase;
import log.charter.data.song.ToneChange;
import log.charter.data.song.notes.ChordNote;
import log.charter.data.song.notes.ChordOrNote;
import log.charter.data.song.notes.ChordOrNote.ChordOrNoteForChord;
import log.charter.data.song.notes.ChordOrNote.ChordOrNoteForNote;
import log.charter.data.song.notes.GuitarSound;
import log.charter.data.song.notes.Note;
import log.charter.data.song.vocals.Vocal;
import log.charter.io.XMLHandler;
import log.charter.io.rs.xml.converters.NullSafeIntegerConverter;
import log.charter.io.rsc.xml.converters.AnchorConverter.TemporaryAnchor;
import log.charter.services.data.copy.data.AnchorsCopyData;
import log.charter.services.data.copy.data.CopyData;
import log.charter.services.data.copy.data.EventPointsCopyData;
import log.charter.services.data.copy.data.FullGuitarCopyData;
import log.charter.services.data.copy.data.HandShapesCopyData;
import log.charter.services.data.copy.data.SoundsCopyData;
import log.charter.services.data.copy.data.VocalsCopyData;
import log.charter.services.data.copy.data.positions.CopiedAnchorPosition;
import log.charter.services.data.copy.data.positions.CopiedArrangementEventsPointPosition;
import log.charter.services.data.copy.data.positions.CopiedHandShapePosition;
import log.charter.services.data.copy.data.positions.CopiedSoundPosition;
import log.charter.services.data.copy.data.positions.CopiedToneChangePosition;
import log.charter.services.data.copy.data.positions.CopiedVocalPosition;
import log.charter.services.editModes.EditMode;
import log.charter.util.collections.ArrayList2;
import log.charter.util.collections.HashMap2;
import log.charter.util.collections.HashSet2;

public class ChartProjectXStreamHandler {
	private static XStream xstream = prepareXStream();

	private static XStream prepareXStream() {
		final XStream xstream = new XStream();
		xstream.registerConverter(new NullSafeIntegerConverter());
		xstream.registerConverter(new CollectionConverter(xstream.getMapper(), ArrayList2.class));
		xstream.registerConverter(new CollectionConverter(xstream.getMapper(), HashSet2.class));
		xstream.registerConverter(new MapConverter(xstream.getMapper(), HashMap2.class), 0);
		xstream.alias("beat", Beat.class);
		xstream.ignoreUnknownElements();
		xstream.processAnnotations(ChartProject.class);
		xstream.processAnnotations(CopyData.class);
		xstream.allowTypes(new Class[] { //
				Anchor.class, //
				AnchorsCopyData.class, //
				Arrangement.class, //
				Beat.class, //
				BendValue.class, //
				ChordNote.class, //
				ChordOrNote.class, //
				ChordOrNoteForChord.class, //
				ChordOrNoteForNote.class, //
				ChordTemplate.class, //
				CopiedAnchorPosition.class, //
				CopiedArrangementEventsPointPosition.class, //
				CopiedHandShapePosition.class, //
				CopiedSoundPosition.class, //
				CopiedToneChangePosition.class, //
				CopiedVocalPosition.class, //
				CopyData.class, //
				EventPoint.class, //
				EventPointsCopyData.class, //
				FullGuitarCopyData.class, //
				GuitarSound.class, //
				HandShape.class, //
				HandShapesCopyData.class, //
				Level.class, //
				Note.class, //
				Phrase.class, //
				ChartProject.class, //
				SoundsCopyData.class, //
				ToneChange.class, //
				Vocal.class, //
				VocalsCopyData.class });

		return xstream;
	}

	private static void updateToVersion2(final ChartProject project) {
		if (project.chartFormatVersion >= 2) {
			return;
		}

		project.editMode = EditMode.GUITAR;
		project.arrangement = 0;
		project.level = 0;
		project.time = 0;

		project.chartFormatVersion = 2;
	}

	private static Anchor transformAnchor(final ImmutableBeatsMap beats, final Anchor anchor) {
		if (anchor instanceof TemporaryAnchor) {
			return ((TemporaryAnchor) anchor).transform(beats);
		}

		return anchor;
	}

	private static void updateToVersion3(final ChartProject project) {
		if (project.chartFormatVersion >= 4) {
			return;
		}

		final ImmutableBeatsMap beats = new BeatsMap(project.beats).immutable;

		project.arrangements.stream()//
				.flatMap(arrangement -> arrangement.levels.stream())//
				.forEach(level -> level.anchors = level.anchors.stream().map(anchor -> transformAnchor(beats, anchor))//
						.collect(Collectors.toCollection(ArrayList::new)));

		project.chartFormatVersion = 3;
	}

	public static ChartProject readChartProject(final String xml) {
		final Object o = xstream.fromXML(xml);
		if (!o.getClass().isAssignableFrom(ChartProject.class)) {
			return null;
		}

		final ChartProject project = (ChartProject) o;
		updateToVersion2(project);
		updateToVersion3(project);

		return project;
	}

	public static CopyData readCopyData(final String xml) {
		final Object o = xstream.fromXML(xml);
		if (!o.getClass().isAssignableFrom(CopyData.class)) {
			return null;
		}

		return (CopyData) o;
	}

	public static String writeChartProject(final ChartProject chartProject) {
		return XMLHandler.generateXML(xstream, chartProject);
	}

	public static String writeCopyData(final CopyData copyData) {
		return XMLHandler.generateXML(xstream, copyData);
	}
}
