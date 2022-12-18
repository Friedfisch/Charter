package log.charter.gui.menuHandlers;

import static java.awt.event.KeyEvent.VK_F5;

import javax.swing.JMenu;

import log.charter.data.ChartData;
import log.charter.data.EditMode;
import log.charter.data.managers.ModeManager;
import log.charter.data.managers.selection.SelectionManager;
import log.charter.gui.chartPanelDrawers.common.AudioDrawer;
import log.charter.gui.handlers.AudioHandler;
import log.charter.song.ArrangementChart;
import log.charter.util.CollectionUtils.HashMap2;

class InstrumentMenuHandler extends CharterMenuHandler {
	private AudioDrawer audioDrawer;
	private AudioHandler audioHandler;
	private ChartData data;
	private CharterMenuBar charterMenuBar;
	private ModeManager modeManager;
	private SelectionManager selectionManager;

	public void init(final AudioDrawer audioDrawer, final AudioHandler audioHandler, final ChartData data,
			final CharterMenuBar charterMenuBar, final ModeManager modeManager,
			final SelectionManager selectionManager) {
		this.audioDrawer = audioDrawer;
		this.audioHandler = audioHandler;
		this.data = data;
		this.charterMenuBar = charterMenuBar;
		this.modeManager = modeManager;
		this.selectionManager = selectionManager;
	}

	@Override
	boolean isApplicable() {
		return !data.isEmpty;
	}

	private void createArrangementMenuItems(final JMenu menu) {
		final HashMap2<String, Integer> arrangementNumbers = new HashMap2<>();
		for (int i = 0; i < data.songChart.arrangements.size(); i++) {
			final ArrangementChart arrangement = data.songChart.arrangements.get(i);
			String arrangementName = arrangement.getTypeNameLabel();
			final int arrangementNumber = arrangementNumbers.getOrDefault(arrangementName, 1);
			arrangementNumbers.put(arrangementName, arrangementNumber + 1);
			if (arrangementNumber > 1) {
				arrangementName += " " + arrangementNumber;
			}

			final int arrangementId = i;
			menu.add(createItem(arrangementName, () -> changeArrangement(arrangementId)));
		}
	}

	@Override
	JMenu prepareMenu() {
		final JMenu menu = new JMenu("Instrument");

		menu.add(createItem(EditMode.VOCALS.label, () -> changeEditMode(EditMode.VOCALS)));
		createArrangementMenuItems(menu);

		menu.addSeparator();
		menu.add(createItem("Toggle waveform drawing", button(VK_F5), this::toggleWaveforDrawing));
		menu.add(createItem("Toggle claps on note", button('C'), audioHandler::toggleClaps));
		menu.add(createItem("Toggle metronome on measures", button('M'), audioHandler::toggleMetronome));

		return menu;
	}

	private void changeEditMode(final EditMode editMode) {
		audioHandler.stopMusic();
		selectionManager.clear();
		data.changeDifficulty(0);
		modeManager.editMode = editMode;

		charterMenuBar.refreshMenus();
	}

	private void changeArrangement(final int arrangementId) {
		data.currentArrangement = arrangementId;
		changeEditMode(EditMode.GUITAR);
	}

	private void toggleWaveforDrawing() {
		audioDrawer.drawAudio = !audioDrawer.drawAudio;
	}
}
