package com.nsfdb.application.views.dashboard;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class BoneImagesView extends VerticalLayout {

    public BoneImagesView() {
        Label title = new Label("Bone Images");
        Label images = new Label("This is where bone images will be");
        add(title, images);
    }
}
