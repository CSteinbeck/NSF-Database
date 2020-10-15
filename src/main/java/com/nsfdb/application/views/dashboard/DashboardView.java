package com.nsfdb.application.views.dashboard;

import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.nsfdb.application.views.main.MainView;
import com.vaadin.flow.router.RouteAlias;
import java.util.ArrayList;

@Route(value = "dashboard", layout = MainView.class)
@PageTitle("Dashboard")
@CssImport("./styles/views/dashboard/dashboard-view.css")
@RouteAlias(value = "", layout = MainView.class)
public class DashboardView extends HorizontalLayout {

    private TextField name;
    private Button search;
    private ComboBox<String> monkeyIdSelector;
    private SplitLayout layout;
    private Accordion famInfo;

    public DashboardView() {

        setId("hello-world-view");
        Analytics test = new Analytics();
        monkeyIdSelector = new ComboBox<>();
        monkeyIdSelector.setItems("420", "123", "103", "001", "939", "478", "275");
        monkeyIdSelector.setLabel("Select Monkey ID");
        Label monkeyInfo = new Label();
        famInfo = new Accordion();

        HorizontalLayout searchComp = new HorizontalLayout();

        search = new Button("Search", click -> {
            ArrayList<String> data = test.getMonkeyInfo(monkeyIdSelector.getValue());
            monkeyInfo.setText(data.get(0));
        });

        searchComp.setVerticalComponentAlignment(Alignment.END, famInfo);
        searchComp.add(monkeyIdSelector, famInfo);

        //layout = new SplitLayout(searchComp);
        add(searchComp);
    }

}
