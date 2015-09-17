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

/**
 * Defines which type do we support.
 * 
 * @author abhsinh2
 * 
 */
public abstract class LocalFileType implements Comparable<LocalFileType> {

	private final String id;
	private final String printName;
	private final int ordinal;

	private LocalFileType(String id, String name, int position) {
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

	public abstract ILocalLocation newLocation(Object obj);

	public int compareTo(LocalFileType other) {
		return this.ordinal - other.ordinal;
	}

	public static final LocalFileType UNKNOWN = new LocalFileType("Unknown",
			"Unknown", 0) {
		public ILocalLocation newLocation(Object obj) {
			return null;
		}
	};

	public static final LocalFileType WORKBENCH_FILE = new LocalFileType(
			"WBFile", "Workbench File", 1) {
		public ILocalLocation newLocation(Object obj) {
			if (!(obj instanceof IFile))
				return null;
			return new LocalFile(this, (IFile) obj);
		}
	};

	public static final LocalFileType WORKBENCH_FOLDER = new LocalFileType(
			"WBFolder", "Workbench Folder", 2) {
		public ILocalLocation newLocation(Object obj) {
			if (!(obj instanceof IFolder))
				return null;
			return new LocalFile(this, (IFolder) obj);
		}
	};

	public static final LocalFileType WORKBENCH_PROJECT = new LocalFileType(
			"WBProj", "WorkbenchProject", 3) {

		public ILocalLocation newLocation(Object obj) {
			if (!(obj instanceof IProject))
				return null;
			return new LocalFile(this, (IProject) obj);
		}
	};

	public static final LocalFileType JAVA_PROJECT = new LocalFileType("JProj",
			"Java Project", 4) {
		public ILocalLocation newLocation(Object obj) {
			if (!(obj instanceof IJavaProject))
				return null;
			return new LocalJavaFile(this, (IJavaProject) obj);
		}
	};

	public static final LocalFileType JAVA_PACKAGE_ROOT = new LocalFileType(
			"JPkgRoot", "Java Package Root", 5) {

		public ILocalLocation newLocation(Object obj) {
			if (!(obj instanceof IPackageFragmentRoot))
				return null;
			return new LocalJavaFile(this, (IPackageFragmentRoot) obj);
		}
	};

	public static final LocalFileType JAVA_PACKAGE = new LocalFileType("JPkg",
			"Java Package", 6) {

		public ILocalLocation newLocation(Object obj) {
			if (!(obj instanceof IPackageFragment))
				return null;
			return new LocalJavaFile(this, (IPackageFragment) obj);
		}
	};

	public static final LocalFileType JAVA_CLASS_FILE = new LocalFileType(
			"JClass", "Java Class File", 7) {

		public ILocalLocation newLocation(Object obj) {
			if (!(obj instanceof IClassFile))
				return null;
			return new LocalJavaFile(this, (IClassFile) obj);
		}
	};

	public static final LocalFileType JAVA_COMP_UNIT = new LocalFileType(
			"JCompUnit", "Java Compilation Unit", 8) {

		public ILocalLocation newLocation(Object obj) {
			if (!(obj instanceof ICompilationUnit))
				return null;
			return new LocalJavaFile(this, (ICompilationUnit) obj);
		}
	};

	public static final LocalFileType JAVA_INTERFACE = new LocalFileType(
			"JInterface", "Java Interface", 9) {

		public ILocalLocation newLocation(Object obj) {
			if (!(obj instanceof IType))
				return null;
			try {
				if (!((IType) obj).isInterface())
					return null;
			} catch (JavaModelException e) {

			}
			return new LocalJavaFile(this, (IType) obj);
		}
	};

	public static final LocalFileType JAVA_CLASS = new LocalFileType("JClass",
			"Java Class", 10) {

		public ILocalLocation newLocation(Object obj) {
			if (!(obj instanceof IType))
				return null;
			try {
				if (((IType) obj).isInterface())
					return null;
			} catch (JavaModelException e) {

			}
			return new LocalJavaFile(this, (IType) obj);
		}
	};

	private static final LocalFileType[] TYPES = { UNKNOWN, WORKBENCH_FILE,
			WORKBENCH_FOLDER, WORKBENCH_PROJECT, JAVA_PROJECT,
			JAVA_PACKAGE_ROOT, JAVA_PACKAGE, JAVA_CLASS_FILE, JAVA_COMP_UNIT,
			JAVA_INTERFACE, JAVA_CLASS };

	public static LocalFileType[] getTypes() {
		return TYPES;
	}
}
