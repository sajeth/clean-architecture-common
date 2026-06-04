package io.github.sajeth.infrastructre.adapter.secondary.observability;

import io.github.sajeth.application.port.output.MetricsOutputPort;

import java.util.Map;

public class NoOpMetricsAdapter implements MetricsOutputPort {

    @Override
    public void increment(String metricName, Map<String, String> tags) {
        // no-op: replace with MicrometerMetricsAdapter when Micrometer is on classpath
    }

    @Override
    public void recordTimer(String metricName, long durationMillis, Map<String, String> tags) {
        // no-op
    }
}
