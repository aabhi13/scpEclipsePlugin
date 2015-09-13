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
import com.abhsinh2.scpplugin.ui.model.local.ILocalLocation;
import com.abhsinh2.scpplugin.ui.model.remote.RemoteLocation;

/**
 * Manager to manage location. Manages add/edit/remove location.
 * 
 * @author abhsinh2
 * 
 */
public class LocationManager {

	private static final String TAG_LOCATIONS = "Locations";
	private static final String TAG_LOCATION = "Location";
	private static final String TAG_NAME = "Name";
	private static final String TAG_REMOTE_ADDRESS = "RemoteAddress";
	private static final String TAG_REMOTE_LOCATION = "RemoteLocation";
	// private static final String TAG_LOCAL_LOCATIONS = "LocalLocations";
	private static final String TAG_LOCAL_LOCATION = "LocalLocation";
	private static final String TAG_USERNAME = "Username";
	private static final String TAG_PASSWORD = "Password";

	private static LocationManager manager;
	private Map<String, Location> locations;
	private List<LocationManagerListener> listeners = new ArrayList<LocationManagerListener>();

	private LocationManager() {
		this.locations = new HashMap<String, Location>(20);
	}

	public static LocationManager getManager() {
		if (manager == null)
			manager = new LocationManager();
		return manager;
	}

	public Collection<Location> getLocations() {
		return locations.values();
	}

	public Location getLocation(String name) {
		return locations.get(name);
	}

	public void addLocation(Location location) {
		if (location == null)
			return;

		if (this.locations == null || this.locations.isEmpty())
			loadLocations();

		Location existingLocation = this.locations.get(location.getName());
		if (existingLocation == null) {
			fireFavoritesChanged(location, LocationManagerOperationType.ADDED);
		} else {
			fireFavoritesChanged(location, LocationManagerOperationType.UPDATED);
		}

		this.locations.put(location.getName(), location);
	}

	public void removeLocation(Location location) {
		if (location == null)
			return;

		if (this.locations == null || this.locations.isEmpty())
			loadLocations();

		remove(location.getName());
	}

	public void removeLocations(Location[] locations) {
		if (locations == null)
			return;

		for (Location location : locations) {
			removeLocation(location);
		}
	}

	public void removeLocation(String name) {
		if (name == null)
			return;

		if (this.locations == null || this.locations.isEmpty())
			loadLocations();

		remove(name);
	}

	private void remove(String name) {
		Location existingLocation = this.locations.get(name);
		if (existingLocation != null) {
			fireFavoritesChanged(existingLocation,
					LocationManagerOperationType.DELETED);
			this.locations.remove(existingLocation.getName());
		}
	}

	public void addLocationManagerListener(LocationManagerListener listener) {
		if (!listeners.contains(listener))
			listeners.add(listener);
	}

	public void removeLocationManagerListener(LocationManagerListener listener) {
		listeners.remove(listener);
	}

	private void fireFavoritesChanged(Location location,
			LocationManagerOperationType eventType) {
		LocationManagerEvent event = new LocationManagerEvent(this, location,
				eventType);
		for (Iterator<LocationManagerListener> iter = listeners.iterator(); iter
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
		IMemento[] locations = memento.getChildren(TAG_LOCATION);
		for (int i = 0; i < locations.length; i++) {

			Collection<String> localFiles = new ArrayList<String>();
			IMemento[] locationLocations = locations[i]
					.getChildren(TAG_LOCAL_LOCATION);
			if (locationLocations != null) {
				for (int j = 0; j < locationLocations.length; j++) {
					localFiles.add(locationLocations[i].getString(TAG_NAME));
				}
			}

			Location item = getLocation(locations[i].getString(TAG_NAME),
					locations[i].getString(TAG_REMOTE_ADDRESS),
					locations[i].getString(TAG_REMOTE_LOCATION),
					locations[i].getString(TAG_USERNAME),
					locations[i].getString(TAG_PASSWORD), localFiles);

			if (item != null)
				this.locations.put(item.getName(), item);
		}
	}

	public Location getLocation(String name, String remoteAddress,
			String remoteLocation, String username, String password,
			Collection<String> localFiles) {
		RemoteLocation remote = new RemoteLocation(remoteAddress,
				remoteLocation, username, password);
		return new Location(name, localFiles, remote);
	}

	public void saveLocations() {
		// if (this.locations == null || this.locations.isEmpty())
		// return;

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
			Location location = this.locations.get(key);

			IMemento locationElement = memento.createChild(TAG_LOCATION);
			locationElement.putString(TAG_NAME, location.getName());

			RemoteLocation remoteLocation = location.getRemoteLocation();

			locationElement.putString(TAG_REMOTE_ADDRESS,
					remoteLocation.getRemoteAddress());
			locationElement.putString(TAG_REMOTE_LOCATION,
					remoteLocation.getRemoteLocation());
			locationElement.putString(TAG_USERNAME,
					remoteLocation.getUsername());
			locationElement.putString(TAG_PASSWORD,
					remoteLocation.getPassword());

			Collection<String> localLocationList = location.getLocalFiles();
			if (localLocationList != null && localLocationList.size() > 0) {
				for (String localLocation : localLocationList) {
					IMemento localLocationElement = locationElement
							.createChild(TAG_LOCAL_LOCATION);
					localLocationElement.putString(TAG_NAME, localLocation);
				}
			}
		}
	}

	public ILocalLocation[] existingLocationFor(Iterator<?> iter) {
		List<ILocalLocation> result = new ArrayList<ILocalLocation>(10);
		while (iter.hasNext()) {
			ILocalLocation item = existingLocationFor(iter.next());
			if (item != null)
				result.add(item);
		}
		return (ILocalLocation[]) result.toArray(new ILocalLocation[result
				.size()]);
	}

	private ILocalLocation existingLocationFor(Object obj) {
		if (obj == null)
			return null;

		if (obj instanceof ILocalLocation)
			return (ILocalLocation) obj;

		return null;
	}

	private File getFavoritesFile() {
		File file = Activator.getDefault().getStateLocation()
				.append("favorites.xml").toFile();
		return file;
	}
	
	public boolean isLocationsEmpty() {
		return locations.isEmpty();
	}

}
