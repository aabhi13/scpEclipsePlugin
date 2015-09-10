package com.abhsinh2.scpplugin.ui.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredSelection;

import com.abhsinh2.scpplugin.ui.model.local.ISCPLocalLocation;
import com.abhsinh2.scpplugin.ui.model.local.SCPLocalFileType;

public class Utility {

	public static Collection<String> getSelectedFiles(
			IStructuredSelection selection) {
		if (selection == null)
			return new ArrayList<String>();

		List<String> selectedLocalFilesList = new ArrayList<String>();

		Object[] selectedObjs = selection.toArray();

		for (int i = 0; i < selectedObjs.length; i++) {
			ISCPLocalLocation item = getLocalFileLocation(selectedObjs[i]);
			if (item == null) {
				item = newLocalFileLocation(selectedObjs[i]);
			}

			selectedLocalFilesList.add(item.getLocation());
		}

		return selectedLocalFilesList;
	}

	private static ISCPLocalLocation getLocalFileLocation(Object obj) {
		if (obj == null)
			return null;

		if (obj instanceof ISCPLocalLocation)
			return (ISCPLocalLocation) obj;

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
}
