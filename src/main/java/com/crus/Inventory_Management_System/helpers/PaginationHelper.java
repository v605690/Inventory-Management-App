package com.crus.Inventory_Management_System.helpers;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Configuration
public class PaginationHelper {

    public <T> Page<T> newPageList(List<T> list, Pageable pageable) {
        int start = (int) pageable.getOffset();

        if (start > list.size()) {
            return new PageImpl<>(List.of(), pageable, list.size());
        }

        int end = Math.min((start + pageable.getPageSize()), list.size());
        List<T> pageContent = list.subList(start, end);

        return new PageImpl<>(pageContent, pageable, list.size());
    }
}