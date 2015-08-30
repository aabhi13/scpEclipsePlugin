package com.abhsinh2.scpplugin.ui.model.remote;

import org.eclipse.core.runtime.IAdaptable;

public class SCPRemoteLocation implements IAdaptable {
	private String remoteAddress;
	private String remoteLocation;
	private String username;
	private String password;

	public SCPRemoteLocation(String remoteAddress, String remoteLocation) {
		super();
		this.remoteAddress = remoteAddress;
		this.remoteLocation = remoteLocation;
	}

	public SCPRemoteLocation(String remoteAddress,
			String remoteLocation, String username, String password) {
		super();
		this.remoteAddress = remoteAddress;
		this.remoteLocation = remoteLocation;
		this.username = username;
		this.password = password;
	}

	public String getRemoteAddress() {
		return remoteAddress;
	}

	public void setRemoteAddress(String remoteAddress) {
		this.remoteAddress = remoteAddress;
	}

	public String getRemoteLocation() {
		return remoteLocation;
	}

	public void setRemoteLocation(String remoteLocation) {
		this.remoteLocation = remoteLocation;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "SCPLocation [remoteAddress=" + remoteAddress
				+ ", remoteLocation=" + remoteLocation + "]";
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class adapter) {
		return null;
	}
}