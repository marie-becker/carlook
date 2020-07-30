package org.carlook.gui.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import org.carlook.model.objects.entities.User;
import org.carlook.process.control.RegistrationControl;
import org.carlook.process.control.exceptions.DatabaseException;
import org.carlook.services.util.Konstanten;
import org.carlook.services.util.RegistrationResult;
import org.carlook.services.util.Roles;
import org.carlook.services.util.UserBuilder;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegView extends VerticalLayout implements View {
    private User user;

    public void enter(ViewChangeListener.ViewChangeEvent event) {
        this.setUp();
    }

    public void setUp() {
        this.setSizeFull();


        VerticalLayout registrierung = new VerticalLayout();
        Panel panel = new Panel("SignUp");
        panel.setSizeUndefined();

        final TextField vornameField = new TextField();
        vornameField.setCaption("Vorname:");
        vornameField.setDescription("Geben sie Ihren Vornamen ein.");

        final TextField nachnameField = new TextField();
        nachnameField.setCaption("Nachname:");
        nachnameField.setDescription("Geben sie Ihren Nachnamen ein.");

        final TextField emailField = new TextField();
        emailField.setCaption("Email-Adresse: ");
        emailField.setDescription("Geben sie ihre Email-Adresse ein.");

        final PasswordField pwField = new PasswordField();
        pwField.setCaption("Passwort: ");
        pwField.setDescription("Geben sie das von Ihnen gewählte Passwort ein.");

        final PasswordField pw2Field = new PasswordField();
        pw2Field.setCaption("Passwort bestätigen");
        pw2Field.setDescription("Wiederholen sie das von Ihnen gewählte Passwort.");


        NativeSelect<String> profiltyp = new NativeSelect<>("Sind sie Kunde oder Vertriebler?");
        profiltyp.setItems("Kunde", "Vertriebler");


        Button register = new Button("Registrieren");
        register.addClickListener(e->{
            RegistrationResult r = null;
            user = UserBuilder.getInstance().createNewUser().withVorname(vornameField.getValue()).withNachname(nachnameField.getValue()).
                    withEmail(emailField.getValue()).withPw(pwField.getValue()).with2ndPw(pw2Field.getValue()).getUser();
            if(profiltyp.getValue() == null || profiltyp.getValue().equals("")){
                Notification.show("Wählen sie eine Profilart aus.", Notification.Type.ERROR_MESSAGE);
                return;
            }
            if(profiltyp.getValue().equals("Kunde")){
                user.setRole(Roles.KUNDE);
            }
            if(profiltyp.getValue().equals("Vertriebler")){
                user.setRole(Roles.VERTRIEBLER);
            }
            try{
                r = RegistrationControl.getInstance().createRegistration(user);
            }catch(SQLException | DatabaseException ex){
                Logger.getLogger(RegView.class.getName()).log(Level.SEVERE, null, ex);
            }

            if(!r.getResult()){
                Notification.show(r.error(), Notification.Type.ERROR_MESSAGE);
                if (r.getFailureType() == RegistrationResult.FailureType.PW_TOO_SHORT || (r.getFailureType() == RegistrationResult.FailureType.PWS_DONT_MATCH)) {
                    pwField.setValue("");
                    pw2Field.setValue("");
                }
                if( r.getFailureType() == RegistrationResult.FailureType.EMAIL_EXISTS){
                    emailField.setValue("");
                }
            }else{
                Notification.show("Registrierung erfolgreich!", "Sie werden automatisch zum Login weitergeleitet", Notification.Type.HUMANIZED_MESSAGE);
                UI.getCurrent().getNavigator().navigateTo(Konstanten.START);
            }

        });

        registrierung.addComponents(vornameField, nachnameField, emailField, pwField, pw2Field, profiltyp, register);
        panel.setContent(registrierung);
        this.addComponent(panel);
        this.setComponentAlignment(panel, Alignment.MIDDLE_CENTER);
    }
}
