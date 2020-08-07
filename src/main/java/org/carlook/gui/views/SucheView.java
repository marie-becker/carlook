package org.carlook.gui.views;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.ButtonRenderer;
import org.carlook.gui.windows.ConfirmationWindow;
import org.carlook.model.objects.Auto;
import org.carlook.model.objects.dao.AutoDAO;
import org.carlook.model.objects.entities.User;
import org.carlook.process.control.LoginControl;
import org.carlook.services.util.Konstanten;
import org.carlook.services.util.Roles;
import org.carlook.gui.components.TopPanel;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SucheView extends VerticalLayout implements View {

    User user;
    VerticalLayout content = new VerticalLayout();
    Label pageTitle;
    HorizontalLayout suchFelder;
    MenuBar bar;
    HorizontalLayout heading;
    TopPanel toppanel;

    public void enter(ViewChangeListener.ViewChangeEvent event) {
        user = (User) VaadinSession.getCurrent().getAttribute(Roles.CURRENT);
        if(user == null || !user.getRole().equals(Roles.KUNDE)) UI.getCurrent().getNavigator().navigateTo(Konstanten.START);
        else this.setUp();
    }

    public void setUp() {
        toppanel = new TopPanel();
        this.addComponent(toppanel);

        /*
        bar = new MenuBar();
        MenuBar.MenuItem item1 = bar.addItem("Menü", null);

        //Logout des Users
        item1.addItem("Logout", VaadinIcons.SIGN_OUT, new MenuBar.Command() {
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                LoginControl.logoutUser();
            }
        });

        item1.addItem("Reservierungen", VaadinIcons.LIST, new MenuBar.Command() {
            public void menuSelected(MenuBar.MenuItem menuItem) {
                UI.getCurrent().getNavigator().navigateTo(Konstanten.RSV_AUTOS);
            }
        });


         */

        pageTitle = new Label();
        pageTitle.setCaption("Autosuche");
        pageTitle.addStyleName("header-suche");

        TextField markeFeld = new TextField("Marke");
        markeFeld.setDescription("Geben sie eine Automarke ein.");
        markeFeld.setValue("");

        TextField baujahrFeld = new TextField("Baujahr:");
        baujahrFeld.setDescription("Geben sie das Baujahr des Autos ein.");
        baujahrFeld.setValue("");


        markeFeld.addValueChangeListener(e-> onTheFly(markeFeld.getValue(), baujahrFeld.getValue()));
        baujahrFeld.addValueChangeListener(e-> onTheFly(markeFeld.getValue(), baujahrFeld.getValue()));

        suchFelder = new HorizontalLayout();
        suchFelder.addComponents(markeFeld, baujahrFeld);

        /*
        heading = new HorizontalLayout();
        heading.addComponents(pageTitle, bar);
        heading.setComponentAlignment(pageTitle, Alignment.TOP_LEFT);
        heading.setComponentAlignment(bar, Alignment.TOP_RIGHT);

         */

        content.addComponents(new Label("&nbsp", ContentMode.HTML), suchFelder);


        //Erstmal alle Autos anzeigen lassen
        onTheFly("", "");
        this.addComponent(content);
        this.setComponentAlignment(content, Alignment.MIDDLE_CENTER);
    }

    public void onTheFly(String marke, String baujahr){
        //Erstellung Tabelle mit Jobangeboten
        content.removeAllComponents();
        content.addComponents(toppanel, new Label("&nbsp", ContentMode.HTML), suchFelder);

        Grid<Auto> autoGrid = new Grid<>();
        autoGrid.setSizeUndefined();
        List<Auto> autos = null;

        try {
            autos = AutoDAO.getInstance().searchAutos(marke, baujahr);
        } catch (SQLException ex) {
            Logger.getLogger(SucheView.class.getName()).log(Level.SEVERE, null, ex);
        }
        autoGrid.removeAllColumns();
        autoGrid.setCaption(marke.equals("") ? "Alle Autos:" : "Ergebnisse für: " + marke);
        autoGrid.setItems(autos);
        autoGrid.setHeightByRows(!autos.isEmpty() ? autos.size() : 1);


        autoGrid.addColumn(Auto::getMarke).setCaption("Automarke").setWidth(230);
        autoGrid.addColumn(Auto::getBaujahr).setCaption("Baujahr").setWidth(90);
        autoGrid.addColumn(Auto::getBeschreibung).setCaption("Beschreibung");

        ButtonRenderer<Auto> reservieren = new ButtonRenderer<>(clickEvent ->{
            ConfirmationWindow window = new ConfirmationWindow("Wollen sie dieses Auto reservieren?", clickEvent.getItem().getAutoid());
            UI.getCurrent().addWindow(window);
        });

        autoGrid.addColumn(Auto -> "Reservieren", reservieren).setWidthUndefined();

        content.addComponents(new Label("&nbsp", ContentMode.HTML), autoGrid);
        autoGrid.setWidth("100%");
    }
}
