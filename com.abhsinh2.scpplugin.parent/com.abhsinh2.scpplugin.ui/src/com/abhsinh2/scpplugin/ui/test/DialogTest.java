package com.abhsinh2.scpplugin.ui.test;

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.abhsinh2.scpplugin.ui.dialog.LocationDialog;
import com.abhsinh2.scpplugin.ui.model.SCPLocationManager;

public class DialogTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		SCPLocationManager manager = SCPLocationManager.getManager();
				
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Combo Example");
		shell.setBounds(100, 100, 200, 100);
		shell.setLayout(new FillLayout(SWT.VERTICAL));

		LocationDialog dialog = new LocationDialog(shell, null, null);
		if (dialog.open() != InputDialog.OK)
			return;

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

}
