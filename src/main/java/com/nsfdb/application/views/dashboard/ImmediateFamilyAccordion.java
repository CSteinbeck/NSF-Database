package com.nsfdb.application.views.dashboard;

import com.nsfdb.application.analytics.FamilyTree.MonkeyNode;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;

import java.util.ArrayList;
import java.util.List;

public class ImmediateFamilyAccordion extends Accordion {

    public ImmediateFamilyAccordion(MonkeyNode monkey, List<MonkeyNode> monkeyList) {
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

            if (mL.size() != 0) {
                add("Mother", motherGrid);
            } else {
                add("Mother", new Label("None"));
            }
            if (sL.size() != 0) {
                add("Siblings", siblingGrid);
            } else {
                add("Siblings", new Label("None"));
            }
            if (cL.size() != 0) {
                add("Children", childrenGrid);
            } else {
                add("Children", new Label("None"));
            }
    }
}
