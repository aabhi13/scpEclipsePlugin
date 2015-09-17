package com.abhsinh2.scpplugin.ui.copy;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import com.abhsinh2.scpplugin.ui.dialog.PassphraseDialog;
import com.abhsinh2.scpplugin.ui.dialog.PasswordDialog;
import com.jcraft.jsch.UIKeyboardInteractive;
import com.jcraft.jsch.UserInfo;

public class RemoteUserInfo implements UserInfo, UIKeyboardInteractive {

	private String passwd;

	public RemoteUserInfo(String passwd) {
		this.passwd = passwd;
	}

	public String getPassword() {
		return passwd;
	}

	public boolean promptYesNo(String str) {
		return false;
	}

	public String getPassphrase() {
		return null;
	}

	public boolean promptPassphrase(String message) {
		return true;
	}

	public boolean promptPassword(String message) {
		if (this.passwd == null) {
			PasswordPrompt prompt = new PasswordPrompt(message);
			Display.getDefault().syncExec(prompt);
			String _password = prompt.getPassword();
			if (_password != null) {
				passwd = _password;
			}

			return _password != null;
		} else {
			return true;
		}		
	}

	public void showMessage(final String message) {
		final Display display = Display.getCurrent();
		display.syncExec(new Runnable() {
			public void run() {
				Shell shell = new Shell(display);
				MessageBox box = new MessageBox(shell, SWT.OK);
				box.setMessage(message);
				box.open();
				shell.dispose();
			}
		});
	}

	public String[] promptKeyboardInteractive(String destination, String name,
			String instruction, String[] prompt, boolean[] echo) {
		return null;
	}

	private class PasswordPrompt implements Runnable {
		private String message;
		private String password;

		PasswordPrompt(String message) {
			this.message = message;
		}

		public void run() {
			Display display = Display.getCurrent();
			Shell shell = new Shell(display);
			PasswordDialog dialog = new PasswordDialog(shell, message);
			dialog.open();
			shell.dispose();
			password = dialog.getPassword();
		}

		public String getPassword() {
			return password;
		}
	}

	private class PassphrasePrompt implements Runnable {
		private String message;
		private String passphrase;

		PassphrasePrompt(String message) {
			this.message = message;
		}

		public void run() {
			Display display = Display.getCurrent();
			Shell shell = new Shell(display);
			PassphraseDialog dialog = new PassphraseDialog(shell, message);
			dialog.open();
			shell.dispose();
			passphrase = dialog.getPassphrase();
		}

		public String getPassphrase() {
			return passphrase;
		}
	}

}
