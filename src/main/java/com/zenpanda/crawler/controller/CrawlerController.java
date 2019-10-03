package com.zenpanda.crawler.controller;

import com.zenpanda.crawler.model.PageNode;
import com.zenpanda.crawler.service.CrawlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@RestController
@RequestMapping(CrawlerController.REQUEST_PATH_API_CRAWLER)
public class CrawlerController {

    static final String REQUEST_PATH_API_CRAWLER = "/api/v1/crawler";
    private final CrawlerService crawlerService;

    @Autowired
    public CrawlerController(final CrawlerService crawlerService) {
        this.crawlerService = crawlerService;
    }

    @Async
    @GetMapping
    public CompletableFuture<ResponseEntity<PageNode>> crawl(@RequestParam String url) {

        return CompletableFuture.supplyAsync(() -> crawlerService.crawl(url))
                .thenApply(ResponseEntity::ok)
                .exceptionally(e -> {
                    throw new CompletionException(e.getCause());
                });
    }
}
