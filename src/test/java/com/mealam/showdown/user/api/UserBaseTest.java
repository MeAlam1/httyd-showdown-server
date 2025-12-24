package com.mealam.showdown.user.api;

import com.mealam.showdown.Main;
import io.javalin.Javalin;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.net.http.HttpClient;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class UserBaseTest {

	protected Javalin app;
	protected int port;
	protected HttpClient client;
	protected UserApiClient api;

	@BeforeEach
	void setup() {
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
		api = new UserApiClient(port, client);
	}

	@AfterEach
	void teardown() {
		if (app != null) {
			try {
				app.stop();
			} finally {
				app = null;
			}
		}
	}
}