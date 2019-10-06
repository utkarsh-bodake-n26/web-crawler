package com.zenpanda.crawler.service;

import com.zenpanda.crawler.model.PageNode;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

@Service
public class CrawlerService {

    private final FetcherService fetcherService;

    @Autowired
    public CrawlerService(final FetcherService fetcherService) {
        this.fetcherService = fetcherService;
    }

    /**
     * Crawls the website in depth first manner tzo produce the sitemap.
     *
     * @param startUrl Base url from which the web crawler will start
     * @return
     */
    public PageNode crawl(String startUrl) {

        // initialize the start web page
        PageNode startNode = new PageNode(startUrl);
        Stack<PageNode> stack = new Stack<>();
        stack.add(startNode);

        // set of examined web pages
        Set<String> visitedUrls = new HashSet<>();
        visitedUrls.add(startNode.getUrl());

        while (!stack.isEmpty()) {

            PageNode pageNode = stack.pop();
            List<PageNode> newPageNodes = getPageNodes(pageNode.getUrl());

            for (PageNode newPageNode : newPageNodes) {

                if (!visitedUrls.contains(newPageNode.getUrl())) {
                    // list of web pages to be examined
                    pageNode.addNode(newPageNode);
                    stack.add(newPageNode);
                    visitedUrls.add(newPageNode.getUrl());
                }
            }
        }

        return startNode;
    }

    private List<PageNode> getPageNodes(String url) {
        try {
            Document doc = fetcherService.fetchDocument(url);
            return fetcherService.extractUrls(doc, url);
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }
}
