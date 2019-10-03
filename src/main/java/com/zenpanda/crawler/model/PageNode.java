package com.zenpanda.crawler.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class PageNode {

    private String url;
    private String title;
    private List<PageNode> nodes;

    public PageNode(String url) {
        this.url = url;
        this.nodes = new ArrayList<>();
    }

    public void addNode(PageNode pageNode) {

        if (this.nodes == null) {
            this.nodes = new ArrayList<>();
        }

        this.nodes.add(pageNode);
    }
}
