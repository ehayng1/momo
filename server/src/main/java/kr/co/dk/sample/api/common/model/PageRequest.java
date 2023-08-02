package kr.co.dk.sample.api.common.model;

import lombok.Getter;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

public final class PageRequest {
    private static final int MIN_SIZE = 1;
    private static final int MAX_SIZE = 100;
    @Getter
    private int page = 1;
    @Getter
    private int size = 10;
    private final List<Sort.Order> orders = new ArrayList<>();

    public PageRequest() {
    }

    public static org.springframework.data.domain.PageRequest of(int size) {
        return org.springframework.data.domain.PageRequest.of(0, size);
    }

    public void setPage(int page) {
        this.page = page <= 0 ? 1 : page;
    }

    public void setSize(int size) {
        this.size = size > MAX_SIZE ? MIN_SIZE : size;
        this.size = size < 1 ? MIN_SIZE : size;
    }

    public void addOrder(Sort.Order order) {
        this.orders.add(order);
    }

    public org.springframework.data.domain.PageRequest of() {
        if (this.orders.isEmpty()) {
            return org.springframework.data.domain.PageRequest.of(page - 1, size);
        } else {
            return org.springframework.data.domain.PageRequest.of(page - 1, size, Sort.by(orders));
        }
    }
}
