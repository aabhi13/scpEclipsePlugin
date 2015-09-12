package com.abhsinh2.scpplugin.ui.handlers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import com.abhsinh2.scpplugin.ui.dialog.LocationDialog;
import com.abhsinh2.scpplugin.ui.model.local.ILocalLocation;
import com.abhsinh2.scpplugin.ui.model.local.LocalFileType;

public class CopyHandler extends AbstractHandler {
	
	Shell shell;
	ExecutionEvent event;
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		this.shell = HandlerUtil.getActiveShell(event);
		this.event = event;

		ISelection selection = HandlerUtil.getCurrentSelection(event);

		List<String> allLocations = new ArrayList<String>();

		if (selection instanceof IStructuredSelection) {
			Object[] selectedObjs = ((IStructuredSelection) selection)
					.toArray();

			for (int i = 0; i < selectedObjs.length; i++) {
				ILocalLocation item = existingLocation(selectedObjs[i]);
				if (item == null) {
					item = newLocation(selectedObjs[i]);
				}

				allLocations.add(item.getLocation());
			}
		}

		opedDialog(allLocations);

		return null;
	}

	private ILocalLocation existingLocation(Object obj) {
		if (obj == null)
			return null;

		if (obj instanceof ILocalLocation)
			return (ILocalLocation) obj;

		return null;
	}

	private ILocalLocation newLocation(Object obj) {
		LocalFileType[] types = LocalFileType.getTypes();
		for (int i = 0; i < types.length; i++) {
			ILocalLocation item = types[i].newLocation(obj);
			if (item != null)
				return item;
		}
		return null;
	}

	private void opedDialog(List<String> localLocations) {
		//Display display = new Display();
		//Shell shell = new Shell(display);
		//shell.setText("SCP");
		//shell.setBounds(100, 100, 200, 100);
		//shell.setLayout(new FillLayout(SWT.VERTICAL));
		LocationDialog dialog = new LocationDialog(shell, event, localLocations);
		
		if (dialog.open() != InputDialog.OK)
			return;

		shell.open();
		/*
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();*/
	}

}
