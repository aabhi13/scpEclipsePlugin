package com.abhsinh2.scpplugin.ui.wizard;

import java.util.Collection;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Widget;

import com.abhsinh2.scpplugin.ui.model.SCPLocation;
import com.abhsinh2.scpplugin.ui.model.SCPLocationManager;
import com.abhsinh2.scpplugin.ui.util.Utility;

public class SelectLocationWizardPage extends WizardPage implements Listener {

	private Button selectExistingRemoteLocationButton;
	private Button addNewRemoteLocationButton;
	private Combo selectLocationCombo;
	private Label selectedRemoteMachineValueLabel;
	private Label selectedRemoteLocationValueLabel;
	private Button useSavedLocalFilesButton;
	private Button useSelectedLocalFilesButton;

	private boolean addNewLocation = false;
	private boolean useSavedLocalFiles = true;
	private SCPLocation location;
	private SCPLocationManager locationManager = SCPLocationManager
			.getManager();
	private IStructuredSelection selection;

	protected SelectLocationWizardPage(String pageName) {
		super(pageName);
	}

	/**
	 * @wbp.parser.constructor
	 */
	public SelectLocationWizardPage(IStructuredSelection selection) {
		this("Select Location");
		setTitle("Select Location");
		setDescription("Select saved Location");
		this.selection = selection;
	}

