/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.File;
import java.io.InputStream;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Fabian
 */
public class Reports {
    
    private DataBase db;
   
    public Reports( DataBase db ){
        this.db = db;
    }
    
    public float median( float row[] ){
        Arrays.sort(row);
        if( row.length %2 == 0)
            return (row[row.length/2]+row[row.length/2 + 1])/2;
        return row[row.length];
    }
    
    public void generateSpecificReport( ModelDepartment town, ModelDepartment department, ModelDepartment event, File f ){
        
        try {
            ResultSet result = db.executeSelect("select * from weekdata where id_event = " + event.getValue() +
                    " and id_town = '" + town.getValue() +"' and id_department = '"+ department.getValue() +"'"
            + "order by week,amount");
            int maxYear = (db.executeSelect("select max(year_data) from weekdata where id_event = " + event.getValue() +
                    " and id_town = '" + town.getValue() +"' and id_department = '"+ department.getValue() +"'" )).getInt(1);
            int minYear = (db.executeSelect("select min(year_data) from weekdata where id_event = " + event.getValue() +
                    " and id_town = '" + town.getValue() +"' and id_department = '"+ department.getValue() +"'" )).getInt(1);
            
            
            int years = maxYear - minYear + 1;
           
            int maxWeek = (db.executeSelect("select max(week) from weekdata where id_event = " + event.getValue() +
                    " and id_town = '" + town.getValue() +"' and id_department = '"+ department.getValue() +"'" )).getInt(1);
            
            int columns = 1 + years*3 + 6;  
            
            
            float matrix[][] = new float[maxWeek+1][columns];
            int sum[] = new int[maxWeek+1];
            for( int i = 0; i < matrix.length; i++ ){
                for( int j = 0; j < matrix[i].length; j++ ){
                    matrix[i][j] = -1;
                }
            } 
            
            while(result.next()){
                int week = result.getInt("week");
                int year = result.getInt("year_data");
                int amount = result.getInt("amount");
                matrix[week][year - minYear + 1] = amount;
                matrix[week][0] = week;
                sum[week]+=amount;
                //System.out.println(week + " " + year + " " + amount);
            }
            
            float median;
            for( int i = 1; i < matrix.length; i++ ){
                float row[] = new float[years];
                for( int j = 0; j < row.length; j++ ){
                    row[i] = matrix[i][j+1];
                }
                median = median(row);
                
                
            }
            
            
            
            for( int i = 1; i < matrix.length; i++ ){
                for( int j = 0; j < matrix[i].length; j++ ){
                    System.out.print(matrix[i][j] + " ");
                }
                System.out.println("");
            }
            
            
        } catch (SQLException ex) {
            Logger.getLogger(Reports.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
}
