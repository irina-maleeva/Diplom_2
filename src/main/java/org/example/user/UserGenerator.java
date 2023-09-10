package org.example.user;

import org.example.model.User;

import static org.example.utils.RandomString.randomString;

public class UserGenerator {
    public static User randomUser() {
        return new User()
                .withEmail(randomString(5) + "@yandex.ru")
                .withPassword(randomString(7))
                .withName(randomString(10));
    }

    public static User userWithoutEmail(){
        return new User()
                .withPassword(randomString(6))
                .withName(randomString(10));
    }

    public static User userWithoutPassword(){
        return new User()
                .withEmail(randomString(5) + "@yandex.ru")
                .withName(randomString(10));
    }

    public static User userWithoutName(){
        return new User()
                .withEmail(randomString(5) + "@yandex.ru")
                .withPassword(randomString(6));
    }

    public static User userWithWrongEmail(User user){
        return new User()
                .withEmail(randomString(5) + "@yandex.ru")
                .withPassword(user.getPassword())
                .withName(user.getName());
    }

    public static User userWithWrongPassword(User user){
        return new User()
                .withEmail(user.getEmail())
                .withPassword(randomString(6))
                .withName(user.getName());
    }
}
