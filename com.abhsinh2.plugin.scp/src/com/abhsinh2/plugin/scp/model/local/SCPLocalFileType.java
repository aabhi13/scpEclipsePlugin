package com.abhsinh2.plugin.scp.model.local;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

public abstract class SCPLocalFileType implements Comparable<SCPLocalFileType> {
	private static final ISharedImages PLATFORM_IMAGES = PlatformUI
			.getWorkbench().getSharedImages();

	private final String id;
	private final String printName;
	private final int ordinal;

	private SCPLocalFileType(String id, String name, int position) {
		this.id = id;
		this.ordinal = position;
		this.printName = name;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return printName;
	}

	public abstract ISCPLocalLocation newLocation(Object obj);

	public int compareTo(SCPLocalFileType other) {
		return this.ordinal - other.ordinal;
	}

	public static final SCPLocalFileType UNKNOWN = new SCPLocalFileType(
			"Unknown", "Unknown", 0) {
		public ISCPLocalLocation newLocation(Object obj) {
			return null;
		}
	};

	public static final SCPLocalFileType WORKBENCH_FILE = new SCPLocalFileType(
			"WBFile", "Workbench File", 1) {
		public ISCPLocalLocation newLocation(Object obj) {
			if (!(obj instanceof IFile))
				return null;
			return new SCPLocalResource(this, (IFile) obj);
		}
	};

	public static final SCPLocalFileType WORKBENCH_FOLDER = new SCPLocalFileType(
			"WBFolder", "Workbench Folder", 2) {
		public ISCPLocalLocation newLocation(Object obj) {
			if (!(obj instanceof IFolder))
				return null;
			return new SCPLocalResource(this, (IFolder) obj);
		}
	};

	private static final SCPLocalFileType[] TYPES = { UNKNOWN, WORKBENCH_FILE,
			WORKBENCH_FOLDER };

	public static SCPLocalFileType[] getTypes() {
		return TYPES;
	}
}
