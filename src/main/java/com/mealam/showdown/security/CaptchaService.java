package com.mealam.showdown.security;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class CaptchaService {

	private final Map<String, Connection> captchaStore = new ConcurrentHashMap<>();

	private record Connection(String question, int answer) {}

	public Map<String, String> generateCaptcha() {
		int a = (int) (Math.random() * 10 + 1);
		int b = (int) (Math.random() * 10 + 1);
		String question = a + " + " + b;
		int answer = a + b;
		String captchaId = UUID.randomUUID().toString();
		captchaStore.put(captchaId, new Connection(question, answer));
		return Map.of("captchaId", captchaId, "question", question);
	}

	public boolean verify(String pCaptchaId, String pAnswer) {
		Connection connection = captchaStore.remove(pCaptchaId);
		return connection != null && Integer.toString(connection.answer()).equals(pAnswer);
	}
}
