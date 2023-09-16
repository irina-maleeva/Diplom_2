package org.example.user;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.example.model.User;
import org.example.model.UserData;
import org.example.utils.BaseClient;
import static io.restassured.RestAssured.given;

public class UserClient extends BaseClient {
    private static final String REGISTER_URL = "https://stellarburgers.nomoreparties.site/api/auth/register";
    private static final String LOGIN_URL = "https://stellarburgers.nomoreparties.site/api/auth/login";
    private static final String AUTHORIZATION_URL = "https://stellarburgers.nomoreparties.site/api/auth/user";

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

    @Step("Получение данных о пользователе")
    public Response getUserData(String authorization) {
        return given()
                .spec(getBaseSpec())
                .and()
                .body(authorization)
                .when()
                .get(AUTHORIZATION_URL);
    }

    @Step("Изменение данных пользователя")
    public Response changeData(String accessToken, UserData userData) {
        return given()
                .header("authorization", "bearer "+ accessToken)
                .spec(getBaseSpec())
                .and()
                .body(userData)
                .when()
                .patch(AUTHORIZATION_URL);
    }
}
