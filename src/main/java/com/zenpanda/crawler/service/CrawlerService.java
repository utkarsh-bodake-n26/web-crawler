package com.zenpanda.crawler.service;

import com.zenpanda.crawler.model.PageNode;
import com.zenpanda.crawler.util.UrlUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

@Service
public class CrawlerService {

    public PageNode crawl(String startUrl) {

        // list of web pages to be examined
        Stack<String> stack = new Stack<>();
        stack.add(startUrl);

        // set of examined web pages
        Set<String> visitedUrls = new HashSet<>();
        visitedUrls.add(startUrl);

        while (!stack.isEmpty()) {
            String url = stack.pop();

            System.out.println(url);

            List<String> newUrls = fetchUrls(url);

            for (String newUrl : newUrls) {

                if (!isUrlVisited(newUrl, visitedUrls)) {
                    stack.add(newUrl);
                    visitedUrls.add(newUrl);
                }
            }
        }

        return null;
    }

    private boolean isUrlVisited(String url, Set<String> visitedUrls) {
        return visitedUrls.contains(url);
    }

    private List<String> fetchUrls(String baseUrl) {

        try {
            Document doc = Jsoup.connect(baseUrl)
                    .timeout(60000)
                    .followRedirects(true)
                    .get();

            return doc.select("a[href]").stream()
                    .map(element -> element.attr("abs:href"))
                    .filter(url -> {
                        try {
                            return UrlUtil.isValidUrl(url) && UrlUtil.isSameDomain(baseUrl, url);
                        } catch (URISyntaxException e) {
                            return false;
                        }
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
