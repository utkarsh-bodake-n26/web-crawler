package com.zenpanda.crawler.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class UrlUtil {

    public static boolean isValidUrl(String url) {
        /* Try creating a valid URL */
        try {
            new URL(url).toURI();
            return true;
        }

        // If there was an Exception
        // while creating URL object
        catch (Exception e) {
            return false;
        }
    }

    private static String getDomain(String url) throws URISyntaxException {
        URI uri = new URI(url);
        String domain = uri.getHost();
        return domain.startsWith("www.") ? domain.substring(4) : domain;
    }

    public static boolean isSameDomain(String url1,
                                       String url2) throws URISyntaxException {
        return getDomain(url1).equalsIgnoreCase(getDomain(url2));
    }
}
