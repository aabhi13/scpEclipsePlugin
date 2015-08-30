package com.abhsinh2.scpplugin.ui.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.ui.IMemento;
import org.eclipse.ui.XMLMemento;

import com.abhsinh2.scpplugin.ui.Activator;
import com.abhsinh2.scpplugin.ui.model.remote.SCPLocation;
import com.abhsinh2.scpplugin.ui.model.remote.SCPRemoteLocation;

public class SCPLocationManager {

	private static final String TAG_LOCATIONS = "Locations";
	private static final String TAG_LOCATION = "Location";
	private static final String TAG_NAME = "Name";
	private static final String TAG_REMOTE_ADDRESS = "RemoteAddress";
	private static final String TAG_REMOTE_LOCATION = "RemoteLocation";
	private static final String TAG_LOCAL_LOCATIONS = "LocalLocations";
	private static final String TAG_LOCAL_LOCATION = "LocalLocation";
	private static final String TAG_USERNAME = "Username";
	private static final String TAG_PASSWORD = "Password";

	private static SCPLocationManager manager;
	private Map<String, SCPLocation> locations;
	private List<SCPLocationManagerListener> listeners = new ArrayList<SCPLocationManagerListener>();

	private SCPLocationManager() {
		this.locations = new HashMap<String, SCPLocation>(20);
	}

	public static SCPLocationManager getManager() {
		if (manager == null)
			manager = new SCPLocationManager();
		return manager;
	}

	// TODO: To be removed . used for testing
	public Map<String, SCPLocation> getAllLocations() {
		return locations;
	}

	public Collection<SCPLocation> getLocations() {
		return locations.values();
	}

	public SCPLocation getLocation(String name) {
		return locations.get(name);
	}

	public void addLocation(SCPLocation location) {
		if (location == null)
			return;

		if (this.locations == null || this.locations.isEmpty())
			loadLocations();

		SCPLocation existingLocation = this.locations.get(location.getName());
		if (existingLocation == null) {
			fireFavoritesChanged(location, SCPLocationManagerType.ADDED);
		} else {
			fireFavoritesChanged(location, SCPLocationManagerType.UPDATED);
		}

		this.locations.put(location.getName(), location);
	}

	public void removeLocation(SCPLocation location) {
		if (location == null)
			return;

		if (this.locations == null || this.locations.isEmpty())
			loadLocations();

		remove(location.getName());
	}

	public void removeLocation(String name) {
		if (name == null)
			return;

		if (this.locations == null || this.locations.isEmpty())
			loadLocations();

		remove(name);
	}

	private void remove(String name) {
		SCPLocation existingLocation = this.locations.get(name);
		if (existingLocation != null) {
			fireFavoritesChanged(existingLocation,
					SCPLocationManagerType.DELETED);
			this.locations.remove(existingLocation.getName());
		}
	}

	public void addLocationManagerListener(SCPLocationManagerListener listener) {
		if (!listeners.contains(listener))
			listeners.add(listener);
	}

	public void removeLocationManagerListener(
			SCPLocationManagerListener listener) {
		listeners.remove(listener);
	}

	private void fireFavoritesChanged(SCPLocation location,
			SCPLocationManagerType eventType) {
		SCPLocationManagerEvent event = new SCPLocationManagerEvent(this,
				location, eventType);
		for (Iterator<SCPLocationManagerListener> iter = listeners.iterator(); iter
				.hasNext();) {
			iter.next().locationChanged(event);
		}
	}

	public void loadLocations() {
		FileReader reader = null;
		try {
			reader = new FileReader(getFavoritesFile());
			loadLocations(XMLMemento.createReadRoot(reader));
		} catch (FileNotFoundException e) {
			// Ignored... no Favorites items exist yet.
		} catch (Exception e) {
			// Log the exception and move on.
		} finally {
			try {
				if (reader != null)
					reader.close();
			} catch (IOException e) {
			}
		}
	}

	private void loadLocations(XMLMemento memento) {
		IMemento[] children = memento.getChildren(TAG_LOCATION);
		for (int i = 0; i < children.length; i++) {
			SCPLocation item = newFavoriteFor(children[i].getString(TAG_NAME),
					children[i].getString(TAG_REMOTE_ADDRESS),
					children[i].getString(TAG_REMOTE_LOCATION),
					children[i].getString(TAG_USERNAME),
					children[i].getString(TAG_PASSWORD));

			if (item != null)
				this.locations.put(item.getName(), item);
		}
	}

	public SCPLocation newFavoriteFor(String name, String remoteAddress,
			String remoteLocation, String username, String password) {
		SCPRemoteLocation remote = new SCPRemoteLocation(remoteAddress,
				remoteLocation, username, password);
		return new SCPLocation(name, null, remote);
	}

	public void saveFavorites() {
		System.out.println("saveFavorites:" + this.locations);

		if (this.locations == null || this.locations.isEmpty())
			return;

		XMLMemento memento = XMLMemento.createWriteRoot(TAG_LOCATIONS);
		saveLocations(memento);
		FileWriter writer = null;
		try {
			writer = new FileWriter(getFavoritesFile());
			memento.save(writer);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (writer != null)
					writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void saveLocations(XMLMemento memento) {
		for (String key : this.locations.keySet()) {
			SCPLocation location = this.locations.get(key);

			IMemento locationElement = memento.createChild(TAG_LOCATION);
			locationElement.putString(TAG_NAME, location.getName());

			SCPRemoteLocation remoteLocation = location.getRemoteLocation();

			locationElement.putString(TAG_REMOTE_ADDRESS,
					remoteLocation.getRemoteAddress());
			locationElement.putString(TAG_REMOTE_LOCATION,
					remoteLocation.getRemoteLocation());
			locationElement.putString(TAG_USERNAME,
					remoteLocation.getUsername());
			locationElement.putString(TAG_PASSWORD,
					remoteLocation.getPassword());

			IMemento localLocationElement = memento
					.createChild(TAG_LOCAL_LOCATIONS);

			Collection<String> localLocationList = location.getLocalFiles();
			if (localLocationList != null && localLocationList.size() > 0) {
				for (String localLocation : localLocationList) {
					localLocationElement.putString(TAG_LOCAL_LOCATION,
							localLocation);
				}
			}
		}
	}

	private File getFavoritesFile() {
		File file = Activator.getDefault().getStateLocation()
				.append("favorites.xml").toFile();
		return file;
	}

}
