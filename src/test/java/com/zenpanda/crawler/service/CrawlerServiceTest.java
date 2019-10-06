package com.zenpanda.crawler.service;

import com.zenpanda.crawler.CrawlerApplication;
import com.zenpanda.crawler.exception.InvalidUrlException;
import com.zenpanda.crawler.model.PageNode;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {CrawlerApplication.class}, webEnvironment = NONE)
class CrawlerServiceTest {

    @Mock
    private FetcherService fetcherService;

    @InjectMocks
    private CrawlerService crawlerService;

    @Test
    void shouldVisitDuplicateChildUrlsOnlyOnce() throws IOException {

        String startUrl = "http://www.google.com";
        String childUrl1 = startUrl + "/a";
        String childUrl2 = startUrl + "/a";
        String childTitle1 = "title1";
        String childTitle2 = "title1";

        Document doc = new Document(startUrl);
        List<PageNode> pageNodeList = Arrays.asList(
                new PageNode(childUrl1, childTitle1),
                new PageNode(childUrl2, childTitle2)
        );

        PageNode expectedResponse = new PageNode(
                startUrl,
                null,
                Arrays.asList(
                        new PageNode(childUrl1, childTitle1)
                )
        );

        when(fetcherService.fetchDocument(any())).thenReturn(doc);
        when(fetcherService.extractUrls(doc, startUrl)).thenReturn(pageNodeList);
        when(fetcherService.extractUrls(doc, childUrl1)).thenReturn(new ArrayList<>());
        when(fetcherService.extractUrls(doc, childUrl2)).thenReturn(new ArrayList<>());

        PageNode actualResponse = crawlerService.crawl(startUrl);

        verify(fetcherService, times(1)).fetchDocument(startUrl);
        verify(fetcherService, times(1)).fetchDocument(childUrl1);
        verify(fetcherService, times(1)).extractUrls(doc, startUrl);
        verify(fetcherService, times(1)).extractUrls(doc, childUrl1);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void shouldCrawlPartiallyIncaseOfExceptionWhileFetchingDocument() throws IOException {

        String startUrl = "http://www.google.com";
        String childUrl1 = startUrl + "/a";
        String childUrl2 = startUrl + "/b";
        String childTitle1 = "title1";
        String childTitle2 = "title2";

        Document doc = new Document(startUrl);
        List<PageNode> pageNodeList = Arrays.asList(
                new PageNode(childUrl1, childTitle1),
                new PageNode(childUrl2, childTitle2)
        );

        PageNode expectedResponse = new PageNode(
                startUrl,
                null,
                Arrays.asList(
                        new PageNode(childUrl1, childTitle1)
                )
        );

        when(fetcherService.fetchDocument(startUrl)).thenReturn(doc);
        when(fetcherService.fetchDocument(childUrl1)).thenReturn(doc);
        when(fetcherService.fetchDocument(childUrl2)).thenThrow(new IOException("Error while fetching web page"));
        when(fetcherService.extractUrls(doc, startUrl)).thenReturn(pageNodeList);
        when(fetcherService.extractUrls(doc, childUrl1)).thenReturn(new ArrayList<>());

        PageNode actualResponse = crawlerService.crawl(startUrl);

        verify(fetcherService, times(3)).fetchDocument(any());
        verify(fetcherService, times(1)).extractUrls(doc, startUrl);
        verify(fetcherService, times(1)).extractUrls(doc, childUrl1);
        verify(fetcherService, times(0)).extractUrls(doc, childUrl2);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void shouldThrowExceptionIncaseOfMalformedUrl() throws IOException {

        String startUrl = "google.com";
        when(fetcherService.fetchDocument(startUrl)).thenThrow(new InvalidUrlException());

        assertThrows(InvalidUrlException.class, () -> {
            crawlerService.crawl(startUrl);
        });
    }

    @Test
    void shouldCrawlWithNoChildLinksSuccessfully() throws IOException {

        String startUrl = "http://www.google.com";
        Document doc = new Document(startUrl);

        PageNode expectedResponse = new PageNode(startUrl);

        when(fetcherService.fetchDocument(startUrl)).thenReturn(doc);
        when(fetcherService.extractUrls(doc, startUrl)).thenReturn(new ArrayList<>());

        PageNode actualResponse = crawlerService.crawl(startUrl);

        verify(fetcherService, times(1)).fetchDocument(startUrl);
        verify(fetcherService, times(1)).extractUrls(doc, startUrl);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void shouldCrawlWithChildLinksSuccessfully() throws IOException {

        String startUrl = "http://www.google.com";
        String childUrl1 = startUrl + "/a";
        String childUrl2 = startUrl + "/b";
        String childTitle1 = "title1";
        String childTitle2 = "title2";

        Document doc = new Document(startUrl);
        List<PageNode> pageNodeList = Arrays.asList(
                new PageNode(childUrl1, childTitle1),
                new PageNode(childUrl2, childTitle2)
        );

        PageNode expectedResponse = new PageNode(
                startUrl,
                null,
                Arrays.asList(
                        new PageNode(childUrl1, childTitle1),
                        new PageNode(childUrl2, childTitle2)
                )
        );

        when(fetcherService.fetchDocument(any())).thenReturn(doc);
        when(fetcherService.extractUrls(doc, startUrl)).thenReturn(pageNodeList);
        when(fetcherService.extractUrls(doc, childUrl1)).thenReturn(new ArrayList<>());
        when(fetcherService.extractUrls(doc, childUrl2)).thenReturn(new ArrayList<>());

        PageNode actualResponse = crawlerService.crawl(startUrl);

        verify(fetcherService, times(3)).fetchDocument(any());
        verify(fetcherService, times(3)).extractUrls(any(), any());

        assertEquals(expectedResponse, actualResponse);
    }
}
