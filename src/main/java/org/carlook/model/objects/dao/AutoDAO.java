package org.carlook.model.objects.dao;

import com.vaadin.ui.Notification;
import org.carlook.model.objects.entities.Auto;
import org.postgresql.util.PSQLException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AutoDAO extends AbstractDAO {
    private static AutoDAO dao = null;
    private  AutoDAO(){}

    //Singleton
    public static AutoDAO getInstance(){
        if(dao == null){
            dao = new AutoDAO();
        }
        return dao;
    }

    //Gibt Statement passend zu Suchparametern zurück
    public PreparedStatement getStatement(String marke, String baujahr) throws SQLException {
        if(!marke.equals("")) marke = "%"+marke.toUpperCase()+"%";
        if(!baujahr.equals("")) baujahr = "%"+baujahr+"%";
        String allCars = "SELECT * FROM carlook.auto";
        PreparedStatement statement;

        if(marke.equals("")){
            if(baujahr.equals("")){
                statement = this.getPreparedStatement(allCars);
            }else{
                String nachBaujahr = allCars + " WHERE baujahr LIKE ?";
                statement = this.getPreparedStatement(nachBaujahr);
                statement.setString(1, baujahr);
            }
        }else{
            if(baujahr.equals("")){
                String nachMarke = allCars + " WHERE UPPER(marke) LIKE ?";
                statement = this.getPreparedStatement(nachMarke);
                statement.setString(1, marke);
            }else{
                String both = allCars + " WHERE UPPER(marke) LIKE ? AND baujahr LIKE ?";
                statement = this.getPreparedStatement(both);
                statement.setString(1, marke);
                statement.setString(2, baujahr);
            }
        }
        return statement;
    }

    //Für die Autosuche
    public List<Auto> searchAutos(String marke, String baujahr) throws SQLException {
        PreparedStatement statement = getStatement(marke, baujahr);
        return getAutoList(statement);
    }

    //Gibt eine Liste von Autos zurück, die auf ein übergebenes Statement passen
    private List<Auto> getAutoList(PreparedStatement statement) {
        List<Auto> autoList = new ArrayList<>();
        try(ResultSet rs = statement.executeQuery()){
            if(rs == null) return Collections.emptyList();
            while(rs.next()) autoList.add(getAuto(rs));
        }catch(SQLException ex){
            Logger.getLogger(AutoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return autoList;
    }

    //Gibt das nächste Auto eines übergebenen ResultSets zurück
    public Auto getAuto(ResultSet rs) throws SQLException {
        Auto auto = new Auto();
        auto.setAutoid(rs.getInt(1));
        auto.setMarke(rs.getString(2));
        auto.setBaujahr(rs.getInt(3));
        auto.setBeschreibung(rs.getString(4));
        return auto;
    }

    //Gibt die inserierten Autos eines Vertrieblers zurück
    public List<Auto> getMyAutos(int verId) throws SQLException {
        String sql = "SELECT * FROM carlook.auto WHERE ver_id = ?";
        PreparedStatement statement = this.getPreparedStatement(sql);
        statement.setInt(1, verId);
        return getAutoList(statement);
    }

    //Gibt die reservierten Autos eines Kunden zurück
    public List<Auto> getMyRsvAutos(int kundeId) throws SQLException {
        String sql = "SELECT * FROM carlook.auto a INNER JOIN carlook.kunde_rsv_auto k ON a.auto_id = k.auto_id WHERE k.kunde_id = ?";
        PreparedStatement statement = this.getPreparedStatement(sql);
        List<Auto> autoList = new ArrayList<>();
        statement.setInt(1,kundeId);
        try(ResultSet set = statement.executeQuery()) {
            if(set == null) return Collections.emptyList();
            while(set.next()) autoList.add(getAuto(set));
        } catch (SQLException ex) {
            Logger.getLogger(AutoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return autoList;
    }

    //Wenn ein Vertriebler ein neues Auto inseriert
    public void insertAuto(String marke, String baujahr, String descr, int verId){
        String sql = "insert into carlook.auto (marke, baujahr, beschreibung, ver_id) values(?,?,?,?)";
        PreparedStatement statement = this.getPreparedStatement(sql);
        try {
            statement.setString(1, marke);
            statement.setString(2, baujahr);
            statement.setString(3, descr);
            statement.setInt(4, verId);

            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(AutoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Wenn ein Kunde ein Auto reserviert
    public void reservierAuto(int autoId, int kundeId){
        String sql = "insert into carlook.kunde_rsv_auto (auto_id, kunde_id) values (?,?)";
        PreparedStatement statement = this.getPreparedStatement(sql);
        try {
            statement.setInt(1, autoId);
            statement.setInt(2, kundeId);

            statement.executeUpdate();
            Notification.show("Auto wurde reserviert", Notification.Type.ERROR_MESSAGE);
        } catch (PSQLException psqlException){
            Notification.show("Sie haben dieses Auto bereits reserviert.", Notification.Type.ERROR_MESSAGE);
        } catch (SQLException ex) {
            Logger.getLogger(AutoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
