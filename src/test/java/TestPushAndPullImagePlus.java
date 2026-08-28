/*-
 * #%L
 * Java wrapper for Clesperanto
 * %%
 * Copyright (C) 2022 - 2025 Robert Haase, MPI CBG and Stephane Rigaud, Institut Pasteur
 * %%
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3. Neither the name of the PoL, TU Dresden nor the names of its contributors
 *    may be used to endorse or promote products derived from this software without
 *    specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */
/*
 * Copyright 2024 Stéphane Rigaud, Robert Haase, Institut Pasteur Paris,
 * Max Planck Institute for Molecular Cell Biology and Genetics Dresden,
 * ScaDS.AI, Leipzig University
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

import ij.IJ;
import ij.ImagePlus;
import ij.process.ImageProcessor;
import net.clesperanto.core.ArrayJ;
import net.clesperanto.core.DeviceJ;
import net.clesperanto.core.MemoryType;
import net.clesperanto.imagej.ImageJConverters;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestPushAndPullImagePlus extends AbstractDeviceTest {

	public final static int MAX_UINT16 = 65536;

	public final static int MAX_INT16 = 65536 / 2 - 1;

	public final static int MAX_UINT8 = 256;

	public final static int MAX_INT8 = 256 / 2 - 1;

	@Test
	public void testImagePlusPushAndPullFloat() {
		ImagePlus inputImp = IJ.createImage("input", 3, 3, 2, 32);

		float[] flatVals = new float[18];
		for (int i = 0; i < flatVals.length; i++)
			flatVals[i] = ThreadLocalRandom.current().nextFloat();

		int c = 0;
		for (int z = 0; z < 2; z++) {
			inputImp.setPositionWithoutUpdate(1, 1 + z, 1);
			ImageProcessor inpIp = inputImp.getProcessor();
			for (int y = 0; y < 3; y++) {
				for (int x = 0; x < 3; x++) {
					inpIp.putPixelValue(x, y, flatVals[c++]);
				}
			}
		}

		DeviceJ device = DeviceJ.getDefaultDevice();
		device.setWaitForKernelFinish(true);
		ArrayJ in = ImageJConverters.copyImagePlus2ToArrayJ(inputImp, device, MemoryType.BUFFER);
		ImagePlus outputImp = ImageJConverters.copyArrayJToImagePlus(in);

		c = 0;
		for (int z = 0; z < 2; z++) {
			inputImp.setPositionWithoutUpdate(1, 1 + z, 1);
			ImageProcessor inpIp = inputImp.getProcessor();
			outputImp.setPositionWithoutUpdate(1, 1 + z, 1);
			ImageProcessor outIp = outputImp.getProcessor();
			for (int y = 0; y < 3; y++) {
				for (int x = 0; x < 3; x++) {
					assertEquals(inpIp.getPixelValue(x, y), outIp.getPixelValue(x, y));
					assertEquals(flatVals[c++], outIp.getPixelValue(x, y));
				}
			}
		}
	}

	@Test
	public void testImagePlusPushAndPullInt() {
		ImagePlus inputImp = IJ.createImage("input", 3, 3, 2, 32);

		int[] flatVals = new int[18];
		for (int i = 0; i < flatVals.length; i++)
			flatVals[i] = ThreadLocalRandom.current().nextInt(100);

		int c = 0;
		for (int z = 0; z < 2; z++) {
			inputImp.setPositionWithoutUpdate(1, 1 + z, 1);
			ImageProcessor inpIp = inputImp.getProcessor();
			for (int y = 0; y < 3; y++) {
				for (int x = 0; x < 3; x++) {
					inpIp.putPixelValue(x, y, flatVals[c++]);
				}
			}
		}

		DeviceJ device = DeviceJ.getDefaultDevice();
		device.setWaitForKernelFinish(true);
		ArrayJ in = ImageJConverters.copyImagePlus2ToArrayJ(inputImp, device, MemoryType.BUFFER);
		ImagePlus outputImp = ImageJConverters.copyArrayJToImagePlus(in);

		c = 0;
		for (int z = 0; z < 2; z++) {
			inputImp.setPositionWithoutUpdate(1, 1 + z, 1);
			ImageProcessor inpIp = inputImp.getProcessor();
			outputImp.setPositionWithoutUpdate(1, 1 + z, 1);
			ImageProcessor outIp = outputImp.getProcessor();
			for (int y = 0; y < 3; y++) {
				for (int x = 0; x < 3; x++) {
					assertEquals(inpIp.getPixelValue(x, y), outIp.getPixelValue(x, y));
					assertEquals(flatVals[c++], outIp.getPixelValue(x, y));
				}
			}
		}
	}

	@Test
	public void testImagePlusPushAndPullUint() {
		ImagePlus inputImp = IJ.createImage("input", 3, 3, 2, 32);

		long[] flatVals = new long[18];
		for (int i = 0; i < flatVals.length; i++)
			flatVals[i] = (long) Integer.MAX_VALUE + (long) ThreadLocalRandom.current().nextInt(0, 100);
		int c = 0;
		for (int z = 0; z < 2; z++) {
			inputImp.setPositionWithoutUpdate(1, 1 + z, 1);
			ImageProcessor inpIp = inputImp.getProcessor();
			for (int y = 0; y < 3; y++) {
				for (int x = 0; x < 3; x++) {
					inpIp.putPixelValue(x, y, flatVals[c++]);
				}
			}
		}

		DeviceJ device = DeviceJ.getDefaultDevice();
		device.setWaitForKernelFinish(true);
		ArrayJ in = ImageJConverters.copyImagePlus2ToArrayJ(inputImp, device, MemoryType.BUFFER);
		ImagePlus outputImp = ImageJConverters.copyArrayJToImagePlus(in);

		c = 0;
		for (int z = 0; z < 2; z++) {
			inputImp.setPositionWithoutUpdate(1, 1 + z, 1);
			ImageProcessor inpIp = inputImp.getProcessor();
			outputImp.setPositionWithoutUpdate(1, 1 + z, 1);
			ImageProcessor outIp = outputImp.getProcessor();
			for (int y = 0; y < 3; y++) {
				for (int x = 0; x < 3; x++) {
					assertEquals(inpIp.getPixelValue(x, y), outIp.getPixelValue(x, y));
					assertEquals(flatVals[c++], outIp.getPixelValue(x, y));
				}
			}
		}
	}

	@Test
	public void testImagePlusPushAndPullShort() {
		ImagePlus inputImp = IJ.createImage("input", 3, 3, 2, 16);

		short[] flatVals = new short[18];
		for (int i = 0; i < flatVals.length; i++)
			flatVals[i] = (short) ThreadLocalRandom.current().nextInt(Short.MIN_VALUE, Short.MAX_VALUE + 1);
		int c = 0;
		for (int z = 0; z < 2; z++) {
			inputImp.setPositionWithoutUpdate(1, 1 + z, 1);
			ImageProcessor inpIp = inputImp.getProcessor();
			for (int y = 0; y < 3; y++) {
				for (int x = 0; x < 3; x++) {
					inpIp.putPixelValue(x, y, flatVals[c] < 0 ? MAX_UINT16 + flatVals[c] : flatVals[c]);
					c++;
				}
			}
		}

		DeviceJ device = DeviceJ.getDefaultDevice();
		device.setWaitForKernelFinish(true);
		ArrayJ in = ImageJConverters.copyImagePlus2ToArrayJ(inputImp, device, MemoryType.BUFFER);
		ImagePlus outputImp = ImageJConverters.copyArrayJToImagePlus(in);

		c = 0;
		for (int z = 0; z < 2; z++) {
			inputImp.setPositionWithoutUpdate(1, 1 + z, 1);
			ImageProcessor inpIp = inputImp.getProcessor();
			outputImp.setPositionWithoutUpdate(1, 1 + z, 1);
			ImageProcessor outIp = outputImp.getProcessor();
			for (int y = 0; y < 3; y++) {
				for (int x = 0; x < 3; x++) {
					float outVal = outIp.getPixelValue(x, y);
					assertEquals(inpIp.getPixelValue(x, y), outVal);
					assertEquals(flatVals[c++], (short) outVal);
				}
			}
		}
	}

	@Test
	public void testImagePlusPushAndPullUshort() {
		ImagePlus inputImp = IJ.createImage("input", 3, 3, 2, 16);

		int[] flatVals = new int[18];
		for (int i = 0; i < flatVals.length; i++)
			flatVals[i] = ThreadLocalRandom.current().nextInt(0, MAX_UINT16);
		int c = 0;
		for (int z = 0; z < 2; z++) {
			inputImp.setPositionWithoutUpdate(1, 1 + z, 1);
			ImageProcessor inpIp = inputImp.getProcessor();
			for (int y = 0; y < 3; y++) {
				for (int x = 0; x < 3; x++) {
					inpIp.putPixelValue(x, y, flatVals[c++]);
				}
			}
		}

		DeviceJ device = DeviceJ.getDefaultDevice();
		device.setWaitForKernelFinish(true);
		ArrayJ in = ImageJConverters.copyImagePlus2ToArrayJ(inputImp, device, MemoryType.BUFFER);
		ImagePlus outputImp = ImageJConverters.copyArrayJToImagePlus(in);

		c = 0;
		for (int z = 0; z < 2; z++) {
			inputImp.setPositionWithoutUpdate(1, 1 + z, 1);
			ImageProcessor inpIp = inputImp.getProcessor();
			outputImp.setPositionWithoutUpdate(1, 1 + z, 1);
			ImageProcessor outIp = outputImp.getProcessor();
			for (int y = 0; y < 3; y++) {
				for (int x = 0; x < 3; x++) {
					assertEquals(inpIp.getPixelValue(x, y), outIp.getPixelValue(x, y));
					assertEquals(flatVals[c++], outIp.getPixelValue(x, y));
				}
			}
		}
	}

	@Test
	public void testImagePlusPushAndPullByte() {
		ImagePlus inputImp = IJ.createImage("input", 3, 3, 2, 8);

		byte[] flatVals = new byte[18];
		for (int i = 0; i < flatVals.length; i++)
			flatVals[i] = (byte) ThreadLocalRandom.current().nextInt(Byte.MIN_VALUE, Byte.MAX_VALUE + 1);
		int c = 0;
		for (int z = 0; z < 2; z++) {
			inputImp.setPositionWithoutUpdate(1, 1 + z, 1);
			ImageProcessor inpIp = inputImp.getProcessor();
			for (int y = 0; y < 3; y++) {
				for (int x = 0; x < 3; x++) {
					inpIp.putPixelValue(x, y, flatVals[c] < 0 ? MAX_UINT8 + flatVals[c] : flatVals[c]);
					c++;
				}
			}
		}

		DeviceJ device = DeviceJ.getDefaultDevice();
		device.setWaitForKernelFinish(true);
		ArrayJ in = ImageJConverters.copyImagePlus2ToArrayJ(inputImp, device, MemoryType.BUFFER);
		ImagePlus outputImp = ImageJConverters.copyArrayJToImagePlus(in);

		c = 0;
		for (int z = 0; z < 2; z++) {
			inputImp.setPositionWithoutUpdate(1, 1 + z, 1);
			ImageProcessor inpIp = inputImp.getProcessor();
			outputImp.setPositionWithoutUpdate(1, 1 + z, 1);
			ImageProcessor outIp = outputImp.getProcessor();
			for (int y = 0; y < 3; y++) {
				for (int x = 0; x < 3; x++) {
					assertEquals(inpIp.getPixelValue(x, y), outIp.getPixelValue(x, y));
					assertEquals(flatVals[c++], (byte) outIp.getPixelValue(x, y));
				}
			}
		}
	}

	@Test
	public void testImagePlusPushAndPullUbyte() {
		ImagePlus inputImp = IJ.createImage("input", 3, 3, 2, 8);

		short[] flatVals = new short[18];
		for (int i = 0; i < flatVals.length; i++)
			flatVals[i] = (short) ThreadLocalRandom.current().nextInt(0, MAX_UINT8);
		int c = 0;
		for (int z = 0; z < 2; z++) {
			inputImp.setPositionWithoutUpdate(1, 1 + z, 1);
			ImageProcessor inpIp = inputImp.getProcessor();
			for (int y = 0; y < 3; y++) {
				for (int x = 0; x < 3; x++) {
					inpIp.putPixelValue(x, y, flatVals[c++]);
				}
			}
		}

		DeviceJ device = DeviceJ.getDefaultDevice();
		device.setWaitForKernelFinish(true);
		ArrayJ in = ImageJConverters.copyImagePlus2ToArrayJ(inputImp, device, MemoryType.BUFFER);
		ImagePlus outputImp = ImageJConverters.copyArrayJToImagePlus(in);

		c = 0;
		for (int z = 0; z < 2; z++) {
			inputImp.setPositionWithoutUpdate(1, 1 + z, 1);
			ImageProcessor inpIp = inputImp.getProcessor();
			outputImp.setPositionWithoutUpdate(1, 1 + z, 1);
			ImageProcessor outIp = outputImp.getProcessor();
			for (int y = 0; y < 3; y++) {
				for (int x = 0; x < 3; x++) {
					assertEquals(inpIp.getPixelValue(x, y), outIp.getPixelValue(x, y));
					assertEquals(flatVals[c++], outIp.getPixelValue(x, y));
				}
			}
		}
	}
}
