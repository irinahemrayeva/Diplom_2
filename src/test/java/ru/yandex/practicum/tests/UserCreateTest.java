package ru.yandex.practicum.tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.clients.UserClient;
import ru.yandex.practicum.models.User;
import ru.yandex.practicum.utils.UserGenerator;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;
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
    @Description("Проверяем, что уникальный пользователь создаётся и возвращается accessToken")
    public void createUniqueUserTest() {
        Response response = userClient.create(user);
        accessToken = response.then().extract().path("accessToken");

        response.then()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("accessToken", notNullValue());
    }

    @Test
    @DisplayName("Создание уже зарегистрированного пользователя")
    @Description("Проверяем, что повторная регистрация возвращает 403 и сообщение User already exists")
    public void createDuplicateUserTest() {
        Response first = userClient.create(user);
        accessToken = first.then().extract().path("accessToken");

        Response second = userClient.create(user);
        second.then()
                .statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Создание пользователя без поля email")
    @Description("Проверяем, что без email регистрация возвращает 403")
    public void createUserWithoutEmailTest() {
        user.setEmail(null);

        userClient.create(user)
                .then()
                .statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без поля password")
    @Description("Проверяем, что без password регистрация возвращает 403")
    public void createUserWithoutPasswordTest() {
        user.setPassword(null);

        userClient.create(user)
                .then()
                .statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без поля name")
    @Description("Проверяем, что без name регистрация возвращает 403")
    public void createUserWithoutNameTest() {
        user.setName(null);

        userClient.create(user)
                .then()
                .statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }
}