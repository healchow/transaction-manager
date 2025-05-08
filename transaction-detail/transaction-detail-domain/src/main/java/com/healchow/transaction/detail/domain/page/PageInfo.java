package com.healchow.transaction.detail.domain.page;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Page info
 *
 * @param <T> type of data in page
 */
@Data
public final class PageInfo<T> {

    /**
     * Data record
     */
    private List<T> list;

    /**
     * Total number of records
     */
    private Long total;

    /**
     * Page size
     */
    private Integer pageSize;

    /**
     * Page number
     */
    private Integer pageNum;

    public PageInfo() {
    }

    public PageInfo(List<T> list, Long total) {
        this.list = list;
        this.total = total;
    }

    public PageInfo(List<T> list, Long total, Integer pageNum, Integer pageSize) {
        this.list = list;
        this.total = total;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public <R> PageInfo<R> map(Function<? super T, ? extends R> mapper) {
        List<R> newList = list.stream().map(mapper).collect(Collectors.toList());
        return new PageInfo<>(newList, total, pageNum, pageSize);
    }

    public PageInfo<T> foreach(Consumer<? super T> action) {
        list.forEach(action);
        return this;
    }

    public static <T> PageInfo<T> empty() {
        return new PageInfo<>(new ArrayList<>(), 0L);
    }

}