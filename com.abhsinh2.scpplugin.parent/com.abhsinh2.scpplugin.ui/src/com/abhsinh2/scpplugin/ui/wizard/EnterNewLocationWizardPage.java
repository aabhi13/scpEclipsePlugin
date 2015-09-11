package com.abhsinh2.scpplugin.ui.wizard;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

import com.abhsinh2.scpplugin.ui.model.SCPLocationManager;
import com.abhsinh2.scpplugin.ui.model.remote.SCPRemoteLocation;
import org.eclipse.wb.swt.SWTResourceManager;

public class EnterNewLocationWizardPage extends WizardPage implements Listener,
		VerifyListener, FocusListener, ModifyListener {
	private Text nameTextBox;
	private Text remoteMachineTextBox;
	private Text remoteLocationTextBox;
	private Text usernameTextBox;
	private Text passwordTextBox;
	private Button savePasswordButton;
	private Label errorMessageLabel;
	private boolean canFlipToNextPage = false;

	SCPLocationManager manager = SCPLocationManager.getManager();

	private static final String IPADDRESS_PATTERN = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
			+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
			+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
			+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

	protected EnterNewLocationWizardPage(String pageName) {
		super(pageName);
	}
	
	/**
	 * @wbp.parser.constructor
	 */
	public EnterNewLocationWizardPage() {
		this("Enter New Location");
		setTitle("Enter New Location");
		setDescription("Enter New Location");
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
		nameTextBox.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
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
		passwordTextBox.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		passwordTextBox.setEnabled(false);
		new Label(container, SWT.NONE);

		savePasswordButton = new Button(container, SWT.CHECK);
		savePasswordButton.setText("Save Password");
		savePasswordButton.addListener(SWT.Selection, this);

		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);

		errorMessageLabel = new Label(container, SWT.NONE);
		errorMessageLabel.setForeground(SWTResourceManager
				.getColor(SWT.COLOR_RED));
		GridData gridData_5 = new GridData(SWT.LEFT, SWT.CENTER, false, false,
				1, 1);
		gridData_5.widthHint = 474;
		errorMessageLabel.setLayoutData(gridData_5);
		errorMessageLabel.setText("Please fill the required fields");
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
		if (this.usernameTextBox != null
				&& (this.usernameTextBox.getText() == null || this.usernameTextBox
						.getText().trim().isEmpty())) {
			return false;
		}

		if (this.remoteLocationTextBox != null
				&& (this.remoteLocationTextBox.getText() == null || this.remoteLocationTextBox
						.getText().trim().isEmpty())) {
			return false;
		}
		return canFlipToNextPage;
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
				errorMessageLabel
						.setText("Name already exists. Go to SCP View and delete the name");
			} else {
				canFlipToNextPage = true;
				errorMessageLabel.setText("");
			}
		} else if (source == remoteMachineTextBox) {
			String givenName = remoteMachineTextBox.getText();
			Pattern pattern = Pattern.compile(IPADDRESS_PATTERN);
			Matcher matcher = pattern.matcher(givenName);
			if (matcher.matches()) {
				canFlipToNextPage = true;
				errorMessageLabel.setText("");
			} else {
				canFlipToNextPage = false;
				errorMessageLabel.setText("Incorrect IP Address.");
			}
		} else if (source == remoteLocationTextBox) {
			if (!isEmpty(remoteLocationTextBox)) {
				canFlipToNextPage = true;
			}
		} else if (source == usernameTextBox) {
			if (!isEmpty(usernameTextBox)) {
				canFlipToNextPage = true;
			}
		}

		getWizard().getContainer().updateButtons();
	}

	@Override
	public void modifyText(ModifyEvent modifyEvent) {
		Widget source = modifyEvent.widget;

		if (source == usernameTextBox) {
			usernameTextBox.removeVerifyListener(this);
			usernameTextBox.removeModifyListener(this);

			if (!isEmpty(usernameTextBox)) {
				canFlipToNextPage = true;
			}

			usernameTextBox.addVerifyListener(this);
			usernameTextBox.addModifyListener(this);
		} else if (source == remoteLocationTextBox) {
			remoteLocationTextBox.removeVerifyListener(this);
			remoteLocationTextBox.removeModifyListener(this);

			if (!isEmpty(remoteLocationTextBox)) {
				canFlipToNextPage = true;
			}

			remoteLocationTextBox.addVerifyListener(this);
			remoteLocationTextBox.addModifyListener(this);
		}

		getWizard().getContainer().updateButtons();
	}

	private boolean isEmpty(Text text) {
		if (text.getText() == null || text.getText().trim().isEmpty()) {
			return true;
		}

		return false;
	}

}
