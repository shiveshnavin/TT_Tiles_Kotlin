package `in`.hoptec.kotlin101.utils

import android.graphics.Camera
import android.graphics.Matrix
import android.view.animation.Animation
import android.view.animation.Transformation

/**
 * Created by shivesh on 5/7/17.
 */

class Rotate3dAnimation
/**
 * Creates a new 3D rotation on the Y axis. The rotation is defined by its
 * start angle and its end angle. Both angles are in degrees. The rotation
 * is performed around a center point on the 2D space, definied by a pair
 * of X and Y coordinates, called centerX and centerY. When the animation
 * starts, a translation on the Z axis (depth) is performed. The length
 * of the translation can be specified, as well as whether the translation
 * should be reversed in time.

 * @param fromDegrees the start angle of the 3D rotation
 * *
 * @param toDegrees the end angle of the 3D rotation
 * *
 * @param centerX the X center of the 3D rotation
 * *
 * @param centerY the Y center of the 3D rotation
 * *
 * @param reverse true if the translation should be reversed, false otherwise
 */
(private val mFromDegrees: Float, private val mToDegrees: Float,
 private val mCenterX: Float, private val mCenterY: Float, private val mDepthZ: Float, private val mReverse: Boolean) : Animation() {
    private var mCamera: Camera? = null

    override fun initialize(width: Int, height: Int, parentWidth: Int, parentHeight: Int) {
        super.initialize(width, height, parentWidth, parentHeight)
        mCamera = Camera()
    }

    override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
        val fromDegrees = mFromDegrees
        val degrees = fromDegrees + (mToDegrees - fromDegrees) * interpolatedTime

        val centerX = mCenterX
        val centerY = mCenterY
        val camera = mCamera

        val matrix = t.matrix

        //Save a camera initial state, for restore()
        camera!!.save()
        if (mReverse) {
            camera.translate(0.0f, 0.0f, mDepthZ * interpolatedTime)
        } else {
            camera.translate(0.0f, 0.0f, mDepthZ * (1.0f - interpolatedTime))
        }
        //Revolving around the Y axis degrees
        camera.rotateY(degrees)
        //Remove the matrix row camera, assigned to matrix
        camera.getMatrix(matrix)
        //Camera return to the initial state, to continue for the next calculation
        camera.restore()

        matrix.preTranslate(-centerX, -centerY)
        matrix.postTranslate(centerX, centerY)
    }
}