	@Override
	public boolean canFlipToNextPage() {
		return addNewLocation;
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		container.setLayout(gridLayout);
		setControl(container);

		Group remoteLocationSelectionButtonGroup = new Group(container,
				SWT.NONE);
		remoteLocationSelectionButtonGroup.setLayoutData(new GridData(SWT.FILL,
				SWT.FILL, false, false, 2, 1));
		remoteLocationSelectionButtonGroup.setLayout(new GridLayout(2, false));

		selectExistingRemoteLocationButton = new Button(
				remoteLocationSelectionButtonGroup, SWT.RADIO);
		selectExistingRemoteLocationButton.setSelection(true);
		selectExistingRemoteLocationButton.addListener(SWT.Selection, this);
		selectExistingRemoteLocationButton.setText("Select Existing Location");

		addNewRemoteLocationButton = new Button(
				remoteLocationSelectionButtonGroup, SWT.RADIO);
		addNewRemoteLocationButton.addListener(SWT.Selection, this);
		addNewRemoteLocationButton.setText("Add New Location");

		final Label label_1 = new Label(container, SWT.NONE);
		final GridData gridData_1 = new GridData();
		gridData_1.horizontalSpan = 2;
		label_1.setLayoutData(gridData_1);

		Label selectLocationLabel = new Label(container, SWT.NONE);
		selectLocationLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				false, false, 1, 1));
		selectLocationLabel.setText("Select Location:");

		selectLocationCombo = new Combo(container, SWT.READ_ONLY);
		selectLocationCombo.addListener(SWT.Selection, this);
		selectLocationCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 1, 1));

		final Label label_2 = new Label(container, SWT.NONE);
		final GridData gridData_2 = new GridData();
		gridData_2.horizontalSpan = 2;
		label_2.setLayoutData(gridData_2);

		Label selectedRemoteMachineLabel = new Label(container, SWT.NONE);
		selectedRemoteMachineLabel.setText("Selected Remote Machine:");

		selectedRemoteMachineValueLabel = new Label(container, SWT.WRAP);
		GridData fd_remoteMachineLabel = new GridData();
		fd_remoteMachineLabel.widthHint = 426;
		fd_remoteMachineLabel.minimumWidth = 20;
		selectedRemoteMachineValueLabel.setLayoutData(fd_remoteMachineLabel);
		selectedRemoteMachineValueLabel
				.setText("<>");

		final Label label_3 = new Label(container, SWT.NONE);
		final GridData gridData_3 = new GridData();
		gridData_3.horizontalSpan = 2;
		label_3.setLayoutData(gridData_3);

		Label selectedRemoteLocationLabel = new Label(container, SWT.NONE);
		selectedRemoteLocationLabel.setText("Selected Remote Location:");

		selectedRemoteLocationValueLabel = new Label(container, SWT.WRAP);
		GridData gd_selectedRemoteLocationValueLabel = new GridData(SWT.LEFT,
				SWT.CENTER, false, false, 1, 1);
		gd_selectedRemoteLocationValueLabel.widthHint = 426;
		selectedRemoteLocationValueLabel
				.setLayoutData(gd_selectedRemoteLocationValueLabel);
		selectedRemoteLocationValueLabel
				.setText("<>");

		final Label label_4 = new Label(container, SWT.NONE);
		final GridData gridData_4 = new GridData();
		gridData_4.horizontalSpan = 2;
		label_4.setLayoutData(gridData_4);

		Group localFilesSelectionButtonGroup = new Group(container, SWT.NONE);
		localFilesSelectionButtonGroup.setLayoutData(new GridData(SWT.FILL,
				SWT.FILL, false, false, 2, 1));
		localFilesSelectionButtonGroup.setLayout(new GridLayout(2, false));

		useSavedLocalFilesButton = new Button(localFilesSelectionButtonGroup,
				SWT.RADIO);
		useSavedLocalFilesButton.setSelection(true);
		useSavedLocalFilesButton.addListener(SWT.Selection, this);
		useSavedLocalFilesButton.setText("Use saved local files");

		useSelectedLocalFilesButton = new Button(localFilesSelectionButtonGroup,
				SWT.RADIO);
		useSelectedLocalFilesButton.addListener(SWT.Selection, this);
		useSelectedLocalFilesButton.setText("Use current selected files");

		populateComboBox();
	}

	private void populateComboBox() {
		for (SCPLocation location : locationManager.getLocations()) {
			selectLocationCombo.add(location.getName());
		}
	}

	public SCPLocation getLocation() {
		return this.location;
	}

	@Override
	public boolean isPageComplete() {
		return super.isPageComplete();
	}
	
	@Override
	public SelectLocationWizard getWizard() {
		return (SelectLocationWizard)super.getWizard();
	}

	@Override
	public void handleEvent(Event event) {
		Widget source = event.widget;

		if (source == selectExistingRemoteLocationButton) {
			addNewLocation = false;
			
			selectLocationCombo.setEnabled(true);
			useSavedLocalFilesButton.setEnabled(true);
			useSelectedLocalFilesButton.setEnabled(true);
			
			getWizard().getContainer().updateButtons();
		} else if (source == addNewRemoteLocationButton) {
			addNewLocation = true;
			
			selectLocationCombo.setEnabled(false);
			useSavedLocalFilesButton.setEnabled(false);
			useSelectedLocalFilesButton.setEnabled(false);
			
			getWizard().getContainer().updateButtons();
		} else if (source == useSavedLocalFilesButton) {
			useSavedLocalFiles = true;
		} else if (source == useSelectedLocalFilesButton) {
			useSavedLocalFiles = false;
		} else if (source == selectLocationCombo) {
			SCPLocation location = locationManager
					.getLocation(selectLocationCombo.getText());
			if (location != null) {
				this.location = location;
				selectedRemoteMachineValueLabel.setText(location
						.getRemoteLocation().getRemoteAddress());
				selectedRemoteLocationValueLabel.setText(location
						.getRemoteLocation().getRemoteLocation());

				// SelectLocationWizardPage.this.getShell().pack();
				// selectedRemoteMachineValueLabel.;
				// selectedRemoteLocationValueLabel.redraw();
				
				getWizard().getContainer().updateButtons();
			}
		}
	}

	public boolean isUseSavedLocalFiles() {
		return useSavedLocalFiles;
	}

	public boolean isAddNewLocation() {
		return addNewLocation;
	}

	public Collection<String> getSelectedLocalFiles() {
		return Utility.getSelectedFiles(this.selection);
	}

}
