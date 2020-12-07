package com.nsfdb.application.views.visualAnalysis;

import com.awesomecontrols.chartlib.Test;
import com.awesomecontrols.chartlib.c3wrapper.*;
import com.nsfdb.application.views.data.UserSession;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.nsfdb.application.views.main.MainView;
import com.vaadin.flow.server.VaadinService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Route(value = "visuals", layout = MainView.class)
@PageTitle("Visual Analysis")
@CssImport("./styles/views/about/about-view.css")
public class VisualAnalysis extends Div {

    private final static Logger LOGGER = Logger.getLogger(Test.class.getName());


    public VisualAnalysis() {
        setId("about-view");
        setSizeFull();

        UserSession uS = (UserSession) VaadinService.getCurrentRequest().getWrappedSession().getAttribute("userSession");
        HorizontalLayout hlC3 = new HorizontalLayout();
        hlC3.setSizeFull();

        ArrayList< ArrayList< Double> > data = uS.getLifeTable().getLifeTableData();
        ArrayList expectancy = new ArrayList();
        ArrayList age = new ArrayList();

        expectancy.add("Ex");
        age.add("Age");

        for (int i = 0; i < data.size(); i++) {
            age.add(data.get(i).get(0));
            expectancy.add(data.get(i).get(11));
        }

        Component table = addLifeTable(age, expectancy);

        hlC3.add(table);

        Label title = new Label("Life Expectancy Vs. Age");

        add(title, hlC3);

    }

    private Component addLifeTable( List age, List exp) {

        C3Chart chart = new C3Chart()
                .setOnInit(new IOnInit() {
                    @Override
                    public void onInit() {
                        LOGGER.log(Level.INFO, "OnInit!!!!!!");
                    }
                })
                .setData(new C3Data()
                        .setXKey("Age")
                        .setColumnsData(List.of(age, exp))
                        .setChartType(C3ChartType.LINE)
                        .setColors(Map.of("Age", "#ff0000",
                                "Ex", "#ffa500"))
                        .setHide(false)
                )
                .setAxis(new C3Axis()
                        .setXLabel("Age (years)", C3Axis.LabelPosition.OUTERRIGHT)
                        .setYLabel("Life Expectancy (years)", C3Axis.LabelPosition.OUTERMIDDLE)
                )
                .setHeight(400)
                .setWidth(500)
                .initialize();

        return chart;
    }

}
