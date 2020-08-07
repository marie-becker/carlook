package org.carlook.gui.views;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import org.carlook.gui.components.TopPanel;
import org.carlook.gui.windows.InseratPopUp;
import org.carlook.model.objects.Auto;
import org.carlook.model.objects.dao.AutoDAO;
import org.carlook.model.objects.entities.User;
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
        Label pagetitle = new Label("Meine eingestellten Autos");
        pagetitle.addStyleName("pageTitle");
        this.addComponents(new TopPanel(), spacer, pagetitle);

        InseratPopUp inserat = new InseratPopUp();
        Button addAuto = new Button(VaadinIcons.PLUS);
        addAuto.addStyleName("edit_button");
        addAuto.addClickListener(e-> {
            UI.getCurrent().addWindow(inserat);
            inserat.setModal(true);
        });

        inserat.addCloseListener(e-> UI.getCurrent().getPage().reload());

        VerticalLayout buttonPan = new VerticalLayout();
        buttonPan.addComponents(addAuto);
        buttonPan.setComponentAlignment(addAuto, Alignment.TOP_RIGHT);


        Grid<Auto> autoGrid = new Grid<>();
        autoGrid.setSizeUndefined();
        List<Auto> autos = null;
        try {
            autos = AutoDAO.getInstance().getMyAutos(user.getVerId());
        } catch (SQLException throwables) {
            Logger.getLogger(ReservierteAutosView.class.getName()).log(Level.SEVERE, null, throwables);
        }

        autoGrid.removeAllColumns();
        //autoGrid.setCaption(" <span style='font-size: 25px'> " + "Meine inserierten Autos: " + " </span>");
        autoGrid.setCaption(" <span style='color:#2891d9; font-size:20px; text-shadow: 1px 1px 1px black; font-family:Helvetica Neue'> " + "Meine inserierten Autos: " + " </span>");


        autoGrid.setItems(autos);
        autoGrid.setHeightByRows(!autos.isEmpty() ? autos.size() : 1);

        autoGrid.addColumn(Auto::getMarke).setCaption("Automarke").setWidth(230);
        autoGrid.addColumn(Auto::getBaujahr).setCaption("Baujahr").setWidth(90);
        autoGrid.addColumn(Auto::getBeschreibung).setCaption("Beschreibung");

        autoGrid.setWidth("100%");

        this.addComponents(buttonPan, autoGrid);
    }
}
