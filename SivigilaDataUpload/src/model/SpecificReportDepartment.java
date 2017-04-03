/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.util.CellUtil;

/**
 *
 * @author Fabian
 */
public class SpecificReportDepartment extends Report {

    /**
     *
     */
    public static final int months[] = {0, 4, 8, 13, 17, 21, 26, 30, 34, 39, 43, 47, 52};
    private ModelDepartment town;
    private ModelDepartment department;
    private ModelDepartment event;
    private ArrayList<Integer> years;

    /**
     *
     * @param db
     * @param path
     * @param town
     * @param department
     * @param event
     */
    public SpecificReportDepartment(DataBase db, String path, ModelDepartment town, ModelDepartment department, ModelDepartment event) {
        super(db, path);
        this.town = town;
        this.department = department;
        this.event = event;
    }

    /**
     *
     * @param years
     * @return
     */
    public ArrayList<Integer> years(ResultSet years) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        try {
            while (years.next()) {
                list.add(years.getInt(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(SpecificReportDepartment.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    /**
     *
     * @param town
     * @param department
     * @param event
     * @param f
     */
    public void generateSpecificReport(ModelDepartment town, ModelDepartment department, ModelDepartment event, String f) {

    }

    /**
     *
     */
    @Override
    public void generate() {
        try {

            @SuppressWarnings("resource")
            HSSFSheet sheet = workbook.createSheet("Casos " + town.getName() + " municipio");
            HSSFRow fisrtRow = sheet.createRow(0);

            ResultSet result = db.executeSelect("select * from weekdata where id_event = " + event.getValue()
                    + " and id_town = '" + town.getValue() + "' and id_department = '" + department.getValue() + "'"
                    + "order by week,amount");
            ResultSet resultYears = db.executeSelect("select distinct year_data from weekdata where id_event = " + event.getValue()
                    + " and id_town = '" + town.getValue() + "' and id_department = '" + department.getValue() + "'"
                    + "order by year_data");

            years = years(resultYears);
            HashMap<Integer, Integer> indexYears = new HashMap<Integer, Integer>();

            for (int i = 0; i < years.size(); i++) {
                indexYears.put(years.get(i), i);
            }
            int temp = 0;
            int numberYears = years.size();

            int maxWeek = (db.executeSelect("select max(week) from weekdata where id_event = " + event.getValue()
                    + " and id_town = '" + town.getValue() + "' and id_department = '" + department.getValue() + "'")).getInt(1);

            int columns = 1 + numberYears * 3 + 6;

            Map<String, Object> map = new HashMap<String, Object>();
            map.put(CellUtil.BORDER_LEFT, CellStyle.BORDER_THIN);
            map.put(CellUtil.BORDER_RIGHT, CellStyle.BORDER_THIN);
            map.put(CellUtil.BORDER_BOTTOM, CellStyle.BORDER_THIN);
            map.put(CellUtil.BORDER_TOP, CellStyle.BORDER_THIN);

            firstLine(fisrtRow);//add first line report

            //create matrix report
            for (int i = 1; i <= maxWeek; i++) {
                sheet.createRow(i);
                sheet.getRow(i).createCell(0).setCellValue(i);
                for (int j = 1; j < columns; j++) {
                    sheet.getRow(i).createCell(j);

                }
            }
            autoSize(sheet, columns);
            while (result.next()) {
                int week = result.getInt("week");
                int year = result.getInt("year_data");
                int amount = result.getInt("amount");
                sheet.getRow(week).getCell(indexYears.get(year) + 1).setCellType(CellType.NUMERIC);
                sheet.getRow(week).getCell(indexYears.get(year) + 1).setCellValue(amount);
                CellUtil.setCellStyleProperties(sheet.getRow(week).getCell(indexYears.get(year) + 1), map);
            }

            int count = 1;
            for (int i = 1; i <= maxWeek; i++) {
                HSSFRow a = sheet.getRow(i);
                a.getCell(1 + numberYears).setCellType(CellType.FORMULA);
                //System.out.println("median(B"+ (i+1) + ":" + (char)('B'+years-1)+ ""+(i+1) +  ")");
                String next = nextColumn("B", numberYears - 1);
                a.getCell(1 + numberYears).setCellFormula("median(B" + (i + 1) + ":" + next + "" + (i + 1) + ")");
                a.getCell(2 + numberYears).setCellFormula("PERCENTILE(B" + (i + 1) + ":" + next + "" + (i + 1) + ",0.25)");
                a.getCell(3 + numberYears).setCellFormula("PERCENTILE(B" + (i + 1) + ":" + next + "" + (i + 1) + ",0.75)");
                temp = 0;
                if (count < months.length && i == months[count]) {
                    if (months[count] == 52 && maxWeek == 53) {
                        temp = 1;
                        a = sheet.getRow(i + 1);
                    }

                    next = "A";
                    for (int j = 4 + numberYears; j < 4 + numberYears * 2; j++) {
                        next = nextColumn(next, 1);
                        int first = months[count] - (months[count] - months[count - 1]) + 2;
                        a.getCell(j).setCellFormula("sum(" + next + "" + first + ":" + next + (i + 1 + temp) + ")");
                    }

                    next = nextColumn("A", numberYears + 4);
                    a.getCell(1 + numberYears * 2 + 3).setCellFormula("median(" + next + (i + 1 + temp) + ":" + nextColumn(next, numberYears - 1) + "" + (i + 1 + temp) + ")");
                    a.getCell(1 + numberYears * 2 + 4).setCellFormula("PERCENTILE(" + next + (i + 1 + temp) + ":" + nextColumn(next, numberYears - 1) + "" + (i + 1 + temp) + ",0.25)");
                    a.getCell(1 + numberYears * 2 + 5).setCellFormula("PERCENTILE(" + next + (i + 1 + temp) + ":" + nextColumn(next, numberYears - 1) + "" + (i + 1 + temp) + ",0.75)");
                    count++;

                    next = nextColumn("A", numberYears + 3);
                    String mean = nextColumn("A", 2 * numberYears + 4);
                    for (int j = 4 + numberYears; j < 4 + numberYears * 2; j++) {
                        next = nextColumn(next, 1);
                        System.out.println("SI(" + next + "" + (i + 1 + temp) + ">" + nextColumn("A", 4 + numberYears * 2) + "" + (i + 1 + temp) + ")");
                        a.getCell(j + numberYears + 3).setCellFormula("IF(" + next + "" + (i + 1 + temp) + ">" + nextColumn("A", 4 + numberYears * 2 + 2) + "" + (i + 1 + temp) + ",1,0)");
                    }
                }
            }

            eval.evaluateAll();

            FileOutputStream out = new FileOutputStream(path);
            workbook.write(out);
            out.close();

        } catch (SQLException ex) {
            Logger.getLogger(SpecificReportDepartment.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SpecificReportDepartment.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     *
     * @param row
     */
    @Override
    public void firstLine(HSSFRow row) {
        row.createCell(0).setCellValue("Semana Epidemiologica");
        int i = 1;
        for (Integer year : years) {
            row.createCell(i++).setCellValue(year);
        }
        row.createCell(i++).setCellValue("Mediana");
        row.createCell(i++).setCellValue("Q25");
        row.createCell(i++).setCellValue("Q75");
        for (Integer year : years) {
            row.createCell(i++, CellType.NUMERIC).setCellValue(year);
        }
        row.createCell(i++).setCellValue("Mediana");
        row.createCell(i++).setCellValue("Q25");
        row.createCell(i++).setCellValue("Q75");
        for (Integer year : years) {
            row.createCell(i++).setCellValue(year + "Brote");
        }
    }
}
