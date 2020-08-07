package org.carlook.gui.windows;

import com.vaadin.server.VaadinSession;
import com.vaadin.ui.*;
import org.carlook.model.objects.dao.AutoDAO;
import org.carlook.model.objects.entities.User;
import org.carlook.services.util.Roles;


public class InseratPopUp extends Window {

    public InseratPopUp(){
        super("Neues Auto einstellen"); //set window caption
        center();
        //basic content for window
        VerticalLayout content = new VerticalLayout();
        content.setMargin(true);

        User user = (User) VaadinSession.getCurrent().getAttribute(Roles.CURRENT);

        TextField markeFeld = new TextField("Automarke:");
        markeFeld.setValue("");
        markeFeld.setDescription("Geben sie die Automarke ein.");

        TextField baujahrFeld = new TextField("Baujahr:");
        baujahrFeld.setValue("");
        baujahrFeld.setDescription("Geben sie das Baujahr des Autos ein.");

        TextField descrFeld = new TextField("Beschreibung:");
        descrFeld.setValue("");
        descrFeld.setDescription("Geben sie eine kurze Beschreibung des Autos an.");

        Button abbruch = new Button("Abbrechen");
        abbruch.addClickListener(e->{
            close();
        });

        Button enter = new Button("Auto einstellen");
        enter.addClickListener(e->{
            if(markeFeld.getValue().equals("")){
                Notification.show("Geben sie die Marke des Autos an!");
            }
            else if(baujahrFeld.getValue().equals("")){
                Notification.show("Geben sie das Baujahr des Autos an!", Notification.Type.ERROR_MESSAGE);
            }
            else if(descrFeld.getValue().equals("")) Notification.show("Geben sie eine Beschreibung des Autos an!");
            else {
                AutoDAO.getInstance().insertAuto(markeFeld.getValue(), baujahrFeld.getValue(), descrFeld.getValue(), user.getVerId());
                Notification.show("Auto erfolgreich eingestellt.");
                close();
            }
        });

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.addComponents(enter, abbruch);

        content.addComponents(markeFeld, baujahrFeld, descrFeld, buttons);
        content.setComponentAlignment(buttons, Alignment.MIDDLE_CENTER);


        setContent(content);
    }

}
