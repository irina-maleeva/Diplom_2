package org.example.user;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.example.model.User;
import org.example.utils.BaseClient;
import static io.restassured.RestAssured.given;

public class UserClient extends BaseClient {
    private static final String REGISTER_URL = "https://stellarburgers.nomoreparties.site/api/auth/register";
    private static final String LOGIN_URL = "https://stellarburgers.nomoreparties.site/api/auth/login";

    @Step("Регистрация пользователя")
    public Response register(User user){
        return given()
                .spec(getBaseSpec())
                .and()
                .body(user)
                .when()
                .post(REGISTER_URL);
    }

    @Step("Логин пользователя")
    public Response login(User user){
        return given()
                .spec(getBaseSpec())
                .and()
                .body(user)
                .when()
                .post(LOGIN_URL);

    }
}
