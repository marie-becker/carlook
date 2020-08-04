package org.carlook.model.objects.dao;

import com.vaadin.ui.Notification;
import org.carlook.model.objects.Auto;
import org.postgresql.util.PSQLException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AutoDAO extends AbstractDAO {
    private static AutoDAO dao = null;
    private  AutoDAO(){

    }
    public static AutoDAO getInstance(){
        if(dao == null){
            dao = new AutoDAO();
        }
        return dao;
    }

    public PreparedStatement getStatement(String marke, String baujahr) throws SQLException {
        marke = "%"+marke.toUpperCase()+"%";
        baujahr = "%"+baujahr+"%";
        String allCars = "SELECT * FROM carlook.auto";
        PreparedStatement statement;

        if(marke.equals("")){
            if(baujahr.equals("")){
                System.out.println("both empty");
                statement = this.getPreparedStatement(allCars);
            }else{
                System.out.println("marke leer, baujahr da");
                String nachBaujahr = allCars + " WHERE baujahr LIKE ?";
                statement = this.getPreparedStatement(nachBaujahr);
                statement.setString(1, baujahr);
            }
        }else{
            if(baujahr.equals("")){
                System.out.println("marke da, baujahr leer");
                String nachMarke = allCars + " WHERE UPPER(marke) LIKE ?";
                statement = this.getPreparedStatement(nachMarke);
                statement.setString(1, marke);
            }else{
                System.out.println("marke da, baujahr da");
                String both = allCars + " WHERE UPPER(marke) LIKE ? AND baujahr LIKE ?";
                statement = this.getPreparedStatement(both);
                statement.setString(1, marke);
                statement.setString(2, baujahr);
            }
        }
        return statement;
    }

    public List<Auto> searchAutos(String marke, String baujahr) throws SQLException {
        PreparedStatement statement = getStatement(marke, baujahr);
        return getAutoList(statement);
    }

    private List<Auto> getAutoList(PreparedStatement statement) {
        List<Auto> autoList = new ArrayList<>();
        try(ResultSet rs = statement.executeQuery()){
            if(rs == null) return null;
            while(rs.next()) autoList.add(getAuto(rs));
        }catch(SQLException ex){
            Logger.getLogger(AutoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return autoList;
    }

    public Auto getAuto(ResultSet rs) throws SQLException {
        Auto auto = new Auto();
        auto.setAutoid(rs.getInt(1));
        auto.setMarke(rs.getString(2));
        auto.setBaujahr(rs.getInt(3));
        auto.setBeschreibung(rs.getString(4));
        return auto;
    }

    public List<Auto> getMyAutos(int verId) throws SQLException {
        String sql = "SELECT * FROM carlook.auto WHERE verId = ?";
        PreparedStatement statement = this.getPreparedStatement(sql);
        statement.setInt(1, verId);
        return getAutoList(statement);
    }

    public List<Auto> getMyRsvAutos(int kundeId) throws SQLException {
        String sql = "SELECT * FROM carlook.auto a and carlook.kunde_rsv_auto k WHERE a.kunde_id = k.kunde_id AND k.kunde_id = ?";
        PreparedStatement statement = this.getPreparedStatement(sql);
        List<Auto> autoList = new ArrayList<>();
        statement.setInt(1,kundeId);
        try(ResultSet set = statement.executeQuery()) {
            if(set == null) return null;
            while(set.next()) autoList.add(getAuto(set));
        } catch (SQLException ex) {
            Logger.getLogger(AutoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return autoList;
    }

    public void insertAuto(String marke, String baujahr, String descr, int verId){
        String sql = "insert into carlook.auto (marke, baujahr, beschreibung) values(?,?,?)";
        PreparedStatement statement = this.getPreparedStatement(sql);
        try {
            statement.setString(1, marke);
            statement.setString(2, baujahr);
            statement.setString(3, descr);

            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(AutoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

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
