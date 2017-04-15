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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import view.MainFrame;

/**
 *
 * @author Fabian
 */
public class Model {

    private DataBase db;
    private Report reporter;
    public static final int months[] = {0, 4, 8, 13, 17, 21, 26, 30, 34, 39, 43, 47, 52};

    /**
     *
     */
    public Model() {
        db = new DataBase();
    }

    /**
     *
     * @param line
     * @return
     */
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

    /**
     *
     */
    public void loadPopulation() {
        String dp = "", mp = "";
        try {
            File f = new File("data/population.csv");

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
            System.out.println("error population" + mp);
            //Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            System.out.println("error population" + dp + " " + mp);
            //Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     *
     */
    public void loadDepartments() {
        FileInputStream file = null;
        try {
            File f = new File("data/dep.csv");
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

    /**
     *
     */
    public void loadTown() {
        String idTown = "", id, name;
        String idDep = "";
        try {
            File f = new File("data/departments.csv");
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

    /**
     *
     * @param table
     * @return
     */
    public String prepareInsertClimatic(String table) {
        String line = "insert  into " + table + "(id,start_month,end_month,start_year,end_year,type,name) values (?,?,?,?,?,?,?)";
        return line;
    }

    /**
     *
     * @param table
     * @return
     */
    public String prepareInsert(String table) {
        String line = "insert  into " + table + "(id,name) values (?,?)";
        return line;
    }

    /**
     *
     * @param table
     * @return
     */
    public String prepareInsertTown(String table) {
        String line = "insert  into " + table + "(id,name,id_department) values (?,?,?)";
        return line;
    }

    /**
     *
     * @param table
     * @return
     */
    public String prepareInsertData(String table) {
        String line = "insert  into " + table + "(week,id_event,id_town,id_department,year_data,amount) values (?,?,?,?,?,?)";
        return line;
    }

    /**
     *
     * @param table
     * @return
     */
    public String prepareInsertDataYPerYear() {
        String line = "insert  into total_per_year (id_event,id_town,id_department,year_data,amount) values (?,?,?,?,?)";
        return line;
    }

    /**
     *
     * @param table
     * @return
     */
    public String prepareInsertDataYPerMonth() {
        String line = "insert  into total_per_month (id_month,id_event,id_town,id_department,year_data,amount) values (?,?,?,?,?,?)";
        return line;
    }

    /**
     *
     * @return
     */
    public String prepareUpdate() {
        String line = "insert into population (id_town,id_department,year_data,amount) values(?,?,?,?)";
        return line;
    }

    /**
     *
     * @param prepTown
     * @param townId
     * @param townName
     * @param departmentCode
     */
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

    /**
     *
     */
    public void loadDepartmentsAndTowns() {
        loadDepartments();
        loadTown();
    }

    /**
     *
     * @param f
     * @param year
     * @return
     */
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
               System.out.println(weeks);

            PreparedStatement prep = db.statement(prepareInsert("event"));
            PreparedStatement data = db.statement(prepareInsertData("weekdata"));
            PreparedStatement prepTown = db.statement(prepareInsertTown("town"));
            PreparedStatement dataPerYear = db.statement(prepareInsertDataYPerYear());
            PreparedStatement dataPerMonth = db.statement(prepareInsertDataYPerMonth());
            db.executeQuery("delete from weekdata where year_data = " + year + "");
            db.executeQuery("delete from total_per_year where year_data = " + year + "");
            db.executeQuery("delete from total_per_month where year_data = " + year + "");

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

                int sum = 0;
                int value;
                int count = 1;
                int sumMonth = 0;
                try {

                    for (int i = 6; i < weeks + 6; i++) {
                        value = Integer.parseInt(split[i]);
                        data.setInt(1, i - 5);
                        data.setInt(2, idEvent);
                        data.setString(3, townCode);
                        data.setString(4, departmentCode);
                        data.setInt(5, year);
                        data.setInt(6, value);
                        sum += value;
                        sumMonth += value;
                        data.addBatch();
                        if ((count < months.length && months[count] == i - 5) || i - 5 == 53) {
                            if (months[count] == 52 && weeks == 53) {
                                continue;
                            }
                            dataPerMonth.setInt(1, count);
                            dataPerMonth.setInt(2, idEvent);
                            dataPerMonth.setString(3, townCode);
                            dataPerMonth.setString(4, departmentCode);
                            dataPerMonth.setInt(5, year);
                            dataPerMonth.setInt(6, sumMonth);
                            dataPerMonth.addBatch();
                            count++;
                            sumMonth = 0;
                        }
                    }
                    data.executeBatch();

                    dataPerYear.setInt(1, idEvent);
                    dataPerYear.setString(2, townCode);
                    dataPerYear.setString(3, departmentCode);
                    dataPerYear.setInt(4, year);
                    dataPerYear.setInt(5, sum);
                    dataPerYear.addBatch();

                } catch (Exception e) {
                    addTown(prepTown, townCode, townName, departmentCode);
                    sumMonth = 0;
                    for (int i = 6; i < weeks + 6; i++) {
                        value = Integer.parseInt(split[i]);
                        data.setInt(1, i - 5);
                        data.setInt(2, idEvent);
                        data.setString(3, townCode);
                        data.setString(4, departmentCode);
                        data.setInt(5, year);
                        data.setInt(6, value);
                        sum += value;
                        sumMonth += value;
                        if ((count < months.length && months[count] == i - 5) || i - 5 == 53) {
                            if (count < months.length && months[count] == 52 && weeks == 53) {
                                continue;
                            }
                         
                            dataPerMonth.setInt(1, count);
                            dataPerMonth.setInt(2, idEvent);
                            dataPerMonth.setString(3, townCode);
                            dataPerMonth.setString(4, departmentCode);
                            dataPerMonth.setInt(5, year);
                            dataPerMonth.setInt(6, sumMonth);
                            dataPerMonth.addBatch();
                            count++;
                            sumMonth = 0;
                        }
                        data.addBatch();
                    }
                    dataPerYear.setInt(1, idEvent);
                    dataPerYear.setString(2, townCode);
                    dataPerYear.setString(3, departmentCode);
                    dataPerYear.setInt(4, year);
                    dataPerYear.setInt(5, sum);
                    dataPerYear.addBatch();
                    data.executeBatch();
                }
                c++;
                if (c % 1000 == 0) {
                    System.out.println(c);
                }
            }
            dataPerYear.executeBatch();
            dataPerMonth.executeBatch();
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

    /**
     *
     * @return
     */
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

    /**
     *
     * @return
     */
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

    /**
     *
     * @param dep
     * @return
     */
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

    /**
     *
     * @return
     */
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

    /**
     *
     * @param tableModel
     */
    public static void deleteRows(DefaultTableModel tableModel) {
        while (tableModel.getRowCount() > 0) {
            tableModel.removeRow(0);
        }
    }

    /**
     *
     * @param startMonth
     * @param endMonth
     * @param startYear
     * @param endYear
     * @param name
     * @param type
     * @return
     */
    public boolean addEvent(String startMonth, String endMonth, int startYear, int endYear, String name, String type) {

        try {
            ResultSet rMax = db.executeSelect("select max(id) from climatic_p");

            PreparedStatement prep = db.statement(prepareInsertClimatic("climatic_p"));
            prep.setInt(1, rMax.getInt(1) + 1);
            prep.setInt(2, Climatic.Month.valueOf(startMonth).index);
            prep.setInt(3, Climatic.Month.valueOf(endMonth).index);
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

    /**
     *
     * @param model
     */
    public void showListEvents(DefaultTableModel model) {
        ArrayList<Climatic> list = new ArrayList<Climatic>();
        try {
            while (model.getRowCount() > 0) {
                model.removeRow(0);
            }

            ResultSet set = db.executeSelect("select * from climatic_p");
            while (set.next()) {
                list.add(new Climatic(set.getInt(1), set.getInt(2), set.getInt(3), set.getInt(4), set.getInt(5),
                        set.getString(6), set.getString(7)));
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

    /**
     *
     * @param town
     * @param department
     * @param event
     * @param f
     */
    public void generateSpecificReport(ModelDepartment town, ModelDepartment department, ModelDepartment event, String f) {
        reporter = new SpecificReportDepartment(db, f, town, department, event);
        reporter.generate();
    }

    /**
     *
     * @param event
     * @param f
     */
    public void generateGeneralReport(ModelDepartment event, String f) {
        if (reporter == null || !(reporter instanceof GeneralReport)) {
            reporter = new GeneralReport(db, f, event);
        } else {
            ((GeneralReport) reporter).setData(f, event);
        }
        reporter.generate();
    }

    /**
     *
     * @param id
     * @param model
     */
    public void deleteFutureEvent(int id, DefaultTableModel model) {
        db.executeQuery("delete from climatic_p where id = " + id);

    }

}
