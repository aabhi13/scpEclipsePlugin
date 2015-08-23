package com.abhsinh2.plugin.scp.dialog;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.abhsinh2.plugin.scp.model.SCPLocationManager;
import com.abhsinh2.plugin.scp.model.SCPLocationManagerEvent;
import com.abhsinh2.plugin.scp.model.SCPLocationManagerListener;
import com.abhsinh2.plugin.scp.model.SCPLocationManagerType;
import com.abhsinh2.plugin.scp.model.remote.SCPLocation;
import com.abhsinh2.plugin.scp.util.SCPCopyLocalToRemote;

public class LocationDialog extends Dialog {

	private Shell parentShell;
	private ExecutionEvent event;
	private SCPLocationManager locationManager = SCPLocationManager
			.getManager();

	private Combo locationsCombo;
	private SCPLocationManagerListener listener;

	private SCPLocation remoteLocation;
	private Collection<String> currentSelectedLocation;

	public static final String REMOTE_MACHINE_STRING = "Remote Machine:";
	public static final String REMOTE_LOCATION_STRING = "Remote Location:";

	private Label alreadySavedLocationLabel;
	private Label remoteMachineLabel;
	private Label remoteLocationLabel;

	private Button useSavedLocationRadioBtn;
	private Button useCurrentSelectionRadioBtn;
	private Button addNewLocationBtn;

	private boolean useCurrentSelection = true;
	private boolean useSavedLocation = false;
	
	private FormData fd_useCurrentSelectionBtn;
	private Button useSavedLocationBtn;
	private FormData fd_btnSaveCurrentSelection;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public LocationDialog(Shell parentShell, ExecutionEvent event, Collection<String> localLocations) {
		super(parentShell);

		this.parentShell = parentShell;
		this.event = event;
		this.currentSelectedLocation = localLocations;
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {

		locationManager.loadLocations();

		final Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new FormLayout());

		createSavedLocationComboBox(container);
		createLocalFileSelectionButtons(container);
		createSaveLocationCheckBox(container);
		createRemoteLocationDetailsLabels(container);
		createNewLocationButton(container);

		addLocationSelectionListener();
		addSCPLocationManagerListener();

		return container;
	}

	private void createSavedLocationComboBox(Composite container) {
		alreadySavedLocationLabel = new Label(container, SWT.NONE);
		FormData fd_lblLocationName = new FormData();
		fd_lblLocationName.left = new FormAttachment(0, 21);
		alreadySavedLocationLabel.setLayoutData(fd_lblLocationName);
		alreadySavedLocationLabel.setText("Location Name:");

		locationsCombo = new Combo(container, SWT.NONE);
		fd_lblLocationName.top = new FormAttachment(locationsCombo, 4, SWT.TOP);
		FormData fd_locationsCombo = new FormData();
		fd_locationsCombo.top = new FormAttachment(0, 52);
		fd_locationsCombo.right = new FormAttachment(100, -96);
		fd_locationsCombo.left = new FormAttachment(alreadySavedLocationLabel,
				6);
		locationsCombo.setLayoutData(fd_locationsCombo);

		fd_locationsCombo.bottom = new FormAttachment(100, -248);

		for (SCPLocation location : locationManager.getLocations()) {
			locationsCombo.add(location.getName());
		}
	}

