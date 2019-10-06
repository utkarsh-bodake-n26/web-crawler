package com.zenpanda.crawler.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class UrlUtil {

    public static boolean isValidUrl(String url) {

        try {
            new URL(url).toURI();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isSameDomain(String url1,
                                       String url2) throws URISyntaxException {
        return getDomain(url1).equalsIgnoreCase(getDomain(url2));
    }

    private static String getDomain(String url) throws URISyntaxException {
        URI uri = new URI(url);
        String domain = uri.getHost();
        if (domain == null) throw new URISyntaxException(url, "host is null");
        return domain.startsWith("www.") ? domain.substring(4) : domain;
    }
}
