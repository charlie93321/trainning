package com.dxm.test.controller;

import lombok.Data;

import java.util.List;

@Data
public class PageInfo<T> {
    private Integer page;
    private Integer pageSize;
    private Integer startRow;
    private List<T> rows;
    private Integer total;
    private Integer totalPages;
    private String keys;
}
