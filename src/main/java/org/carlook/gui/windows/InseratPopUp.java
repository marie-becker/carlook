package org.carlook.gui.windows;

import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import org.carlook.model.objects.dao.AutoDAO;
import org.carlook.model.objects.entities.User;
import org.carlook.services.util.Roles;

import java.util.Calendar;


public class InseratPopUp extends Window {

    public InseratPopUp(){
        super("Neues Auto einstellen"); //set window caption
        center();
        //basic content for window
        VerticalLayout content = new VerticalLayout();
        content.setMargin(true);

        User user = (User) VaadinSession.getCurrent().getAttribute(Roles.CURRENT);

        TextField markeFeld = new TextField("Automarke*:");
        markeFeld.setValue("");
        markeFeld.setDescription("Geben sie die Automarke ein.");

        TextField baujahrFeld = new TextField("Baujahr*:");
        baujahrFeld.setValue("");
        baujahrFeld.setDescription("Geben sie das Baujahr des Autos ein.");

        TextArea descrFeld = new TextArea("Beschreibung*:");
        descrFeld.setValue("");
        descrFeld.setDescription("Geben sie eine kurze Beschreibung des Autos an.");

        Button abbruch = new Button("Abbrechen");
        abbruch.addClickListener(e-> close());
        abbruch.setDescription("Klicken sie hier um den Vorgang abzubrechen.");

        Button enter = new Button("Auto einstellen");
        enter.addClickListener(e->{
            if(markeFeld.getValue().equals("")){
                Notification.show("Geben sie die Marke des Autos an!", Notification.Type.ERROR_MESSAGE);
                return;
            }
            else if(baujahrFeld.getValue().equals("")) {
                Notification.show("Geben sie das Baujahr des Autos an!", Notification.Type.ERROR_MESSAGE);
                return;
            }

            int baujahr;
            try {
                baujahr = Integer.parseInt(baujahrFeld.getValue());
                int now = Calendar.getInstance().get(Calendar.YEAR);
                if (baujahr > now) {
                    Notification.show("Das Baujahr kann nicht in der Zukunft liegen!", Notification.Type.ERROR_MESSAGE);
                    baujahrFeld.setValue("");
                    return;
                }
                if (baujahr < 1900) {
                    Notification.show("Unzulässiges Baujahr!", Notification.Type.ERROR_MESSAGE); //Beispielwert, kA was realistisch ist
                    baujahrFeld.setValue("");
                    return;
                }
            } catch (NumberFormatException ex) {
                Notification.show("Geben sie das Baujahr in Zahlen an!", Notification.Type.ERROR_MESSAGE);
                baujahrFeld.setValue("");
                return;
            }

            if(descrFeld.getValue().equals("")) Notification.show("Geben sie eine Beschreibung des Autos an!");
            else {
                AutoDAO.getInstance().insertAuto(markeFeld.getValue(), baujahrFeld.getValue(), descrFeld.getValue(), user.getVerId());
                Notification.show("Auto erfolgreich eingestellt.");
                close();
            }
        });
        enter.setDescription("Klicken sie hier, um den Vorgang abzuschließen.");

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.addComponents(enter, abbruch);
        content.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        content.addComponents(markeFeld, baujahrFeld, descrFeld, new Label("&nbsp", ContentMode.HTML), buttons);

        setContent(content);
    }
}