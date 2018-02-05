/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author ramonachantaf
 */
public class Tag implements Serializable {
    
    int id;
    String name;
    List<Document> files;
    
    public Tag(){
        
    }

    public Tag(int id, String name) {
        this.id = id;
        this.name = name;
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

    public List<Document> getFiles() {
        return files;
    }

    public void setFiles(List<Document> files) {
        this.files = files;
    }
    
    @Override
     public String toString(){ 
       //System.out.print(this.getName());
        return this.getName(); 
    }
}
