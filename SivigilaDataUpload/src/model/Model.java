/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import view.MainFrame;

/**
 *
 * @author Fabian
 */
public class Model {

    private DataBase db;
    private Report reporter;

    public Model() {
        db = new DataBase();
    }

    public int countWeeks(String line) {
        int count = 0;

        String split[] = line.split(";");
        for (int i = 0; i < split.length; i++) {
            split[i] = split[i].toLowerCase();
            if (split[i].contains("semana")) {
                count++;
            }
        }

        return count;
    }

    public void loadPopulation() {
        FileInputStream file = null;
        String dp="", mp="";
        try {
            File f = new File("population.csv");
            file = new FileInputStream(f);
            BufferedReader in = new BufferedReader(new FileReader(f));
            String line;
            in.readLine();
            PreparedStatement prep = db.statement(prepareUpdate());
            in.readLine();
            in.readLine();
            
            while ((line = in.readLine()) != null) {
                String split[] = line.split(";");
                dp = split[0];
               
                mp = split[2].substring(2, 5);
                for (int i = 0; i < 16; i++) {
                    prep.setString(1, mp);
                    prep.setString(2, dp);
                    prep.setInt(3, 2005 + i);
                    prep.setInt(4, Integer.parseInt(split[4 + i].replaceAll("\\.", "")));
                    prep.addBatch();
                }
                prep.executeBatch();
            }
            
             db.getConnection().commit();
        } catch (FileNotFoundException ex) {
            System.out.println("error population" + mp);
            //Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            System.out.println("error population" +mp);
            //Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            System.out.println("error population" + dp + " " + mp);
            //Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        }
        
       
    }

    public void loadDepartments() {
        FileInputStream file = null;
        try {
            File f = new File("dep.csv");
            file = new FileInputStream(f);
            BufferedReader in = new BufferedReader(new FileReader(f));
            String line;
            in.readLine();
            PreparedStatement prep = db.statement(prepareInsert("department"));
            while ((line = in.readLine()) != null) {
                String split[] = line.split(",");
                String id = split[0].replaceAll("\"", "");
                String name = split[1].trim().replaceAll("\"", "");
                prep.setString(1, id);
                prep.setString(2, name);
                prep.addBatch();
            }
            prep.executeBatch();
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            //Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }

    public void loadTown() {
        String idTown = "", id, name;
        String idDep = "";
        try {
            File f = new File("departments.csv");
            BufferedReader in = new BufferedReader(new FileReader(f));

            String line;
            in.readLine();
            PreparedStatement prepTown = db.statement(prepareInsertTown("town"));
            while ((line = in.readLine()) != null) {
                String split[] = line.split(";");
                name = split[0];
                id = split[2];
                if (id.length() == 5) {
                    idTown = id.substring(2, 5);
                    idDep = id.substring(0, 2);
                } else {
                    idTown = id.substring(1, 4);
                    idDep = "0" + id.substring(0, 1);
                }
                //System.out.println( idTown + " " + idDep);
                prepTown.setString(1, idTown);
                prepTown.setString(2, name);
                prepTown.setString(3, idDep);

                prepTown.addBatch();
                //prepTown.executeBatch();
            }

            prepTown.executeBatch();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            //System.out.println(idTown +" " +  idDep);
            //Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String prepareInsertClimatic(String table) {
        String line = "insert  into " + table + "(id,start_month,end_month,start_year,end_year,type,name) values (?,?,?,?,?,?,?)";
        return line;
    }

    public String prepareInsert(String table) {
        String line = "insert  into " + table + "(id,name) values (?,?)";
        return line;
    }

    public String prepareInsertTown(String table) {
        String line = "insert  into " + table + "(id,name,id_department) values (?,?,?)";
        return line;
    }

    public String prepareInsertData(String table) {
        String line = "insert  into " + table + "(week,id_event,id_town,id_department,year_data,amount) values (?,?,?,?,?,?)";
        return line;
    }

    public String prepareUpdate() {
        String line = "insert into population (id_town,id_department,year_data,amount) values(?,?,?,?)";
        return line;
    }

    public void addTown(PreparedStatement prepTown, String townId, String townName, String departmentCode) {
        try {
            prepTown.setString(1, townId);
            prepTown.setString(2, townName);
            prepTown.setString(3, departmentCode);
            prepTown.addBatch();
            try {
                prepTown.executeBatch();
            } catch (Exception e) {
            }
        } catch (SQLException ex) {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadDepartmentsAndTowns() {
        loadDepartments();
        loadTown();
    }

    public boolean loadData(File f, int year) {

        String departmentCode = null;
        String townCode = null;
        String departmentName = null;
        String townName;
        int idEvent = 0;
        String nameEvent;
        int c = 0;
        try {
            FileInputStream file = null;
            file = new FileInputStream(f);
            BufferedReader in = new BufferedReader(new FileReader(f));
            int weeks = 0;
            String line;
            while ((line = in.readLine()) != null) {
                line = line.toLowerCase();
                if (line.contains("semana")) {
                    weeks = countWeeks(line);
                    break;
                }
            }

            PreparedStatement prep = db.statement(prepareInsert("event"));
            PreparedStatement data = db.statement(prepareInsertData("weekdata"));
            PreparedStatement prepTown = db.statement(prepareInsertTown("town"));

            db.executeQuery("delete from weekdata where year_data = " + year + "");
            db.getConnection().commit();
            while ((line = in.readLine()) != null) {
                String split[] = line.split(";");
                if (split[0].equals("Total general")) {
                    break;
                }
                if (split[1].equals("Total")) {
                    continue;
                }

                if (!split[0].equals("")) {
                    idEvent = Integer.parseInt(split[0]);
                    nameEvent = split[1];
                    prep.setInt(1, idEvent);
                    prep.setString(2, nameEvent);
                    prep.addBatch();
                    try {
                        prep.executeBatch();
                    } catch (Exception e) {
                    }
                }
                if (!split[2].equals("")) {
                    departmentCode = split[2].substring(0, 2);
                    departmentName = split[3];
                }

                townCode = split[4];
                townName = split[5];
                /*addTown(prepTown, townCode, townName, departmentCode);
                if (townCode.equals("000")) {
                    addTown(prepTown, "000", townName, departmentCode);
                }
                if (townCode.equals("001")) {
                    addTown(prepTown, "001", departmentName, departmentCode);
                }
                if (departmentCode.equals("11")) {
                    addTown(prepTown, townCode, townName, departmentCode);
                }
                if (departmentCode.equals("01")) {
                    addTown(prepTown, townCode, townName, departmentCode);
                }*/
                try {
                    for (int i = 6; i < weeks + 6; i++) {
                        data.setInt(1, i - 5);
                        data.setInt(2, idEvent);
                        data.setString(3, townCode);
                        data.setString(4, departmentCode);
                        data.setInt(5, year);
                        data.setString(6, split[i]);
                        data.addBatch();
                    }
                    data.executeBatch();
                } catch (Exception e) {
                    addTown(prepTown, townCode, townName, departmentCode);
                    for (int i = 6; i < weeks + 6; i++) {
                        data.setInt(1, i - 5);
                        data.setInt(2, idEvent);
                        data.setString(3, townCode);
                        data.setString(4, departmentCode);
                        data.setInt(5, year);
                        data.setString(6, split[i]);
                        data.addBatch();
                    }
                    data.executeBatch();
                }
                c++;
                if (c % 1000 == 0) {
                    System.out.println(c);
                }
            }

            //data.executeBatch();
            db.getConnection().commit();
            return true;

        } catch (FileNotFoundException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public Object[] years() {
        Object data[] = null;
        try {
            ResultSet set = db.executeSelect("select DISTINCT  year_data from weekdata");
            ArrayList<Integer> list = new ArrayList<>();
            while (set.next()) {
                list.add(set.getInt("year_data"));
            }
            Collections.sort(list);
            data = new Object[list.size()];
            for (int i = 0; i < data.length; i++) {
                data[i] = list.get(i);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        }
        return data;
    }

    public ArrayList<ModelDepartment> getDepartments() {

        ArrayList<ModelDepartment> list = new ArrayList<ModelDepartment>();
        try {
            ResultSet set = db.executeSelect("select * from department");
            while (set.next()) {
                list.add(new ModelDepartment(set.getString("name"), set.getString("id")));
            }
            Collections.sort(list);
        } catch (SQLException ex) {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public ArrayList<ModelDepartment> getTowns(String dep) {
        ArrayList<ModelDepartment> list = new ArrayList<ModelDepartment>();
        try {
            ResultSet set = db.executeSelect("select * from town where id_department = '"
                    + dep + "'");
            while (set.next()) {
                list.add(new ModelDepartment(set.getString("name"), set.getString("id")));
            }
            Collections.sort(list);
        } catch (SQLException ex) {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public ArrayList<ModelDepartment> getEvents() {
        ArrayList<ModelDepartment> list = new ArrayList<ModelDepartment>();
        try {
            ResultSet set = db.executeSelect("select * from event");
            while (set.next()) {
                list.add(new ModelDepartment(set.getString("name"), set.getString("id")));
            }
            Collections.sort(list);
        } catch (SQLException ex) {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public static void deleteRows(DefaultTableModel tableModel) {
        while (tableModel.getRowCount() > 0) {
            tableModel.removeRow(0);
        }
    }

    public boolean addEvent(String startMonth, String endMonth, int startYear, int endYear, String name, String type) {

        try {
            ResultSet rMax = db.executeSelect("select max(id) from climatic_p");

            PreparedStatement prep = db.statement(prepareInsertClimatic("climatic_p"));
            prep.setInt(1, rMax.getInt(1) + 1);
            prep.setString(2, startMonth);
            prep.setString(3, endMonth);
            prep.setInt(4, startYear);
            prep.setInt(5, endYear);
            prep.setString(6, type);
            prep.setString(7, name);
            prep.addBatch();
            prep.executeBatch();
            db.getConnection().commit();

            return true;
        } catch (SQLException ex) {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }

    public void showListEvents(DefaultTableModel model) {
        ArrayList<Climatic> list = new ArrayList<Climatic>();
        try {
            while (model.getRowCount() > 0) {
                model.removeRow(0);
            }

            ResultSet set = db.executeSelect("select * from climatic_p");
            while (set.next()) {
                list.add(new Climatic(set.getInt(1), set.getString(2).toString(), set.getString(3).toString(), set.getInt(4), set.getInt(5),
                        set.getString(6).toString(), set.getString(7).toString()));
            }

            for (int i = 0; i < list.size(); i++) {
                Vector v = new Vector();
                v.add(list.get(i).getId());
                v.add(list.get(i).getName());
                v.add(list.get(i).getType());
                v.add(list.get(i).getStartMonth() + "-" + list.get(i).getStartYear());
                v.add(list.get(i).getEndMonth() + "-" + list.get(i).getEndYear());
                model.addRow(v);
            }

        } catch (SQLException ex) {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void generateSpecificReport(ModelDepartment town, ModelDepartment department, ModelDepartment event, String f) {
        reporter = new SpecificReportDepartment(db, f,town,department, event);
        reporter.generate();
    }

    public void deleteFutureEvent(int id, DefaultTableModel model) {
        db.executeQuery("delete from climatic_p where id = " + id);

    }

}
