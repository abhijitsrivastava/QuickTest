package com.eduglasses.frontflip.domain;

import java.util.HashMap;
import java.util.Map;

public enum SectionTypeEnum {
	POWERPOINT(0), VIDEO(1), EDUTEACH(2), QUIZ(3), EDUDEFINE(4), EDUTOOLS(5);

	private static final Map<Integer, SectionTypeEnum> lookup = new HashMap<Integer, SectionTypeEnum>();
	static {
		for (SectionTypeEnum d : SectionTypeEnum.values())
			lookup.put(d.getStatus(), d);
	}

	private int status;

	private SectionTypeEnum(int status) {
		this.status = status;
	}

	public int getStatus() {
		return status;
	}

	public static SectionTypeEnum get(int status) {
		return lookup.get(status);
	}

}
