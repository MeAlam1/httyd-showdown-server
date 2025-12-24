package com.mealam.showdown.user.infra;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mealam.showdown.user.api.UserRepository;
import com.mealam.showdown.user.data.UserId;
import com.mealam.showdown.user.model.UserAccount;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public final class FileUserRepository implements UserRepository {

	private final Path filePath;
	private final ObjectMapper objectMapper;

	private final Map<String, UserAccount> byId = new HashMap<>();

	public FileUserRepository(Path pFilePath, ObjectMapper pObjectMapper) {
		this.filePath = Objects.requireNonNull(pFilePath);
		this.objectMapper = Objects.requireNonNull(pObjectMapper);
		loadFromDisk();
	}

	@Override
	public synchronized Optional<UserAccount> findById(UserId pUserId) {
		if (pUserId == null) return Optional.empty();
		return Optional.ofNullable(byId.get(pUserId.toString()));
	}

	@Override
	public synchronized Optional<UserAccount> findByUsername(String pUsername) {
		if (pUsername == null) return Optional.empty();
		return byId.values().stream()
				.filter(a -> pUsername.equalsIgnoreCase(a.username()))
				.findFirst();
	}

	@Override
	public synchronized UserAccount save(UserAccount pAccount) {
		Objects.requireNonNull(pAccount);
		Objects.requireNonNull(pAccount.id());
		Objects.requireNonNull(pAccount.username());

		byId.put(pAccount.id().toString(), pAccount);
		writeToDisk();
		return pAccount;
	}

	@Override
	public synchronized List<UserAccount> findAll() {
		return new ArrayList<>(byId.values());
	}

	private void loadFromDisk() {
		try {
			if (Files.notExists(filePath)) {
				Files.createDirectories(filePath.getParent());
				writeToDisk();
				return;
			}
			byte[] json = Files.readAllBytes(filePath);
			if (json.length == 0) return;

			Map<String, StoredUserAccount> stored = objectMapper.readValue(
					json, new TypeReference<>() {
					}
			);
			byId.clear();
			for (var entry : stored.entrySet()) {
				var s = entry.getValue();
				byId.put(entry.getKey(), new UserAccount(UserId.parse(s.id), s.username));
			}
		} catch (IOException e) {
			throw new IllegalStateException("Failed to load user accounts from " + filePath, e);
		}
	}

	private void writeToDisk() {
		try {
			Map<String, StoredUserAccount> stored = new LinkedHashMap<>();
			for (var entry : byId.entrySet()) {
				UserAccount a = entry.getValue();
				stored.put(entry.getKey(), new StoredUserAccount(a.id().toString(), a.username()));
			}
			byte[] json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(stored);
			Files.write(filePath, json);
		} catch (IOException e) {
			throw new IllegalStateException("Failed to write user accounts to " + filePath, e);
		}
	}

	private static final class StoredUserAccount {
		public String id;
		public String username;

		@SuppressWarnings("unused")
		public StoredUserAccount() {
		}

		public StoredUserAccount(String pUserId, String pUsername) {
			this.id = pUserId;
			this.username = pUsername;
		}
	}
}