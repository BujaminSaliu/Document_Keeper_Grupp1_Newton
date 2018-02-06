/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
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
    int size;
    String path;
    List<Tag> tags;
   
    
    public Document(){
        
    }

    public Document(int id, String name,Date date, String type,int size, String path) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.type = type;
        this.size = size;
        this.path = path;
        this.tags = new ArrayList<>();
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

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
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
       if(tags != null){
           if(tags.isEmpty()){
               return this.getName() +"."+ this.getType(); 
           } else {
               return "\u2713\t"+ this.getName() +"."+this.getType();
           }
       }else{
             return this.getName()+"."+this.getType();
       }
         // returnerar Stringen name
    }
    
}
