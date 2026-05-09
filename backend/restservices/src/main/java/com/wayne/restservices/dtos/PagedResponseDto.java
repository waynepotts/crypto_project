package com.wayne.restservices.dtos;

import org.springframework.data.domain.Page;

import java.util.List;

public class PagedResponseDto<T> {

    private List<T> content;

    private int page;
    private int size;

    private long totalElements;
    private int totalPages;

    private boolean last;

    public PagedResponseDto(Page<T> paged) {
        this.content = paged.getContent();
        this.page = paged.getNumber();
        this.size = paged.getSize();
        this.totalElements = paged.getTotalElements();
        this.totalPages = paged.getTotalPages();
        this.last = paged.isLast();
    }
    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }
}
