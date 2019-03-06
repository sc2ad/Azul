package com.sc2ad.azul.util.paths;

/**
 * Provides handling for multiple MotionPaths combined together.
 * DOES NOT INTEGRATE ANY OF THE PROVIDED PATHS
 * It uses the positions from each path to construct the overall curve
 * Constructs each curve offset from the previous (maybe?)
 *
 * @author Sc2ad
 *
 */
public class CombinedPath implements MotionPath {
    /*
     * ENUM CLASSES
     */
    /**
     * Creates a {@link MotionPath} based off of a trapezoid's first derivative.
     *
     * @author Sc2ad
     *
     */
    public static class LongitudalTrapezoid extends CombinedPath {

        /**
         * Constructs the {@link MotionPath} using kinematics equations.
         *
         * @param start 	the start position for the curve (integral + C offset)
         * @param distance	the distance to travel with the overall curve (- for downwards)
         * @param maxV		the velocity to construct the trapezoidal lines with
         * @param a			the acceleration to construct the trapezoidal lines with
         */
        public LongitudalTrapezoid(double start, double distance, double maxV, double a) {
            super(start);
            MotionPath[] p = new MotionPath[3];
            p[0] = new LinearDerivativePath(0, maxV, a);
            p[2] = new LinearDerivativePath(maxV, 0, -a);
            // The cruise one is the one we know the LEAST about, need to use positions of others
            p[1] = new LinearDerivativePath(distance - 2 * p[0].getTotalDistance(), maxV);
            if (Math.abs(p[0].getTotalDistance()) > Math.abs(distance / 2)) {
                // Need to setup a triangle instead
                // 1/2at^2 = distance/2
                double newTime = Math.sqrt(distance / a);
                p[0] = new LinearDerivativePath(0, newTime * a, a); // don't question jankness
                p[1] = new Hold(0); // Legit, nothing
                p[2] = new LinearDerivativePath(newTime * a, 0, -a);
            }
            setPath(p);
        }
    }

    @SuppressWarnings("javadoc")
    private MotionPath[] paths;
    @SuppressWarnings("javadoc")
    private double travelledPathDistance, start, distance, totTime;

    /**
     * Construct a CombinedPath with simply a start.
     * Must be used in conjunction with {@link #setPath(MotionPath[] p) setPath}, otherwise NullPointerExceptions will occur.
     *
     * @param start the offset this path has (typically 0)
     */
    public CombinedPath(double start) {
        this.start = start;
    }
    /**
     * Standard construction.
     *
     * @param start the offset this path has (typically 0)
     * @param p the paths that make up this CombinedPath
     */
    public CombinedPath(double start, MotionPath... p) {
        paths = p;
        this.start = start;
    }
    /**
     * Returns the current {@link MotionPath} at the given time.
     *
     * @param time time since this class started
     * @return the {@link MotionPath} at the current time
     */
    private MotionPath getCurve(double time) {
        travelledPathDistance = 0;
        if (time <= paths[0].getTotalTime()) {
            return paths[0];
        }
        for (int i = 1; i < paths.length; i++) {
            double sum = paths[i].getTotalTime();
            double dsum = 0;
            for (int j=i-1; j >= 0; j--) {
                sum += paths[j].getTotalTime();
                dsum += paths[j].getTotalDistance();
            }
            if (time <= sum) {
                travelledPathDistance = dsum;
                return paths[i];
            }
        }
        return paths[paths.length-1]; // should never happen
    }

    /**
     * Returns the time since the current {@link MotionPath} has begun.
     *
     * @param time time since this class started
     * @return returns time since the last {@link MotionPath} has begun running
     */
    private double getDeltaTime(double time) {
        double maxSum = 0;
        if (time <= paths[0].getTotalTime()) {
            return time;
        }
        for (int i = 1; i < paths.length; i++) {
            double sum = 0;
            for (int j=i-1; j >= 0; j--) {
                sum += paths[j].getTotalTime();
            }
            maxSum = sum + paths[i].getTotalTime();
            if (time <= maxSum) {
                return time - sum;
            }
        }
        return 0; // should never happen
    }

    /**
     * Used to set the {@link MotionPath} array of this object.
     * @see #paths
     *
     * @param p the path array to set {@link #paths} to
     */
    public void setPath(MotionPath[] p) {
        paths = p;
    }

    public MotionPath copy() {
        MotionPath[] paf = new MotionPath[paths.length];
        for (int i = 0; i < paf.length; i++) {
            paf[i] = paths[i].copy();
        }
        return new CombinedPath(start, paf);
    }

    public double getSpeed(double time) {
        double dt = getDeltaTime(time);
        if (time > getTotalTime()) {
            return getCurve(time).getSpeed(dt + dt < getCurve(time).getTotalTime() ? getCurve(time).getTotalTime() : 0);
        }
        return getCurve(time).getSpeed(dt);
    }

    public double getAccel(double time) {
        double dt = getDeltaTime(time);
        return getCurve(time).getAccel(dt);
    }

    public double getPosition(double time) {
        double dt = getDeltaTime(time);
        if (time >= getTotalTime()) {
            return start+getTotalDistance();
        }
        return start+getCurve(time).getPosition(dt) + travelledPathDistance;
    }

    public double getTotalTime() {
        if (totTime != 0) {
            return totTime;
        }
        double sum = 0;
        for (MotionPath p : paths) {
            sum += p.getTotalTime();
        }
        totTime = sum;
        return sum;
    }

    @Override
    public double getTotalDistance() {
        if (distance != 0) {
            return distance;
        }
        double sum = 0;
        for (MotionPath p : paths) {
            sum += p.getTotalDistance();
        }
        distance = sum;
        return sum;
    }

    @Override
    public boolean validate() {
        return true;
    }
}