package com.sc2ad.azul.util.paths;

/**
 * The overall MotionPath interface. This could also be rewritten to an abstract class.
 * This makes creating new paths trivial. One can also visualize any MotionPath.
 *
 * @author Sc2ad
 *
 */
public interface MotionPath {
    /**
     * Copies the current MotionPath. This serves as a 'copy constructor'
     *
     * @return the copied {@link MotionPath}
     */
    public MotionPath copy();
    /**
     * Returns the speed at any given time along the {@link MotionPath}.
     *
     * @param time the time at which to find the speed
     * @return the speed at the given time
     */
    public double getSpeed(double time);
    /**
     * Returns the acceleration at any given time along the {@link MotionPath}.
     *
     * @param time the time at which to find the acceleration
     * @return the acceleration at the given time
     */
    public double getAccel(double time);
    /**
     * Returns the <i>real</i> position at any given time along the {@link MotionPath}.
     *
     * @param time the time at which to find the position
     * @return the real position at the given time
     */
    public double getPosition(double time);
    /**
     * Returns the total time for the {@link MotionPath} to complete.
     *
     * @return the total time for the {@link MotionPath} to complete
     */
    public double getTotalTime();
    /**
     * POTENTIALLY MISLEADING
     * Returns the total distance traveled along the {@link MotionPath} at the end.
     *
     * @return the total distance traveled
     */
    public double getTotalDistance(); // Basically the same for all paths
    /**
     * UNUSED
     *
     * @return if the {@link MotionPath} is valid
     */
    public boolean validate();
}