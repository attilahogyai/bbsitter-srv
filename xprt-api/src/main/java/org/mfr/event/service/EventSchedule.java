package org.mfr.event.service;

import java.util.concurrent.ScheduledFuture;

import org.mfr.data.XEvent;

public class EventSchedule {
	public EventSchedule(XEvent event, ScheduledFuture schedule) {
		super();
		this.event = event;
		this.schedule = schedule;
	}
	private XEvent event;
	private ScheduledFuture schedule;
	public XEvent getEvent() {
		return event;
	}
	public void setEvent(XEvent event) {
		this.event = event;
	}
	public ScheduledFuture getSchedule() {
		return schedule;
	}
	public void setSchedule(ScheduledFuture schedule) {
		this.schedule = schedule;
	}
}
