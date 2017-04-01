/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.FormulaEvaluator;

/**
 *
 * @author Fabian
 */
public abstract class Report {

    protected DataBase db;
    protected HSSFWorkbook workbook;
    protected FormulaEvaluator eval;
    protected String path;

    public Report(DataBase db, String path) {
        this.db = db;
        workbook = new HSSFWorkbook();
        eval = workbook.getCreationHelper().createFormulaEvaluator();
        this.path = path;
    }

    public String nextColumn(String value) {
        int last = -1;
        for (int i = 0; i < value.length(); i++) {
            if (value.charAt(i) != 'Z') {
                last = i;
            }
        }
        if (last == -1) {
            String a = "";
            for (int i = 0; i <= value.length(); i++) {
                a += "A";
            }
            return a;
        }
        String a = value.substring(0, last);
        a += (char) (value.charAt(last) + 1);
        for (int i = last + 1; i < value.length(); i++) {
            a += "A";
        }
        return a;
    }

    public String nextColumn(String value, int k) {
        String s = value;
        while (k-- > 0) {
            s = nextColumn(s);
        }
        return s;
    }

    public void save() {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(path);
            workbook.write(out);
            out.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Report.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Report.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public abstract void generate();

    public abstract void firstLine(HSSFRow row);

}
