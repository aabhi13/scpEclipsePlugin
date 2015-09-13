package com.abhsinh2.scpplugin.ui.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;

import com.abhsinh2.scpplugin.ui.model.Location;
import com.abhsinh2.scpplugin.ui.model.local.ILocalLocation;
import com.abhsinh2.scpplugin.ui.model.local.LocalFileType;
import com.abhsinh2.scpplugin.ui.model.remote.RemoteLocation;

/**
 * Utility class.
 * 
 * @author abhsinh2
 * 
 */
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
				if (item instanceof ILocalLocation) {
					ILocalLocation localLocation = (ILocalLocation) item;
					selectedLocalFilesList.add(localLocation.getLocation());
				} else if (item instanceof Location) {
					Location location = (Location) item;
					selectedLocalFilesList.addAll(location.getLocalFiles());
				}
			} else {
				ILocalLocation localLocation = newLocalFileLocation(selectedObjs[i]);
				selectedLocalFilesList.add(localLocation.getLocation());
			}
		}

		return selectedLocalFilesList;
	}

	private static Object getLocation(Object obj) {
		if (obj == null)
			return null;

		if (obj instanceof ILocalLocation)
			return (ILocalLocation) obj;

		if (obj instanceof Location)
			return (Location) obj;

		if (obj instanceof RemoteLocation)
			return (RemoteLocation) obj;

		return null;
	}

	private static ILocalLocation newLocalFileLocation(Object obj) {
		LocalFileType[] types = LocalFileType.getTypes();
		for (int i = 0; i < types.length; i++) {
			ILocalLocation item = types[i].newLocation(obj);
			if (item != null)
				return item;
		}
		return null;
	}

	public static List<Location> getSCPLocations(IStructuredSelection selection) {
		if (selection == null) {
			return null;
		}

		Object[] selectedObjs = selection.toArray();

		if (selectedObjs.length > 0) {
			List<Location> locations = new ArrayList<Location>();

			for (int i = 0; i < selectedObjs.length; i++) {
				Object obj = selectedObjs[i];
				if (obj != null && obj instanceof Location) {
					locations.add((Location) obj);
				}
			}

			return locations;
		}

		return null;
	}

	/**
	 * Open an editor on the first selected element
	 * 
	 * @param page
	 *            the page in which the editor should be opened
	 * @param selection
	 *            the selection containing the object to be edited
	 */
	public static void openEditor(IWorkbenchPage page, ISelection selection) {

		// Get the first element.

		if (!(selection instanceof IStructuredSelection))
			return;
		Iterator<?> iter = ((IStructuredSelection) selection).iterator();
		if (!iter.hasNext())
			return;
		Object elem = iter.next();
		// Adapt the first element to a file.

		if (!(elem instanceof IAdaptable))
			return;

		IFile file = (IFile) ((IAdaptable) elem).getAdapter(IFile.class);
		if (file == null)
			return;

		// Open an editor on that file.

		try {
			IDE.openEditor(page, file);
		} catch (PartInitException e) {
		}
	}

}
