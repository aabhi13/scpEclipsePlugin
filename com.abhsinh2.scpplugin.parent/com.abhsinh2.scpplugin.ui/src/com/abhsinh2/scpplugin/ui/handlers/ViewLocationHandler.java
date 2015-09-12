package com.abhsinh2.scpplugin.ui.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.abhsinh2.scpplugin.ui.dialog.ViewLocationDialog;
import com.abhsinh2.scpplugin.ui.model.SCPLocation;
import com.abhsinh2.scpplugin.ui.util.Utility;

public class ViewLocationHandler extends AbstractHandler {
	public Object execute(ExecutionEvent event) throws ExecutionException {
		System.out.println("ViewLocationHandler");
		
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		ISelection selection = HandlerUtil.getCurrentSelection(event);

		if (selection instanceof IStructuredSelection) {
			SCPLocation location = Utility.getSCPLocations((IStructuredSelection)selection).get(0);
			ViewLocationDialog dialog = new ViewLocationDialog(window.getShell(), location);
			dialog.open();
		}		

		return null;
	}
}