	private void createLocalFileSelectionButtons(Composite container) {
		useSavedLocationRadioBtn = new Button(container, SWT.CHECK);
		fd_btnSaveCurrentSelection = new FormData();
		fd_btnSaveCurrentSelection.left = new FormAttachment(alreadySavedLocationLabel, 0, SWT.LEFT);
		useSavedLocationRadioBtn.setLayoutData(fd_btnSaveCurrentSelection);
		useSavedLocationRadioBtn.setText("Save Current Selection");
		useSavedLocationRadioBtn.setSelection(useSavedLocation);
		useSavedLocationRadioBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Button button = ((Button) e.widget);
				if (button.getSelection())
					useSavedLocation = true;
				else
					useSavedLocation = false;
			}
		});

		useCurrentSelectionRadioBtn = new Button(container, SWT.RADIO);
		fd_useCurrentSelectionBtn = new FormData();
		fd_useCurrentSelectionBtn.left = new FormAttachment(0, 27);
		fd_useCurrentSelectionBtn.top = new FormAttachment(locationsCombo, 28);
		useCurrentSelectionRadioBtn.setLayoutData(fd_useCurrentSelectionBtn);
		useCurrentSelectionRadioBtn.setText("Use Current Selection");
		useCurrentSelectionRadioBtn.setSelection(useCurrentSelection);
		useCurrentSelectionRadioBtn
				.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						Button button = ((Button) e.widget);
						if (button.getSelection())
							useCurrentSelection = true;
						else
							useCurrentSelection = false;
					}
				});
	}

	private void createSaveLocationCheckBox(Composite container) {
		useSavedLocationBtn = new Button(container, SWT.RADIO);
		useSavedLocationBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				useSavedLocation = true;
			}
		});
		FormData fd_useSavedLocationBtn = new FormData();
		fd_useSavedLocationBtn.top = new FormAttachment(useCurrentSelectionRadioBtn, 0, SWT.TOP);
		fd_useSavedLocationBtn.left = new FormAttachment(useCurrentSelectionRadioBtn, 75);
		fd_useSavedLocationBtn.right = new FormAttachment(100, -240);
		useSavedLocationBtn.setLayoutData(fd_useSavedLocationBtn);
		useSavedLocationBtn.setText("Use Saved Location");
		useSavedLocationBtn.setSelection(useSavedLocation);
	}

	private void createRemoteLocationDetailsLabels(Composite container) {
		remoteMachineLabel = new Label(container, SWT.NONE);
		FormData fd_remoteMachineLabel = new FormData();
		fd_remoteMachineLabel.top = new FormAttachment(useCurrentSelectionRadioBtn, 18);
		fd_remoteMachineLabel.right = new FormAttachment(100, -66);
		fd_remoteMachineLabel.left = new FormAttachment(0, 21);
		remoteMachineLabel.setLayoutData(fd_remoteMachineLabel);
		remoteMachineLabel.setText(REMOTE_MACHINE_STRING);

		remoteLocationLabel = new Label(container, SWT.NONE);
		fd_btnSaveCurrentSelection.top = new FormAttachment(remoteLocationLabel, 29);
		FormData fd_remoteLocationLabel = new FormData();
		fd_remoteLocationLabel.top = new FormAttachment(remoteMachineLabel, 24);
		fd_remoteLocationLabel.right = new FormAttachment(remoteMachineLabel, 0, SWT.RIGHT);
		fd_remoteLocationLabel.left = new FormAttachment(0, 21);
		remoteLocationLabel.setLayoutData(fd_remoteLocationLabel);
		remoteLocationLabel.setText(REMOTE_LOCATION_STRING);
	}

	private void createNewLocationButton(Composite container) {
		addNewLocationBtn = new Button(container, SWT.NONE);
		FormData fd_btnAddNewLocation = new FormData();
		fd_btnAddNewLocation.bottom = new FormAttachment(100, -10);
		fd_btnAddNewLocation.left = new FormAttachment(
				alreadySavedLocationLabel, 0, SWT.LEFT);
		addNewLocationBtn.setLayoutData(fd_btnAddNewLocation);
		addNewLocationBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				NewLocationDialog newDialog = new NewLocationDialog(
						parentShell, currentSelectedLocation, LocationDialog.this);
				newDialog.open();
			}
		});
		addNewLocationBtn.setText("Add New Location");
	}

	private void addLocationSelectionListener() {
		locationsCombo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				SCPLocation location = locationManager
						.getLocation(locationsCombo.getText());
				if (location != null) {
					remoteLocation = location;
					remoteMachineLabel.setText(REMOTE_MACHINE_STRING + " "
							+ location.getRemoteLocation().getRemoteAddress());
					remoteLocationLabel.setText(REMOTE_LOCATION_STRING + " "
							+ location.getRemoteLocation().getRemoteLocation());
				}
			}
		});
	}

	private void addSCPLocationManagerListener() {
		if (listener == null) {
			listener = new SCPLocationManagerListener() {
				@Override
				public void locationChanged(SCPLocationManagerEvent event) {
					if (event.getEventType() == SCPLocationManagerType.ADDED) {
						SCPLocation location = event.getLocation();
						
						if (!parentShell.isDisposed()) {
							locationsCombo.add(location.getName());
							locationsCombo.setText(location.getName());
							
							remoteLocation = location;
						}						
					}
				}
			};

			locationManager.addLocationManagerListener(listener);
		}
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	@Override
	protected void okPressed() {
		
		if (useCurrentSelection) {
			//staryCopying(currentSelectedLocation);
			startCopyingInProgressDialog(currentSelectedLocation);
		} else if (useSavedLocation) {
			//staryCopying(remoteLocation.getLocalFiles());
			startCopyingInProgressDialog(remoteLocation.getLocalFiles());
		}
		
		super.okPressed();
	}

	private IStatus staryCopying(final Collection<String> fileList) {
		if (fileList == null) {
			System.out.println("Cancelling");
			return Status.CANCEL_STATUS;
		}

		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				for (String localLocation : fileList) {
					SCPCopyLocalToRemote copy = new SCPCopyLocalToRemote(
							localLocation, remoteLocation.getRemoteLocation()
									.getRemoteAddress(), remoteLocation
									.getRemoteLocation().getRemoteLocation(),
							remoteLocation.getRemoteLocation().getUsername(),
							remoteLocation.getRemoteLocation().getPassword());
					
					try {
						copy.copy();
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
			}
		});
		
		return Status.OK_STATUS;
	}
	
	private IStatus startCopyingWithJob(final Collection<String> fileList) {
		if (fileList == null) {
			System.out.println("Cancelling");
			return Status.CANCEL_STATUS;
		}

		Job job = new Job("Copying Files") {
			protected IStatus run(final IProgressMonitor monitor) {
				Display.getDefault().asyncExec(new Runnable() {
					public void run() {						
						try {
							monitor.beginTask("Preparing", fileList.size());
							
							for (String localLocation : fileList) {
								monitor.subTask("Copying " + localLocation);
								
								//SubMonitor subMonitor = SubMonitor.convert(
								//		monitor, "Copying " + localLocation,
								//		1);
								
								//subMonitor.subTask("Doing something");
								
								SCPCopyLocalToRemote copy = new SCPCopyLocalToRemote(
										localLocation, remoteLocation
												.getRemoteLocation()
												.getRemoteAddress(), remoteLocation
												.getRemoteLocation()
												.getRemoteLocation(),
										remoteLocation.getRemoteLocation()
												.getUsername(), remoteLocation
												.getRemoteLocation().getPassword());

								copy.copy();
								
								monitor.worked(1);
								
								if (monitor.isCanceled()) {
									break;
									
									/*
									Display.getDefault().asyncExec(
											new Runnable() {
												@Override
												public void run() {
													
												}
											});
									*/
								}
							}
						} finally {
							monitor.done();
						}
					}
				});
				return Status.OK_STATUS;
			}
		};
		job.schedule();

		return Status.OK_STATUS;
	}
	
	private void startCopyingInProgressDialog(final Collection<String> fileList) {
		// Execute the operation

		try {
			// Display progress either using the ProgressMonitorDialog ...

			// Shell shell = HandlerUtil.getActiveShell(event);
			// IRunnableContext context = new ProgressMonitorDialog(shell);
			// ... or using the window's status bar ...

			// IWorkbenchWindow context = HandlerUtil.getActiveWorkbenchWindow(event);
			// ... or using the workbench progress service

			IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
			IRunnableContext context = window.getWorkbench().getProgressService();
			context.run(true, false, new IRunnableWithProgress() {
				public void run(IProgressMonitor monitor)
						throws InvocationTargetException, InterruptedException {
					try {
						monitor.beginTask("Preparing", fileList.size());

						for (String localLocation : fileList) {
							monitor.subTask("Copying " + localLocation);

							SCPCopyLocalToRemote copy = new SCPCopyLocalToRemote(
									localLocation, remoteLocation
											.getRemoteLocation()
											.getRemoteAddress(), remoteLocation
											.getRemoteLocation()
											.getRemoteLocation(),
									remoteLocation.getRemoteLocation()
											.getUsername(), remoteLocation
											.getRemoteLocation().getPassword());

							copy.copy();

							monitor.worked(1);

							if (monitor.isCanceled()) {
								break;
							}
						}
						
						monitor.done();
					} finally {
						//monitor.done();
					}

				}
			});
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	private IStatus startCopyingWithIProgressService(final Collection<String> fileList) {
		if (fileList == null) {
			System.out.println("Cancelling");
			return Status.CANCEL_STATUS;
		}
		
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		try {
			window.getWorkbench().getProgressService().run(true, true,
					   new IRunnableWithProgress() {
					      public void run(IProgressMonitor monitor)
					         throws InvocationTargetException, InterruptedException {
					    	  try {
									monitor.beginTask("Preparing", fileList.size());

									for (String localLocation : fileList) {
										monitor.subTask("Copying " + localLocation);

										SCPCopyLocalToRemote copy = new SCPCopyLocalToRemote(
												localLocation, remoteLocation
														.getRemoteLocation()
														.getRemoteAddress(), remoteLocation
														.getRemoteLocation()
														.getRemoteLocation(),
												remoteLocation.getRemoteLocation()
														.getUsername(), remoteLocation
														.getRemoteLocation().getPassword());

										copy.copy();

										monitor.worked(1);

										if (monitor.isCanceled()) {
											break;
										}
									}
									
									monitor.done();
								} finally {
									//monitor.done();
								}
					      }
					});
		} catch (InvocationTargetException | InterruptedException e) {
			e.printStackTrace();
		}

		return Status.OK_STATUS;
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(650, 415);
	}

	public Combo getLocationsCombo() {
		return locationsCombo;
	}

	public SCPLocation getRemoteLocation() {
		return remoteLocation;
	}

	public void setRemoteLocation(SCPLocation remoteLocation) {
		this.remoteLocation = remoteLocation;
	}
}
