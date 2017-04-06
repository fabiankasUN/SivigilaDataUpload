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

        public int getIndex() {
            return index;
        }

        public static String getMonth(int j) {
            if (j == 1) {
                return Enero.name();
            }
            if (j == 2) {
                return Febrero.name();
            }
            if (j == 3) {
                return Marzo.name();
            }
            if (j == 4) {
                return Abril.name();
            }
            if (j == 5) {
                return Mayo.name();
            }
            if (j == 6) {
                return Junio.name();
            }
            if (j == 7) {
                return Julio.name();
            }
            if (j == 8) {
                return Agosto.name();
            }
            if (j == 9) {
                return Septiembre.name();
            }
            if (j == 10) {
                return Octubre.name();
            }
            if (j == 11) {
                return Noviembre.name();
            }
            if (j == 12) {
                return Diciembre.name();
            }
            return null;
        }

    }

    private int id;
    private int startMonth;
    private int endMonth;
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
    public Climatic(int id, int startMonth, int endMonth, int startYear, int endYear, String name, String type) {
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
    public int getStartMonth() {
        return startMonth;
    }

    /**
     *
     * @param startMonth
     */
    public void setStartMonth(int startMonth) {
        this.startMonth = startMonth;
    }

    /**
     *
     * @return
     */
    public int getEndMonth() {
        return endMonth;
    }

    /**
     *
     * @param endMonth
     */
    public void setEndMonth(int endMonth) {
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
        if (startMonth < o.startMonth) {
            return -1;
        }
        if (startMonth > o.startMonth) {
            return 1;
        }
        if (endMonth < o.endMonth) {
            return -1;
        }
        if (endMonth > o.endMonth) {
            return 1;
        }
        return id - o.id;
    }
}
