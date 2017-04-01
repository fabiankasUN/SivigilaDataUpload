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
import org.apache.poi.hssf.usermodel.HSSFSheet;

/**
 *
 * @author Fabian
 */
public class GeneralReport extends Report {

    public GeneralReport(DataBase db, String path) {
        super(db, path);
    }

    public void generateGeneralReport(ModelDepartment event, String f) {

    }

    @Override
    public void generate() {
        FileOutputStream out = null;

        HSSFSheet sheet = workbook.createSheet("# Brotes");
        HSSFRow fisrtRow = sheet.createRow(0);

        
        
        save();
    }

    @Override
    public void firstLine(HSSFRow row) {

    }

}
