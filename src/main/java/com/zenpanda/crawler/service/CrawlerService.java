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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

@Service
public class CrawlerService {

    @Value("${jsoup.timeout}")
    private Integer jsoupTimeout;

    @Value("${jsoup.follow.redirects}")
    private boolean jsoupFollowRedirects;

    public PageNode crawl(String startUrl) {

        // list of web pages to be examined
        PageNode startNode = new PageNode(startUrl);
        Stack<PageNode> stack = new Stack<>();
        stack.add(startNode);

        // set of examined web pages
        Set<String> visitedUrls = new HashSet<>();
        visitedUrls.add(startNode.getUrl());

        while (!stack.isEmpty()) {
            PageNode pageNode = stack.pop();

            System.out.println(pageNode.getUrl());

            List<PageNode> newPageNodes = fetchUrls(pageNode.getUrl());

            for (PageNode newPageNode : newPageNodes) {

                if (!isUrlVisited(newPageNode.getUrl(), visitedUrls)) {
                    pageNode.addNode(newPageNode);
                    stack.add(newPageNode);
                    visitedUrls.add(newPageNode.getUrl());
                }
            }
        }

        return startNode;
    }

    private boolean isUrlVisited(String url, Set<String> visitedUrls) {
        return visitedUrls.contains(url);
    }

    private List<PageNode> fetchUrls(String baseUrl) {

        try {
            Document doc = Jsoup.connect(baseUrl)
                    .timeout(jsoupTimeout)
                    .followRedirects(jsoupFollowRedirects)
                    .get();

            return doc.select("a[href]").stream()
                    .map(element -> new PageNode(element.attr("abs:href"), doc.title()))
                    .filter(pageNode -> validateUrl(pageNode.getUrl(), baseUrl))
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new InvalidUrlException();
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    private boolean validateUrl(String url, String baseUrl) {
        try {
            return UrlUtil.isValidUrl(url) && UrlUtil.isSameDomain(baseUrl, url);
        } catch (URISyntaxException e) {
            return false;
        }
    }
}
