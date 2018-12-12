package com.concessionform.rajmehta.concessionform;

public class UserSAP {
    public String SAPID,email,password,uid;

    public UserSAP(){

    }

    public UserSAP(String SAPID, String email, String password, String uid) {
        this.SAPID = SAPID;
        this.email = email;
        this.password = password;
        this.uid=uid;
    }
}
