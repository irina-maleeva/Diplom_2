package org.example.user;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.example.model.User;
import org.junit.After;
import org.junit.Test;

import static org.example.user.UserGenerator.*;
import static org.junit.Assert.*;

public class UserLoginTest {
    UserClient userClient = new UserClient();
    String accessToken;

    @Test
    @DisplayName("Существующий пользователь проходит авторизацию")
    public void testLoginExistingUser() {
        User user = randomUser();
        Response response = userClient.register(user);
        Response loginResponse = userClient.login(user);
        accessToken = loginResponse.body().path("accessToken").toString().substring(7);
        assertEquals(true, loginResponse.body().path("success"));
    }

    @Test
    @DisplayName("Если email неверный, вернется код ответа 401")
    public void testLoginUserWrongEmail() {
        User correctUser = randomUser();
        Response response = userClient.register(correctUser);
        User wrongEmailUser = userWithWrongEmail(correctUser);
        Response loginResponse = userClient.login(wrongEmailUser);
        assertEquals(false, loginResponse.body().path("success"));
        assertEquals(401, loginResponse.statusCode());
        assertEquals("email or password are incorrect", loginResponse.body().path("message"));
    }

    @Test
    @DisplayName("Если пароль неверный, вернется код ответа 401")
    public void testLoginUserWrongPassword() {
        User correctUser = randomUser();
        Response response = userClient.register(correctUser);
        User wrongPasswordUser = userWithWrongPassword(correctUser);
        Response loginResponse = userClient.login(wrongPasswordUser);
        assertEquals(false, loginResponse.body().path("success"));
        assertEquals(401, loginResponse.statusCode());
        assertEquals("email or password are incorrect", loginResponse.body().path("message"));
    }
    @After
    public void cleanUp() {
        userClient.deleteUser(accessToken);
    }
}