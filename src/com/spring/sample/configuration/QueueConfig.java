package com.spring.sample.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

import com.github.sonus21.rqueue.spring.EnableRqueue;
import com.github.sonus21.rqueue.spring.RqueueMetricsProperties;

import io.micrometer.prometheus.PrometheusMeterRegistry;

@Configuration
@EnableRqueue
@EnableRedisRepositories
@EnableAsync
@PropertySource("classpath:application.properties")
public class QueueConfig {

	@Bean
	public PrometheusMeterRegistry meterRegistry() {
		return new PrometheusMeterRegistry(key -> null);
	}

	@Bean
	public RqueueMetricsProperties rqueueMetricsProperties() {
		return new RqueueMetricsProperties();
	}
}
