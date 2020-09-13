package org.carlook.gui.windows;

import com.vaadin.server.VaadinSession;
import com.vaadin.ui.*;
import org.carlook.model.objects.dao.AutoDAO;
import org.carlook.model.objects.entities.User;
import org.carlook.services.util.Roles;


public class ConfirmationWindow extends Window {

    public ConfirmationWindow(String text, int autoId){
        super("Confirmation");
        center();
        User user = (User) VaadinSession.getCurrent().getAttribute(Roles.CURRENT);

        //basic content for window
        VerticalLayout content = new VerticalLayout();
        content.setMargin(true);
        setContent(content);

        HorizontalLayout buttons = new HorizontalLayout();

        Button no = new Button("Nein");
        no.addClickListener(e-> close());
        no.setDescription("Klicken sie hier, um den Vorgang abzubrechen.");

        Button yes = new Button("Ja");
        yes.addClickListener(e->{
            AutoDAO.getInstance().reservierAuto(autoId, user.getKundeId());
            close();
        });
        yes.setDescription("Klicken sie hier, um den Vorgang abzuschließen.");

        buttons.addComponents(yes, no);
        content.addComponents(new Label(text), buttons);
        content.setComponentAlignment(buttons, Alignment.MIDDLE_CENTER);
    }
}