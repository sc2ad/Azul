package com.sc2ad.azul.util.paths;

/**
 * A Linear velocity curve {@link MotionPath}. This is one of the simplest {@link MotionPath}s.
 * It uses kinematic equations to determine position, speed, and acceleration based off of input.
 *
 * @author Sc2ad
 *
 */
public class LinearDerivativePath implements MotionPath {
    @SuppressWarnings("javadoc")
    private double velStart,velEnd,accel,distance,totalTime;

    /**
     * Large constructor, useful if everything (except time) is known.
     *
     * @param distance the distance to travel
     * @param v0 the initial velocity
     * @param v the final velocity
     * @param a the acceleration to use
     */
    public LinearDerivativePath(double distance, double v0, double v, double a) {
        velStart = v0;
        velEnd = v;
        accel = a;
        this.distance = distance;
        totalTime = getTotalTime();
    }
    /**
     * Useful constructor. Constructs the line from an initial and final velocity as well as acceleration.
     *
     * @param v0 the initial velocity
     * @param v the final velocity
     * @param a the acceleration to accelerate with
     * @throws IllegalArgumentException an acceleration of 0 was given
     */
    public LinearDerivativePath(double v0, double v, double a) {
        velStart = v0;
        velEnd = v;
        accel = a;
        if (accel == 0) {
            throw new IllegalArgumentException("This constructor requires an acceleration");
        }
        totalTime = getTotalTime();
    }
    /**
     * Useful constructor. Constructs the line without acceleration. Simply uses distance and velocity.
     *
     * @param distance the distance to travel
     * @param v the velocity to travel at
     */
    public LinearDerivativePath(double distance, double v) {
        velStart = v;
        velEnd = v;
        accel = 0;
        this.distance = distance;
        totalTime = getTotalTime();
    }
    @Override
    public MotionPath copy() {
        return new LinearDerivativePath(distance, velStart, velEnd, accel);
    }
    @Override
    public double getTotalTime() {
        if (totalTime != 0) {
            return totalTime;
        }
        if (accel == 0) {
            totalTime = distance / velStart;
            return totalTime;
        }
        // 0.5 at^2 + v0t = dx
        // v^2 = v0^2 + 2ax
        // v = v0 + at
        totalTime = (velEnd - velStart) / accel;
        return totalTime;
    }
    @Override
    public double getTotalDistance() {
        if (distance != 0) {
            return distance;
        }
        distance = getPosition(getTotalTime());
        return distance;
    }
    @Override
    public double getSpeed(double time) {
        return velStart + accel * time;
    }
    @Override
    public double getAccel(double time) {
        return accel;
    }
    @Override
    public double getPosition(double time) {
        return velStart * time + 0.5 * getAccel(time) * time * time; // not needed because doing already
    }
    @Override
    public boolean validate() {
        return true;
    }
}