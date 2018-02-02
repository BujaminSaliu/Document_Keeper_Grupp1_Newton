/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ramonachantaf
 */
public class Document implements Serializable {
    int id;
    String name;
    Date date;
    String type;
    double size;
    String path;
    List<Tag> tags;
   
    
    public Document(){
        
    }

    public Document(int id, String name,Date date, String type,double size, String path) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.type = type;
        this.size = size;
        this.path = path;

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


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

 
    
    
    @Override
     public String toString(){ // metod för att skriva ut Document
       System.out.print(this.getName());
        return this.getName(); // returnerar Stringen name
    }
    
}
