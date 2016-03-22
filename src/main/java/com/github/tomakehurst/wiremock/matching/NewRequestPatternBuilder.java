package com.github.tomakehurst.wiremock.matching;

import com.github.tomakehurst.wiremock.http.RequestMethod;

import java.util.Map;

import static com.google.common.collect.Maps.newLinkedHashMap;

public class NewRequestPatternBuilder {

    private UrlPattern url;
    private RequestMethod method;
    private Map<String, MultiValuePattern> headers = newLinkedHashMap();

    public NewRequestPatternBuilder() {
    }

    public NewRequestPatternBuilder(RequestMethod method, UrlPattern url) {
        this.method = method;
        this.url = url;
    }

    public static NewRequestPatternBuilder newRequestPattern(RequestMethod method, UrlPattern url) {
        return new NewRequestPatternBuilder(method, url);
    }

    public static NewRequestPatternBuilder newRequestPattern() {
        return new NewRequestPatternBuilder();
    }

    public NewRequestPatternBuilder withUrl(String url) {
        this.url = UrlPattern.equalsTo(url);
        return this;
    }

    public NewRequestPattern build() {
        return new NewRequestPattern(url, method, headers);
    }

    public NewRequestPatternBuilder withHeader(String key, MultiValuePattern valuePattern) {
        headers.put(key, valuePattern);
        return this;
    }
}
