package com.abhsinh2.scpplugin.ui.model;

import java.util.EventObject;

/**
 * Event to identify operation on location.
 * 
 * @author abhsinh2
 * 
 */
public class LocationManagerEvent extends EventObject {
	private static final long serialVersionUID = 2206616214127639189L;

	private final Location location;
	private final LocationManagerOperationType eventType;

	public LocationManagerEvent(LocationManager source, Location location,
			LocationManagerOperationType eventType) {
		super(source);
		this.location = location;
		this.eventType = eventType;
	}

	public Location getLocation() {
		return location;
	}

	public LocationManagerOperationType getEventType() {
		return eventType;
	}
}