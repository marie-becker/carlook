package org.carlook.gui.components;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.*;
import org.carlook.model.objects.entities.User;
import org.carlook.process.control.LoginControl;
import org.carlook.services.util.Konstanten;
import org.carlook.services.util.Roles;


public class TopPanel extends HorizontalLayout {

    public TopPanel() {
        this.setSizeFull();
        User user = (User) VaadinSession.getCurrent().getAttribute(Roles.CURRENT);

        Button carlook = new Button("Carlook");
        carlook.addStyleName("anchor-button");
        carlook.addClickListener(e->{
            if(user == null) UI.getCurrent().getNavigator().navigateTo(Konstanten.START);
            else if(user.getRole().equals(Roles.KUNDE)) UI.getCurrent().getNavigator().navigateTo(Konstanten.SUCHE);
            else if(user.getRole().equals(Roles.VERTRIEBLER)) UI.getCurrent().getNavigator().navigateTo(Konstanten.VER_MAIN);
        });

        this.addComponent(carlook);
        this.setComponentAlignment(carlook, Alignment.TOP_LEFT);

        HorizontalLayout horLayout = new HorizontalLayout();

        if(user != null) {
            String vorname = user.getVorname();

            Label loggedLabel = new Label("Willkommen, " + vorname + "!");
            loggedLabel.setSizeUndefined();

            horLayout.addComponent(loggedLabel);
            horLayout.setComponentAlignment(loggedLabel, Alignment.MIDDLE_CENTER);

            MenuBar bar = new MenuBar();
            MenuBar.MenuItem item1 = bar.addItem("Menü", null);

            //Kunde hat Optionen für Views, Vertriebler nur Logout
            if(user.getRole().equals(Roles.KUNDE)) {
                item1.addItem("Reservierungen", VaadinIcons.LIST, menuItem -> UI.getCurrent().getNavigator().navigateTo(Konstanten.RSV_AUTOS));

                item1.addItem("Autosuche", VaadinIcons.SEARCH, menuItem -> UI.getCurrent().getNavigator().navigateTo(Konstanten.SUCHE));
            }

            item1.addItem("Logout", VaadinIcons.SIGN_OUT, selectedItem -> LoginControl.logoutUser());

            horLayout.addComponent(bar);
            this.addComponent(horLayout);
            this.setComponentAlignment(horLayout, Alignment.TOP_RIGHT);
        }
    }
}
