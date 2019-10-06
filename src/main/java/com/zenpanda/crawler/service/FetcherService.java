package com.zenpanda.crawler.service;

import com.zenpanda.crawler.exception.InvalidUrlException;
import com.zenpanda.crawler.model.PageNode;
import com.zenpanda.crawler.util.UrlUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class FetcherService {

    private static final Logger Log = Logger.getLogger(FetcherService.class.getName());
    private static final String linkCssQuery = "a[href]";
    private static final String linkAttributeKey = "abs:href";

    @Value("${jsoup.timeout}")
    private Integer jsoupTimeout;

    @Value("${jsoup.follow.redirects}")
    private boolean jsoupFollowRedirects;

    /**
     * Fetch the entire web page corresponding to the given URL.
     */
    Document fetchDocument(String baseUrl) throws IOException {

        Log.info(baseUrl);
        try {
            return Jsoup.connect(baseUrl)
                    .timeout(jsoupTimeout)
                    .followRedirects(jsoupFollowRedirects)
                    .get();
        } catch (IllegalArgumentException e) {
            throw new InvalidUrlException();
        }
    }

    /**
     * Extract all the urls from the given document.
     * Exclude urls which have different base domain.
     */
    List<PageNode> extractUrls(Document doc, String baseUrl) {

        return doc.select(linkCssQuery).stream()
                .map(element -> new PageNode(element.attr(linkAttributeKey), doc.title()))
                .filter(pageNode -> validateUrl(pageNode.getUrl(), baseUrl))
                .collect(Collectors.toList());
    }

    /**
     * Check if the given URL is valid, also checks if the URL has same domain as the base URL.
     */
    private boolean validateUrl(String url, String baseUrl) {
        try {
            return UrlUtil.isValidUrl(url) && UrlUtil.isSameDomain(baseUrl, url);
        } catch (URISyntaxException e) {
            return false;
        }
    }
}
