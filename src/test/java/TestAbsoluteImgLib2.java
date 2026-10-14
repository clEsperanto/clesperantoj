import net.clesperanto.core.ArrayJ;
import net.clesperanto.core.DeviceJ;
import net.clesperanto.core.MemoryType;
import net.clesperanto.imglib2.ImgLib2Converters;
import net.clesperanto.kernels.Tier1;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.type.numeric.integer.IntType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestAbsoluteImgLib2 extends AbstractDeviceTest {

	@Test
	public void testAbsoluteImgLib2() {
		int[] flatVals = { 1, 1, -1, -1 };
		RandomAccessibleInterval<IntType> inputImg = ArrayImgs.ints(flatVals, new long[] { 2, 2 });
		RandomAccessibleInterval<IntType> outputImg = ArrayImgs.ints(flatVals, new long[] { 2, 2 });

		DeviceJ device = DeviceJ.getDefaultDevice();
		device.setWaitForKernelFinish(true);
		ArrayJ in = ImgLib2Converters.copyImgLib2ToArrayJ(inputImg, device, MemoryType.BUFFER);
		ArrayJ out = ImgLib2Converters.copyImgLib2ToArrayJ(outputImg, device, MemoryType.BUFFER);

		Tier1.absolute(device, in, out);

		outputImg = ImgLib2Converters.copyArrayJToImgLib2(out);

		double min = outputImg.firstElement().getRealDouble();
		double max = min;
		double mean = 0;

		for (IntType px : outputImg) {
			double val = px.getRealDouble();
			mean += val / 4;
			min = Math.min(min, val);
			max = Math.max(max, val);
		}

		assertEquals(1, min);
		assertEquals(1, max);
		assertEquals(1, mean);
		in = null;
		out = null;
	}

	@Test
	public void testAbsolute1ImgLib2() {
		int[] flatVals = { 1, 1, -1, -1 };
		RandomAccessibleInterval<IntType> inputImg = ArrayImgs.ints(flatVals, new long[] { 2, 2 });

		DeviceJ device = DeviceJ.getDefaultDevice();
		device.setWaitForKernelFinish(true);
		ArrayJ in = ImgLib2Converters.copyImgLib2ToArrayJ(inputImg, device, MemoryType.BUFFER);

		ArrayJ out = Tier1.absolute(device, in, null);

		RandomAccessibleInterval<IntType> outputImg = ImgLib2Converters.copyArrayJToImgLib2(out);

		for (IntType px : outputImg) {
			double val = px.getRealDouble();
			assertEquals(1, val);
		}
		in = null;
		out = null;
	}
}
