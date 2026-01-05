/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.battle.get;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.mealam.showdown.battle.api.BattleBaseTest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import org.junit.jupiter.api.Test;

public class BattleGet200Test extends BattleBaseTest {

	private static final int STATUS_CODE = 200;

	@Test
	void shouldGetIdAfterCreatingManyBattles() throws Exception {
		int n = 25;
		ExecutorService pool = Executors.newFixedThreadPool(8);
		List<Callable<String>> tasks = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			tasks.add(() -> api.createBattleAndGetId());
		}
		List<Future<String>> futures = pool.invokeAll(tasks);
		pool.shutdown();

		List<String> ids = new ArrayList<>();
		for (Future<String> future : futures) {
			String id = future.get(10, TimeUnit.SECONDS);
			assertNotNull(id);
			assertFalse(id.isBlank());
			assertTrue(id.matches("[A-Za-z0-9_-]+"));
			ids.add(id);

			assertEquals(STATUS_CODE, api.get(id).statusCode());
		}

		long distinct = ids.stream().distinct().count();
		assertEquals(ids.size(), distinct, "Battle ids should be unique");
	}
}
