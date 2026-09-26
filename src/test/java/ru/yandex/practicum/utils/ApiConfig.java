package ru.yandex.practicum.utils;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

public class ApiConfig {

    public static final String BASE_URL = "https://stellarburgers.education-services.ru";

    public static RequestSpecification baseSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(BASE_URL)
                .setContentType("application/json")
                .build();
    }
}