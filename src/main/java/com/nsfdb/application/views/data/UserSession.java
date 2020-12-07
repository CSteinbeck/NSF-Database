package com.nsfdb.application.views.data;

import com.nsfdb.application.analytics.Analytics;
import com.nsfdb.application.analytics.FamilyTree.MonkeyNode;
import com.nsfdb.application.analytics.LifeTable;

public class UserSession {

    private User user;
    private Analytics data;
    private LifeTable lifeTable;
    private MonkeyNode currentMonkey;

    public UserSession(User u, Analytics a, LifeTable lT) {
        this.user = u;
        this.data = a;
        this.lifeTable = lT;
        this.currentMonkey = this.data.getMonkeys().get(0);
    }

    public User getUser() {
        return user;
    }

    public Analytics getData() {
        return data;
    }

    public LifeTable getLifeTable() {
        return lifeTable;
    }

    public MonkeyNode getCurrentMonkey() {
        return currentMonkey;
    }

    public void setCurrentMonkey(MonkeyNode currentMonkey) {
        this.currentMonkey = currentMonkey;
    }
}
