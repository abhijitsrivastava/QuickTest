package com.eduglasses.frontflip.domain;

import java.util.HashMap;
import java.util.Map;

public enum LessonStatusEnum {
	INACTIVE(0), ACTIVE(1);

	private static final Map<Integer, LessonStatusEnum> lookup = new HashMap<Integer, LessonStatusEnum>();
	static {
		for (LessonStatusEnum d : LessonStatusEnum.values())
			lookup.put(d.getStatus(), d);
	}

	private int status;

	private LessonStatusEnum(int status) {
		this.status = status;
	}

	public int getStatus() {
		return status;
	}

	public static LessonStatusEnum get(int status) {
		return lookup.get(status);
	}

}
