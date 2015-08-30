package com.abhsinh2.scpplugin.ui.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class InteractiveDialog extends Dialog {

	private Text[] texts;
	protected String destination;
	protected String name;
	protected String instruction;
	protected String[] prompt;
	protected boolean[] echo;
	private String message;
	private String[] result;

	public InteractiveDialog(Shell parentShell, String destination,
			String name, String instruction, String[] prompt, boolean[] echo) {
		super(parentShell);
		this.destination = destination;
		this.name = name;
		this.instruction = instruction;
		this.prompt = prompt;
		this.echo = echo;
		this.message = "Keyboard Interactive for " + destination
				+ (name != null && name.length() > 0 ? ": " + name : "");
	}

	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(message);
	}

	public void create() {
		super.create();
		if (texts.length > 0) {
			texts[0].setFocus();
		}
	}

	protected Control createDialogArea(Composite parent) {
		Composite main = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		main.setLayout(layout);
		main.setLayoutData(new GridData(GridData.FILL_BOTH));

		if (message != null) {
			Label messageLabel = new Label(main, SWT.WRAP);
			messageLabel.setText(message);
			GridData data = new GridData(GridData.FILL_HORIZONTAL);
			data.horizontalSpan = 3;
			messageLabel.setLayoutData(data);
		}
		if (instruction != null && instruction.length() > 0) {
			Label messageLabel = new Label(main, SWT.WRAP);
			messageLabel.setText(instruction);
			GridData data = new GridData(GridData.FILL_HORIZONTAL);
			data.horizontalSpan = 3;
			messageLabel.setLayoutData(data);
		}
		createPasswordFields(main);
		return main;
	}

	protected void createPasswordFields(Composite parent) {
		texts = new Text[prompt.length];

		for (int i = 0; i < prompt.length; i++) {
			new Label(parent, SWT.NONE).setText(prompt[i]);
			texts[i] = new Text(parent, SWT.BORDER);
			GridData data = new GridData(GridData.FILL_HORIZONTAL);
			data.widthHint = convertHorizontalDLUsToPixels(IDialogConstants.ENTRY_FIELD_WIDTH);
			texts[i].setLayoutData(data);

			if (!echo[i]) {
				texts[i].setEchoChar('*');
			}
			new Label(parent, SWT.NONE);
		}

	}

	public String[] getResult() {
		return result;
	}

	protected void okPressed() {
		result = new String[prompt.length];
		for (int i = 0; i < texts.length; i++) {
			result[i] = texts[i].getText();
		}
		super.okPressed();
	}

	protected void cancelPressed() {
		result = null;
		super.cancelPressed();
	}
}
