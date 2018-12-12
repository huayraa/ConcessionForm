package com.concessionform.rajmehta.concessionform;

public class Blog {
    public String fullname, mainblog, title, uid;
    public Boolean approved, comments;

    public Blog(){

    }

    public Blog(Boolean approved, Boolean comments, String fullname, String mainblog, String title, String uid){
        this.approved = approved;
        this.comments = comments;
        this.fullname = fullname;
        this.mainblog = mainblog;
        this.title = title;
        this.uid = uid;
    }
}
