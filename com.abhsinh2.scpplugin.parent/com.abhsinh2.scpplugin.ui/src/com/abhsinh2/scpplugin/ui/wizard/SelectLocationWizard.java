package com.abhsinh2.scpplugin.ui.wizard;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.abhsinh2.scpplugin.ui.Activator;
import com.abhsinh2.scpplugin.ui.SCPLog;
import com.abhsinh2.scpplugin.ui.model.SCPLocation;
import com.abhsinh2.scpplugin.ui.model.SCPLocationManager;
import com.abhsinh2.scpplugin.ui.model.remote.SCPRemoteLocation;
import com.abhsinh2.scpplugin.ui.util.SCPCopyLocalToRemote;
import com.abhsinh2.scpplugin.ui.util.Utility;

public class SelectLocationWizard extends Wizard implements INewWizard {

	private IStructuredSelection initialSelection;
	private SelectLocationWizardPage selectLocationWizardPage;
	private CreateLocationWizardPage createLocationWizardPage;
	private SelectLocalFilesWizardPage selectLocalFilesWizardPage;
	private ExecutionEvent executionEvent;

	private SCPLocationManager locationManager = SCPLocationManager
			.getManager();

	private boolean enableFinishButton = false;

	public SelectLocationWizard(ExecutionEvent executionEvent) {
		this.executionEvent = executionEvent;

		IDialogSettings favoritesSettings = Activator.getDefault()
				.getDialogSettings();
		IDialogSettings wizardSettings = favoritesSettings
				.getSection("SelectLocationWizard");
		if (wizardSettings == null)
			wizardSettings = favoritesSettings
					.addNewSection("SelectLocationWizard");
		setDialogSettings(favoritesSettings);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.initialSelection = selection;
	}

	public void addPages() {
		setWindowTitle("SCP");
		
		List<SCPLocation> locations = Utility.getSCPLocations(this.initialSelection);
		if (locations != null && locations.size() > 0) {
			// Edit command from View
			createLocationWizardPage = new CreateLocationWizardPage(locations.get(0));
			addPage(createLocationWizardPage);			
		} else {
			if (locationManager.getAllLocations().isEmpty()) {
				createLocationWizardPage = new CreateLocationWizardPage();
				addPage(createLocationWizardPage);
			} else {
				selectLocationWizardPage = new SelectLocationWizardPage(
						this.initialSelection);
				addPage(selectLocationWizardPage);

				createLocationWizardPage = new CreateLocationWizardPage();
				addPage(createLocationWizardPage);
			}
		}		

		selectLocalFilesWizardPage = new SelectLocalFilesWizardPage(
				this.initialSelection);
		addPage(selectLocalFilesWizardPage);
	}

	@Override
	public boolean needsPreviousAndNextButtons() {
		return true;
	}

	@Override
	public boolean canFinish() {
		if (selectLocationWizardPage != null) {
			if (!selectLocationWizardPage.isAddNewLocation()
					&& selectLocationWizardPage.getLocation() != null) {
				return true;
			}
		}
		return enableFinishButton;
	}

	@Override
	public boolean performFinish() {
		SCPRemoteLocation remoteLocation = null;
		Collection<String> localFiles = null;

		if (selectLocationWizardPage == null) {
			remoteLocation = createLocationWizardPage.getSCPRemoteLocation();
			localFiles = selectLocalFilesWizardPage.getLocalFilesPath();

			SCPLocation location = new SCPLocation(
					createLocationWizardPage.getLocationName(), localFiles,
					remoteLocation);
			locationManager.addLocation(location);
		} else {
			if (selectLocationWizardPage.isAddNewLocation()) {
				remoteLocation = createLocationWizardPage
						.getSCPRemoteLocation();

				if (selectLocationWizardPage.isUseSavedLocalFiles()) {
					localFiles = selectLocalFilesWizardPage.getLocalFilesPath();
				} else {
					localFiles = selectLocationWizardPage
							.getSelectedLocalFiles();
				}
			} else {
				SCPLocation location = selectLocationWizardPage.getLocation();
				remoteLocation = location.getRemoteLocation();

				if (selectLocationWizardPage.isUseSavedLocalFiles()) {
					localFiles = location.getLocalFiles();
				} else {
					localFiles = selectLocationWizardPage
							.getSelectedLocalFiles();
				}
			}
		}

		this.startCopying(remoteLocation, localFiles);

		return true;
	}

	private boolean startCopying(final SCPRemoteLocation remoteLocation,
			final Collection<String> localFiles) {
		try {
			getContainer().run(true, true, new IRunnableWithProgress() {
				public void run(IProgressMonitor monitor)
						throws InvocationTargetException, InterruptedException {
					performOperation(remoteLocation, localFiles, monitor);
				}
			});
		} catch (InvocationTargetException e) {
			SCPLog.logError(e);
			return false;
		} catch (InterruptedException e) {
			SCPLog.logError(e);
			return false;
		}

		return true;
	}

	private void performOperation(final SCPRemoteLocation remoteLocation,
			final Collection<String> localFiles, IProgressMonitor monitor) {
		try {
			monitor.beginTask("Preparing", localFiles.size());

			for (String localLocation : localFiles) {
				monitor.subTask("Copying " + localLocation);

				SCPCopyLocalToRemote copy = new SCPCopyLocalToRemote(
						localLocation, remoteLocation.getRemoteAddress(),
						remoteLocation.getRemoteLocation(),
						remoteLocation.getUsername(),
						remoteLocation.getPassword());

				// copy.copy();

				monitor.worked(1);

				if (monitor.isCanceled()) {
					break;
				}
			}

			monitor.done();
		} finally {
			// monitor.done();
		}
	}

	private void startCopyingInProgressDialog(
			final SCPRemoteLocation remoteLocation,
			final Collection<String> localFiles) {
		try {
			IWorkbenchWindow window = HandlerUtil
					.getActiveWorkbenchWindow(this.executionEvent);
			IRunnableContext context = window.getWorkbench()
					.getProgressService();
			context.run(true, false, new IRunnableWithProgress() {
				public void run(IProgressMonitor monitor)
						throws InvocationTargetException, InterruptedException {
					performOperation(remoteLocation, localFiles, monitor);
				}
			});
		} catch (Exception e) {
			SCPLog.logError(e);
		}
	}

	public boolean isEnableFinishButton() {
		return enableFinishButton;
	}

	public void setEnableFinishButton(boolean enableFinishButton) {
		this.enableFinishButton = enableFinishButton;
	}

}
