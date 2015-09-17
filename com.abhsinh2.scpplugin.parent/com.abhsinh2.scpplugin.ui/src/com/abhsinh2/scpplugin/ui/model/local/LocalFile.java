package com.abhsinh2.scpplugin.ui.model.local;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;

/**
 * POJO for any local file.
 * 
 * @author abhsinh2
 * 
 */
public class LocalFile implements ILocalLocation {

	private LocalFileType type;
	private IResource resource;
	private String name;

	LocalFile(LocalFileType type, IResource resource) {
		this.type = type;
		this.resource = resource;
	}

	public static LocalFile loadFavorite(LocalFileType type, String info) {
		IResource res = ResourcesPlugin.getWorkspace().getRoot()
				.findMember(new Path(info));
		if (res == null)
			return null;

		return new LocalFile(type, res);
	}

	public String getName() {
		if (name == null)
			name = resource.getName();
		return name;
	}

	public void setName(String newName) {
		name = newName;
	}

	public String getLocation() {
		/*
		 * IPath path = resource.getLocation().removeLastSegments(1); if
		 * (path.segmentCount() == 0) return ""; return path.toString();
		 */

		return resource.getLocation().toString();
	}

	public LocalFileType getType() {
		return type;
	}

	public boolean equals(Object obj) {
		return this == obj
				|| ((obj instanceof LocalFile) && resource
						.equals(((LocalFile) obj).resource));
	}

	public int hashCode() {
		return resource.hashCode();
	}

	@SuppressWarnings("rawtypes")
	public Object getAdapter(Class adapter) {
		return getAdapterDelegate(adapter);
	}

	private Object getAdapterDelegate(Class<?> adapter) {
		if (adapter.isInstance(resource))
			return resource;
		return Platform.getAdapterManager().getAdapter(this, adapter);
	}

	public String getInfo() {
		return resource.getFullPath().toString();
	}
}