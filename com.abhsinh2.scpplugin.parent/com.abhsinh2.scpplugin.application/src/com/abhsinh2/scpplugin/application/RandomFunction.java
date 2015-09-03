package com.abhsinh2.scpplugin.application;

import org.eclipse.e4.core.contexts.ContextFunction;
import org.eclipse.e4.core.contexts.IEclipseContext;

public class RandomFunction extends ContextFunction {

	@Override
	public Object compute(IEclipseContext context) {
		return Math.random();
	}

}
