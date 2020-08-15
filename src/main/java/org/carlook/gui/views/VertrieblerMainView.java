package org.carlook.gui.views;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import org.carlook.gui.components.TopPanel;
import org.carlook.gui.windows.InseratPopUp;
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

public class VertrieblerMainView extends VerticalLayout implements View {

    User user;

    public void enter(ViewChangeListener.ViewChangeEvent event) {
        user = (User) VaadinSession.getCurrent().getAttribute(Roles.CURRENT);
        if(user == null || !user.getRole().equals(Roles.VERTRIEBLER)) UI.getCurrent().getNavigator().navigateTo(Konstanten.START);
        else this.setUp();
    }

    public void setUp() {
        Label spacer = new Label("&nbsp", ContentMode.HTML);


        InseratPopUp inserat = new InseratPopUp();
        Button addAuto = new Button("Neues Auto hinzufügen", VaadinIcons.PLUS);
        addAuto.addClickListener(e-> {
            UI.getCurrent().addWindow(inserat);
            inserat.setModal(true);
        });

        inserat.addCloseListener(e-> UI.getCurrent().getNavigator().navigateTo(Konstanten.VER_MAIN));

        HorizontalLayout buttonPan = new HorizontalLayout();
        buttonPan.addComponents(addAuto);

        List<Auto> autos = null;
        try {
            autos = AutoDAO.getInstance().getMyAutos(user.getVerId());
        } catch (SQLException throwables) {
            Logger.getLogger(ReservierteAutosView.class.getName()).log(Level.SEVERE, null, throwables);
        }
        Grid<Auto> autoGrid = GridBuild.basicGrid(autos);

        autoGrid.setCaption(" <span style='color:#EAECEC; font-size:25px; text-shadow: 1px 1px 1px black; font-family: Roboto, sans-serif;'> " + "Meine inserierten Autos: " + " </span>");


        VerticalLayout content = new VerticalLayout();
        content.addComponents(new TopPanel(), spacer, buttonPan, autoGrid);
        content.setComponentAlignment(buttonPan, Alignment.MIDDLE_RIGHT);
        this.addComponents(content);
        this.setComponentAlignment(content, Alignment.MIDDLE_CENTER);
    }
}
