package com.nsfdb.application.views.dashboard;

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

@Route(value = "dashboard", layout = MainView.class)
@PageTitle("Dashboard")
@CssImport("./styles/views/dashboard/dashboard-view.css")
public class DashboardView extends HorizontalLayout {

    private TextField name;
    private Button search;
    private ComboBox<String> monkeyIdSelector;
    private SplitLayout layout;
    private Accordion famInfo;

    public DashboardView() {

        setId("dashboard-view");
        Label monkeyInfo = new Label();
        Label columns = new Label();
        Analytics data = new Analytics("CayoSantiagoRhesusDB");
        setSizeFull();

        monkeyIdSelector = new ComboBox<>();
        monkeyIdSelector.setItems(data.getMonkeySubjectIds());
        monkeyIdSelector.setLabel("Select Monkey ID");

        HorizontalLayout searchComp = new HorizontalLayout();
        VerticalLayout leftSide = new VerticalLayout();
        VerticalLayout rightSide = new VerticalLayout();
        leftSide.setId("leftSideLayout");
        rightSide.setId("rightSideLayout");

        List<MonkeyNode> monkeyList = data.getMonkeys();
        TreeGrid<MonkeyNode> grid = new TreeGrid<>();

        grid.setItems(data.getRootMonkies(), data::getChildMonkies);
        grid.addComponentHierarchyColumn(MonkeyNodeComponent::new).setHeader("Family Tree");
        grid.expandRecursively(monkeyList, 0);

        grid.setId("familyTree");
        grid.setHeightFull();

        grid.addItemClickListener( event -> {
            MonkeyNode mN = event.getItem();
            rightSide.removeAll();
            Optional<MonkeyNode> foundMonkey = monkeyList.stream().filter(monkey -> monkey.getMonkey()
                    .getSubjectId() == mN.getMonkey().getSubjectId()).findAny();
            monkeyIdSelector.setValue(foundMonkey.orElse(null).getMonkey().getSubjectId());
            famInfo = createFamilyView(foundMonkey.orElse(null), monkeyList);
            famInfo.setId("family-info");
            Label title = new Label("Monkey ID: " + foundMonkey.orElse(null).getMonkey());
            title.setId("selectedMonkey");
            rightSide.add(title, famInfo);
            rightSide.setHorizontalComponentAlignment(Alignment.CENTER);
        });

        search = new Button("Search", click -> {
            rightSide.removeAll();
            Optional<MonkeyNode> foundMonkey = monkeyList.stream().filter(monkey -> monkey.getMonkey()
                    .getSubjectId() == monkeyIdSelector.getValue()).findAny();
            grid.asSingleSelect().setValue(foundMonkey.orElse(null));
            grid.scrollToIndex(monkeyList.indexOf(foundMonkey.orElse(null)));
            famInfo = createFamilyView(foundMonkey.orElse(null), monkeyList);
            famInfo.setId("family-info");
            Label title = new Label("Monkey ID: " + foundMonkey.orElse(null).getMonkey());
            title.setId("selectedMonkey");
            rightSide.add(title, famInfo);
            rightSide.setHorizontalComponentAlignment(Alignment.CENTER);
        });

        searchComp.add(monkeyIdSelector, search);
        searchComp.setVerticalComponentAlignment(Alignment.END, search);

        leftSide.add(searchComp, grid);
        add(leftSide, rightSide);
    }

    public Accordion createFamilyView(MonkeyNode monkey, List<MonkeyNode> monkeyList) {
        List<MonkeyNode> mL = new ArrayList<>();
        List<MonkeyNode> sL = new ArrayList<>();
        List<MonkeyNode> cL = new ArrayList<>();
        for (int i = 0; i < monkeyList.size(); i++) {
            if (monkeyList.get(i).getMonkey().getSubjectId().equals(monkey.getMonkey().getMotherId())) {
                mL.add(monkeyList.get(i));
            } else if (monkeyList.get(i).getMonkey().getMotherId() != null) {
                if (monkeyList.get(i).getMonkey().getMotherId().equals(monkey.getMonkey().getMotherId()) &&
                        !monkeyList.get(i).getMonkey().getSubjectId().equals(monkey.getMonkey().getSubjectId())) {
                    sL.add(monkeyList.get(i));
                } else if (monkeyList.get(i).getMonkey().getMotherId().equals(monkey.getMonkey().getSubjectId())) {
                    cL.add(monkeyList.get(i));
                }
            }
        }

        Grid<MonkeyNode> motherGrid = new Grid<>();
        motherGrid.setItems(mL);
        Grid.Column<MonkeyNode> motherIDColumn = motherGrid.addColumn(MonkeyNode::getMonkey).setHeader("ID");
        motherGrid.setHeightByRows(true);

        Grid<MonkeyNode> siblingGrid = new Grid<>();
        siblingGrid.setItems(sL);
        Grid.Column<MonkeyNode> siblingIDColumn = siblingGrid.addColumn(MonkeyNode::getMonkey).setHeader("ID");
        siblingGrid.setHeightByRows(true);

        Grid<MonkeyNode> childrenGrid = new Grid<>();
        childrenGrid.setItems(cL);
        Grid.Column<MonkeyNode> childrenIDColumn = childrenGrid.addColumn(MonkeyNode::getMonkey).setHeader("ID");
        childrenGrid.setHeightByRows(true);

        Accordion famView = new Accordion();
        if (mL.size() != 0) {
            famView.add("Mother", motherGrid);
        } else {
            famView.add("Mother", new Label("None"));
        }
        if (sL.size() != 0) {
            famView.add("Siblings", siblingGrid);
        } else {
            famView.add("Siblings", new Label("None"));
        }
        if (cL.size() != 0) {
            famView.add("Children", childrenGrid);
        } else {
            famView.add("Children", new Label("None"));
        }
        return famView;
    }

}
