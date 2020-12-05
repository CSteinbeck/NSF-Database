package com.nsfdb.application.views.dashboard;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class BoneCharacteristicsView extends VerticalLayout {

    public BoneCharacteristicsView() {
        Label title = new Label("Bone Characteristics, Morphology and Pathology");
        Label info = new Label("Bone information goes here");
        add(title, info);
    }

}
