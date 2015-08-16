package com.abhsinh2.plugin.scp.model.local;

import org.eclipse.core.runtime.IAdaptable;

public interface ISCPLocalLocation extends IAdaptable {
	public String getName();

	public void setName(String newName);

	public String getLocation();

	public SCPLocalFileType getType();

	public static ISCPLocalLocation[] NONE = new ISCPLocalLocation[] {};
}