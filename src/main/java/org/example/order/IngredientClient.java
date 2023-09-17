package org.example.order;

import io.qameta.allure.Step;
import org.example.model.IngredientsResponse;
import org.example.utils.BaseClient;

import static io.restassured.RestAssured.given;

public class IngredientClient extends BaseClient {
    private static final String INGREDIENT_URL = "https://stellarburgers.nomoreparties.site/api/ingredients";

    @Step("Получение данных об ингедиентах")
    public IngredientsResponse getIngredientsData() {
        return  given()
                .spec(getBaseSpec())
                .get(INGREDIENT_URL)
                .as(IngredientsResponse.class);
    }

}
