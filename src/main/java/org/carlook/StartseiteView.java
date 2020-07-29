package org.carlook;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import org.carlook.exceptions.DatabaseException;
import org.carlook.exceptions.NoSuchUserOrPassword;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StartseiteView extends VerticalLayout implements View {

    public void enter(ViewChangeListener.ViewChangeEvent event){
        this.setUp();}

    public void setUp(){
        this.setSizeFull();
        Label spacer = new Label("&nbsp", ContentMode.HTML);

        VerticalLayout login = new VerticalLayout();
        Panel panel = new Panel("SignIn");
        panel.setSizeUndefined();

        final TextField email = new TextField();
        email.setCaption("Email-Adresse: ");
        email.setDescription("Geben sie ihre Email-Adresse ein.");

        final PasswordField pw = new PasswordField();
        pw.setCaption("Passwort: ");
        pw.setDescription("Geben sie das von Ihnen gewählte Passwort ein.");

        Button signin = new Button("Login");
        signin.addClickListener(e->{
            String emailIn = email.getValue();
            String pwIn = pw.getValue();

            try {
                LoginControl.checkAuthentication(emailIn, pwIn);
                Notification.show("Login erfolgreich! :D", Notification.Type.ERROR_MESSAGE);
            }catch(NoSuchUserOrPassword nsup){
                Notification.show("Benutzerfehler", "Email-Adresse oder Passwort falsch!", Notification.Type.ERROR_MESSAGE);
                email.setValue("");
                pw.setValue("");
            }catch(DatabaseException exception){
                Notification.show("DB-Fehler", Notification.Type.ERROR_MESSAGE);
            }catch(SQLException sqlException){
                Logger.getLogger(StartseiteView.class.getName()).log(Level.SEVERE, null, sqlException);            }
        });

        /*
        VerticalLayout register = new VerticalLayout();
        Label regis = new Label("Noch kein Konto? Registrieren Sie sich");
        Button signUp = new Button("hier", e->{
            UI.getCurrent().getNavigator().navigateTo(Konstanten.START);
        });
        signUp.addStyleName(ValoTheme.BUTTON_LINK);
        register.addComponents(regis, signUp);

         */


        login.addComponents(email, pw, signin);
        login.setComponentAlignment(signin, Alignment.MIDDLE_CENTER);
        panel.setContent(login);

        this.addComponent(panel);
        this.setComponentAlignment(panel, Alignment.MIDDLE_CENTER);
    }


}
