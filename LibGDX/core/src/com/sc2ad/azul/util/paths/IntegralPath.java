package com.sc2ad.azul.util.paths;

public class IntegralPath implements MotionPath {
    private MotionPath speedPath;
    private double position;
    private double lastTime;
    // This acts as an integral of the speed path provided

    public IntegralPath(MotionPath speedPath) {
        this.speedPath = speedPath;
        position = 0;
        lastTime = 0;
    }
    public IntegralPath(double start, MotionPath speedPath) {
        this.speedPath = speedPath;
        position = start;
        lastTime = 0;
    }

    @Override
    public MotionPath copy() {
        return new IntegralPath(position, speedPath.copy());
    }
    @Override
    public double getSpeed(double time) {
        return speedPath.getPosition(time);
    }
    @Override
    public double getAccel(double time) {
        return speedPath.getSpeed(time);
    }
    @Override
    public double getPosition(double time) {
        // x + v0t + 1/2at^2
        position += getSpeed(time) * (time - lastTime);
        lastTime = time;
        return position;
//		return getSpeed(0) * time + getSpeed(time) * time;
//		return getSpeed(0) * time + getSpeed(time) * time + getAccel(time) * time; // redundant because it already happens
    }
    @Override
    public double getTotalTime() {
        return speedPath.getTotalTime();
    }
    @Override
    public double getTotalDistance() {
        return getPosition(getTotalTime());
    } // Basically the same for all paths

    @Override
    public boolean validate() {
        return true;
    }
}
