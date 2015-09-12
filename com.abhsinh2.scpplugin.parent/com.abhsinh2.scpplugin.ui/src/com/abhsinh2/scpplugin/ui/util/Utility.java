package com.abhsinh2.scpplugin.ui.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredSelection;

import com.abhsinh2.scpplugin.ui.model.SCPLocation;
import com.abhsinh2.scpplugin.ui.model.local.ISCPLocalLocation;
import com.abhsinh2.scpplugin.ui.model.local.SCPLocalFileType;
import com.abhsinh2.scpplugin.ui.model.remote.SCPRemoteLocation;

public class Utility {

	public static Collection<String> getSelectedFiles(
			IStructuredSelection selection) {
		if (selection == null)
			return new ArrayList<String>();

		List<String> selectedLocalFilesList = new ArrayList<String>();

		Object[] selectedObjs = selection.toArray();

		for (int i = 0; i < selectedObjs.length; i++) {
			Object item = getLocation(selectedObjs[i]);

			if (item != null) {
				if (item instanceof ISCPLocalLocation) {
					ISCPLocalLocation localLocation = (ISCPLocalLocation) item;
					selectedLocalFilesList.add(localLocation.getLocation());
				} else if (item instanceof SCPLocation) {
					SCPLocation location = (SCPLocation) item;
					selectedLocalFilesList.addAll(location.getLocalFiles());
				}
			} else {
				ISCPLocalLocation localLocation = newLocalFileLocation(selectedObjs[i]);
				selectedLocalFilesList.add(localLocation.getLocation());
			}
		}

		return selectedLocalFilesList;
	}

	private static Object getLocation(Object obj) {
		if (obj == null)
			return null;

		if (obj instanceof ISCPLocalLocation)
			return (ISCPLocalLocation) obj;

		if (obj instanceof SCPLocation)
			return (SCPLocation) obj;

		if (obj instanceof SCPRemoteLocation)
			return (SCPRemoteLocation) obj;

		return null;
	}

	private static ISCPLocalLocation newLocalFileLocation(Object obj) {
		SCPLocalFileType[] types = SCPLocalFileType.getTypes();
		for (int i = 0; i < types.length; i++) {
			ISCPLocalLocation item = types[i].newLocation(obj);
			if (item != null)
				return item;
		}
		return null;
	}

	public static List<SCPLocation> getSCPLocations(IStructuredSelection selection) {
		if (selection == null) {
			return null;
		}

		Object[] selectedObjs = selection.toArray();
		
		if (selectedObjs.length > 0) {
			List<SCPLocation> locations = new ArrayList<SCPLocation>();

			for (int i = 0; i < selectedObjs.length; i++) {
				Object obj = selectedObjs[i];
				if (obj != null && obj instanceof SCPLocation) {
					locations.add((SCPLocation) obj);
				}
			}

			return locations;
		}
		
		return null;
	}
}
