package com.abhsinh2.scpplugin.ui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IMemento;

import com.abhsinh2.scpplugin.ui.view.SCPViewNameFilter;

/**
 */
public class SCPViewFilterAction extends Action {
	private final Shell shell;
	private final SCPViewNameFilter nameFilter;

	public SCPViewFilterAction(StructuredViewer viewer, String text) {
		super(text);
		shell = viewer.getControl().getShell();
		nameFilter = new SCPViewNameFilter(viewer);
	}

	public void run() {
		InputDialog dialog = new InputDialog(shell, "SCP Name Filter",
				"Enter a name filter pattern"
						+ " (* = any string, ? = any character)"
						+ System.getProperty("line.separator")
						+ "or an empty string for no filtering:",
				nameFilter.getPattern(), null);
		if (dialog.open() == InputDialog.OK)
			nameFilter.setPattern(dialog.getValue().trim());
	}

	public void saveState(IMemento memento) {
		nameFilter.saveState(memento);
	}

	public void init(IMemento memento) {
		nameFilter.init(memento);
	}
}
