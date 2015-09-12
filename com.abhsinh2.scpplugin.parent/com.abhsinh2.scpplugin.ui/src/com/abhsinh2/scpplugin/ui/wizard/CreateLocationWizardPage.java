package com.abhsinh2.scpplugin.ui.wizard;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;

import com.abhsinh2.scpplugin.ui.model.SCPLocation;
import com.abhsinh2.scpplugin.ui.model.SCPLocationManager;
import com.abhsinh2.scpplugin.ui.model.remote.SCPRemoteLocation;

public class CreateLocationWizardPage extends WizardPage implements Listener,
		VerifyListener, FocusListener, ModifyListener {
	private Text nameTextBox;
	private Text remoteMachineTextBox;
	private Text remoteLocationTextBox;
	private Text usernameTextBox;
	private Text passwordTextBox;
	private Button savePasswordButton;
	private boolean canFlipToNextPage = false;

	private SCPLocationManager manager = SCPLocationManager.getManager();
	private SCPLocation location;
	private Status status;

	private static final String IPADDRESS_PATTERN = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
			+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
			+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
			+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

	protected CreateLocationWizardPage(String pageName) {
		super(pageName);

		status = new Status(IStatus.OK, "not_used", 0, "", null);
	}

	/**
	 * @wbp.parser.constructor
	 */
	public CreateLocationWizardPage() {
		this("Create Location");
	}

	public CreateLocationWizardPage(SCPLocation location) {
		this("Edit Location");
		this.location = location;
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		container.setLayout(gridLayout);
		setControl(container);

		Label nameLabel = new Label(container, SWT.NONE);
		nameLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		nameLabel.setText("Name*:");

		nameTextBox = new Text(container, SWT.BORDER);
		if (this.location != null) {
			nameTextBox.setText(this.location.getName());
		}
		nameTextBox.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		nameTextBox.setFocus();
		nameTextBox.addFocusListener(this);

		final Label label_1 = new Label(container, SWT.NONE);
		final GridData gridData_1 = new GridData();
		gridData_1.horizontalSpan = 2;
		label_1.setLayoutData(gridData_1);

		Label remoteMachineLabel = new Label(container, SWT.NONE);
		remoteMachineLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				false, false, 1, 1));
		remoteMachineLabel.setText("Remote Machine*:");

		remoteMachineTextBox = new Text(container, SWT.BORDER);
		if (this.location != null) {
			remoteMachineTextBox.setText(this.location.getRemoteLocation()
					.getRemoteAddress());
		}
		remoteMachineTextBox.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 1, 1));
		remoteMachineTextBox.addFocusListener(this);

		final Label label_2 = new Label(container, SWT.NONE);
		final GridData gridData_2 = new GridData();
		gridData_2.horizontalSpan = 2;
		label_2.setLayoutData(gridData_2);

		Label remoteLocationLabel = new Label(container, SWT.NONE);
		remoteLocationLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				false, false, 1, 1));
		remoteLocationLabel.setText("Remote Location*:");

		remoteLocationTextBox = new Text(container, SWT.BORDER);
		if (this.location != null) {
			remoteLocationTextBox.setText(this.location.getRemoteLocation()
					.getRemoteLocation());
		}
		remoteLocationTextBox.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 1, 1));
		remoteLocationTextBox.addModifyListener(this);
		remoteLocationTextBox.addFocusListener(this);

		final Label label_3 = new Label(container, SWT.NONE);
		final GridData gridData_3 = new GridData();
		gridData_3.horizontalSpan = 3;
		label_3.setLayoutData(gridData_3);

		Label usernameLabel = new Label(container, SWT.NONE);
		usernameLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		usernameLabel.setText("Username*:");

		usernameTextBox = new Text(container, SWT.BORDER);
		if (this.location != null) {
			usernameTextBox.setText(this.location.getRemoteLocation()
					.getUsername());
		}
		usernameTextBox.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		usernameTextBox.addModifyListener(this);
		usernameTextBox.addFocusListener(this);

		final Label label_4 = new Label(container, SWT.NONE);
		final GridData gridData_4 = new GridData();
		gridData_4.horizontalSpan = 2;
		label_4.setLayoutData(gridData_4);

		Label passwordLabel = new Label(container, SWT.NONE);
		passwordLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		passwordLabel.setText("Password:");

		passwordTextBox = new Text(container, SWT.BORDER | SWT.PASSWORD);
		if (this.location != null) {
			passwordTextBox.setText(this.location.getRemoteLocation()
					.getPassword());
		}
		passwordTextBox.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		passwordTextBox.setEnabled(false);
		new Label(container, SWT.NONE);

		savePasswordButton = new Button(container, SWT.CHECK);
		savePasswordButton.setText("Save Password");
		savePasswordButton.addListener(SWT.Selection, this);
	}

	public String getLocationName() {
		return nameTextBox.getText();
	}

	public String getRemoteMachine() {
		return remoteMachineTextBox.getText();
	}

	public String getRemoteLocation() {
		return remoteLocationTextBox.getText();
	}

	public String getRemoteUsername() {
		return usernameTextBox.getText();
	}

	public String getRemotePassword() {
		return passwordTextBox.getText();
	}

	public SCPRemoteLocation getSCPRemoteLocation() {
		return new SCPRemoteLocation(this.getRemoteMachine(),
				this.getRemoteLocation(), this.getRemoteUsername(),
				this.getRemotePassword());
	}

	@Override
	public void handleEvent(Event event) {
		Widget source = event.widget;

		if (source == savePasswordButton) {
			if (savePasswordButton.getSelection()) {
				passwordTextBox.setEnabled(true);
			} else {
				passwordTextBox.setEnabled(false);
			}
		}

	}

	@Override
	public boolean canFlipToNextPage() {		
		if (getErrorMessage() != null) 
			return false;
		
		if (this.isTextEmpty(usernameTextBox)) {
			return false;
		}

		if (this.isTextEmpty(remoteLocationTextBox)) {
			return false;
		}
		
		return true;
	}

	@Override
	public void verifyText(VerifyEvent verifyEvent) {

	}

	@Override
	public void focusGained(FocusEvent e) {

	}

	@Override
	public void focusLost(FocusEvent focusEvent) {
		Widget source = focusEvent.widget;

		if (source == nameTextBox) {
			String givenName = nameTextBox.getText();
			if (manager.getLocation(givenName) != null) {
				canFlipToNextPage = false;
				status = new Status(
						IStatus.ERROR,
						"not_used",
						0,
						"Name already exists. Go to SCP View and delete the name",
						null);
			} else {
				canFlipToNextPage = true;
				status = new Status(IStatus.OK, "not_used", 0, "", null);
			}
		} else if (source == remoteMachineTextBox) {
			String givenName = remoteMachineTextBox.getText();
			Pattern pattern = Pattern.compile(IPADDRESS_PATTERN);
			Matcher matcher = pattern.matcher(givenName);
			if (matcher.matches()) {
				canFlipToNextPage = true;
				status = new Status(IStatus.OK, "not_used", 0, "", null);
			} else {
				canFlipToNextPage = false;
				status = new Status(IStatus.ERROR, "not_used", 0,
						"Incorrect IP Address.", null);
			}
		} else if (source == remoteLocationTextBox) {
			if (!isTextEmpty(remoteLocationTextBox)) {
				canFlipToNextPage = true;
			}
		} else if (source == usernameTextBox) {
			if (!isTextEmpty(usernameTextBox)) {
				canFlipToNextPage = true;
			}
		}

		applyStatusLine();
		getWizard().getContainer().updateButtons();
	}

	@Override
	public void modifyText(ModifyEvent modifyEvent) {
		Widget source = modifyEvent.widget;

		if (source == usernameTextBox) {
			usernameTextBox.removeVerifyListener(this);
			usernameTextBox.removeModifyListener(this);

			if (!isTextEmpty(usernameTextBox)) {
				canFlipToNextPage = true;
			}

			usernameTextBox.addVerifyListener(this);
			usernameTextBox.addModifyListener(this);
		} else if (source == remoteLocationTextBox) {
			remoteLocationTextBox.removeVerifyListener(this);
			remoteLocationTextBox.removeModifyListener(this);

			if (!isTextEmpty(remoteLocationTextBox)) {
				canFlipToNextPage = true;
			}

			remoteLocationTextBox.addVerifyListener(this);
			remoteLocationTextBox.addModifyListener(this);
		}

		applyStatusLine();
		getWizard().getContainer().updateButtons();

		// MessageDialog.openInformation(this.getShell(),"", "Flight price "+
		// price);
		// MessageDialog.openInformation(workbench.getActiveWorkbenchWindow().getShell(),
		// "Holiday info", summary);
	}

	private boolean isTextEmpty(Text text) {
		if (text == null || text.getText() == null || text.getText().trim().isEmpty()) {
			return true;
		}
		return false;
	}

	private void applyStatusLine() {
		if (!status.isOK())
			setErrorMessage(status.getMessage());
		else
			setErrorMessage(null);
	}

}
