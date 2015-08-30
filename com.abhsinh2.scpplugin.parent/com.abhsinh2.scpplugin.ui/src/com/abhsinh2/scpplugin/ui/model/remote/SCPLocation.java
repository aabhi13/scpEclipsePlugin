package com.abhsinh2.scpplugin.ui.model.remote;

import java.util.Collection;

import org.eclipse.core.runtime.IAdaptable;

public class SCPLocation implements IAdaptable{
	private String name;
	private Collection<String> localFiles;
	private SCPRemoteLocation remoteLocation;

	public SCPLocation(String name, Collection<String> localFiles,
			SCPRemoteLocation remoteLocation) {
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

	public SCPRemoteLocation getRemoteLocation() {
		return remoteLocation;
	}

	public void setRemoteLocation(SCPRemoteLocation remoteLocation) {
		this.remoteLocation = remoteLocation;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class adapter) {
		return null;
	}
}
