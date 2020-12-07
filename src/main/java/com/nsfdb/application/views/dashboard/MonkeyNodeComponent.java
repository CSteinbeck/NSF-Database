package com.nsfdb.application.views.dashboard;

import com.nsfdb.application.analytics.FamilyTree.MonkeyNode;
import com.nsfdb.application.analytics.Monkey;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

@CssImport("./styles/views/dashboard/monkey-node.css")
public class MonkeyNodeComponent extends Div {

    private Label text;
    private Image image;
    private HorizontalLayout hL;

    public MonkeyNodeComponent(MonkeyNode monkeyNode) {
        setMonkeyNode(monkeyNode);
    }

    public void setMonkeyNode(MonkeyNode monkeyNode) {
        text = new Label(monkeyNode.getMonkey().toString());
        image = new Image("images/GenderIcons/" + monkeyNode.getIcon(), "Monkey Icon");
        image.setHeight("30");
        image.setWidth("20");
        hL = new HorizontalLayout();
        hL.setId("cell");
        hL.add(image, text);
        add(hL);
    }

}
