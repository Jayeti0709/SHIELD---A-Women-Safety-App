package com.example.womensateyapp;

public class MainModel {
    String Name,emailid,Relation;

    String Phoneno;

   MainModel(){

   }
    public MainModel(String name, String emailid, String relation, String Phoneno) {
        Name = name;
        this.emailid = emailid;
        Relation = relation;
        this.Phoneno = Phoneno;
    }
    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmailid() {
        return emailid;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    public String getRelation() {
        return Relation;
    }

    public void setRelation(String relation) {
        Relation = relation;
    }

    public String getPhoneno() {
        return Phoneno;
    }

    public void setPhoneno(String Phoneno) {
        Phoneno = Phoneno;
    }


}
