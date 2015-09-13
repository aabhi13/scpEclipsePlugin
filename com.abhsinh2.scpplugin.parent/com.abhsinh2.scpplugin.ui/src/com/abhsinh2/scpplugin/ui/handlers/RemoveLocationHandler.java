package com.abhsinh2.scpplugin.ui.handlers;

import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import com.abhsinh2.scpplugin.ui.model.Location;
import com.abhsinh2.scpplugin.ui.model.LocationManager;

/**
 * Removes selected Location from View.
 * 
 * @author abhsinh2
 * 
 */
public class RemoveLocationHandler extends AbstractHandler {
	
	@SuppressWarnings("unchecked")
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getCurrentSelection(event);

		if (selection instanceof IStructuredSelection) {
			IStructuredSelection strucSel = (IStructuredSelection) selection;
			Location[] locations = new Location[strucSel.size()];

			Iterator<Object> iter = strucSel.iterator();
			int i = 0;
			while (iter.hasNext()) {
				Object obj = iter.next();

				if (obj instanceof Location) {
					locations[i] = (Location) obj;
				}
				i++;
			}

			LocationManager.getManager().removeLocations(locations);
		}

		return null;
	}
}
