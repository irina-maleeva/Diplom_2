package org.example.user;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.example.model.User;
import org.example.model.UserData;
import org.junit.Before;
import org.junit.Test;

import static org.example.user.UserGenerator.randomUser;
import static org.example.utils.RandomString.randomString;
import static org.junit.Assert.assertEquals;

public class UserChangeDataTest {

    UserClient userClient = new UserClient();
    String accessToken;
    String initialEmail;
    String initialName;
    String newEmail = randomString(5) + "@gmail.com";
    String newName = randomString(10);

    @Before
    public void setup(){
        User user = randomUser();
        Response registerResponse = userClient.register(user);
        accessToken = registerResponse.body().path("accessToken").toString().substring(7);
        initialEmail = user.getEmail();
        initialName = user.getName();
    }
    @Test
    @DisplayName("Изменить email авторизованного пользователя")
    public void testChangeEmailAuthorizedUser() {
        UserData userDataNewEmail = new UserData(newEmail, initialName);
        Response response = userClient.changeData(accessToken, userDataNewEmail);
        assertEquals(200, response.statusCode());
        assertEquals(true, response.body().path("success"));
        assertEquals(newEmail, response.body().path("user.email"));
        assertEquals(initialName, response.body().path("user.name"));
    }

    @Test
    @DisplayName("Изменить имя авторизованного пользователя")
    public void testChangeNameAuthorizedUser() {
        UserData userDataNewName = new UserData(initialEmail, newName);
        Response response = userClient.changeData(accessToken, userDataNewName);
        assertEquals(200, response.statusCode());
        assertEquals(true, response.body().path("success"));
        assertEquals(initialEmail, response.body().path("user.email"));
        assertEquals(newName, response.body().path("user.name"));
    }
}