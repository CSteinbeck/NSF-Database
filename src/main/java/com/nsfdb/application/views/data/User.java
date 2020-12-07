package com.nsfdb.application.views.data;

import com.nsfdb.application.analytics.Analytics;

public class User {

    private String firstName;
    private String lastName;
    private String userName;
    private String accessLevel;
    private String userRole;

    public User(String uN, String aL) {
        this.userName = uN;
        this.accessLevel = aL;
        if (aL == "4") {
            this.userRole = "Editor";
        } else if (aL == "5") {
            this.userRole = "Administrator";
        } else {
            this.userRole = "Guest";
        }
    }
    public User(String fN, String lN, String uN, String aL, String uR) {
        this.firstName = fN;
        this.lastName = lN;
        this.userName = uN;
        this.accessLevel = aL;
        this.userRole = uR;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUserName() {
        return userName;
    }

    public String getAccessLevel() {
        return accessLevel;
    }

    public String getUserRole() {
        return userRole;
    }
}
