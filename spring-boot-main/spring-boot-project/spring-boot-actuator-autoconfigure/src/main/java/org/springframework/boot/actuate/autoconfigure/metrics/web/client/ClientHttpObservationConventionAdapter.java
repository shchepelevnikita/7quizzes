/*
 * Copyright 2012-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.boot.actuate.autoconfigure.metrics.web.client;

import io.micrometer.common.KeyValues;
import io.micrometer.core.instrument.Tag;
import io.micrometer.observation.Observation;

import org.springframework.boot.actuate.metrics.web.client.RestTemplateExchangeTagsProvider;
import org.springframework.http.client.observation.ClientHttpObservationContext;
import org.springframework.http.client.observation.ClientHttpObservationConvention;

/**
 * Adapter class that applies {@link RestTemplateExchangeTagsProvider} tags as a
 * {@link ClientHttpObservationConvention}.
 *
 * @author Brian Clozel
 */
@SuppressWarnings({ "removal" })
class ClientHttpObservationConventionAdapter implements ClientHttpObservationConvention {

	private final String metricName;

	private final RestTemplateExchangeTagsProvider tagsProvider;

	ClientHttpObservationConventionAdapter(String metricName, RestTemplateExchangeTagsProvider tagsProvider) {
		this.metricName = metricName;
		this.tagsProvider = tagsProvider;
	}

	@Override
	public boolean supportsContext(Observation.Context context) {
		return context instanceof ClientHttpObservationContext;
	}

	@Override
	@SuppressWarnings("deprecation")
	public KeyValues getLowCardinalityKeyValues(ClientHttpObservationContext context) {
		KeyValues keyValues = KeyValues.empty();
		Iterable<Tag> tags = this.tagsProvider.getTags(context.getUriTemplate(), context.getCarrier(),
				context.getResponse());
		for (Tag tag : tags) {
			keyValues = keyValues.and(tag.getKey(), tag.getValue());
		}
		return keyValues;
	}

	@Override
	public KeyValues getHighCardinalityKeyValues(ClientHttpObservationContext context) {
		return KeyValues.empty();
	}

	@Override
	public String getName() {
		return this.metricName;
	}

}
