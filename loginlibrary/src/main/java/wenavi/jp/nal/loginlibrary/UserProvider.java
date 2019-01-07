package wenavi.jp.nal.loginlibrary;

import java.util.ArrayList;
import java.util.List;

import wenavi.jp.nal.loginlibrary.model.User;

/**
 * Copyright © Nals
 * Created by TrangLT on 1/7/19.
 */

public class UserProvider {
    public static List<User> getListUser(String rssFeed) {
        List<User> users = new ArrayList<>();
        // random number of item but at least 5
        for (int i = 0; i < 10; i++) {
            // create sample data
            User item = new User("Oliver ", "19/10/1994",
                    "tranglt@gmail.com", "123123123");
            users.add(item);
        }
        return users;
    }
}
