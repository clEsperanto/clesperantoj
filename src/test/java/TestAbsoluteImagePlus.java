import ij.IJ;
import ij.ImagePlus;
import ij.process.ImageProcessor;
import net.clesperanto.core.ArrayJ;
import net.clesperanto.core.DeviceJ;
import net.clesperanto.core.MemoryType;
import net.clesperanto.imagej.ImageJConverters;
import net.clesperanto.kernels.Tier1;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestAbsoluteImagePlus extends AbstractDeviceTest {

	@Test
	public void testAbsoluteImagePlus() {
		ImagePlus inputImp = IJ.createImage("input", 2, 2, 1, 32);
		ImagePlus outputImp = IJ.createImage("input", 2, 2, 1, 32);
		ImageProcessor inpIp = inputImp.getProcessor();
		ImageProcessor outIp = outputImp.getProcessor();

		int[] vals = { -1, -1, 1, 1 };
		int c = 0;
		for (int x = 0; x < 2; x++) {
			for (int y = 0; y < 2; y++) {
				inpIp.putPixelValue(x, y, vals[c]);
				outIp.putPixelValue(x, y, vals[c++]);
			}
		}

		DeviceJ device = DeviceJ.getDefaultDevice();
		device.setWaitForKernelFinish(true);
		ArrayJ in = ImageJConverters.copyImagePlus2ToArrayJ(inputImp, device, MemoryType.BUFFER);
		ArrayJ out = ImageJConverters.copyImagePlus2ToArrayJ(outputImp, device, MemoryType.BUFFER);

		Tier1.absolute(device, in, out);

		outputImp = ImageJConverters.copyArrayJToImagePlus(out);
		outIp = outputImp.getProcessor();

		double min = Double.MAX_VALUE;
		double max = Double.MIN_VALUE;
		double mean = 0;
		for (int x = 0; x < 2; x++) {
			for (int y = 0; y < 2; y++) {
				double val = outIp.getPixelValue(x, y);
				mean += val / 4;
				min = Math.min(min, val);
				max = Math.max(max, val);
			}
		}

		assertEquals(1, min);
		assertEquals(1, max);
		assertEquals(1, mean);
		in = null;
		out = null;
	}

	@Test
	public void testAbsolute1ImagePlus() {
		ImagePlus inputImp = IJ.createImage("input", 2, 2, 1, 32);
		ImageProcessor inpIp = inputImp.getProcessor();

		int[] vals = { -1, -1, 1, 1 };
		int c = 0;
		for (int x = 0; x < 2; x++) {
			for (int y = 0; y < 2; y++) {
				inpIp.putPixelValue(x, y, vals[c++]);
			}
		}

		DeviceJ device = DeviceJ.getDefaultDevice();
		device.setWaitForKernelFinish(true);
		ArrayJ in = ImageJConverters.copyImagePlus2ToArrayJ(inputImp, device, MemoryType.BUFFER);

		ArrayJ out = Tier1.absolute(device, in, null);

		ImagePlus outputImp = ImageJConverters.copyArrayJToImagePlus(out);
		ImageProcessor outIp = outputImp.getProcessor();

		for (int x = 0; x < 2; x++) {
			for (int y = 0; y < 2; y++) {
				double val = outIp.getPixelValue(x, y);
				assertEquals(1, val);
			}
		}
		in = null;
		out = null;
	}
}
