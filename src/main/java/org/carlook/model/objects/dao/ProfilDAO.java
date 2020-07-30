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

    public void registerKunde(Registrierung reg){
        String sql = "insert into carlook.user (vorname, nachname, email, passwort, rolle) values (?,?,?,?,?)";
        String sql2 = "insert into carlook.user_to_rolle (email, rolle) values (?,?)";
        PreparedStatement statement2 = this.getPreparedStatement(sql2);
        PreparedStatement statement = this.getPreparedStatement(sql);

        try{
            statement.setString(1, reg.getVorname());
            statement.setString(2, reg.getNachname());
            statement.setString(3, reg.getEmail());
            statement.setString(4, reg.getPw());
            statement.setString(5, "kunde");

            statement2.setString(1, reg.getEmail());
            statement2.setString(2, "kunde");

            statement.executeUpdate();
            statement2.executeUpdate();
        }catch(SQLException ex){
            Logger.getLogger(ProfilDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void registerVer(Registrierung reg){
        String sql = "insert into carlook.vertriebler_view (vorname, nachname, email, passwort) values (?,?,?,?)";
        PreparedStatement statement = this.getPreparedStatement(sql);

        try{
            statement.setString(1, reg.getVorname());
            statement.setString(2, reg.getNachname());
            statement.setString(3, reg.getEmail());
            statement.setString(4, reg.getPw());

            statement.executeUpdate();
        }catch(SQLException ex){
            Logger.getLogger(ProfilDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
