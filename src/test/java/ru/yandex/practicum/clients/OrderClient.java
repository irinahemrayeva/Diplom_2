package ru.yandex.practicum.clients;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.practicum.models.Order;
import ru.yandex.practicum.utils.ApiConfig;

import static io.restassured.RestAssured.given;

public class OrderClient {

    @Step("Создать заказ с авторизацией")
    public Response createOrderWithAuth(Order order, String accessToken) {
        return given()
                .spec(ApiConfig.baseSpec())
                .header("Authorization", accessToken)
                .body(order)
                .when()
                .post("/api/orders");
    }

    @Step("Создать заказ без авторизации")
    public Response createOrderWithoutAuth(Order order) {
        return given()
                .spec(ApiConfig.baseSpec())
                .body(order)
                .when()
                .post("/api/orders");
    }
}