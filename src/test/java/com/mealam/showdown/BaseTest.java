package com.mealam.showdown;

import io.javalin.Javalin;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpClient;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class BaseTest {

	protected Javalin app;
	protected int port;
	protected HttpClient client;

	@BeforeEach
	void setupBase() {
		try {
			Path classesRoot = Paths.get(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI());
			Path dataRoot = classesRoot.resolve("..").normalize().resolve("httyd-showdown-server");

			Files.createDirectories(dataRoot);
			System.setProperty("showdown.data.dir", dataRoot.toString());
		} catch (Exception e) {
			throw new IllegalStateException("Failed to prepare data dir", e);
		}

		app = Main.createApp();
		app.start(0);
		port = app.port();
		client = HttpClient.newHttpClient();
	}

	protected String loadResource(String pResourcePath) throws IOException {
		try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(pResourcePath)) {
			if (inputStream == null) throw new FileNotFoundException(pResourcePath);
			return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
		}
	}

	@AfterEach
	void teardownBase() {
		if (app != null) {
			try {
				app.stop();
			} finally {
				app = null;
			}
		}
	}
}