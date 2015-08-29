package com.abhsinh2.scpplugin.junit;

import static org.junit.Assert.assertTrue;

import org.eclipse.core.runtime.Platform;
import org.junit.Test;

public class PlatformTest {
	@Test
	public void test() {
		assertTrue(Platform.isRunning());
	}
}
