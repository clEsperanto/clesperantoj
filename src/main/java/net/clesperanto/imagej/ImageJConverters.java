package net.clesperanto.imagej;

import ij.ImagePlus;
import ij.ImageStack;
import ij.process.ByteProcessor;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;
import ij.process.ShortProcessor;
import net.clesperanto.core.ArrayJ;
import net.clesperanto.core.DataType;
import net.clesperanto.core.DeviceJ;
import net.clesperanto.core.MemoryType;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.util.Intervals;

import static net.imglib2.util.Util.safeInt;

/**
 * TODO
 * - define whether the buffers follow big or little endian and whether the copies are c- or fortran order
 * - using array img and blocks copy limits to images whose array size is smaller than the integer max
 */
/**
 * Class to copy {@link ImagePlus} into {@link ArrayJ} and vice-versa
 */
public class ImageJConverters {

	/**
	 * Convert an {@link ArrayJ} into an IJ {@link ImagePlus} of the same dimensions and data type.
	 * Creates a copy of the ArrayJ in the GPU into an ImagePlus in the CPU
	 *
	 * @param arrayJ
	 * 	array that is located in the GPU for clesperanto to do some operations
	 * @return an IJ {@link ImagePlus} on the CPU copied from the {@link ArrayJ} on the GPU
	 */
	public static ImagePlus copyArrayJToImagePlus(ArrayJ arrayJ) {

		final int width = safeInt(arrayJ.width());
		final int height = safeInt(arrayJ.height());
		final int depth = safeInt(arrayJ.depth());

		final ImageStack stack = new ImageStack(width, height);
		final DataType dataType = arrayJ.dataType();

		final CreateImageProcessor creator;
		switch (dataType) {
			case INT8:
			case UINT8:
				creator = ByteProcessor::new;
				break;
			case INT16:
			case UINT16:
				creator = ShortProcessor::new;
				break;
			default:
				creator = FloatProcessor::new;
		}

		final ReadSlice reader;
		switch (dataType) {
			case INT32: {
				final int[] temp = new int[width * height];
				reader = (dest, z) -> {
					dataType.memory().readToArray(arrayJ, temp, 0, 0, z, width, height, 1);
					convertI32ToF32(temp, (float[]) dest.getPixels());
					return dest;
				};
				break;
			}
			case UINT32: {
				final int[] temp = new int[width * height];
				reader = (dest, z) -> {
					dataType.memory().readToArray(arrayJ, temp, 0, 0, z, width, height, 1);
					convertU32ToF32(temp, (float[]) dest.getPixels());
					return dest;
				};
				break;
			}
			default: {
				reader = (dest, z) -> {
					dataType.memory().readToArray(arrayJ, dest.getPixels(), 0, 0, z, width, height, 1);
					return dest;
				};
			}
		}

		for (int z = 0; z < depth; ++z) {
			stack.addSlice("", reader.read(creator.create(width, height), z));
		}

		ImagePlus imp = new ImagePlus("image", stack);
		imp.setDimensions(1, depth, 1);
		if (depth > 1)
			imp.setOpenAsHyperStack(true);
		return imp;
	}

	@FunctionalInterface
	private interface CreateImageProcessor {
		ImageProcessor create(int width, int height);
	}

	@FunctionalInterface
	private interface ReadSlice {
		ImageProcessor read(ImageProcessor dest, int z);
	}

	private static void convertI32ToF32(int[] ints, float[] floats) {
		for (int i = 0; i < ints.length; ++i) {
			floats[i] = ints[i];
		}
	}

	private static void convertU32ToF32(int[] ints, float[] floats) {
		for (int i = 0; i < ints.length; ++i) {
			floats[i] = (float) (ints[i] & 0xffffffffL);
		}
	}

	/**
	 * Copy a {@link RandomAccessibleInterval} on the CPU into an {@link ArrayJ} on the device (GPU) of interest.
	 * The {@link RandomAccessibleInterval} should have at most 3 dimensions, and the order of the dimensions
	 * should be [width, height, depth]
	 *
	 * @param imp
	 *  the {@link RandomAccessibleInterval} that is going to be copied into the GPU
	 * @param device
	 * 	the device into which the imp is going to be copied. If null, the default system device is used.
	 * @param memoryType
	 * 	the type of memory array that we are working with. The options are image or buffer. For image use the
	 * 	String "image", for buffer use "buffer"
	 * @return an {@link ArrayJ} copied from the {@link RandomAccessibleInterval} of the CPU
	 */
	public static ArrayJ copyImagePlus2ToArrayJ(ImagePlus imp, DeviceJ device, MemoryType memoryType) {

		// TODO: How to handle channels and time? Looks like we're ignoring it for now?
		final int width = imp.getWidth();
		final int height = imp.getHeight();
		final int channels = 1; // imp.getNChannels();
		final int depth = imp.getNSlices();
		final int frames = 1; // imp.getNFrames();

		if (Intervals.numElements(width, height, channels, depth, frames) > Integer.MAX_VALUE)
			throw new IllegalArgumentException();

		final DataType dataType = dataType(imp);
		final ArrayJ arrayJ = device.createArray(dataType, memoryType, width, height, depth);

		for (int t = 0; t < frames; ++t) {
			for (int z = 0; z < depth; ++z) {
				for (int c = 0; c < channels; ++c) {
					final int stackIndex = imp.getStackIndex(c + 1, z + 1, t + 1);
					final Object data = imp.getStack().getProcessor(stackIndex).getPixels();
					dataType.memory().writeFromArray(arrayJ, data, 0, 0, z, width, height, 1);
				}
			}
		}

		return arrayJ;
	}

	private static DataType dataType(final ImagePlus imp) {
		switch (imp.getType()) {
			case ImagePlus.GRAY8:
				return DataType.UINT8;
			case ImagePlus.GRAY16:
				return DataType.UINT16;
			case ImagePlus.GRAY32:
				return DataType.FLOAT32;
			case ImagePlus.COLOR_256:
				throw new IllegalArgumentException("Unsupported image type: ImagePlus.COLOR_256");
			case ImagePlus.COLOR_RGB:
				throw new IllegalArgumentException("Unsupported image type: ImagePlus.COLOR_RGB");
			default:
				throw new IllegalArgumentException("Unknown image type: " + imp.getType());
		}
	}
}
