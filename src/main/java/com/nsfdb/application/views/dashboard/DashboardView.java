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
import java.util.HashMap;

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
        monkeyIdSelector.setItems("415", "416", "417", "418", "419", "420", "421");
        monkeyIdSelector.setLabel("Select Monkey ID");
        Label monkeyInfo = new Label();
        Label columns = new Label();
        famInfo = new Accordion();

        HorizontalLayout searchComp = new HorizontalLayout();
        VerticalLayout infoPanel = new VerticalLayout();

        search = new Button("Search", click -> {
            HashMap<String,String> data = test.getMonkeyInfo(monkeyIdSelector.getValue());
            String columnText = "";
            String text = "";
            for (String s : data.keySet()) {
                columnText += s + ", ";
                text += data.get(s) + ", ";
            }
            monkeyInfo.setText(text);
            columns.setText(columnText);
        });

        infoPanel.add(columns, monkeyInfo);

        searchComp.setVerticalComponentAlignment(Alignment.END, search);
        searchComp.add(monkeyIdSelector, search);

        //layout = new SplitLayout(searchComp);
        add(searchComp, infoPanel);
    }

}
