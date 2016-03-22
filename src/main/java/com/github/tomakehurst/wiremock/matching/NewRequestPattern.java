package com.github.tomakehurst.wiremock.matching;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.RequestMethod;
import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.Map;

public class NewRequestPattern implements ValueMatcher<Request> {

    private final UrlPattern url;
    private final RequestMethod method;
    private final Map<String, MultiValuePattern> headers;

    public NewRequestPattern(@JsonProperty("url") UrlPattern url,
                             @JsonProperty("method") RequestMethod method,
                             @JsonProperty("headers") Map<String, MultiValuePattern> headers) {
        this.url = url;
        this.method = method;
        this.headers = headers;
    }

    @Override
    public MatchResult match(Request request) {
        return MatchResult.aggregate(
            url.match(request.getUrl()),
            method.match(request.getMethod()),
            allHeadersMatchResult(request)
        );
    }

    private MatchResult allHeadersMatchResult(final Request request) {
        if (headers != null && !headers.isEmpty()) {
            return MatchResult.aggregate(
                FluentIterable.from(headers.entrySet()).transform(new Function<Map.Entry<String, MultiValuePattern>, MatchResult>() {
                    public MatchResult apply(Map.Entry<String, MultiValuePattern> headerPattern) {
                        return headerPattern.getValue().match(request.header(headerPattern.getKey()));
                    }
                }).toList()
            );
        }

        return MatchResult.exactMatch();
    }

    public boolean isMatchedBy(Request request, Map<String, RequestMatcherExtension> customMatchers) {
        return match(request).isExactMatch();
    }
}
