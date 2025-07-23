/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.api;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/uuid")
public class UUIDController {

	private final Map<String, Map<String, Long>> purposeUuids = new ConcurrentHashMap<>();
	private static final long EXPIRATION_MILLIS = 30 * 60 * 1000;

	@GetMapping("/generate")
	public ResponseEntity<Map<String, String>> generateUUID(@RequestParam String pPurpose) {
		String uuid = UUID.randomUUID().toString();
		purposeUuids.computeIfAbsent(pPurpose, k -> new ConcurrentHashMap<>())
				.put(uuid, System.currentTimeMillis());
		Map<String, String> response = new HashMap<>();
		response.put("uuid", uuid);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/validate")
	public ResponseEntity<Map<String, Boolean>> validateUUID(@RequestBody Map<String, String> body) {
		String purpose = body.get("purpose");
		String pUuid = body.get("uuid");
		Map<String, Long> cache = purposeUuids.get(purpose);
		Long created = cache != null ? cache.get(pUuid) : null;
		boolean isValid = created != null && (System.currentTimeMillis() - created) < EXPIRATION_MILLIS;
		Map<String, Boolean> response = new HashMap<>();
		response.put("valid", isValid);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/remove")
	public ResponseEntity<Void> removeUUID(@RequestBody Map<String, String> pBody) {
		String purpose = pBody.get("purpose");
		String pUuid = pBody.get("uuid");
		Map<String, Long> cache = purposeUuids.get(purpose);
		if (cache != null) {
			cache.remove(pUuid);
		}
		return ResponseEntity.ok().build();
	}

	@Scheduled(fixedRate = 5 * 60 * 1000)
	public void cleanupExpiredUuids() {
		long now = System.currentTimeMillis();
		for (Map<String, Long> cache : purposeUuids.values()) {
			cache.entrySet().removeIf(e -> now - e.getValue() > EXPIRATION_MILLIS);
		}
	}
}
