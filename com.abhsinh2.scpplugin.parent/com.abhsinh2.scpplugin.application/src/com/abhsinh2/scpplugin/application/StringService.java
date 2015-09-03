package com.abhsinh2.scpplugin.application;

import org.eclipse.e4.core.di.annotations.Creatable;

@Creatable
public class StringService {
	public String process(String string) {
		return string.toUpperCase();
	}
}
