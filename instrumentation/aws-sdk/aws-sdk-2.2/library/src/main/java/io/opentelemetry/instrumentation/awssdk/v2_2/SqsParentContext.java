/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.opentelemetry.instrumentation.awssdk.v2_2;

import io.opentelemetry.context.Context;
import io.opentelemetry.context.propagation.TextMapGetter;
import io.opentelemetry.contrib.awsxray.propagator.AwsXrayPropagator;
import java.util.Collections;
import java.util.Map;

final class SqsParentContext {

  enum MapGetter implements TextMapGetter<Map<String, String>> {
    INSTANCE;

    @Override
    public Iterable<String> keys(Map<String, String> map) {
      return map.keySet();
    }

    @Override
    public String get(Map<String, String> map, String s) {
      return map.get(s);
    }
  }

  static final String AWS_TRACE_SYSTEM_ATTRIBUTE = "AWSTraceHeader";

  static Context ofSystemAttributes(Map<String, String> systemAttributes) {
    String traceHeader = systemAttributes.get(AWS_TRACE_SYSTEM_ATTRIBUTE);
    return AwsXrayPropagator.getInstance()
        .extract(
            Context.root(),
            Collections.singletonMap("X-Amzn-Trace-Id", traceHeader),
            MapGetter.INSTANCE);
  }

  private SqsParentContext() {}
}
