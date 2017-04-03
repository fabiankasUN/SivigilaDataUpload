/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Fabian
 */
public class ModelTown {
    
    private String id;
    private String name;
    private String id_department;

    /**
     *
     * @param id
     * @param name
     * @param id_department
     */
    public ModelTown(String id, String name, String id_department) {
        this.id = id;
        this.name = name;
        this.id_department = id_department;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId_department() {
        return id_department;
    }

    public void setId_department(String id_department) {
        this.id_department = id_department;
    }
    
    
    
    
}
