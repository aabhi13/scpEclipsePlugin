package com.abhsinh2.scpplugin.ui.model.local;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

public abstract class SCPLocalFileType implements Comparable<SCPLocalFileType> {

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

	public static final SCPLocalFileType WORKBENCH_PROJECT = new SCPLocalFileType(
			"WBProj", "WorkbenchProject", 3) {

		public ISCPLocalLocation newLocation(Object obj) {
			if (!(obj instanceof IProject))
				return null;
			return new SCPLocalResource(this, (IProject) obj);
		}
	};

	public static final SCPLocalFileType JAVA_PROJECT = new SCPLocalFileType(
			"JProj", "Java Project", 4) {
		public ISCPLocalLocation newLocation(Object obj) {
			if (!(obj instanceof IJavaProject))
				return null;
			return new SCPLocalJavaElement(this, (IJavaProject) obj);
		}
	};

	public static final SCPLocalFileType JAVA_PACKAGE_ROOT = new SCPLocalFileType(
			"JPkgRoot", "Java Package Root", 5) {

		public ISCPLocalLocation newLocation(Object obj) {
			if (!(obj instanceof IPackageFragmentRoot))
				return null;
			return new SCPLocalJavaElement(this, (IPackageFragmentRoot) obj);
		}
	};

	public static final SCPLocalFileType JAVA_PACKAGE = new SCPLocalFileType(
			"JPkg", "Java Package", 6) {

		public ISCPLocalLocation newLocation(Object obj) {
			if (!(obj instanceof IPackageFragment))
				return null;
			return new SCPLocalJavaElement(this, (IPackageFragment) obj);
		}
	};

	public static final SCPLocalFileType JAVA_CLASS_FILE = new SCPLocalFileType(
			"JClass", "Java Class File", 7) {

		public ISCPLocalLocation newLocation(Object obj) {
			if (!(obj instanceof IClassFile))
				return null;
			return new SCPLocalJavaElement(this, (IClassFile) obj);
		}
	};

	public static final SCPLocalFileType JAVA_COMP_UNIT = new SCPLocalFileType(
			"JCompUnit", "Java Compilation Unit", 8) {

		public ISCPLocalLocation newLocation(Object obj) {
			if (!(obj instanceof ICompilationUnit))
				return null;
			return new SCPLocalJavaElement(this, (ICompilationUnit) obj);
		}
	};

	public static final SCPLocalFileType JAVA_INTERFACE = new SCPLocalFileType(
			"JInterface", "Java Interface", 9) {

		public ISCPLocalLocation newLocation(Object obj) {
			if (!(obj instanceof IType))
				return null;
			try {
				if (!((IType) obj).isInterface())
					return null;
			} catch (JavaModelException e) {

			}
			return new SCPLocalJavaElement(this, (IType) obj);
		}
	};

	public static final SCPLocalFileType JAVA_CLASS = new SCPLocalFileType(
			"JClass", "Java Class", 10) {

		public ISCPLocalLocation newLocation(Object obj) {
			if (!(obj instanceof IType))
				return null;
			try {
				if (((IType) obj).isInterface())
					return null;
			} catch (JavaModelException e) {

			}
			return new SCPLocalJavaElement(this, (IType) obj);
		}
	};

	private static final SCPLocalFileType[] TYPES = { UNKNOWN, WORKBENCH_FILE,
			WORKBENCH_FOLDER, WORKBENCH_PROJECT, JAVA_PROJECT,
			JAVA_PACKAGE_ROOT, JAVA_PACKAGE, JAVA_CLASS_FILE, JAVA_COMP_UNIT,
			JAVA_INTERFACE, JAVA_CLASS };

	public static SCPLocalFileType[] getTypes() {
		return TYPES;
	}
}
