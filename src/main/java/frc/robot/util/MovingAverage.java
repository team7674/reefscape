package frc.robot.util;

//smoothes moving and whatnot
//written by wpi

public class MovingAverage {

    double[] values;
    int count = 0;

    public MovingAverage() {
        values = new double[100];
    }

    public MovingAverage(int length){
        values = new double[length];
    }

    public void reset() {
        values = new double[values.length];
        count = 0;
    }

    public void reset(int length) {
        values = new double[length];
        count = 0;
    }

    public void update(double value) {
        for (int i = values.length - 1; i > 0; i--) {
            values[i] = values[i-1];
        }
        values[0] = value;
        count++;
    }

    public double getAverage() {
        double max = values[0];
        double min = values[0];
        double total = 0;
        for (int i = 0; i < count && i < values.length; i++) {
            total += values[i];
            if (values[i] > max) max = values[i];
            if (values[i] < min) min = values[i];
        }
        
        if (count > 2){
            total -= max;
            total -= min;
            return total / (Math.min(values.length, count) - 2);
        }
        else
            return total / Math.min(values.length, count);
    }
    
    public double getAverage(int n) {
        double max = values[0];
        double min = values[0];
        double total = 0;
        for (int i = 0; i < count && i < n && i < values.length; i++) {
            total += values[i];
            if (values[i] > max) max = values[i];
            if (values[i] < min) min = values[i];
        }
        if (count > 2 && n > 2){
            total -= max;
            total -= min;
            return total / (Math.min(Math.min(values.length, count), n) - 2);
        }
        else
            return total / (Math.min(Math.min(values.length, count), n));
    }

    public int getLength() {
        return values.length;
    }
}