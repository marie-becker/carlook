package org.carlook.process.control;

import com.vaadin.server.VaadinSession;
import org.carlook.services.util.Roles;
import org.carlook.services.db.JDBCConnection;
import org.carlook.model.objects.entities.User;
import org.carlook.process.control.exceptions.DatabaseException;
import org.carlook.process.control.exceptions.NoSuchUserOrPassword;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginControl {
    private LoginControl(){}

    public static void checkAuthentication(String email, String pw) throws DatabaseException, SQLException, NoSuchUserOrPassword {
        User user = new User();
        String sql = "SELECT * FROM carlook.user WHERE carlook.user.email = ? AND carlook.user.passwort = ?";
        PreparedStatement statement = JDBCConnection.getInstance().getPreparedStatement(sql);
        statement.setString(1, email);
        statement.setString(2, pw);

        try(ResultSet set = statement.executeQuery()){
            if(set.next()){
                user.setVorname(set.getString(1));
                user.setNachname(set.getString(2));
                user.setEmail(set.getString(3));
                user.setPw(set.getString(4));
                user.setRole(set.getString(5));
            }else{
                throw new NoSuchUserOrPassword();
            }
        }catch(SQLException ex){
            Logger.getLogger(LoginControl.class.getName()).log(Level.SEVERE, null, ex);
        }
        VaadinSession.getCurrent().setAttribute(Roles.CURRENT, user);

    }
}
