/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Fabian
 */
public class Climatic implements Comparable<Climatic> {

    public enum Month {
        Enero(1), Febrero(2), Marzo(3), Abril(4), Mayo(5), Junio(6),
        Julio(7), Agosto(8), Septiembre(9), Octubre(10), Noviembre(11), Diciembre(12);
        public final int index;

        Month(int index) {
            this.index = index;
        }
    }

    private int id;
    private String startMonth;
    private String endMonth;
    private int startYear;
    private int endYear;
    private String name;
    private String type;

    /**
     *
     * @param id
     * @param startMonth
     * @param endMonth
     * @param startYear
     * @param endYear
     * @param name
     * @param type
     */
    public Climatic(int id, String startMonth, String endMonth, int startYear, int endYear, String name, String type) {
        this.id = id;
        this.startMonth = startMonth;
        this.endMonth = endMonth;
        this.startYear = startYear;
        this.endYear = endYear;
        this.name = name;
        this.type = type;

    }

    /**
     *
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public String getStartMonth() {
        return startMonth;
    }

    /**
     *
     * @param startMonth
     */
    public void setStartMonth(String startMonth) {
        this.startMonth = startMonth;
    }

    /**
     *
     * @return
     */
    public String getEndMonth() {
        return endMonth;
    }

    /**
     *
     * @param endMonth
     */
    public void setEndMonth(String endMonth) {
        this.endMonth = endMonth;
    }

    /**
     *
     * @return
     */
    public int getStartYear() {
        return startYear;
    }

    /**
     *
     * @param startYear
     */
    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    /**
     *
     * @return
     */
    public int getEndYear() {
        return endYear;
    }

    /**
     *
     * @param endYear
     */
    public void setEndYear(int endYear) {
        this.endYear = endYear;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     *
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int compareTo(Climatic o) {
        if (startYear < o.startYear) {
            return -1;
        }
        if (startYear > o.startYear) {
            return 1;
        }
        if (endYear < o.endYear) {
            return -1;
        }
        if (endYear > o.endYear) {
            return 1;
        }
        if (Month.valueOf(startMonth).index < Month.valueOf(o.startMonth).index) {
            return -1;
        }
        if (Month.valueOf(startMonth).index > Month.valueOf(o.startMonth).index) {
            return 1;
        }
        if (Month.valueOf(endMonth).index < Month.valueOf(o.endMonth).index) {
            return -1;
        }
        if (Month.valueOf(endMonth).index > Month.valueOf(o.endMonth).index) {
            return 1;
        }
        return id - o.id;
    }
}
