package org.salespointframework.core.time;

import java.util.Objects;

import org.springframework.stereotype.Service;

@Service
class TimeServiceImpl implements TimeService {

	private Time time;
	
	TimeServiceImpl() {
		time = new DefaultTime();
	}
	
	
	@Override
	public Time getTime() {
		return time;
	}

	@Override
	public void setTime(Time time) {
		this.time = Objects.requireNonNull(time, "time must not be null");

	}

}
