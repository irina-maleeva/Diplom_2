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
import static org.example.utils.RandomString.randomString;
import static org.junit.Assert.assertEquals;

public class OrderCreationTest {
    UserClient userClient = new UserClient();
    OrderClient orderClient = new OrderClient();
    IngredientClient ingredientClient = new IngredientClient();
    public IngredientsResponse listOfIngredients;
    List<String> ingredients;

    String accessToken;
    @Before
    public void setup(){

        listOfIngredients =  ingredientClient.getIngredientsData();
        ingredients = new ArrayList<>();
        ingredients.add(listOfIngredients.data.get(0).get_id());
        ingredients.add(listOfIngredients.data.get(1).get_id());
        ingredients.add(listOfIngredients.data.get(2).get_id());

        User user = randomUser();
        Response registerResponse = userClient.register(user);
        accessToken = registerResponse.body().path("accessToken").toString().substring(7);

    }
    @Test
    @DisplayName("Создать заказ с авторизацией и с ингредиентами")
    public void testCreateOrderAfterAuthorization() {
        Order order = new Order(ingredients);
        Response response = orderClient.createOrderWithAuthorization(accessToken, order);
        assertEquals(200, response.statusCode());
        assertEquals(true, response.body().path("success"));
        assertEquals("Бессмертный метеоритный флюоресцентный бургер", response.body().path("name"));
    }

    @Test
    @DisplayName("Создать заказ без авторизации")
    public void testCreateOrderWithoutAuthorization() {
        Order order = new Order(ingredients);
        Response response = orderClient.createOrderWithoutAuthorization(order);
//        В документации сказано, что создавать заказы могут только авторизованные пользователи,
//        поэтому предполагаю что должен быть код 401 - Unauthorized, хотя по факту код ответа 200 -
//        предполагаю что это либо баг документации, либо баг этого API запроса
        assertEquals("Код ответа не соотвествует ожидаемому: 401 - Unauthorized", 401, response.statusCode());
    }

    @Test
    @DisplayName("Создать заказ с авторизацией но без игредиентов")
    public void testCreateOrderWithoutIngredients() {
        Order order = new Order(null);
        Response response = orderClient.createOrderWithAuthorization(accessToken, order);
        assertEquals(400, response.statusCode());
        assertEquals(false, response.body().path("success"));
        assertEquals("Ingredient ids must be provided", response.body().path("message"));
    }

    @Test
    @DisplayName("Создать заказ с неверным хешем игредиентов")
    public void testCreateOrderWithWrongIngredientsIds() {
        ingredients.add(randomString(24));
        Order order = new Order(ingredients);
        Response response = orderClient.createOrderWithAuthorization(accessToken, order);
        assertEquals(500, response.statusCode());
    }
    @After
    public void cleanUp() {
        userClient.deleteUser(accessToken);
    }
}