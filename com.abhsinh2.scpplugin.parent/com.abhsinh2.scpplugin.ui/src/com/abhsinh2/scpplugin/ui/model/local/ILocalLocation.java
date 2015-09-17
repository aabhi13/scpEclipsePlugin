package com.abhsinh2.scpplugin.ui.model.local;

import org.eclipse.core.runtime.IAdaptable;

/**
 * Interface to all local file handler.
 * 
 * @author abhsinh2
 * 
 */
public interface ILocalLocation extends IAdaptable {
	public String getName();

	public void setName(String newName);

	public String getLocation();

	public LocalFileType getType();

	public static ILocalLocation[] NONE = new ILocalLocation[] {};
}