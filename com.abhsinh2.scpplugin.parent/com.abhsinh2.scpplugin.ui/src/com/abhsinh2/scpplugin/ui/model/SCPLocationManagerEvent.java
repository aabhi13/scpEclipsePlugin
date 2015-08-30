package com.abhsinh2.scpplugin.ui.model;

import java.util.EventObject;

import com.abhsinh2.scpplugin.ui.model.remote.SCPLocation;

public class SCPLocationManagerEvent extends EventObject {
	private static final long serialVersionUID = 2206616214127639189L;
	
	private final SCPLocation location;
	private final SCPLocationManagerType eventType;

	public SCPLocationManagerEvent(SCPLocationManager source,
			SCPLocation location, SCPLocationManagerType eventType) {
		super(source);
		this.location = location;
		this.eventType = eventType;
	}

	public SCPLocation getLocation() {
		return location;
	}

	public SCPLocationManagerType getEventType() {
		return eventType;
	}
}