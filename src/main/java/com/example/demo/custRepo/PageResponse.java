package com.example.demo.custRepo;

import java.util.List;

public class PageResponse<T> {

    private List<T> data;
    private long totalCount;

    public PageResponse(List<T> data, long totalCount) {
        this.data = data;
        this.totalCount = totalCount;
    }

    public List<T> getData() {
        return data;
    }

    public long getTotalCount() {
        return totalCount;
    }
}
