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
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.data.provider.hierarchy.AbstractBackEndHierarchicalDataProvider;
import com.vaadin.flow.data.provider.hierarchy.HierarchicalDataProvider;
import com.vaadin.flow.data.provider.hierarchy.HierarchicalQuery;
import com.vaadin.flow.router.Route;
import com.nsfdb.application.views.main.MainView;
import com.vaadin.flow.router.RouteAlias;
import java.util.HashMap;
import com.nsfdb.application.analytics.FamilyTree.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Collections;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import com.nsfdb.application.analytics.Analytics;
import com.nsfdb.application.analytics.Monkey;

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
        monkeyIdSelector = new ComboBox<>();
        monkeyIdSelector.setItems("415", "416", "417", "418", "419", "420", "421");
        monkeyIdSelector.setLabel("Select Monkey ID");
        Label monkeyInfo = new Label();
        Label columns = new Label();
        famInfo = new Accordion();

        HorizontalLayout searchComp = new HorizontalLayout();
        VerticalLayout infoPanel = new VerticalLayout();

        search = new Button("Search", click -> {
            monkeyInfo.setText(monkeyIdSelector.getValue());
        });

        infoPanel.add(columns, monkeyInfo);

        searchComp.setVerticalComponentAlignment(Alignment.END, search);
        searchComp.add(monkeyIdSelector, search);

        Analytics data = new Analytics("CayoSantiagoRhesusDB");

        TreeGrid<Monkey> grid = new TreeGrid<>();

        grid.setItems(data.getRootMonkey(), data.getChildMonkies("1"));
        grid.addHierarchyColumn(Monkey::getSubjectId).setHeader("Subject ID");
        grid.addColumn(Monkey::getBirthYear).setHeader("Birth Year");

        //layout = new SplitLayout(searchComp);
        add(searchComp, infoPanel, grid);
    }

}
