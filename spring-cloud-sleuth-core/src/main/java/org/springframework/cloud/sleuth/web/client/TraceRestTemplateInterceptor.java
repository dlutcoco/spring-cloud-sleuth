/*
 * Copyright 2012-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.cloud.sleuth.web.client;

import static org.springframework.cloud.sleuth.Trace.SPAN_ID_NAME;
import static org.springframework.cloud.sleuth.Trace.TRACE_ID_NAME;

import java.io.IOException;

import org.springframework.cloud.sleuth.SpanHolder;
import org.springframework.cloud.sleuth.Trace;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

/**
 * Interceptor that verifies whether the trance and span id has been set on the
 * request and sets them if one or both of them are missing.
 *
 * @see org.springframework.web.client.RestTemplate
 * @see Trace
 *
 * @author Marcin Grzejszczak, 4financeIT
 * @author Spencer Gibb
 */
public class TraceRestTemplateInterceptor implements ClientHttpRequestInterceptor {

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body,
			ClientHttpRequestExecution execution) throws IOException {
		setHeader(request, SPAN_ID_NAME, SpanHolder.getCurrentSpan().getSpanId());
		setHeader(request, TRACE_ID_NAME, SpanHolder.getCurrentSpan().getTraceId());
		return execution.execute(request, body);
	}

	public void setHeader(HttpRequest request, String spanIdName, String spanId) {
		if (!request.getHeaders().containsKey(spanIdName) && SpanHolder.isTracing()) {
			request.getHeaders().add(spanIdName, spanId);
		}
	}

}
