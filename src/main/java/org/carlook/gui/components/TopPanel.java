package org.carlook.gui.components;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;

import org.carlook.model.objects.entities.User;
import org.carlook.process.control.LoginControl;
import org.carlook.services.util.Konstanten;
import org.carlook.services.util.Roles;


public class TopPanel extends HorizontalLayout {

    public TopPanel() {
        this.setSizeFull();

        //Name des Systems <i> für kursiv
        Label headLabel = new Label("Carlook", ContentMode.HTML);
        headLabel.setSizeUndefined();
        headLabel.addStyleName("header_carlook");

        this.addComponent(headLabel);

        HorizontalLayout horLayout = new HorizontalLayout();

        User user = (User) VaadinSession.getCurrent().getAttribute(Roles.CURRENT);

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

            //Logout des Users
            item1.addItem("Reservierungen", VaadinIcons.LIST, new MenuBar.Command() {
                public void menuSelected(MenuBar.MenuItem menuItem) {
                    UI.getCurrent().getNavigator().navigateTo(Konstanten.RSV_AUTOS);
                }
            });

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
