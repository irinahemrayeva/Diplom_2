package ru.yandex.practicum.clients;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.practicum.models.User;
import ru.yandex.practicum.utils.ApiConfig;

import static io.restassured.RestAssured.given;

public class UserClient {

    @Step("Создать пользователя: {user.email}")
    public Response create(User user) {
        return given()
                .spec(ApiConfig.baseSpec())
                .body(user)
                .when()
                .post("/api/auth/register");
    }

    @Step("Залогинить пользователя: {user.email}")
    public Response login(User user) {
        return given()
                .spec(ApiConfig.baseSpec())
                .body(user)
                .when()
                .post("/api/auth/login");
    }

    @Step("Удалить пользователя по токену")
    public Response delete(String accessToken) {
        return given()
                .spec(ApiConfig.baseSpec())
                .header("Authorization", accessToken)
                .when()
                .delete("/api/auth/user");
    }
}