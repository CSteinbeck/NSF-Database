package com.nsfdb.application.views.main;

import java.awt.color.ProfileDataException;
import java.util.Optional;

import com.nsfdb.application.views.data.UserSession;
import com.nsfdb.application.views.profile.ProfileView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.PWA;
import com.nsfdb.application.views.dashboard.DashboardView;
import com.nsfdb.application.views.visualAnalysis.VisualAnalysis;
import com.vaadin.flow.server.VaadinService;
import org.vaadin.erik.SlideMode;
import org.vaadin.erik.SlideTab;
import org.vaadin.erik.SlideTabBuilder;
import org.vaadin.erik.SlideTabPosition;

/**
 * The main view is a top-level placeholder for other views.
 */
@JsModule("./styles/shared-styles.js")
@CssImport("./styles/views/main/main-view.css")
@PWA(name = "NSF Monkey DB", shortName = "NSF Monkey DB", enableInstallPrompt = false)
public class MainView extends AppLayout {

    private final Tabs menu;
    private H1 viewTitle;
    private UserSession session = (UserSession) VaadinService.getCurrentRequest().getWrappedSession().getAttribute("userSession");

    public MainView() {
        setPrimarySection(Section.NAVBAR);
        addToNavbar(true, createHeaderContent());
        menu = createMenu();
        addToDrawer(createDrawerContent(menu));
    }

    private Component createHeaderContent() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setId("header");
        layout.getThemeList().set("dark", true);
        layout.setWidthFull();
        layout.setSpacing(false);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.add(new DrawerToggle());
        viewTitle = new H1();
        layout.add(viewTitle);
        SlideTabBuilder sb = new SlideTabBuilder(createProfileView())
                .mode(SlideMode.RIGHT)
                .tabPosition(SlideTabPosition.BEGINNING);
        SlideTab profileView = new ProfileView(sb);
        profileView.setId("profile-tab");
        profileView.setExpandComponent(createProfileLogo());
        profileView.setCollapseComponent(createProfileLogo());
        layout.add(createProfileLogo());
        layout.add(profileView);
        return layout;
    }

    private Component createDrawerContent(Tabs menu) {
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);
        layout.getThemeList().set("spacing-s", true);
        layout.setAlignItems(FlexComponent.Alignment.STRETCH);
        HorizontalLayout logoLayout = new HorizontalLayout();
        logoLayout.setId("logo");
        logoLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        logoLayout.add(new Image("images/logo.png", "NSF Monkey DB logo"));
        logoLayout.add(new H1("NSF Monkey DB"));
        layout.add(logoLayout, menu);
        return layout;
    }

    private Tabs createMenu() {
        final Tabs tabs = new Tabs();
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        tabs.addThemeVariants(TabsVariant.LUMO_MINIMAL);
        tabs.setId("tabs");
        tabs.add(createMenuItems());
        return tabs;
    }

    private HorizontalLayout createProfileView() {
        HorizontalLayout name = new HorizontalLayout();
        name.add(new Label(session.getUser().getUserName()), new Label(session.getUser().getUserRole()));
        name.setId("name-layout");
        name.add(new Button("Logout"));
        return name;
    }

    private Component[] createMenuItems() {
        return new Tab[] {
            createTab("Dashboard", DashboardView.class),
            createTab("Visual Analysis", VisualAnalysis.class)
        };
    }

    private static Tab createTab(String text, Class<? extends Component> navigationTarget) {
        final Tab tab = new Tab();
        tab.add(new RouterLink(text, navigationTarget));
        ComponentUtil.setData(tab, Class.class, navigationTarget);
        return tab;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        getTabForComponent(getContent()).ifPresent(menu::setSelectedTab);
        viewTitle.setText(getCurrentPageTitle());
    }

    private Optional<Tab> getTabForComponent(Component component) {
        return menu.getChildren()
                .filter(tab -> ComponentUtil.getData(tab, Class.class)
                        .equals(component.getClass()))
                .findFirst().map(Tab.class::cast);
    }

    private Image createProfileLogo() {
        Image im = new Image("images/user.svg", "Avatar");
        return im;
    }

    private String getCurrentPageTitle() {
        return getContent().getClass().getAnnotation(PageTitle.class).value();
    }
}
