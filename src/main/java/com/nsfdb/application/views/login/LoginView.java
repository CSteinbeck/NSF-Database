package com.nsfdb.application.views.login;

import com.nsfdb.application.analytics.SqlServerDbAccessor;
import com.nsfdb.application.views.main.MainView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.login.AbstractLogin;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.router.Route;

import java.sql.ResultSet;
import java.sql.Statement;

//import java.sql.*;

//import analytics.SqlServerDbAccessor;
/**
 * The main view contains a button and a click listener.
 */
@Route(value = "login")
@PageTitle("Login")
@CssImport("./styles/views/dashboard/dashboard-view.css")
@RouteAlias(value = "", layout = MainView.class)
public class LoginView extends VerticalLayout {
    private String username;
    private String password;
    private String accessLevel;
    private LoginI18n li;
    //private SqlServerDbAccessor dba;

    public LoginView() {

        li = LoginI18n.createDefault();
        LoginForm component = new LoginForm();
        component.setI18n(li);
        component.addLoginListener(e -> {
            boolean isAuthenticated = authenticate(e);
            if (isAuthenticated == true) {
                navigateToMainPage();
            } else {
                component.setError(true);
            }
        });
        // The login button is disabled when clicked to prevent multiple submissions.
        // To restore it, call component.setEnabled(true)
        Button restoreLogin = new Button("Restore login button",
                event -> component.setEnabled(true));

        // Setting error to true also enables the login button.
        Button showError = new Button("Show error",
                event -> component.setError(true));
        add(component, restoreLogin, showError);
        add(component);
    }

    private void navigateToMainPage() {
        System.out.println(accessLevel);
        Button button = new Button("Click me",
                event -> Notification.show("Yay!"));
        add(button);
    }

    private boolean authenticate(AbstractLogin.LoginEvent e) {

        username = e.getUsername();
        password = e.getPassword();
        accessLevel = "0";
        System.out.println(username);
        System.out.println(password);

        if(loginCredentials(username, password, accessLevel) == true){
            return true;
        }
        else
            return false;
    }

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }

    public String getAccessLevel(){
        return accessLevel;
    }

    public static boolean loginCredentials(String username, String password, String accessLevel){
        SqlServerDbAccessor dba = new SqlServerDbAccessor("csdata.cd4sevot432y.us-east-1.rds.amazonaws.com",
                "administrator", "MercerBear$2020", "NSF_480");
        dba.setDbName("NSF_480");
        dba.connectToDb();
        String sql = ("SELECT * FROM Users WHERE UserName = '"+username+"' AND UserPassword = '"+password+"'");
        System.out.println(sql);
        try {
            Statement stmt = dba.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next() == false) {
                System.out.println("boo 1");
                return false;
            }
            else {
                accessLevel = rs.getString("AccessLevel");
                System.out.println("yay");
                return true;
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return false;
    }
}


