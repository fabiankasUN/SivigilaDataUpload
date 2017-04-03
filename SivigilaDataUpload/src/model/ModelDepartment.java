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
public class ModelDepartment implements Comparable<ModelDepartment> {
    
    private String name;
    private String value;
    
    /**
     *
     */
    public ModelDepartment(){
        this.name = "";
        this.value = "";
    }
    
    /**
     *
     * @param name
     * @param value
     */
    public ModelDepartment(String name, String value){
        this.name = name;
        this.value = value;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     */
    public String getValue() {
        return value;
    }

    /**
     *
     * @param value
     */
    public void setValue(String value) {
        this.value = value;
    }
    
    public String toString(){
        return name + "(" + value + ")";
    }

    @Override
    public int compareTo(ModelDepartment o) {
        return Integer.parseInt(value) - Integer.parseInt(o.value);
    }
    
}
