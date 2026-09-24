package ru.yandex.practicum.tests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.clients.UserClient;
import ru.yandex.practicum.models.User;
import ru.yandex.practicum.utils.UserGenerator;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UserCreateTest {

    private UserClient userClient;
    private User user;
    private String accessToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.randomUser();
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.delete(accessToken);
        }
    }

    @Test
    @DisplayName("Создание уникального пользователя")
    public void createUniqueUserSuccess() {
        Response response = userClient.create(user);
        accessToken = response.then().extract().path("accessToken");

        response.then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("accessToken", notNullValue());
    }

    @Test
    @DisplayName("Создание уже зарегистрированного пользователя")
    public void createDuplicateUserReturns403() {
        Response first = userClient.create(user);
        accessToken = first.then().extract().path("accessToken");

        Response second = userClient.create(user);
        second.then()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Создание пользователя без обязательного поля email")
    public void createUserWithoutEmailReturns403() {
        user.setEmail(null);

        userClient.create(user)
                .then()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }
}