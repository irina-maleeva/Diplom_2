package org.example.user;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.example.model.User;
import org.junit.Test;

import static org.example.user.UserGenerator.*;
import static org.junit.Assert.assertEquals;

public class UserRegisterTest {

    UserClient userClient = new UserClient();

    @Test
    @DisplayName("Можно создать уникального пользователя. В ответе 'success': true. Созданный пользователь может пройти авторизацию")
    public void testRegisterRandomUser() {
        User user = randomUser();
        Response response = userClient.register(user);
        assertEquals(true, response.body().path("success"));
        Response loginResponse = userClient.login(user);
        assertEquals(true, loginResponse.body().path("success"));
    }

    @Test
    @DisplayName("Нельзя создать пользователя, который уже зарегистрировался. Код ответа 403")
    public void testCannotRegisterExistingUser() {
        User user = randomUser();
        Response response1 = userClient.register(user);
        Response response2 = userClient.register(user);
        assertEquals(false, response2.body().path("success"));
        assertEquals("User already exists", response2.body().path("message"));
        assertEquals(403, response2.statusCode());
    }

    @Test
    @DisplayName("Нельзя создать пользователя без email. Код ответа 403")
    public void testCannotRegisterUserWithoutEmail() {
        User userWithoutEmail = userWithoutEmail();
        Response response = userClient.register(userWithoutEmail);
        assertEquals(false, response.body().path("success"));
        assertEquals("Email, password and name are required fields"
                , response.body().path("message"));
        assertEquals(403, response.statusCode());
    }

    @Test
    @DisplayName("Нельзя создать пользователя без пароля. Код ответа 403")
    public void testCannotRegisterUserWithoutPassword() {
        User userWithoutPassword = userWithoutPassword();
        Response response = userClient.register(userWithoutPassword);
        assertEquals(false, response.body().path("success"));
        assertEquals("Email, password and name are required fields"
                , response.body().path("message"));
        assertEquals(403, response.statusCode());
    }

    @Test
    @DisplayName("Нельзя создать пользователя без имени. Код ответа 403")
    public void testCannotRegisterUserWithoutName() {
        User userWithoutName = userWithoutName();
        Response response = userClient.register(userWithoutName);
        assertEquals(false, response.body().path("success"));
        assertEquals("Email, password and name are required fields"
                , response.body().path("message"));
        assertEquals(403, response.statusCode());
    }
}