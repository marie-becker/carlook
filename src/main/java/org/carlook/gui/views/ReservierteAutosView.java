package org.carlook.gui.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.carlook.gui.components.TopPanel;
import org.carlook.model.objects.Auto;
import org.carlook.model.objects.dao.AutoDAO;
import org.carlook.model.objects.entities.User;
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
        this.addComponents(new TopPanel(), spacer, spacer);

        Grid<Auto> autoGrid = new Grid<>();
        autoGrid.setSizeUndefined();
        List<Auto> autos = null;
        try {
            autos = AutoDAO.getInstance().getMyRsvAutos(user.getKundeId());
        } catch (SQLException throwables) {
            Logger.getLogger(ReservierteAutosView.class.getName()).log(Level.SEVERE, null, throwables);
        }

        autoGrid.removeAllColumns();
        autoGrid.setCaption("Meine reservierten Autos:"); //TODO Schrift größer
        autoGrid.setItems(autos);
        autoGrid.setHeightByRows(!autos.isEmpty() ? autos.size() : 1);


        autoGrid.addColumn(Auto::getMarke).setCaption("Automarke").setWidth(230);
        autoGrid.addColumn(Auto::getBaujahr).setCaption("Baujahr").setWidth(90);
        autoGrid.addColumn(Auto::getBeschreibung).setCaption("Beschreibung");

        autoGrid.setWidth("100%");

        this.addComponent(autoGrid);
    }
}
