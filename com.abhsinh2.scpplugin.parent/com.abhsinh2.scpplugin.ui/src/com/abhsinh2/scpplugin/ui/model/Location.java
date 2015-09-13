package com.abhsinh2.scpplugin.ui.model;

import java.util.Collection;

import org.eclipse.core.runtime.IAdaptable;

import com.abhsinh2.scpplugin.ui.model.remote.RemoteLocation;

/**
 * POJO to store remote machine and local files details.
 * 
 * @author abhsinh2
 * 
 */
public class Location implements IAdaptable {
	private String name;
	private Collection<String> localFiles;
	private RemoteLocation remoteLocation;

	public Location(String name, Collection<String> localFiles,
			RemoteLocation remoteLocation) {
		super();
		this.name = name;
		this.localFiles = localFiles;
		this.remoteLocation = remoteLocation;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Collection<String> getLocalFiles() {
		return localFiles;
	}

	public void setLocalFiles(Collection<String> localFiles) {
		this.localFiles = localFiles;
	}

	public RemoteLocation getRemoteLocation() {
		return remoteLocation;
	}

	public void setRemoteLocation(RemoteLocation remoteLocation) {
		this.remoteLocation = remoteLocation;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class adapter) {
		return null;
	}
}
