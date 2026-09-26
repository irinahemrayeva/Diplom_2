package ru.yandex.practicum.tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.clients.OrderClient;
import ru.yandex.practicum.clients.UserClient;
import ru.yandex.practicum.models.Order;
import ru.yandex.practicum.models.User;
import ru.yandex.practicum.utils.UserGenerator;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class OrderCreateTest {

    private UserClient userClient;
    private OrderClient orderClient;
    private String accessToken;

    private static final List<String> VALID_INGREDIENTS =
            Arrays.asList("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa70");

    @Before
    public void setUp() {
        userClient = new UserClient();
        orderClient = new OrderClient();

        User user = UserGenerator.randomUser();
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
    @DisplayName("Создание заказа с авторизацией")
    @Description("Проверяем, что авторизованный пользователь может создать заказ")
    public void createOrderWithAuthTest() {
        Order order = new Order(VALID_INGREDIENTS);

        orderClient.createOrderWithAuth(order, accessToken)
                .then()
                .statusCode(SC_OK)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    @Description("Проверяем создание заказа без токена")
    public void createOrderWithoutAuthTest() {
        Order order = new Order(VALID_INGREDIENTS);

        orderClient.createOrderWithoutAuth(order)
                .then()
                .statusCode(SC_OK)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа с ингредиентами")
    @Description("Проверяем, что при создании заказа возвращается его номер")
    public void createOrderWithIngredientsTest() {
        Order order = new Order(VALID_INGREDIENTS);

        orderClient.createOrderWithAuth(order, accessToken)
                .then()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("order.number", notNullValue());
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    @Description("Проверяем, что без ингредиентов возвращается 400 и сообщение об ошибке")
    public void createOrderWithoutIngredientsTest() {
        Order order = new Order(Collections.emptyList());

        orderClient.createOrderWithAuth(order, accessToken)
                .then()
                .statusCode(SC_BAD_REQUEST)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    @Description("Проверяем, что неверный хеш ингредиента возвращает 500")
    public void createOrderWithInvalidHashTest() {
        Order order = new Order(Collections.singletonList("invalid_hash_123"));

        orderClient.createOrderWithAuth(order, accessToken)
                .then()
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }
}