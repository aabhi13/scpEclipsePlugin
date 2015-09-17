package com.abhsinh2.scpplugin.ui.wizard;

import java.util.ArrayList;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Widget;

import com.abhsinh2.scpplugin.ui.util.Utility;

/**
 * Wizard page to add/edit local files to be copied to remote machine.
 * 
 * @author abhsinh2
 * 
 */
public class SelectLocalFilesWizardPage extends WizardPage implements Listener {

	private List localFilesList;
	private Button removeSelectedFilesButton;
	private Button addNewFilesButton;

	private java.util.List<String> finalLocalFileList = new ArrayList<String>();
	private IStructuredSelection selection;

	protected SelectLocalFilesWizardPage(String pageName) {
		super(pageName);
	}

	/**
	 * @wbp.parser.constructor
	 */
	public SelectLocalFilesWizardPage(IStructuredSelection selection) {
		this("Select Local Files");
		this.selection = selection;
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);

		localFilesList = new List(container, SWT.BORDER);
		localFilesList.setBounds(21, 55, 529, 152);

		Label selectedLocalFilesLabel = new Label(container, SWT.NONE);
		selectedLocalFilesLabel.setBounds(21, 25, 120, 14);
		selectedLocalFilesLabel.setText("Selected Local Files:");

		removeSelectedFilesButton = new Button(container, SWT.NONE);
		removeSelectedFilesButton.setBounds(21, 224, 163, 28);
		removeSelectedFilesButton.setText("Remove Selected Files");
		removeSelectedFilesButton.addListener(SWT.Selection, this);

		addNewFilesButton = new Button(container, SWT.NONE);
		addNewFilesButton.setBounds(217, 224, 128, 28);
		addNewFilesButton.setText("Add New Files");
		addNewFilesButton.addListener(SWT.Selection, this);

		populateSWTList();
	}

	private void populateSWTList() {
		for (String filepath : Utility.getSelectedFiles(this.selection)) {
			localFilesList.add(filepath);
			finalLocalFileList.add(filepath);
		}
	}

	@Override
	public void handleEvent(Event event) {
		Widget source = event.widget;
		if (source == removeSelectedFilesButton) {
			removeSelectedFilesFromList();
		}

		if (source == addNewFilesButton) {
			addFileToList();
		}
	}

	private void removeSelectedFilesFromList() {
		String[] selectedFilesFromList = this.localFilesList.getSelection();
		for (String selectedFile : selectedFilesFromList) {
			this.localFilesList.remove(selectedFile);
			this.finalLocalFileList.remove(selectedFile);
		}
	}

	private void addFileToList() {
		browseForLocalFile();
	}

	/**
	 * Open a file browser dialog to locate a file
	 */
	protected void browseForLocalFile() {
		IPath path = browse(getLocalLocation(), false);
		if (path == null)
			return;
		IPath rootLoc = ResourcesPlugin.getWorkspace().getRoot().getLocation();
		if (rootLoc.isPrefixOf(path))
			path = path.setDevice(null).removeFirstSegments(
					rootLoc.segmentCount());

		this.localFilesList.add(path.toString());
		this.finalLocalFileList.add(path.toString());
	}

	/**
	 * Open a file dialog for selecting a file
	 */
	private IPath browse(IPath path, boolean mustExist) {
		FileDialog dialog = new FileDialog(getShell(), mustExist ? SWT.OPEN
				: SWT.SAVE);
		if (path != null) {
			if (path.segmentCount() > 1)
				dialog.setFilterPath(path.removeLastSegments(1).toOSString());
			if (path.segmentCount() > 0)
				dialog.setFileName(path.lastSegment());
		}
		String result = dialog.open();
		if (result == null)
			return null;
		return new Path(result);
	}

	public IPath getLocalLocation() {
		return ResourcesPlugin.getWorkspace().getRoot().getLocation();		
	}

	public java.util.List<String> getLocalFilesPath() {
		return this.finalLocalFileList;
	}
	
	@Override
	public boolean isPageComplete() {
		return !finalLocalFileList.isEmpty();
	}
}
