/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Grupp 1 & 2 Newton 2018
 */
public class Folder implements Serializable {

    int id;
    String name;
    Date date;
    List<Document> files;

    public Folder() {

    }

    public Folder(int id, String name, Date date) {
        this.id = id;
        this.name = name;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<Document> getFiles() {
        return files;
    }

    public void setFiles(List<Document> files) {
        this.files = files;
    }

}
