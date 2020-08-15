package org.carlook.model.objects.dao;

import org.carlook.model.objects.entities.Registrierung;
import org.carlook.services.util.Roles;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

    public void registerKunde(Registrierung reg) throws SQLException {
        //Erst neuen Kunden erstellen -> kunde_id
        String kunde = "insert into carlook.kunde DEFAULT VALUES";
        PreparedStatement statement = this.getPreparedStatement(kunde);
        statement.executeUpdate();

        //kunde_id des neuen Eintrags holen
        String kundeid = "SELECT MAX(kunde_id) FROM carlook.kunde";
        PreparedStatement statement2 = this.getPreparedStatement(kundeid);

        //Neuen User anlegen mit der besorgten kunde_id
        String user = "insert into carlook.user (vorname, nachname, email, passwort, rolle, kunde_id) values (?,?,?,?,?,?)";
        PreparedStatement statement3 = this.getPreparedStatement(user);



        try(ResultSet rs = statement2.executeQuery()){
            int kundeId = 0;
            if(rs.next()) {
                kundeId = rs.getInt(1);
            }
            statement3.setString(1, reg.getVorname());
            statement3.setString(2, reg.getNachname());
            statement3.setString(3, reg.getEmail());
            statement3.setString(4, reg.getPw());
            statement3.setString(5, Roles.KUNDE);
            statement3.setInt(6, kundeId);
            statement3.executeUpdate();
        } catch (SQLException ex) {
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
