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

package org.springframework.boot.actuate.metrics.web.reactive.client;

import io.micrometer.observation.ObservationConvention;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.tck.TestObservationRegistry;
import org.junit.jupiter.api.Test;

import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.ClientObservationConvention;
import org.springframework.web.reactive.function.client.DefaultClientObservationConvention;
import org.springframework.web.reactive.function.client.WebClient;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link ObservationWebClientCustomizer}
 *
 * @author Brian Clozel
 */
@SuppressWarnings("deprecation")
class ObservationWebClientCustomizerTests {

	private static final String TEST_METRIC_NAME = "http.test.metric.name";

	private TestObservationRegistry observationRegistry = TestObservationRegistry.create();

	private ClientObservationConvention observationConvention = new DefaultClientObservationConvention(
			TEST_METRIC_NAME);

	private ObservationWebClientCustomizer customizer = new ObservationWebClientCustomizer(this.observationRegistry,
			this.observationConvention);

	private WebClient.Builder clientBuilder = WebClient.builder();

	@Test
	void shouldCustomizeObservationConfiguration() {
		this.customizer.customize(this.clientBuilder);
		assertThat((ObservationRegistry) ReflectionTestUtils.getField(this.clientBuilder, "observationRegistry"))
				.isEqualTo(this.observationRegistry);
		assertThat((ObservationConvention<?>) ReflectionTestUtils.getField(this.clientBuilder, "observationConvention"))
				.isInstanceOf(DefaultClientObservationConvention.class).extracting("name").isEqualTo(TEST_METRIC_NAME);
	}

}
