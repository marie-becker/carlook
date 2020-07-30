package org.carlook.services.util;

import org.carlook.model.objects.entities.User;

public class UserBuilder {

    private static UserBuilder builder = null;
    private User user = null;

    public UserBuilder createNewUser(){
        this.user = new User();
        return builder;
    }
    public static UserBuilder getInstance(){
        builder = new UserBuilder();
        return builder;
    }

    public UserBuilder withName(String name){
        this.user.setName(name);
        return this;
    }
    public UserBuilder withEmail(String email){
        this.user.setEmail(email);
        return this;
    }
    public UserBuilder withPw(String pw){
        this.user.setPw(pw);
        return this;
    }

    public UserBuilder with2ndPw(String pw){
        this.user.setPw(pw);
        return this;
    }

    public User getUser(){
        return this.user;
    }
}
