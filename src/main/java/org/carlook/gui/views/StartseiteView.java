package org.carlook.gui.views;

import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.carlook.gui.components.TopPanel;
import org.carlook.model.objects.entities.User;
import org.carlook.process.control.LoginControl;
import org.carlook.services.exceptions.DatabaseException;
import org.carlook.services.exceptions.NoSuchUserOrPassword;
import org.carlook.services.util.Konstanten;
import org.carlook.services.util.Roles;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StartseiteView extends VerticalLayout implements View {

    public void enter(ViewChangeListener.ViewChangeEvent event){
        User user = (User) VaadinSession.getCurrent().getAttribute(Roles.CURRENT);
        if(user != null){
            if(user.getRole().equals(Roles.KUNDE)) UI.getCurrent().getNavigator().navigateTo(Konstanten.SUCHE);
            if(user.getRole().equals(Roles.VERTRIEBLER)) UI.getCurrent().getNavigator().navigateTo(Konstanten.VER_MAIN);
        }
        else this.setUp();
    }

    public void setUp(){
        final String SPACER = "&nbsp";
        VerticalLayout content = new VerticalLayout();
        TopPanel topPanel = new TopPanel();
        content.addComponent(topPanel);
        content.setComponentAlignment(topPanel, Alignment.TOP_LEFT);

        VerticalLayout login = new VerticalLayout();
        Panel panel = new Panel("SignIn");
        panel.setWidth("20%");

        final TextField email = new TextField();
        email.setCaption("Email-Adresse: ");
        email.setDescription("Geben sie ihre Email-Adresse ein.");
        email.setId("emailField");

        final PasswordField pw = new PasswordField();
        pw.setCaption("Passwort: ");
        pw.setDescription("Geben sie das von Ihnen gewählte Passwort ein.");
        pw.setId("pwField");

        Button signin = new Button("Login", VaadinIcons.USER);
        signin.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        signin.setId("loginButton");
        signin.setDescription("Klicken sie hier, um sich einzuloggen.");

        signin.addClickListener(e->{
            String emailIn = email.getValue();
            String pwIn = pw.getValue();
            try {
                LoginControl.checkAuthentication(emailIn, pwIn);
            }catch(NoSuchUserOrPassword nsup){
                Notification.show("Benutzerfehler", "Email-Adresse oder Passwort falsch!", Notification.Type.ERROR_MESSAGE);
                email.setValue("");
                pw.setValue("");
            }catch(DatabaseException exception){
                Notification.show("DB-Fehler", Notification.Type.ERROR_MESSAGE);
            }catch(SQLException sqlException){
                Logger.getLogger(StartseiteView.class.getName()).log(Level.SEVERE, null, sqlException);            }
        });


        HorizontalLayout register = new HorizontalLayout();
        Label regis1 = new Label("Noch kein Konto?");
        Label regis2 = new Label( "Registrieren Sie sich ");
        Button signUp = new Button("hier", e-> UI.getCurrent().getNavigator().navigateTo(Konstanten.REGISTER));
        signUp.addStyleName(ValoTheme.BUTTON_LINK);
        signUp.addStyleName("here-button");
        signUp.setId("regButton");
        signUp.setDescription("Klicken sie hier, um zur Registrierung zu kommen.");
        signUp.setWidthUndefined();
        register.addComponents(regis2, signUp);
        register.setComponentAlignment(regis2, Alignment.MIDDLE_CENTER);
        register.setComponentAlignment(signUp, Alignment.MIDDLE_CENTER);
        register.setSpacing(false);


        login.addComponents(new Label(SPACER, ContentMode.HTML),email, pw, new Label(SPACER, ContentMode.HTML), regis1, register, new Label(SPACER, ContentMode.HTML), signin, new Label(SPACER, ContentMode.HTML));
        login.setComponentAlignment(signin, Alignment.MIDDLE_CENTER);
        login.setComponentAlignment(email, Alignment.MIDDLE_CENTER);
        login.setComponentAlignment(pw, Alignment.MIDDLE_CENTER);
        login.setComponentAlignment(register, Alignment.MIDDLE_CENTER);
        login.setComponentAlignment(regis1, Alignment.MIDDLE_CENTER);
        panel.setContent(login);

        content.addComponent(panel);
        content.setComponentAlignment(panel, Alignment.MIDDLE_CENTER);
        this.addComponent(content);
        this.setComponentAlignment(content, Alignment.MIDDLE_CENTER);
    }
}
