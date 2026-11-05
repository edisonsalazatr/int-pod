package it.terna.nas.service.material.utility;

import java.time.LocalDateTime;

public class TimeUtils {

	public static LocalDateTime min(LocalDateTime a, LocalDateTime b) {
		if(a == null) return b;
		if(b == null) return a;
		return a.isBefore(b) ? a : b;
	}
	
	public static LocalDateTime max(LocalDateTime a, LocalDateTime b) {
		if(a == null) return b;
		if(b == null) return a;
		return a.isAfter(b) ? a : b;
	}
}
