/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.File;

import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;

import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.table.DefaultTableModel;
import model.Model;
import model.ModelDepartment;
import view.MainFrame;

/**
 *
 * @author Fabian
 */
public class App {

    private static MainFrame frame;
    private static Model model;
    //private static 

    public static void main(String args[]) {
        if( model == null )
            model = new Model();
        if (frame == null) {
            try {
                for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (ClassNotFoundException ex) {
                java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (InstantiationException ex) {
                java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (javax.swing.UnsupportedLookAndFeelException ex) {
                java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }

            frame = new MainFrame();
            frame.setVisible(true);
        }
        
        loadDepartmentsAndTowns();
        loadPopulationTowns();

    }
    
    public static void loadDepartmentsAndTowns(){
        model.loadDepartmentsAndTowns();
    }
    
    public static void loadPopulationTowns(){
        model.loadPopulation();
    }
    
    public static boolean loadData( File f, int year ){
        return model.loadData(f, year);
    }
    
    public static void loadYears( JList list ){
        list.setListData(model.years());
    }
    
    
     public static void fillDepartments( JComboBox departments, JComboBox towns, JComboBox events,  JComboBox events2  ){
        
        departments.setModel(new DefaultComboBoxModel(model.getDepartments().toArray()));
        String first = ((ModelDepartment)departments.getSelectedItem()).getValue();
        towns.setModel(new DefaultComboBoxModel(model.getTowns(first).toArray()));
        ArrayList<ModelDepartment> mEvents = model.getEvents();
        events.setModel(new DefaultComboBoxModel(mEvents.toArray()));
        events2.setModel(new DefaultComboBoxModel(mEvents.toArray()));
    }
    
    public static void fillDepartmentsAndTowns( JComboBox departments, JComboBox towns ){
        
        String value = ((ModelDepartment)departments.getSelectedItem()).getValue();
        towns.setModel(new DefaultComboBoxModel(model.getTowns(value).toArray()));
        
    }
    
    
    public static void addEvents( String name, String type, String startMonth, String startYear, String
            endMonth, String endYear, DefaultTableModel model ){
        
    }
    
    public static void showListEvents( DefaultTableModel dataModel ){
        model.showListEvents(dataModel);
    }
    
    public static void generateSpecificReport( ModelDepartment town, ModelDepartment department, ModelDepartment event, String f ){
        model.generateSpecificReport(town, department, event, f);
    }
    
    public static void generateGeneralReport( ModelDepartment event, String f ){
        model.generateGeneralReport(event, f);
    }
    
    public static void addFutureEvent( String startMonth, String endMonth, int startYear, int endYear, String name, String type
    , DefaultTableModel tableModel ){
        
        model.addEvent(startMonth, endMonth, startYear, endYear, name, type);
        model.showListEvents(tableModel);
    } 
    
    public static void deleteFutureEvent( int id, DefaultTableModel tableModel ){
        model.deleteFutureEvent(id, tableModel );
        model.showListEvents(tableModel);
    }
    
    
}
