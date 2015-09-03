package com.abhsinh2.scpplugin.ui.provider;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;

import com.abhsinh2.scpplugin.ui.model.SCPLocationManager;
import com.abhsinh2.scpplugin.ui.model.SCPLocationManagerEvent;
import com.abhsinh2.scpplugin.ui.model.SCPLocationManagerListener;
import com.abhsinh2.scpplugin.ui.model.SCPLocationManagerType;

public class SCPViewContentProvider implements IStructuredContentProvider,
		SCPLocationManagerListener {
	private TableViewer viewer;
	private SCPLocationManager manager;

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		this.viewer = (TableViewer) viewer;

		if (manager != null)
			manager.removeLocationManagerListener(this);

		manager = (SCPLocationManager) newInput;

		if (manager != null)
			manager.addLocationManagerListener(this);
	}

	public void dispose() {
	}

	public Object[] getElements(Object parent) {
		return manager.getLocations().toArray();
	}

	public void locationChanged(SCPLocationManagerEvent event) {
		viewer.getTable().setRedraw(false);
		try {
			if (event.getEventType() == SCPLocationManagerType.ADDED
					|| event.getEventType() == SCPLocationManagerType.UPDATED) {
				viewer.add(event.getLocation());
			}

			if (event.getEventType() == SCPLocationManagerType.DELETED) {
				viewer.remove(event.getLocation());
			}

		} finally {
			viewer.getTable().setRedraw(true);
		}
	}
}