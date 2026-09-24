package ru.yandex.practicum.clients;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.practicum.models.Order;

import static io.restassured.RestAssured.given;

public class OrderClient {

    private static final String BASE_URL = "https://stellarburgers.education-services.ru";

    @Step("Создать заказ с авторизацией")
    public Response createOrderWithAuth(Order order, String accessToken) {
        return given()
                .baseUri(BASE_URL)
                .header("Content-Type", "application/json")
                .header("Authorization", accessToken)
                .body(order)
                .when()
                .post("/api/orders");
    }

    @Step("Создать заказ без авторизации")
    public Response createOrderWithoutAuth(Order order) {
        return given()
                .baseUri(BASE_URL)
                .header("Content-Type", "application/json")
                .body(order)
                .when()
                .post("/api/orders");
    }

}
