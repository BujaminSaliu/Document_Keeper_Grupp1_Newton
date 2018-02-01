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
 * @author ramonachantaf
 */
public class Files implements Serializable {
    int id;
    String name;
    Date date;
    double type;
    String ocr;
    List<Tags> tags;
    
    public Files(){
        
    }

    public Files(int id, String name, Date date, double type, String ocr) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.type = type;
        this.ocr = ocr;
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

    public double getType() {
        return type;
    }

    public void setType(double type) {
        this.type = type;
    }

    public String getOcr() {
        return ocr;
    }

    public void setOcr(String ocr) {
        this.ocr = ocr;
    }

    public List<Tags> getTags() {
        return tags;
    }

    public void setTags(List<Tags> tags) {
        this.tags = tags;
    }
    
    
    
}
