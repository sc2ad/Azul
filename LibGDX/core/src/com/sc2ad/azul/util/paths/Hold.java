package com.sc2ad.azul.util.paths;

/**
 * A class for simple flat lines.
 * An easier to use alternative over {@link LinearDerivativePath} as it does not need velocity.
 *
 * @author Sc2ad
 *
 */
public class Hold implements MotionPath {

    @SuppressWarnings("javadoc")
    double time, value;

    /**
     * Holds a 0 value for the given time
     *
     * @param time how long to hold 0 for
     */
    public Hold(double time) {
        this.time = time;
        value = 0;
    }
    /**
     * Holds a value for the given time
     *
     * @param time how long to hold the value for
     * @param value the value to hold
     */
    public Hold(double time, double value) {
        this.time = time;
        this.value = value;
    }
    public MotionPath copy() {
        return new Hold(time, value);
    }

    public double getSpeed(double time) {
        return 0;
    }

    public double getAccel(double time) {
        return 0;
    }

    public double getPosition(double time) {
        return value; // 0 for a hold
    }

    public double getTotalTime() {
        return time;
    }

    public double getTotalDistance() {
        return 0; // 0 for a hold!
    }

    public boolean validate() {
        // TODO this prob needs no false validate tho
        return true;
    }

}
