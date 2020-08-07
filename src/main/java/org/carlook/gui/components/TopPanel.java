package org.carlook.gui.components;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;

import com.vaadin.ui.themes.ValoTheme;
import org.carlook.model.objects.entities.User;
import org.carlook.process.control.LoginControl;
import org.carlook.services.util.Konstanten;
import org.carlook.services.util.Roles;


public class TopPanel extends HorizontalLayout {

    public TopPanel() {
        this.setSizeFull();
        User user = (User) VaadinSession.getCurrent().getAttribute(Roles.CURRENT);

        //Name des Systems <i> für kursiv
        Label headLabel = new Label("Carlook", ContentMode.HTML);
        Button carlook = new Button("Carlook");
        carlook.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        carlook.addClickListener(e->{
            if(user == null) UI.getCurrent().getNavigator().navigateTo(Konstanten.START);
            else if(user.getRole().equals("kunde")) UI.getCurrent().getNavigator().navigateTo(Konstanten.SUCHE);
            else if(user.getRole().equals("vertriebler")) UI.getCurrent().getNavigator().navigateTo(Konstanten.VER_MAIN);
        });
        carlook.setWidth("45px");

        headLabel.setSizeUndefined();
        headLabel.addStyleName("header_carlook");

        this.addComponent(carlook);

        HorizontalLayout horLayout = new HorizontalLayout();



        if(user != null) {


            String vorname = null;
            if (user != null) {
                vorname = user.getVorname();
            }

            Label loggedLabel = new Label("Willkommen " + vorname + "!");
            loggedLabel.setSizeUndefined();

            horLayout.addComponent(loggedLabel);
            horLayout.setComponentAlignment(loggedLabel, Alignment.MIDDLE_CENTER);


            MenuBar bar = new MenuBar();
            MenuBar.MenuItem item1 = bar.addItem("Menü", null);

            //Kunde hat eigene View für Reservierungen:
            if(user.getRole().equals("kunde")) {
                item1.addItem("Reservierungen", VaadinIcons.LIST, new MenuBar.Command() {
                    public void menuSelected(MenuBar.MenuItem menuItem) {
                        UI.getCurrent().getNavigator().navigateTo(Konstanten.RSV_AUTOS);
                    }
                });
            }

            item1.addItem("Logout", VaadinIcons.SIGN_OUT, new MenuBar.Command() {
                public void menuSelected(MenuBar.MenuItem selectedItem) {
                    LoginControl.logoutUser();
                }
            });


            horLayout.addComponent(bar);
            this.addComponent(horLayout);
            this.setComponentAlignment(horLayout, Alignment.TOP_RIGHT);
        }
    }
}
