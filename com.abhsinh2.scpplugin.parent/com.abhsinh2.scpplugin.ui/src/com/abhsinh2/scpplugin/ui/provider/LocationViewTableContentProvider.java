package com.abhsinh2.scpplugin.ui.provider;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;

import com.abhsinh2.scpplugin.ui.model.LocationManager;
import com.abhsinh2.scpplugin.ui.model.LocationManagerEvent;
import com.abhsinh2.scpplugin.ui.model.LocationManagerListener;
import com.abhsinh2.scpplugin.ui.model.LocationManagerOperationType;

/**
 * Provides content to View in tabular form.
 * 
 * @author abhsinh2
 * 
 */
public class LocationViewTableContentProvider implements
		IStructuredContentProvider, LocationManagerListener {
	private TableViewer viewer;
	private LocationManager manager;

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		this.viewer = (TableViewer) viewer;

		if (manager != null)
			manager.removeLocationManagerListener(this);

		manager = (LocationManager) newInput;

		if (manager != null)
			manager.addLocationManagerListener(this);
	}

	public void dispose() {
	}

	public Object[] getElements(Object parent) {
		return manager.getLocations().toArray();
	}

	public void locationChanged(LocationManagerEvent event) {
		viewer.getTable().setRedraw(false);
		try {
			if (event.getEventType() == LocationManagerOperationType.ADDED
					|| event.getEventType() == LocationManagerOperationType.UPDATED) {
				viewer.add(event.getLocation());
			}

			if (event.getEventType() == LocationManagerOperationType.DELETED) {
				viewer.remove(event.getLocation());
			}

		} finally {
			viewer.getTable().setRedraw(true);
		}
	}
}