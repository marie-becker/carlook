package org.carlook.model.objects.dao;

import org.carlook.model.objects.entities.Registrierung;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProfilDAO extends AbstractDAO {
    private static ProfilDAO dao = null;
    private  ProfilDAO(){

    }
    public static ProfilDAO getInstance(){
        if(dao == null){
            dao = new ProfilDAO();
        }
        return dao;
    }

    public void registerUser(Registrierung reg){
        String sql = "insert into carlook.user (name, email, passwort, rolle) values (?,?,?,?)";
        PreparedStatement statement = this.getPreparedStatement(sql);

        try{
            statement.setString(1, reg.getName());
            statement.setString(2, reg.getEmail());
            statement.setString(3, reg.getPw());
            statement.setString(4, reg.getRolle());

            statement.executeUpdate();
        }catch(SQLException ex){
            Logger.getLogger(ProfilDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
