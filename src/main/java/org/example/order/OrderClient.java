package org.example.order;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.example.model.Order;
import org.example.utils.BaseClient;

import static io.restassured.RestAssured.given;

public class OrderClient extends BaseClient {
    private static final String ORDER_URL = "https://stellarburgers.nomoreparties.site/api/orders";


    @Step("Создание заказа без авторизации")
    public Response createOrderWithoutAuthorization(Order order) {
        return given()
                .spec(getBaseSpec())
                .and()
                .body(order)
                .when()
                .post(ORDER_URL);
    }

    @Step("Создание заказа с авторизацией")
    public Response createOrderWithAuthorization(String accessToken, Order order) {
        return given()
                .header("authorization", "bearer "+ accessToken)
                .spec(getBaseSpec())
                .and()
                .body(order)
                .when()
                .post(ORDER_URL);
    }

    @Step("Получение заказов пользователя")
    public Response getUserOrders(String accessToken) {
        return given()
                .header("authorization", "bearer "+ accessToken)
                .spec(getBaseSpec())
                .get(ORDER_URL);
    }

    @Step("Получение заказов пользователя без авторизации")
    public Response getUserOrdersWithoutAuthorization() {
        return given()
                .spec(getBaseSpec())
                .get(ORDER_URL);
    }
}
