package com.crus.Inventory_Management_System.helpers;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class SortHelper {

    public Sort.Direction parseSort(String sortOrder) {
        String normalSortOrder = sortOrder.toLowerCase();
        return switch (normalSortOrder) {
            case "desc" -> Sort.Direction.DESC;
            case "asc" -> Sort.Direction.ASC;
            default -> throw new IllegalArgumentException("Invalid sort order " + sortOrder);
        };
    }

    public Pageable createPageable(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort.Direction direction = parseSort(sortOrder);
        return PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortBy));
    }
}

