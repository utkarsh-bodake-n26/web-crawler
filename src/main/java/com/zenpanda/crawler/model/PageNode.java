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
    }

    public PageNode(String url, String title) {
        this.url = url;
        this.title = title;
    }

    public PageNode(String url, String title, List<PageNode> nodes) {
        this.url = url;
        this.title = title;
        this.nodes = nodes;
    }

    public void addNode(PageNode pageNode) {

        if (this.nodes == null) {
            this.nodes = new ArrayList<>();
        }

        this.nodes.add(pageNode);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PageNode)) return false;

        PageNode that = (PageNode) o;

        if (this.nodes == null && that.nodes != null)
            return false;
        else if (this.nodes != null) {

            if (that.nodes == null) return  false;

            for (int i = 0; i < this.nodes.size(); i++) {

                if(!this.nodes.get(i).equals(that.nodes.get(i)))
                    return false;
            }
        }

        return this.url == null ? that.url == null : this.url.equals(that.url) &&
                this.title == null ? that.title == null : this.title.equals(that.title);
    }
}
