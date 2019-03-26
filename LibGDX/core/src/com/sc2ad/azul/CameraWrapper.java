package com.sc2ad.azul;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.sc2ad.azul.util.paths.CombinedPath;
import com.sc2ad.azul.util.paths.MotionPath;

public class CameraWrapper {
    public static CameraWrapper Instance;
    private OrthographicCamera camera;

    private Vector3 targetPos;
    private boolean panningToTarget;
    private Vector3 initialPos;
    private long panStartTime;
    private MotionPath panningPath;

    private static final float PAN_ACCEL = 0.1f / 10000000000000f; // Pixels per nanosecond^2
    private static final float PAN_MAX_SPEED = 1 / 100000f; // Pixels per nanosecond
    private static final float PAN_COMPLETE_THRESHOLD = 1f; // Distance for panning to be considered complete

    private float targetZoom;
    private boolean zooming;
    private float initialZoom;
    private long zoomStartTime;
    private MotionPath zoomingPath;

    private static final float ZOOM_ACCEL = 1f / 100000000f; // Zoom factor per nanosecond^2
    private static final float ZOOM_MAX_SPEED = 0.12f / 100000000f; // Zoom factor per nanosecond
    private static final float ZOOM_COMPLETE_THRESHOLD = 0.001f;
    private static final float ZOOM_TARGET_PERCENTAGE = 0.75f;

    private AzulDrawable currentTarget;

    public static CameraWrapper CreateCamera(float w, float h) {
        if (Instance == null) {
            Instance = new CameraWrapper(w, h);
        }
        return Instance;
    }

    private CameraWrapper(float w, float h) {
        camera = new OrthographicCamera(w, w * (h / w));
        camera.position.set(w/2, h/2, 0);
        camera.update();
        initialZoom = camera.zoom;
        initialPos = camera.position;
        targetPos = camera.position;
        targetZoom = initialZoom;
        panningToTarget = false;
        zooming = false;
    }

    public void update() {
        if (panningToTarget) {
            // Calculate new position using current time - start time
            long deltaTime = System.nanoTime() - panStartTime;
            // Calculate the direction of the pan
            Vector3 direction = targetPos.cpy();
            Vector3 tempStart = initialPos.cpy();

            direction.sub(tempStart);
            direction.nor();
            //TODO OBSERVE HOW THIS IS DONE
            direction.scl((float) panningPath.getPosition(deltaTime));
//            direction.add(camera.position);
            tempStart.add(direction);
            camera.position.set(tempStart);

            if (camera.position.dst(targetPos) < PAN_COMPLETE_THRESHOLD) {
                panningToTarget = false;
            }
        }
        if (zooming) {
            // Calculate new zoom using current time - start time
            long deltaTime = System.nanoTime() - zoomStartTime;
            //TODO OBSERVE HOW THIS IS DONE!
            camera.zoom = (float) zoomingPath.getPosition(deltaTime);
            if (Math.abs(camera.zoom - targetZoom) < ZOOM_COMPLETE_THRESHOLD) {
                // We have arrived within a zoom!
                zooming = false;
            }
        }
//        if (!panningToTarget && !zooming) {
//            initialZoom = camera.zoom;
//            initialPos = new Vector3(camera.position.x, camera.position.y, 0);
//        }
        camera.update();
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    /**
     * Focuses the camera on a drawable by zooming and centering the position on it.
     * @param drawable
     */
    public void focusCamera(AzulDrawable drawable) {
        if (drawable == currentTarget) {
            // Can't focus on the same target twice!
            return;
        }
        if (panningToTarget || zooming) {
            // Can't focus while focusing on something else!
            return;
        }
        panStartTime = System.nanoTime();
        zoomStartTime = System.nanoTime();
        if (drawable == null) {
            // Then we reset the camera's zoom
            targetZoom = initialZoom;
            zooming = true;
            targetPos = initialPos;
            panningToTarget = true;

//            initialZoom = camera.zoom;
            initialPos = camera.position.cpy();

            panningPath = new CombinedPath.LongitudalTrapezoid(0, initialPos.dst(targetPos), PAN_MAX_SPEED, PAN_ACCEL);
            float distance = targetZoom - camera.zoom;
            if (distance < 0) {
                zoomingPath = new CombinedPath.LongitudalTrapezoid(camera.zoom, distance, -ZOOM_MAX_SPEED, -ZOOM_ACCEL);
            } else {
                zoomingPath = new CombinedPath.LongitudalTrapezoid(camera.zoom, distance, ZOOM_MAX_SPEED, ZOOM_ACCEL);
            }
            currentTarget = null;
            camera.update();
            return;
        }
        if (currentTarget != null) {
            // Can't change focus without losing track of initial position/zoom!
            return;
        }
        currentTarget = drawable;
        panningToTarget = true;
        zooming = true;
        initialZoom = camera.zoom;
        // Calculate zoom based on camera's width/height and width/height of drawable
        targetZoom = (drawable.getSprite().getWidth() * (1 + ZOOM_TARGET_PERCENTAGE)) / camera.viewportWidth;

        initialPos = camera.position.cpy();
        targetPos = new Vector3(drawable.getCenterX(), drawable.getCenterY(), 0);

        panningPath = new CombinedPath.LongitudalTrapezoid(0, initialPos.dst(targetPos), PAN_MAX_SPEED, PAN_ACCEL);
        float distance = targetZoom - camera.zoom;
        if (distance < 0) {
            zoomingPath = new CombinedPath.LongitudalTrapezoid(camera.zoom, distance, -ZOOM_MAX_SPEED, -ZOOM_ACCEL);
        } else {
            zoomingPath = new CombinedPath.LongitudalTrapezoid(camera.zoom, distance, ZOOM_MAX_SPEED, ZOOM_ACCEL);
        }

        camera.update();
    }
    public Vector3 getPosition() {
        return camera.position;
    }
}
