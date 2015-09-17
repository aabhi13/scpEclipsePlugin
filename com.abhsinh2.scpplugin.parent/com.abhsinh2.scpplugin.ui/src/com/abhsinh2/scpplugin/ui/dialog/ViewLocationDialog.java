package com.abhsinh2.scpplugin.ui.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.List;

import com.abhsinh2.scpplugin.ui.model.Location;

public class ViewLocationDialog extends Dialog {

	private Location location;
	public ViewLocationDialog(Shell shell, Location location) {
		super(shell);		
		this.location = location;
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		
		container.setLayout(gridLayout);
		
		Label lblName = new Label(container, SWT.NONE);
		lblName.setText("Name:");
		
		Label nameValue = new Label(container, SWT.NONE);
		nameValue.setText(this.location.getName());
		nameValue.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 1, 1));
		
		final Label label_1 = new Label(container, SWT.NONE);
		final GridData gridData_1 = new GridData();
		gridData_1.horizontalSpan = 2;
		label_1.setLayoutData(gridData_1);
		
		Label lblRemoteMachine = new Label(container, SWT.NONE);
		lblRemoteMachine.setText("Remote Machine");
		
		Label remoteMachineValue = new Label(container, SWT.NONE);
		remoteMachineValue.setText(this.location.getRemoteLocation().getRemoteAddress());
		remoteMachineValue.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 1, 1));
		
		final Label label_2 = new Label(container, SWT.NONE);
		final GridData gridData_2 = new GridData();
		gridData_2.horizontalSpan = 2;
		label_2.setLayoutData(gridData_2);
		
		Label lblRemoteLocation = new Label(container, SWT.NONE);
		lblRemoteLocation.setText("Remote Location:");
		
		Label remoteLocationValue = new Label(container, SWT.NONE);
		remoteLocationValue.setText(this.location.getRemoteLocation().getRemoteLocation());
		remoteLocationValue.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 1, 1));
		
		final Label label_3 = new Label(container, SWT.NONE);
		final GridData gridData_3 = new GridData();
		gridData_3.horizontalSpan = 2;
		label_3.setLayoutData(gridData_3);
		
		Label lblUsername = new Label(container, SWT.NONE);
		lblUsername.setText("Username:");
		
		Label usernameValue = new Label(container, SWT.NONE);
		usernameValue.setText(this.location.getRemoteLocation().getUsername());
		usernameValue.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 1, 1));
		
		final Label label_4 = new Label(container, SWT.NONE);
		final GridData gridData_4 = new GridData();
		gridData_4.horizontalSpan = 2;
		label_4.setLayoutData(gridData_4);
		
		Label lblLocalFiles = new Label(container, SWT.NONE);
		lblLocalFiles.setText("Local Files:");
		
		List localFileList = new List(container, SWT.BORDER);
		for (String localFile : this.location.getLocalFiles()) {
			localFileList.add(localFile);
		}
		localFileList.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 1, 1));
		
		return container;
	}
}
