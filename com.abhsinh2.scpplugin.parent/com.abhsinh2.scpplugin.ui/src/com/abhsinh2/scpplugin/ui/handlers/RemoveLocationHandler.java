package com.abhsinh2.scpplugin.ui.handlers;

import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import com.abhsinh2.scpplugin.ui.model.SCPLocation;
import com.abhsinh2.scpplugin.ui.model.SCPLocationManager;

public class RemoveLocationHandler extends AbstractHandler {
	public Object execute(ExecutionEvent event) throws ExecutionException {		
		
		System.out.println("RemoveLocationHandler");
		ISelection selection = HandlerUtil.getCurrentSelection(event);

		if (selection instanceof IStructuredSelection) {
			IStructuredSelection strucSel = (IStructuredSelection) selection;
			SCPLocation[] locations = new SCPLocation[strucSel.size()];

			Iterator<Object> iter = strucSel.iterator();
			int i = 0;
			while (iter.hasNext()) {
				Object obj = iter.next();

				if (obj instanceof SCPLocation) {
					locations[i] = (SCPLocation) obj;
				}
				i++;
			}

			//SCPLocationManager.getManager().removeLocations(locations);
		}

		return null;
	}
}
