package org.carlook.model.objects.factories;

import org.carlook.model.objects.entities.Registrierung;
import org.carlook.model.objects.entities.User;

public class RegFactory {
    private RegFactory(){

    }

    public static Registrierung createReg(User request){
        Registrierung reg = new Registrierung();
        reg.setName(request.getName());
        reg.setEmail(request.getEmail());
        reg.setPw(request.getPw());
        reg.setRolle(request.getRole());

        return reg;
    }
}
