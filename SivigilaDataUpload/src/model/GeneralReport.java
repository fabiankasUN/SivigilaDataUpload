package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;

/**
 *
 * @author Fabian
 */
public class GeneralReport extends Report {

    private ModelDepartment event;
    private int minYear;
    private int maxYear;
    private ArrayList<ModelTown> towns;
    private HashMap<String, ModelDepartment> departments;

    private HSSFSheet sheetOne;
    private HSSFSheet sheetTwo;
    private HSSFSheet sheetThree;
    private HSSFSheet sheetFour;

    /**
     *
     * @param db database used
     * @param path path report
     * @param event it's the event for the report.
     */
    public GeneralReport(DataBase db, String path, ModelDepartment event) {
        super(db, path);
        this.event = event;

    }

    /**
     * guarda los departamentos y los municipios en memoria
     *
     */
    public void getDepartmentsAndTowns() {
        if (towns == null && departments == null) {
            try {
                ResultSet resultDep = db.executeSelect("select * from department");
                ResultSet resultTown = db.executeSelect("select * from town order by name");
                ResultSet resultPop = db.executeSelect("select DISTINCT id_town,id_department from population");
                HashSet<String> set = new HashSet<String>();
                while (resultPop.next()) {
                    set.add(resultPop.getString(2) + resultPop.getString(1));
                }
                departments = new HashMap<>();
                towns = new ArrayList<ModelTown>();
                while (resultDep.next()) {
                    departments.put(resultDep.getString(1), new ModelDepartment(resultDep.getString(2), resultDep.getString(1)));
                }

                while (resultTown.next()) {
                    if (set.contains(resultTown.getString(3) + resultTown.getString(1))) {
                        towns.add(new ModelTown(resultTown.getString(1), resultTown.getString(2), resultTown.getString(3)));
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(GeneralReport.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void createPages() {
        sheetOne = workbook.createSheet("# Brotes");
        sheetTwo = workbook.createSheet("# Brotes por mes");
        sheetThree = workbook.createSheet("# casos");
        sheetFour = workbook.createSheet("# casos por mes");
    }

    public void fillDepartmentsAndTownsAllPages() {
        fillDataDepartmentsAndTowns(sheetOne);
        fillDataDepartmentsAndTowns(sheetTwo);
        fillDataDepartmentsAndTowns(sheetThree);
        fillDataDepartmentsAndTowns(sheetFour);
    }

    /**
     *
     * @param row
     */
    @Override
    public void firstLine(HSSFRow row) {
        createCell(row, "Nombre municipio");
        createCell(row, "Orden");
        createCell(row, "Departamento");
        createCell(row, "Codigo DANE");
        for (int i = 2005; i <= 2020; i++) {
            createCell(row, "PoblacionMunicipal " + i);
        }

        ResultSet resultYears = db.executeSelect("select distinct year_data from weekdata where id_event = " + event.getValue()
                + " order by year_data");
        try {
            minYear = resultYears.getInt(1);
            while (resultYears.next()) {
                maxYear = resultYears.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(GeneralReport.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (int i = minYear; i <= maxYear; i++) {
            createCell(row, "Total casos " + i);
        }

    }

    public void fillDataDepartmentsAndTowns(HSSFSheet sheet) {

        int i = 1;
        for (ModelTown town : towns) {
            HSSFRow row = sheet.createRow(i);
            createCell(row, town.getName());
            createCell(row, i + "");
            createCell(row, departments.get(town.getId_department()).getName());
            createCell(row, town.getId_department() + town.getId());
            i++;
        }
    }

    public void fillPopulation(HSSFSheet sheet[]) {

        int i = 1;
        for (ModelTown town : towns) {
            try {
                ResultSet resultPopulation = db.executeSelect("select year_data,amount from population where id_town = '" + town.getId() + "'"
                        + " and id_department = '" + town.getId_department() + "'");
                ArrayList<Integer> list = new ArrayList<Integer>();
                while (resultPopulation.next()) {
                    list.add(resultPopulation.getInt(2));
                }
                for (int j = 0; j < sheet.length; j++) {
                    HSSFRow row = sheet[j].getRow(i);
                    for (Integer number : list) {
                        createCellNumber(row, number);
                    }
                }
                i++;
            } catch (SQLException ex) {
                Logger.getLogger(GeneralReport.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void fillAllCasesYear(HSSFSheet sheet) {
        int i = 1;
        for (ModelTown town : towns) {
            //if( i == 100)
            //    break;
            try {
                HSSFRow row = sheet.getRow(i);
                int firstYear = minYear;
                ResultSet resultPopulation = db.executeSelect("select year_data,amount from total_per_year where id_town = '" + town.getId() + "'"
                        + " and id_department = '" + town.getId_department() + "' and id_event = " + event.getValue()
                        + " order by year_data");
                while (resultPopulation.next()) {
                    createCellNumber(row, resultPopulation.getInt(2), 20 + (resultPopulation.getInt(1) - minYear));
                }

                i++;
            } catch (SQLException ex) {
                Logger.getLogger(GeneralReport.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     *
     */
    public void pageOne() {
        HSSFRow firstRow = sheetOne.createRow(0);
        firstLine(firstRow);
        fillAllCasesYear(sheetOne);
        autoSize(sheetOne, sheetOne.getRow(0).getLastCellNum() + 1);
    }

    /**
     *
     */
    public void pageTwo() {
        HSSFRow firstRow = sheetTwo.createRow(0);
        firstLine(firstRow);

        autoSize(sheetTwo, sheetTwo.getRow(0).getLastCellNum() + 1);
    }

    /**
     *
     */
    public void pageThree() {
        HSSFRow firstRow = sheetThree.createRow(0);
        firstLine(firstRow);

        autoSize(sheetThree, sheetThree.getRow(0).getLastCellNum() + 1);
    }

    /**
     *
     */
    public void pageFour() {
        HSSFRow firstRow = sheetFour.createRow(0);
        firstLine(firstRow);

        autoSize(sheetFour, sheetFour.getRow(0).getLastCellNum() + 1);
    }

    /**
     *
     */
    @Override
    public void generate() {

        getDepartmentsAndTowns();
        createPages();
        fillDepartmentsAndTownsAllPages();
        fillPopulation(new HSSFSheet[]{sheetOne, sheetTwo, sheetThree, sheetFour});

        pageOne();
        pageTwo();
        pageThree();
        pageFour();

        save();
    }

}
