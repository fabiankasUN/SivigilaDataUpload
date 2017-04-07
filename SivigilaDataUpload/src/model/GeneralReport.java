package model;

import java.awt.Point;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FormulaEvaluator;

/**
 *
 * @author Fabian
 */
public class GeneralReport extends Report {

    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;
    public static final int PAGE_THREE = 2;
    public static final int PAGE_FOUR = 3;

    private ModelDepartment event;//evento del reporte
    private int minYear;//a;o minimo del reporte
    private int maxYear;//anio maximo del reporte
    private int lastTotalYear;//ultimo anio del total de anios del reporte
    private int lastTotalMonth;
    private ArrayList<ModelTown> towns;
    private HashMap<String, ModelDepartment> departments;

    /*
        hoja 1 -> Brotes por evento
        hoja 2 -> total casos por mes
        hoja 3 -> brotes por mes
        hoja 4 -> total casos por evento
     */
    private HSSFSheet sheets[];

    /**
     *
     * @param db database used
     * @param path path report
     * @param event it's the event for the report.
     */
    public GeneralReport(DataBase db, String path, ModelDepartment event) {
        super(db, path);
        this.event = event;
        getDepartmentsAndTowns();
    }

    public void setData(String path, ModelDepartment event) {
        workbook = new HSSFWorkbook();
        eval = workbook.getCreationHelper().createFormulaEvaluator();
        this.path = path;
        this.event = event;
    }

