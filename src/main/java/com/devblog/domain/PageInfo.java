package com.devblog.domain;

import java.util.List;

import lombok.Data;

@Data
public class PageInfo {

    private int currentPage;        // current page number (1-based)
    private int totalPages;         // total number of pages
    private int totalCount;         // total number of items
    private boolean hasPrevious;    // has previous page
    private boolean hasNext;        // has next page
    private List<Integer> pageNumbers;  // page numbers for pagination display

    // Factory method to create PageInfo
    public static PageInfo of(int currentPage, int totalCount, int pageSize) {
        PageInfo info = new PageInfo();
        info.currentPage = currentPage;
        info.totalCount = totalCount;
        info.totalPages = (int) Math.ceil((double) totalCount / pageSize);
        info.hasPrevious = currentPage > 1;
        info.hasNext = currentPage < info.totalPages;

        // Generate page numbers (show max 10 pages)
        int startPage = Math.max(1, currentPage - 4);
        int endPage = Math.min(info.totalPages, startPage + 9);
        startPage = Math.max(1, endPage - 9);

        info.pageNumbers = java.util.stream.IntStream
                .rangeClosed(startPage, endPage)
                .boxed()
                .collect(java.util.stream.Collectors.toList());

        return info;
    }
}
