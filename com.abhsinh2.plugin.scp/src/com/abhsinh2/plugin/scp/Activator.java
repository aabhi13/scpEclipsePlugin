package com.abhsinh2.plugin.scp;

import java.io.File;
import java.net.URL;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import com.abhsinh2.plugin.scp.model.SCPLocationManager;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.abhsinh2.plugin.scp"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;
	private IEclipsePreferences configPrefs;

	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	public void stop(BundleContext context) throws Exception {
		SCPLocationManager.getManager().saveFavorites();
		saveConfigPrefs();
		plugin = null;
		super.stop(context);
	}

	public void saveConfigPrefs() {
		if (configPrefs != null) {
			try {
				configPrefs.flush();
			} catch (BackingStoreException e) {
				e.printStackTrace();
			}
		}
	}

	public Preferences getConfigPrefs() {
		if (configPrefs == null)
			configPrefs = new ConfigurationScope().getNode(PLUGIN_ID);
		return configPrefs;
	}

	public File getConfigDir() {
		Location location = Platform.getConfigurationLocation();
		if (location != null) {
			URL configURL = location.getURL();
			if (configURL != null && configURL.getProtocol().startsWith("file")) {
				return new File(configURL.getFile(), PLUGIN_ID);
			}
		}
		// If the configuration directory is read-only,
		// then return an alternate location
		// rather than null or throwing an Exception.
		return getStateLocation().toFile();
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
}
