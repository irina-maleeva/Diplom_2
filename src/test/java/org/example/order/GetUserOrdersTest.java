package org.example.order;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.example.model.IngredientsResponse;
import org.example.model.Order;
import org.example.model.User;
import org.example.user.UserClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.example.user.UserGenerator.randomUser;
import static org.junit.Assert.*;

public class GetUserOrdersTest {
    UserClient userClient = new UserClient();
    OrderClient orderClient = new OrderClient();
    String accessToken;
    IngredientClient ingredientClient = new IngredientClient();
    public IngredientsResponse listOfIngredients;
    List<String> ingredients = new ArrayList<>();


    @Before
    public void setup() {
        listOfIngredients = ingredientClient.getIngredientsData();
        ingredients.add(listOfIngredients.data.get(0).get_id());
        ingredients.add(listOfIngredients.data.get(1).get_id());
        ingredients.add(listOfIngredients.data.get(2).get_id());

        User user = randomUser();
        Response registerResponse = userClient.register(user);
        accessToken = registerResponse.body().path("accessToken").toString().substring(7);

        Order order1 = new Order(ingredients);
        Response orderResponse = orderClient.createOrderWithAuthorization(accessToken, order1);
    }

    @Test
    @DisplayName("Получение списка заказов авторизованного пользователя")
    public void testGetAuthorizedUserOrders() {
        Response response = orderClient.getUserOrders(accessToken);
        assertEquals(200, response.statusCode());
        assertEquals(true, response.body().path("success"));
        assertNotNull(response.body().path("orders"));
    }

    @Test
    @DisplayName("Получение списка заказов неавторизованного пользователя")
    public void testGetUnauthorizedUserOrders() {
        Response response = orderClient.getUserOrdersWithoutAuthorization();
        assertEquals(401, response.statusCode());
        assertEquals(false, response.body().path("success"));
        assertEquals("You should be authorised", response.body().path("message"));
    }
    @After
    public void cleanUp() {
        userClient.deleteUser(accessToken);
    }
}