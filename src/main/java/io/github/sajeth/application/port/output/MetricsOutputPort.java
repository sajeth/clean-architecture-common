package io.github.sajeth.application.port.output;

import java.util.Map;

/**
 * Output port for recording application metrics.
 */
public interface MetricsOutputPort {
    void increment(String metricName, Map<String, String> tags);

    default void increment(String metricName) {
        increment(metricName, Map.of());
    }

    void recordTimer(String metricName, long durationMillis, Map<String, String> tags);
}
