package net.clesperanto.core;

import java.util.ArrayList;
import java.util.Objects;

import net.clesperanto._internals.jclic._AffineTransformJ;

/**
 * Class to build and manipulate an affine transformation matrix.
 * The matrix is meant to be used to transform an image or a volume using
 * {@link net.clesperanto.kernels.Transform#affineTransform}.
 */
public class AffineTransform {

	private final _AffineTransformJ _affineTransformJ;

	/**
	 * Constructs a new identity affine transform matrix.
	 */
	public AffineTransform() {
		_affineTransformJ = new _AffineTransformJ();
	}

	/**
	 * Constructs a new affine transform matrix from a 3x3 or 4x4 matrix given as
	 * a flat, row-major list of 9 or 16 floats.
	 *
	 * @param transformMatrix (ArrayList&lt;Float&gt;) - Flat 3x3 (9 values) or 4x4 (16 values) matrix.
	 * @throws NullPointerException if transformMatrix is null.
	 */
	public AffineTransform(ArrayList<Float> transformMatrix) {
		Objects.requireNonNull(transformMatrix, "transformMatrix cannot be null");
		_affineTransformJ = new _AffineTransformJ(Utils.toFloatVector(transformMatrix));
	}

	/**
	 * Scaling transformation.
	 * @param scaleX (float) - Scaling factor for the x axis.
	 * @param scaleY (float) - Scaling factor for the y axis.
	 * @param scaleZ (float) - Scaling factor for the z axis.
	 */
	public void scale(float scaleX, float scaleY, float scaleZ) {
		_affineTransformJ.scale(scaleX, scaleY, scaleZ);
	}

	/**
	 * Rotation transformation.
	 * @param axis (int) - Axis of rotation (0: x, 1: y, 2: z).
	 * @param angleDeg (float) - Angle of rotation (in degrees).
	 */
	public void rotate(int axis, float angleDeg) {
		_affineTransformJ.rotate(axis, angleDeg);
	}

	/**
	 * Rotation transformation around the x axis.
	 * @param angleDeg (float) - Angle of rotation (in degrees).
	 */
	public void rotateAroundXAxis(float angleDeg) {
		_affineTransformJ.rotateAroundXAxis(angleDeg);
	}

	/**
	 * Rotation transformation around the y axis.
	 * @param angleDeg (float) - Angle of rotation (in degrees).
	 */
	public void rotateAroundYAxis(float angleDeg) {
		_affineTransformJ.rotateAroundYAxis(angleDeg);
	}

	/**
	 * Rotation transformation around the z axis.
	 * @param angleDeg (float) - Angle of rotation (in degrees).
	 */
	public void rotateAroundZAxis(float angleDeg) {
		_affineTransformJ.rotateAroundZAxis(angleDeg);
	}

	/**
	 * Translation transformation.
	 * @param translateX (float) - Translation value for the x axis.
	 * @param translateY (float) - Translation value for the y axis.
	 * @param translateZ (float) - Translation value for the z axis.
	 */
	public void translate(float translateX, float translateY, float translateZ) {
		_affineTransformJ.translate(translateX, translateY, translateZ);
	}

	/**
	 * Centering transformation.
	 * @param shape (ArrayList&lt;Integer&gt;) - Shape of the array to center.
	 * @param undo (boolean) - Undo flag to apply the reverse transformation.
	 * @throws NullPointerException if shape is null.
	 */
	public void center(ArrayList<Integer> shape, boolean undo) {
		Objects.requireNonNull(shape, "shape cannot be null");
		_affineTransformJ.center(Utils.toIntVector(shape), undo);
	}

	/**
	 * Shearing transformation in the x plane.
	 * @param shearYDeg (float) - Shear angle in the y axis (in degrees).
	 * @param shearZDeg (float) - Shear angle in the z axis (in degrees).
	 */
	public void shearInXPlane(float shearYDeg, float shearZDeg) {
		_affineTransformJ.shearInXPlane(shearYDeg, shearZDeg);
	}

	/**
	 * Shearing transformation in the y plane.
	 * @param shearXDeg (float) - Shear angle in the x axis (in degrees).
	 * @param shearZDeg (float) - Shear angle in the z axis (in degrees).
	 */
	public void shearInYPlane(float shearXDeg, float shearZDeg) {
		_affineTransformJ.shearInYPlane(shearXDeg, shearZDeg);
	}

	/**
	 * Shearing transformation in the z plane.
	 * @param shearXDeg (float) - Shear angle in the x axis (in degrees).
	 * @param shearYDeg (float) - Shear angle in the y axis (in degrees).
	 */
	public void shearInZPlane(float shearXDeg, float shearYDeg) {
		_affineTransformJ.shearInZPlane(shearXDeg, shearYDeg);
	}

	/**
	 * Deskewing transformation in the x plane.
	 * @param angleDeg (float) - Angle of deskewing (in degrees).
	 * @param voxelSizeX (float) - Voxel size in the x axis.
	 * @param voxelSizeY (float) - Voxel size in the y axis.
	 * @param voxelSizeZ (float) - Voxel size in the z axis.
	 * @param scaleFactor (float) - Scaling factor.
	 */
	public void deskewX(float angleDeg, float voxelSizeX, float voxelSizeY, float voxelSizeZ, float scaleFactor) {
		_affineTransformJ.deskewX(angleDeg, voxelSizeX, voxelSizeY, voxelSizeZ, scaleFactor);
	}

	/**
	 * Deskewing transformation in the y plane.
	 * @param angleDeg (float) - Angle of deskewing (in degrees).
	 * @param voxelSizeX (float) - Voxel size in the x axis.
	 * @param voxelSizeY (float) - Voxel size in the y axis.
	 * @param voxelSizeZ (float) - Voxel size in the z axis.
	 * @param scaleFactor (float) - Scaling factor.
	 */
	public void deskewY(float angleDeg, float voxelSizeX, float voxelSizeY, float voxelSizeZ, float scaleFactor) {
		_affineTransformJ.deskewY(angleDeg, voxelSizeX, voxelSizeY, voxelSizeZ, scaleFactor);
	}

	/**
	 * Returns the transformation matrix as a flat, row-major list of 16 floats.
	 * @return ArrayList&lt;Float&gt;
	 */
	public ArrayList<Float> getMatrix() {
		return Utils.toArrayList(_affineTransformJ.getMatrix());
	}

	/**
	 * Internal method to get the raw native object.
	 * @return _AffineTransformJ
	 */
	public _AffineTransformJ getRaw() {
		return _affineTransformJ;
	}
}
