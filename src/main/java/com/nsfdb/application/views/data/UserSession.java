package com.nsfdb.application.views.data;

import com.nsfdb.application.analytics.Analytics;

public class UserSession {

    private User user;
    private Analytics data;

    public UserSession(User u, Analytics a) {
        this.user = u;
        this.data = a;
    }

    public User getUser() {
        return user;
    }

    public Analytics getData() {
        return data;
    }
}
