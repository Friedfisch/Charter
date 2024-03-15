package log.charter.services.data.selection;

import java.util.ArrayList;
import java.util.List;

import log.charter.data.song.position.IVirtualConstantPosition;
import log.charter.data.types.PositionType;

class SelectionAccessor<T extends IVirtualConstantPosition> implements ISelectionAccessor<T> {
	private final SelectionList<?, ?, T> selectionList;

	SelectionAccessor(final SelectionList<?, ?, T> selectionList) {
		this.selectionList = selectionList;
	}

	@Override
	public PositionType type() {
		return selectionList.type;
	}

	@Override
	public List<Selection<T>> getSelected() {
		return selectionList.getSelected();
	}

	@Override
	public List<Integer> getSelectedIds(final PositionType forType) {
		if (selectionList.type != forType) {
			return new ArrayList<>();
		}

		return new ArrayList<>(selectionList.getSelectedIds());
	}

	@Override
	public List<T> getSelectedElements() {
		return selectionList.getSelectedElements();
	}

	@Override
	public boolean isSelected() {
		return !selectionList.getSelected().isEmpty();
	}
}
