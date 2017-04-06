/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.Arrays;
import model.Climatic;

/**
 *
 * @author Fabian
 */
public class Test {
    
    public static double evaluate(final double[] values, final int begin,
            final int length, final double p) {

        if (length == 0) {
            return Double.NaN;
        }
        if (length == 1) {
            return values[begin]; // always return single value for n = 1
        }
        double n = length;
        double pos = p * (n + 1) / 100;
        double fpos = Math.floor(pos);
        int intPos = (int) fpos;
        double dif = pos - fpos;
        double[] sorted = new double[length];
        System.arraycopy(values, begin, sorted, 0, length);
        Arrays.sort(sorted);

        if (pos < 1) {
            return sorted[0];
        }
        if (pos >= n) {
            return sorted[length - 1];
        }
        double lower = sorted[intPos - 1];
        double upper = sorted[intPos];
        return lower + dif * (upper - lower);
    }
    
    public static long[] percentiles(long[] latencies, double... percentiles) {
    Arrays.sort(latencies, 0, latencies.length);
    long[] values = new long[percentiles.length];
    for (int i = 0; i < percentiles.length; i++) {
      int index = (int) (percentiles[i] * latencies.length);
      values[i] = latencies[index];
    }
    return values;
  }
    
    public static void main( String args[]){
        System.out.println(Test.evaluate(new double[]{3,0,0,1}, 0, 4, 75));
    }
}
