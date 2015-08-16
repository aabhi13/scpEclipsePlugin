package com.abhsinh2.plugin.scp.dialog;

import java.util.Collection;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;

import com.abhsinh2.plugin.scp.model.SCPLocationManager;
import com.abhsinh2.plugin.scp.model.remote.SCPLocation;
import com.abhsinh2.plugin.scp.model.remote.SCPRemoteLocation;

public class NewLocationDialog extends Dialog {
	private Text nameTextBox;
	private Text machineTextBox;
	private Text locationTextBox;
	private Text usernameTextBox;
	private Text passwordTextBox;

	SCPLocationManager manager = SCPLocationManager.getManager();
	Collection<String> currentSelectedLocation;
	
	private LocationDialog prevLocationDialog;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public NewLocationDialog(Shell parentShell,
			Collection<String> localLocations,
			LocationDialog prevLocationDialog) {
		super(parentShell);
		this.currentSelectedLocation = localLocations;
		this.prevLocationDialog = prevLocationDialog;
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new FormLayout());

		Label lblName = new Label(container, SWT.NONE);
		FormData fd_lblName = new FormData();
		fd_lblName.top = new FormAttachment(0, 34);
		fd_lblName.left = new FormAttachment(0, 21);
		lblName.setLayoutData(fd_lblName);
		lblName.setText("Name:");

		nameTextBox = new Text(container, SWT.BORDER);
		FormData fd_nameTextBox = new FormData();
		fd_nameTextBox.top = new FormAttachment(0, 32);
		fd_nameTextBox.left = new FormAttachment(0, 99);
		nameTextBox.setLayoutData(fd_nameTextBox);

		Label lblMachine = new Label(container, SWT.NONE);
		FormData fd_lblMachine = new FormData();
		fd_lblMachine.top = new FormAttachment(0, 81);
		fd_lblMachine.left = new FormAttachment(0, 21);
		lblMachine.setLayoutData(fd_lblMachine);
		lblMachine.setText("Machine:");

		machineTextBox = new Text(container, SWT.BORDER);
		FormData fd_machineTextBox = new FormData();
		fd_machineTextBox.right = new FormAttachment(0, 272);
		fd_machineTextBox.top = new FormAttachment(0, 79);
		fd_machineTextBox.left = new FormAttachment(0, 99);
		machineTextBox.setLayoutData(fd_machineTextBox);

		Label lblLocation = new Label(container, SWT.NONE);
		FormData fd_lblLocation = new FormData();
		fd_lblLocation.top = new FormAttachment(0, 128);
		fd_lblLocation.left = new FormAttachment(0, 21);
		lblLocation.setLayoutData(fd_lblLocation);
		lblLocation.setText("Location:");

		locationTextBox = new Text(container, SWT.BORDER);
		FormData fd_locationTextBox = new FormData();
		fd_locationTextBox.right = new FormAttachment(0, 542);
		fd_locationTextBox.top = new FormAttachment(0, 126);
		fd_locationTextBox.left = new FormAttachment(0, 99);
		locationTextBox.setLayoutData(fd_locationTextBox);

		Label lblUsername = new Label(container, SWT.NONE);
		FormData fd_lblUsername = new FormData();
		fd_lblUsername.top = new FormAttachment(0, 175);
		fd_lblUsername.left = new FormAttachment(0, 21);
		lblUsername.setLayoutData(fd_lblUsername);
		lblUsername.setText("Username:");

		usernameTextBox = new Text(container, SWT.BORDER);
		FormData fd_usernameTextBox = new FormData();
		fd_usernameTextBox.right = new FormAttachment(0, 272);
		fd_usernameTextBox.top = new FormAttachment(0, 173);
		fd_usernameTextBox.left = new FormAttachment(0, 99);
		usernameTextBox.setLayoutData(fd_usernameTextBox);

		Label lblPassword = new Label(container, SWT.NONE);
		FormData fd_lblPassword = new FormData();
		fd_lblPassword.top = new FormAttachment(0, 222);
		fd_lblPassword.left = new FormAttachment(0, 21);
		lblPassword.setLayoutData(fd_lblPassword);
		lblPassword.setText("Password:");

		passwordTextBox = new Text(container, SWT.BORDER | SWT.PASSWORD);
		fd_nameTextBox.right = new FormAttachment(passwordTextBox, 0, SWT.RIGHT);
		FormData fd_passwordTextBox = new FormData();
		fd_passwordTextBox.right = new FormAttachment(0, 272);
		fd_passwordTextBox.top = new FormAttachment(0, 220);
		fd_passwordTextBox.left = new FormAttachment(0, 99);
		passwordTextBox.setLayoutData(fd_passwordTextBox);
		passwordTextBox.setEnabled(false);

		Label label = new Label(container, SWT.NONE);
		FormData fd_label = new FormData();
		fd_label.top = new FormAttachment(0, 246);
		fd_label.left = new FormAttachment(0, 21);
		label.setLayoutData(fd_label);

		Button btnSavePassoword = new Button(container, SWT.CHECK);
		btnSavePassoword.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Button btn = (Button) e.getSource();
				if (btn.getSelection()) {
					passwordTextBox.setEnabled(true);
				} else {
					passwordTextBox.setEnabled(false);
				}
			}
		});
		FormData fd_btnSavePassoword = new FormData();
		fd_btnSavePassoword.top = new FormAttachment(lblPassword, -3, SWT.TOP);
		fd_btnSavePassoword.left = new FormAttachment(passwordTextBox, 60);
		btnSavePassoword.setLayoutData(fd_btnSavePassoword);
		btnSavePassoword.setText("Save Password");

		return container;
	}

	@Override
	protected void okPressed() {
		String name = nameTextBox.getText();
		String remoteAdd = machineTextBox.getText();
		String remoteLoc = locationTextBox.getText();
		String username = usernameTextBox.getText();
		String password = passwordTextBox.getText();
		
		SCPLocation location = manager.getLocation(name);
		if (location != null) {
			YesNoPrompt prompt=new YesNoPrompt("Name already exists. Do you want to overwrite?");
		    Display.getDefault().syncExec(prompt);
		    
			if (prompt.getResult() == SWT.YES) {
				SCPRemoteLocation newRemoteLocation = new SCPRemoteLocation(remoteAdd,
						remoteLoc, username, password);
				location = new SCPLocation(name,
						this.currentSelectedLocation, newRemoteLocation);
			}
		} else {
			SCPRemoteLocation newRemoteLocation = new SCPRemoteLocation(remoteAdd,
					remoteLoc, username, password);
			location = new SCPLocation(name,
					this.currentSelectedLocation, newRemoteLocation);
		}
		
		this.prevLocationDialog.setRemoteLocation(location);

		manager.addLocation(location);

		super.okPressed();
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

	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Add Remote Location Details");
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(553, 364);
	}
	
	private class YesNoPrompt implements Runnable {
		private String prompt;
		private int result;

		YesNoPrompt(String prompt) {
			this.prompt = prompt;
		}

		public void run() {
			Display display = Display.getCurrent();
			Shell shell = new Shell(display);
			MessageBox box = new MessageBox(shell, SWT.YES | SWT.NO);
			box.setMessage(prompt);
			result = box.open();
			shell.dispose();
		}

		public int getResult() {
			return result;
		}
	}
}
