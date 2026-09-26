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

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
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
    @Description("Проверяем, что существующий пользователь успешно логинится")
    public void loginExistingUserTest() {
        userClient.login(user)
                .then()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("accessToken", notNullValue());
    }

    @Test
    @DisplayName("Вход с неверным логином")
    @Description("Проверяем, что при неверном email возвращается 401")
    public void loginWithWrongEmailTest() {
        User wrongEmailUser = new User("wrong_email@yandex.ru", user.getPassword(), user.getName());

        userClient.login(wrongEmailUser)
                .then()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Вход с неверным паролем")
    @Description("Проверяем, что при неверном пароле возвращается 401")
    public void loginWithWrongPasswordTest() {
        User wrongPassUser = new User(user.getEmail(), "wrong_password_123", user.getName());

        userClient.login(wrongPassUser)
                .then()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }
}