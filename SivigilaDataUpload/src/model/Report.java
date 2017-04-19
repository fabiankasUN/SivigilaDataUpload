/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.awt.Font;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Sheet;

/**
 *
 * @author Fabian
 */
public abstract class Report {

    /**
     *
     */
    protected DataBase db;

    /**
     *
     */
    protected HSSFWorkbook workbook;

    /**
     *
     */
    protected FormulaEvaluator eval;

    /**
     *
     */
    protected String path;

    /**
     *
     * @param db
     * @param path
     */
    public Report(DataBase db, String path) {
        this.db = db;
        workbook = new HSSFWorkbook();
        eval = workbook.getCreationHelper().createFormulaEvaluator();
        this.path = path;
    }

    /**
     *
     * @param value
     * @return
     */
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

    /**
     *
     * @param value
     * @param k
     * @return
     */
    public String nextColumn(String value, int k) {
        String s = value;
        while (k-- > 0) {
            s = nextColumn(s);
        }
        return s;
    }

    /**
     *
     * @param row
     * @param value
     */
    public HSSFCell createCell(HSSFRow row, String value) {
        if (row.getLastCellNum() == -1) {
            HSSFCell cell = row.createCell(0);
            cell.setCellValue(value);
            return cell;
        } else {
            HSSFCell cell = row.createCell(row.getLastCellNum());
            cell.setCellValue(value);
            return cell;
        }
    }

    public HSSFCell createCellNumber(HSSFRow row, int value) {
        if (row.getLastCellNum() == -1) {
            HSSFCell cell = row.createCell(0);
            cell.setCellType(CellType.NUMERIC);
            cell.setCellValue(value);
            return cell;
        } else {
            HSSFCell cell = row.createCell(row.getLastCellNum());
            cell.setCellType(CellType.NUMERIC);
            cell.setCellValue(value);
            return cell;
        }
    }

    public HSSFCell createCellNumber(HSSFRow row, int value, int col) {
        HSSFCell cell = row.createCell(col);
        cell.setCellType(CellType.NUMERIC);
        cell.setCellValue(value);
        return cell;

    }

    public void colorCell(HSSFCell cell, short color) {
        CellStyle style = workbook.createCellStyle();
        style.setFillBackgroundColor(color);
        style.setFillPattern(FillPatternType.NO_FILL);

        cell.setCellStyle(style);
    }

    /**
     *
     * @param sheet
     * @param columns
     */
    public void autoSize(Sheet sheet, int columns) {
        for (int i = 0; i < columns; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    /**
     *
     */
    public void save() {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(path);
            workbook.write(out);
            out.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Report.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(Report.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public void save( HSSFWorkbook workbook ) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(path);
            workbook.write(out);
            out.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Report.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(Report.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     *
     */
    public abstract void generate();

    /**
     *
     * @param row
     */
    public abstract void firstLine(HSSFRow row);

}