    /**
     * basado en el tamanio de las columnas las acomoda en el excel
     *
     */
    public void autoSize() {
        for (int i = 0; i < sheets.length; i++) {
            autoSize(sheets[i], sheets[i].getRow(0).getLastCellNum() + 1);
        }
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
                //int k = 0;
                while (resultTown.next()) {
                    //if (k++ == 50) {
                    //    break;
                    //}
                    if (set.contains(resultTown.getString(3) + resultTown.getString(1))) {
                        towns.add(new ModelTown(resultTown.getString(1), resultTown.getString(2), resultTown.getString(3)));
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(GeneralReport.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * inicializa las 4 paginas del reporte general
     *
     */
    public void createPages() {
        sheets = new HSSFSheet[4];
        sheets[0] = workbook.createSheet("# Brotes por evento");
        sheets[1] = workbook.createSheet("# Casos por mes");
        sheets[2] = workbook.createSheet("# Brotes por mes");
        sheets[3] = workbook.createSheet("# casos por evento");
    }

    /**
     * llena las 4 paginas con los departamentos
     *
     */
    public void fillDepartmentsAndTownsAllPages() {
        for (int i = 0; i < sheets.length; i++) {
            fillDataDepartmentsAndTowns(sheets[i]);
        }
    }

    /**
     *
     * llena la primera fila del excel
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
        lastTotalYear = 20;
        for (int i = minYear; i <= maxYear; i++) {
            lastTotalYear++;
            createCell(row, "Total casos " + i);
        }
    }

    public void firstLinePageThree(HSSFRow row) {
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
            for (int j = 1; j <= 12; j++) {
                createCell(row, j + "-" + i);
            }
        }
    }

    public void firstLinePerMonth(HSSFRow row) {
        createCell(row, "Nombre municipio");
        createCell(row, "Orden");
        createCell(row, "Departamento");
        createCell(row, "Codigo DANE");
        for (int i = 2005; i <= 2020; i++) {
            createCell(row, "PoblacionMunicipal " + i);
        }
        lastTotalMonth = 20;
        for (int i = minYear; i <= maxYear; i++) {
            for (int j = 1; j <= 12; j++) {
                lastTotalMonth++;
                createCell(row, "Total casos " + Climatic.Month.getMonth(j) + " de " + i);
            }
        }
    }

    public boolean isValid(Climatic c, int year, int month) {

        if (year > c.getStartYear() && year < c.getEndYear()) {
            return true;
        }
        if (year == c.getStartYear() && year < c.getEndYear() && month >= c.getStartMonth()) {
            return true;
        }
        if (year == c.getEndYear() && year > c.getStartYear() && month <= c.getEndMonth()) {
            return true;
        }
        if (year == c.getEndYear() && year == c.getStartYear() && month <= c.getEndMonth() && month >= c.getStartMonth()) {
            return true;
        }
        return false;
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

    /**
     * llena los eventos basado en los eventos futuros agregados
     *
     */
    public ArrayList<Climatic> FirstLineEvents() {
        ArrayList<Climatic> list = new ArrayList<Climatic>();
        try {

            ResultSet set = db.executeSelect("select * from climatic_p " + "order by type,start_year,start_month");
            while (set.next()) {
                list.add(new Climatic(set.getInt(1), set.getInt(2), set.getInt(3), set.getInt(4), set.getInt(5),
                        set.getString(6), set.getString(7)));
            }
        } catch (SQLException ex) {
            Logger.getLogger(GeneralReport.class.getName()).log(Level.SEVERE, null, ex);
        }
        String value;
        for (Climatic event : list) {
            value = event.getType() + " " + event.getName() + " " + event.getStartMonth() + "-" + event.getStartYear()
                    + " a " + event.getEndMonth() + "-" + event.getEndYear();
            for (int i = 0; i < sheets.length; i += 3) {
                if (i != 1 && i != 2) {
                    createCell(sheets[i].getRow(0), value);
                }
            }
        }
        return list;
    }

    /*public void fillEvents(ArrayList<Climatic> list) {

        int matrix[][] = new int[13][maxYear - minYear + 1];
        int c = 0;
        int p = 0;
        for (Climatic climatic : list) {
            //if (climatic.getId() == 7) {

            int id = 1;
            for (ModelTown town : towns) {

                ResultSet set = db.executeSelect("select * from total_per_Month where id_town = '" + town.getId() + "'"
                        + " and id_department = '" + town.getId_department() + "' and id_event = " + event.getValue()
                        + " order by id_month,year_data");

                try {

                    for (int i = 0; i < matrix.length; i++) {
                        matrix[i] = new int[maxYear - minYear + 1];
                    }

                    while (set.next()) {
                        matrix[set.getInt(1)][set.getInt(5) - minYear] = set.getInt(6);
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(GeneralReport.class.getName()).log(Level.SEVERE, null, ex);
                }

                HSSFWorkbook test = new HSSFWorkbook();
                FormulaEvaluator eval = test.getCreationHelper().createFormulaEvaluator();
                HSSFSheet sheet = test.createSheet("test");

                int index = matrix[0].length;
                for (int j = 1; j < matrix.length; j++) {
                    sheet.createRow(j - 1);
                    for (int k = 0; k < matrix[j].length; k++) {
                        sheet.getRow(j - 1).createCell(k).setCellValue(matrix[j][k]);
                    }
                    sheet.getRow(j - 1).createCell(index).setCellType(CellType.FORMULA);
                    sheet.getRow(j - 1).createCell(index + 1).setCellType(CellType.FORMULA);
                    sheet.getRow(j - 1).createCell(index + 2).setCellType(CellType.FORMULA);
                    String next = nextColumn("A", maxYear - minYear);

                    sheet.getRow(j - 1).getCell(index).setCellFormula("MEDIAN(A" + (j) + ":" + next + "" + (j) + ")");
                    sheet.getRow(j - 1).getCell(index + 1).setCellFormula("PERCENTILE(A" + (j) + ":" + next + "" + (j) + ",0.25)");
                    sheet.getRow(j - 1).getCell(index + 2).setCellFormula("PERCENTILE(A" + (j) + ":" + next + "" + (j) + ",0.75)");
                    next = "A";
                    for (int k = 0; k < matrix[j].length; k++) {
                        sheet.getRow(j - 1).createCell(index + 2 + k + 1).setCellFormula("IF(" + next + "" + (j) + ">" + nextColumn("A", index + 2) + "" + (j) + ",1,0)");
                        next = nextColumn(next, 1);
                    }
                }
                eval.clearAllCachedResultValues();
                eval.evaluateAll();
                int sum = 0;
                for (int j = 1; j < matrix.length; j++) {
                    for (int k = index + 3; k < index + 3 + matrix[j].length; k++) {
                        CellValue x = eval.evaluate(sheet.getRow(j - 1).getCell(k));
                        int y = (k - index + 3) + minYear;
                        if (isValid(climatic, y, j)) {
                            sum += x.getNumberValue();
                        }
                    }
                    createCell(sheet.getRow(j - 1), sum + "");
                }

                createCellNumber(sheets[0].getRow(id), sum, lastTotalYear + c);
                id++;
            }
            //}
            c++;
        }
    }*/
    public void fillEvents(ArrayList<Climatic> list) {

        int matrix[][] = new int[13][maxYear - minYear + 1];
        int c = 0;
        int id = 1;
        for (ModelTown town : towns) {

            ResultSet set = db.executeSelect("select * from total_per_Month where id_town = '" + town.getId() + "'"
                    + " and id_department = '" + town.getId_department() + "' and id_event = " + event.getValue()
                    + " order by id_month,year_data");
            try {

                for (int i = 0; i < matrix.length; i++) {
                    matrix[i] = new int[maxYear - minYear + 1];
                }

                while (set.next()) {
                    matrix[set.getInt(1)][set.getInt(5) - minYear] = set.getInt(6);
                }
            } catch (SQLException ex) {
                Logger.getLogger(GeneralReport.class.getName()).log(Level.SEVERE, null, ex);
            }

            HSSFWorkbook test = new HSSFWorkbook();
            FormulaEvaluator eval = test.getCreationHelper().createFormulaEvaluator();
            HSSFSheet sheet = test.createSheet("test");

            int index = matrix[0].length;
            for (int j = 1; j < matrix.length; j++) {
                sheet.createRow(j - 1);
                for (int k = 0; k < matrix[j].length; k++) {
                    sheet.getRow(j - 1).createCell(k).setCellValue(matrix[j][k]);
                }
                sheet.getRow(j - 1).createCell(index).setCellType(CellType.FORMULA);
                sheet.getRow(j - 1).createCell(index + 1).setCellType(CellType.FORMULA);
                sheet.getRow(j - 1).createCell(index + 2).setCellType(CellType.FORMULA);
                String next = nextColumn("A", maxYear - minYear);

                sheet.getRow(j - 1).getCell(index).setCellFormula("MEDIAN(A" + (j) + ":" + next + "" + (j) + ")");
                sheet.getRow(j - 1).getCell(index + 1).setCellFormula("PERCENTILE(A" + (j) + ":" + next + "" + (j) + ",0.25)");
                sheet.getRow(j - 1).getCell(index + 2).setCellFormula("PERCENTILE(A" + (j) + ":" + next + "" + (j) + ",0.75)");
                next = "A";
                for (int k = 0; k < matrix[j].length; k++) {
                    sheet.getRow(j - 1).createCell(index + 2 + k + 1).setCellFormula("IF(" + next + "" + (j) + ">" + nextColumn("A", index + 2) + "" + (j) + ",1,0)");
                    next = nextColumn(next, 1);
                }
            }
            eval.clearAllCachedResultValues();
            eval.evaluateAll();
            int sum = 0;
            /*
            for (int j = 1; j < matrix.length; j++) {
                for (int k = index + 3; k < index + 3 + matrix[j].length; k++) {
                    eval.evaluateInCell(sheet.getRow(j - 1).getCell(k));
                }
                createCell(sheet.getRow(j - 1), sum + "");
            }*/
            c = 0;
            for (Climatic climatic : list) {
                sum = 0;
                int sumTotal = 0;
                for (int j = 1; j < matrix.length; j++) {
                    for (int k = index + 3; k < index + 3 + matrix[j].length; k++) {
                        eval.evaluateInCell(sheet.getRow(j - 1).getCell(k));
                        int y = (k - (index + 3)) + minYear;
                        if (isValid(climatic, y, j)) {
                            sum += sheet.getRow(j - 1).getCell(k).getNumericCellValue();
                            sumTotal += sheet.getRow(j - 1).getCell(k - (index+3)).getNumericCellValue();
                        }
                    }
                }
                createCellNumber(sheets[0].getRow(id), sum, lastTotalYear + c);
                createCellNumber(sheets[3].getRow(id), sumTotal, lastTotalYear + c++);
            }

            for (int j = 1; j < matrix.length; j++) {
                for (int k = index + 3; k < index + 3 + matrix[j].length; k++) {
                    int idx = 20 + j - 1 + (k - (index + 3)) * 12;
                    createCellNumber(sheets[2].getRow(id), (int) sheet.getRow(j - 1).getCell(k).getNumericCellValue(), idx);
                }
            }

            id++;
        }
    }

    public void fillPopulation() {

        int i = 1;
        for (ModelTown town : towns) {
            try {
                ResultSet resultPopulation = db.executeSelect("select year_data,amount from population where id_town = '" + town.getId() + "'"
                        + " and id_department = '" + town.getId_department() + "'");
                ArrayList<Integer> list = new ArrayList<Integer>();
                while (resultPopulation.next()) {
                    list.add(resultPopulation.getInt(2));
                }
                for (int j = 0; j < sheets.length; j++) {
                    HSSFRow row = sheets[j].getRow(i);
                    for (Integer number : list) {
                        createCellNumber(row, number);
                    }
                }
                i++;

            } catch (SQLException ex) {
                Logger.getLogger(GeneralReport.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void fillAllCasesYear(HSSFSheet sheet[]) {
        int i = 1;
        Point a = null;
        for (ModelTown town : towns) {
            try {
                ResultSet resultPopulation = db.executeSelect("select year_data,amount from total_per_year where id_town = '" + town.getId() + "'"
                        + " and id_department = '" + town.getId_department() + "' and id_event = " + event.getValue()
                        + " order by year_data");
                ArrayList<Point> list = new ArrayList<Point>();
                while (resultPopulation.next()) {
                    list.add(new Point(resultPopulation.getInt(1), resultPopulation.getInt(2)));
                }
                for (int j = 0; j < sheet.length; j++) {
                    HSSFRow row = sheet[j].getRow(i);
                    int firstYear = minYear;
                    for (int k = 0; k < list.size(); k++) {
                        a = list.get(k);
                        createCellNumber(row, list.get(k).y, 20 + (list.get(k).x - minYear));
                    }
                }

                for (int j = 0; j < sheet.length; j++) {
                    HSSFRow row = sheet[j].getRow(i);
                    for (int k = 20; k < 20 + (maxYear - minYear + 1); k++) {
                        if (row.getCell(k) == null) {
                            createCellNumber(row, 0, k);
                        }
                    }
                }

                i++;

            } catch (SQLException ex) {
                Logger.getLogger(GeneralReport.class
                        .getName()).log(Level.SEVERE, null, ex);

            }
        }
    }

    public static class Data_per_month {

        int id;
        int year;
        int amount;

        public Data_per_month(int year, int id, int amount) {
            this.year = year;
            this.id = id;
            this.amount = amount;
        }

        @Override
        public String toString() {
            return id + " " + year + " " + amount;
        }

    }

    public void fillAllCasesMonth(HSSFSheet sheet[]) {
        int i = 1;
        for (ModelTown town : towns) {
            //if( i == 100)
            //    break;
            try {
                ResultSet resultPopulation = db.executeSelect("select year_data,id_month,amount from total_per_Month where id_town = '" + town.getId() + "'"
                        + " and id_department = '" + town.getId_department() + "' and id_event = " + event.getValue()
                        + " order by year_data,id_month");
                ArrayList<Data_per_month> list = new ArrayList<Data_per_month>();
                while (resultPopulation.next()) {
                    list.add(new Data_per_month(resultPopulation.getInt(1), resultPopulation.getInt(2), resultPopulation.getInt(3)));
                }
                if (list.size() > 0) {
                    for (int j = 0; j < sheet.length; j++) {
                        HSSFRow row = sheet[j].getRow(i);
                        for (int k = 0; k < list.size(); k++) {
                            createCellNumber(row, list.get(k).amount, 20 + (list.get(k).year - minYear) * 12 + list.get(k).id - 1);
                        }
                    }
                }
                for (int k = 20; k < 20 + (maxYear - minYear + 1) * 12; k++) {
                    if (sheet[0].getRow(i).getCell(k) == null) {
                        createCellNumber(sheet[0].getRow(i), 0, k);
                    }
                }
                i++;
            } catch (SQLException ex) {
                Logger.getLogger(GeneralReport.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     *
     */
    public void pageOne() {
        HSSFRow firstRow = sheets[0].createRow(0);
        firstLine(firstRow);

    }

    /**
     *
     */
    public void pageTwo() {
        HSSFRow firstRow = sheets[1].createRow(0);
        firstLinePerMonth(firstRow);

    }

    /**
     *
     */
    public void pageThree() {
        HSSFRow firstRow = sheets[2].createRow(0);
        firstLinePageThree(firstRow);

    }

    /**
     *
     */
    public void pageFour() {
        HSSFRow firstRow = sheets[3].createRow(0);
        firstLine(firstRow);

    }

    /**
     *
     */
    @Override
    public void generate() {
        long total = System.currentTimeMillis();
        long time = System.currentTimeMillis();
        createPages();
        System.out.println("Creating pages " + (System.currentTimeMillis() - time));
        time = System.currentTimeMillis();
        pageOne();
        pageTwo();
        pageThree();
        pageFour();
        System.out.println("Creating first Row all : " + (System.currentTimeMillis() - time));
        time = System.currentTimeMillis();
        ArrayList<Climatic> list = FirstLineEvents();
        System.out.println("first line events : " + (System.currentTimeMillis() - time));
        time = System.currentTimeMillis();
        autoSize();
        System.out.println("Autosize " + (System.currentTimeMillis() - time));
        time = System.currentTimeMillis();
        fillDepartmentsAndTownsAllPages();
        System.out.println("Fill departments " + (System.currentTimeMillis() - time));
        time = System.currentTimeMillis();
        fillPopulation();
        System.out.println("Fill population " + (System.currentTimeMillis() - time));
        time = System.currentTimeMillis();
        fillAllCasesYear(new HSSFSheet[]{sheets[0], sheets[3]});
        System.out.println("Fill cases year " + (System.currentTimeMillis() - time));
        time = System.currentTimeMillis();
        fillAllCasesMonth(new HSSFSheet[]{sheets[1]});
        System.out.println("Fill cases Month " + (System.currentTimeMillis() - time));

        time = System.currentTimeMillis();
        fillEvents(list);
        System.out.println("Fill events row " + (System.currentTimeMillis() - time));

        time = System.currentTimeMillis();
        save();
        System.out.println("save " + (System.currentTimeMillis() - time));

        System.out.println("Total : " + (System.currentTimeMillis() - total));
    }

}
