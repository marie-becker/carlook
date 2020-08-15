package org.carlook.process.control;

import org.carlook.model.objects.dao.ProfilDAO;
import org.carlook.model.objects.entities.Registrierung;
import org.carlook.model.objects.entities.User;
import org.carlook.model.objects.factories.RegFactory;
import org.carlook.process.control.exceptions.DatabaseException;
import org.carlook.services.db.JDBCConnection;
import org.carlook.services.util.RegistrationResult;
import org.carlook.services.util.Roles;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegistrationControl {
    private RegistrationControl(){

    }
    private static RegistrationControl process = null;

    public static RegistrationControl getInstance(){
        if(process == null){
            process = new RegistrationControl();
        }
        return process;
    }

    public RegistrationResult createRegistration(User request) throws DatabaseException, SQLException {
        RegistrationResult result = new RegistrationResult();
        result.setResult(true);
        Registrierung reg;
        String rolle = request.getRole();

        reg = RegFactory.createReg(request);

        if(request.getRole().equals("") || request.getRole() == null){
            result.setReason(RegistrationResult.FailureType.NO_ROLE);
            result.setResult(false);
        }

        if(!request.getPw2().equals(request.getPw())){
            result.setReason(RegistrationResult.FailureType.PWS_DONT_MATCH);
            result.setResult(false);
        }
        if(request.getPw().length() < 8){
            result.setReason(RegistrationResult.FailureType.PW_TOO_SHORT);
            result.setResult(false);
        }
        if(request.getPw() == null || request.getPw().equals("")){
            result.setReason(RegistrationResult.FailureType.PW_MISSING);
            result.setResult(false);
        }
        //Check ob Email schon vergeben ist
        String sql = "SELECT * FROM carlook.user WHERE carlook.user.email = ?";
        PreparedStatement statement = JDBCConnection.getInstance().getPreparedStatement(sql);
        statement.setString(1, request.getEmail());
        try(ResultSet set = statement.executeQuery()){
            if(set.next()){
                result.setReason(RegistrationResult.FailureType.EMAIL_EXISTS);
                result.setResult(false);
            }
        }
        if(!RegistrationResult.isValid(request.getEmail())){
            result.setReason(RegistrationResult.FailureType.EMAIL_INVALID);
            result.setResult(false);
        }
        if ( request.getEmail() == null || request.getEmail().equals("")) {
            result.setReason(RegistrationResult.FailureType.EMAIL_MISSING);
            result.setResult(false);
        }
        if(request.getNachname() == null || request.getNachname().equals("")){
            result.setReason(RegistrationResult.FailureType.NACHNAME_MISSING);
            result.setResult(false);
        }
        if(request.getVorname() == null || request.getVorname().equals("")){
            result.setReason(RegistrationResult.FailureType.VORNAME_MISSING);
            result.setResult(false);
        }

        if(result.getResult()){
            if(rolle.equals(Roles.KUNDE)) ProfilDAO.getInstance().registerKunde(reg);
            if(rolle.equals(Roles.VERTRIEBLER)) ProfilDAO.getInstance().registerVer(reg);
        }

        return result;
    }
}
