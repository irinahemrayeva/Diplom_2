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

public class UserLoginTest {

    private UserClient userClient;
    private User user;
    private String accessToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.randomUser();
        Response response = userClient.create(user);
        accessToken = response.then().extract().path("accessToken");
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.delete(accessToken);
        }
    }

    @Test
    @DisplayName("Вход под существующим пользователем")
    public void loginExistingUserSuccess() {
        userClient.login(user)
                .then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("accessToken", notNullValue());
    }

    @Test
    @DisplayName("Вход с неверным логином и паролем")
    public void loginWithWrongCredentialsReturns401() {
        User wrongUser = new User("wrong@yandex.ru", "wrongpass", "Wrong");

        userClient.login(wrongUser)
                .then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }
}