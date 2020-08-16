package org.carlook.gui.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import org.carlook.gui.components.TopPanel;
import org.carlook.model.objects.entities.Auto;
import org.carlook.model.objects.dao.AutoDAO;
import org.carlook.model.objects.entities.User;
import org.carlook.services.util.GridBuild;
import org.carlook.services.util.Konstanten;
import org.carlook.services.util.Roles;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;



public class ReservierteAutosView extends VerticalLayout implements View {

    User user;

    public void enter(ViewChangeListener.ViewChangeEvent event) {
        user = (User) VaadinSession.getCurrent().getAttribute(Roles.CURRENT);
        if(user == null || !user.getRole().equals(Roles.KUNDE)) UI.getCurrent().getNavigator().navigateTo(Konstanten.START);
        else this.setUp();
    }

    public void setUp() {
        Label spacer = new Label("&nbsp", ContentMode.HTML);
        TopPanel toppanel = new TopPanel();

        List<Auto> autos = null;
        try {
            autos = AutoDAO.getInstance().getMyRsvAutos(user.getKundeId());
        } catch (SQLException throwables) {
            Logger.getLogger(ReservierteAutosView.class.getName()).log(Level.SEVERE, null, throwables);
        }
        Grid<Auto> autoGrid = GridBuild.basicGrid(autos);

        autoGrid.setCaption(" <span style='color:#EAECEC; font-size:25px; text-shadow: 1px 1px 1px black; font-family: Roboto, sans-serif;'> " + "Meine reservierten Autos: " + " </span>");

        VerticalLayout content = new VerticalLayout();
        content.addComponents(toppanel, spacer, autoGrid);

        this.addComponent(content);
        this.setComponentAlignment(content, Alignment.MIDDLE_CENTER);
    }
}


