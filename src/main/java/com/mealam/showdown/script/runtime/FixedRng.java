/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.script.runtime;

/**
 * Deterministic RNG that cycles through fixed values.
 * Useful for testing and reproducible runs.
 */
public final class FixedRng implements Rng {

	private final double[] values;
	private int index;

	public FixedRng(double... pValues) {
		this.values = (pValues == null || pValues.length == 0)
				? new double[] { 0.0 }
				: pValues.clone();
		this.index = 0;
	}

	@Override
	public double nextDouble() {
		double value = values[index % values.length];
		index++;
		return value;
	}

	public void reset() {
		index = 0;
	}
}
