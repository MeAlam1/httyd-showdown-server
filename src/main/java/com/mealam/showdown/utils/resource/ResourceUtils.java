package com.mealam.showdown.utils.resource;

import com.mealam.showdown.api.StaticAPIRouter;

import java.io.IOException;
import java.io.InputStream;

public class ResourceUtils {

	public static String loadResource(String pPath) {
		try (InputStream in = StaticAPIRouter.class.getClassLoader().getResourceAsStream(pPath)) {
			if (in == null) return null;
			return new String(in.readAllBytes());
		} catch (IOException e) {
			return null;
		}
	}
}
