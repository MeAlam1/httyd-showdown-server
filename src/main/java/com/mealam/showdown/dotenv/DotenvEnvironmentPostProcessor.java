package com.mealam.showdown.dotenv;

import io.github.cdimascio.dotenv.Dotenv;
import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

public class DotenvEnvironmentPostProcessor implements EnvironmentPostProcessor {

	@Override
	public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
		Map<String, Object> map = new HashMap<>();
		dotenv.entries().forEach(entry -> map.put(entry.getKey(), entry.getValue()));
		environment.getPropertySources().addFirst(new MapPropertySource("dotenv", map));
	}
}
