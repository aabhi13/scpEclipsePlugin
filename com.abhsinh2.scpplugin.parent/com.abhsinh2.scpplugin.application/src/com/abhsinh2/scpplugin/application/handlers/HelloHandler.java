package com.abhsinh2.scpplugin.application.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

public class HelloHandler {

	
	public void hello1(@Named(IServiceConstants.ACTIVE_SHELL) Shell s) {
		MessageDialog.openInformation(s, "Hello1 World",
				"Welcome to Eclipse 4 technology");
	}

	
	public void hello2(@Named(IServiceConstants.ACTIVE_SHELL) Shell s,
			@Named("math.random") double value) {
		MessageDialog.openInformation(s, "Hello2 World",
				"Welcome to Eclipse 4 technology");
	}

	@Execute
	public void hello(
			@Named(IServiceConstants.ACTIVE_SHELL) Shell s,
			@Optional @Named("com.abhsinh2.scpplugin.application.commandparameter.hello.value") String hello,
			@Named("math.random") double value) {
		MessageDialog.openInformation(s, "Hello3 World", hello + value);
	}

}
