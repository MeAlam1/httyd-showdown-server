package com.mealam.showdown.utils.types;

import java.util.Collections;
import java.util.List;

public class ListUtils {

	public static <T> List<T> safeUnmodifiableList(List<T> pList) {
		return Collections.unmodifiableList(pList == null ? List.of() : pList);
	}
}
