package com.mealam.showdown.loader.cache.common;

import com.mealam.showdown.loader.json.deserialize.common.Measurement;
import org.jetbrains.annotations.Nullable;

public record MeasurementCache(
		@Nullable Float ft,
		@Nullable Float meters,
		@Nullable Float inches,
		@Nullable Float centimeters,
		@Nullable Float yards,
		@Nullable Float kilometers,
		@Nullable Float miles
) {
	public static MeasurementCache construct(Measurement pMeasurement) {
		return new MeasurementCache(
				pMeasurement.ft(),
				pMeasurement.meters(),
				pMeasurement.inches(),
				pMeasurement.centimeters(),
				pMeasurement.yards(),
				pMeasurement.kilometers(),
				pMeasurement.miles()
		);
	}
}