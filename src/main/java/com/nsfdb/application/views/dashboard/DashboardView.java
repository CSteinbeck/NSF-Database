package com.nsfdb.application.views.dashboard;

import com.nsfdb.application.views.data.UserSession;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
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
import com.vaadin.flow.component.html.Image;
import com.nsfdb.application.views.main.MainView;
import com.vaadin.flow.router.RouteAlias;

import java.util.*;

import com.nsfdb.application.analytics.FamilyTree.*;

import java.util.stream.Stream;
import java.util.stream.Collectors;
import com.nsfdb.application.analytics.Analytics;
import com.nsfdb.application.analytics.Monkey;
import com.vaadin.flow.server.VaadinService;

@Route(value = "dashboard", layout = MainView.class)
@PageTitle("Dashboard")
@CssImport("./styles/views/dashboard/dashboard-view.css")
public class DashboardView extends HorizontalLayout {

    private TextField name;
    private Button search;
    private ComboBox<String> monkeyIdSelector;
    private SplitLayout layout;
    private ImmediateFamilyAccordion famInfo;
    private UserSession session = (UserSession) VaadinService.getCurrentRequest().getWrappedSession().getAttribute("userSession");

    public DashboardView() {

        setId("dashboard-view");
        Label monkeyInfo = new Label();
        Label columns = new Label();
        Analytics data = session.getData();
        setSizeFull();

        monkeyIdSelector = new ComboBox<>();
        monkeyIdSelector.setItems(data.getMonkeySubjectIds());
        monkeyIdSelector.setLabel("Select Monkey ID");
        monkeyIdSelector.setTabIndex(0);

        HorizontalLayout searchComp = new HorizontalLayout();
        VerticalLayout leftSide = new VerticalLayout();
        VerticalLayout rightSide = new VerticalLayout();
        leftSide.setId("leftSideLayout");
        rightSide.setId("rightSideLayout");

        List<MonkeyNode> monkeyList = data.getMonkeys();
        TreeGrid<MonkeyNode> grid = new TreeGrid<>();

        grid.setItems(data.getRootMonkies(), data::fetchChildren);
        grid.addComponentHierarchyColumn(MonkeyNodeComponent::new).setHeader("Family Tree");
        grid.expandRecursively(monkeyList, 0);

        grid.setId("familyTree");
        grid.setHeightFull();

        grid.addItemClickListener( event -> {
            MonkeyNode mN = event.getItem();
            rightSide.removeAll();
            for (int i = 0; i < monkeyList.size(); i++) {
                MonkeyNode foundMonkey = monkeyList.get(i);
                if (foundMonkey.equals(mN)) {
                    monkeyIdSelector.setValue(foundMonkey.getMonkey().getSubjectId());
                    session.setCurrentMonkey(foundMonkey);
                    famInfo = new ImmediateFamilyAccordion(foundMonkey, monkeyList);
                    famInfo.setId("family-info");
                    Label title = new Label("Monkey ID: " + foundMonkey.getMonkey());
                    title.setId("selectedMonkey");
                    rightSide.add(title, famInfo, new BoneCharacteristicsView(), new BoneImagesView());
                    rightSide.setHorizontalComponentAlignment(Alignment.CENTER);
                }
            }
        });

        search = new Button("Search", click -> {
            rightSide.removeAll();
            Optional<MonkeyNode> foundMonkey = monkeyList.stream().filter(monkey -> monkey.getMonkey()
                    .getSubjectId() == monkeyIdSelector.getValue()).findAny();
            System.out.println(monkeyIdSelector.getValue());
            System.out.println(foundMonkey);
            grid.asSingleSelect().setValue(foundMonkey.orElse(null));
            grid.scrollToIndex(monkeyList.indexOf(foundMonkey.orElse(null)));
            session.setCurrentMonkey(foundMonkey.get());
            famInfo = new ImmediateFamilyAccordion(foundMonkey.orElse(null), monkeyList);
            famInfo.setId("family-info");
            Label title = new Label("Monkey ID: " + foundMonkey.orElse(null).getMonkey());
            title.setId("selectedMonkey");
            rightSide.add(title, famInfo, new BoneCharacteristicsView(), new BoneImagesView());
            rightSide.setHorizontalComponentAlignment(Alignment.CENTER);
        });

        searchComp.add(monkeyIdSelector, search);
        searchComp.setVerticalComponentAlignment(Alignment.END, search);

        leftSide.add(searchComp, grid);
        add(leftSide, rightSide);

        MonkeyNode currentMonkey = session.getCurrentMonkey();
        grid.select(currentMonkey);
        monkeyIdSelector.setValue(currentMonkey.getMonkey().getSubjectId());
        famInfo = new ImmediateFamilyAccordion(currentMonkey, monkeyList);
        famInfo.setId("family-info");
        Label title = new Label("Monkey ID: " + currentMonkey.getMonkey());
        title.setId("selectedMonkey");
        rightSide.add(title, famInfo, new BoneCharacteristicsView(), new BoneImagesView());
        rightSide.setHorizontalComponentAlignment(Alignment.CENTER);
    }
}
