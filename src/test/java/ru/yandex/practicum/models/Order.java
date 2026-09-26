package ru.yandex.practicum.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    private List<String> ingredients;
    private String name;
    private boolean success;
    private OrderInfo order;

    public Order(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderInfo {
        private int number;
    }
}