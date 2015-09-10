package com.abhsinh2.scpplugin.ui.wizard;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
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

public class EnterNewLocationWizardPage extends WizardPage implements Listener {
	private Text nameTextBox;
	private Text remoteMachineTextBox;
	private Text remoteLocationTextBox;
	private Text usernameTextBox;
	private Text passwordTextBox;
	private Button savePasswordButton;
	
	SCPLocationManager manager = SCPLocationManager.getManager();

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
		nameLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		nameLabel.setText("Name:");
		
		nameTextBox = new Text(container, SWT.BORDER);
		nameTextBox.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		final Label label_1 = new Label(container, SWT.NONE);
	    final GridData gridData_1 = new GridData();
	    gridData_1.horizontalSpan = 2;
	    label_1.setLayoutData(gridData_1);
		
		Label remoteMachineLabel = new Label(container, SWT.NONE);
		remoteMachineLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		remoteMachineLabel.setText("Remote Machine:");
		
		remoteMachineTextBox = new Text(container, SWT.BORDER);
		remoteMachineTextBox.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		final Label label_2 = new Label(container, SWT.NONE);
	    final GridData gridData_2 = new GridData();
	    gridData_2.horizontalSpan = 2;
	    label_2.setLayoutData(gridData_2);
		
		Label remoteLocationLabel = new Label(container, SWT.NONE);
		remoteLocationLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		remoteLocationLabel.setText("Remote Location:");
		
		remoteLocationTextBox = new Text(container, SWT.BORDER);
		remoteLocationTextBox.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		final Label label_3 = new Label(container, SWT.NONE);
	    final GridData gridData_3 = new GridData();
	    gridData_3.horizontalSpan = 3;
	    label_3.setLayoutData(gridData_3);
		
		Label usernameLabel = new Label(container, SWT.NONE);
		usernameLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		usernameLabel.setText("Username:");
		
		usernameTextBox = new Text(container, SWT.BORDER);
		usernameTextBox.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		final Label label_4 = new Label(container, SWT.NONE);
	    final GridData gridData_4 = new GridData();
	    gridData_4.horizontalSpan = 2;
	    label_4.setLayoutData(gridData_4);
		
		Label passwordLabel = new Label(container, SWT.NONE);
		passwordLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		passwordLabel.setText("Password:");
		
		passwordTextBox = new Text(container, SWT.BORDER | SWT.PASSWORD);
		passwordTextBox.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
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
		return new SCPRemoteLocation(
				this.getRemoteMachine(), 
				this.getRemoteLocation(), 
				this.getRemoteUsername(), 
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
	
}
