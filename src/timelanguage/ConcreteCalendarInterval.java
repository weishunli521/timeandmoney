package timelanguage;

import java.util.TimeZone;

class ConcreteCalendarInterval extends CalendarInterval {
	
	private CalendarDate start;
	private CalendarDate end;
	
	static ConcreteCalendarInterval from(CalendarDate start, CalendarDate end) {
		return new ConcreteCalendarInterval(start, end);
	}
	
	ConcreteCalendarInterval(CalendarDate start, CalendarDate end) {
		this.start = start;
		this.end = end;
	}
	
	public TimeInterval asTimeInterval(TimeZone zone) {
		TimePoint startPoint = start.asTimeInterval(zone).start();
		TimePoint endPoint = end.asTimeInterval(zone).end();
		return TimeInterval.over(startPoint, endPoint);
	}

	public Comparable upperLimit() {
		return start;
	}

	public Comparable lowerLimit() {
		return end;
	}


}