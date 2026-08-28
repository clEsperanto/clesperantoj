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

import ij.ImagePlus;
import ij.process.ImageProcessor;
import net.clesperanto.core.ArrayJ;
import net.clesperanto.core.DataType;
import net.clesperanto.core.DeviceJ;
import net.clesperanto.core.MemoryType;
import net.clesperanto.imagej.ImageJConverters;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestPullImagePlus extends AbstractDeviceTest {

	public final static int MAX_UINT16 = 65536;

	public final static int MAX_INT16 = 65536 / 2 - 1;

	public final static int MAX_UINT8 = 256;

	public final static int MAX_INT8 = 256 / 2 - 1;

	@Test
	public void testPullFloat() {

		float[] flatVals = new float[18];
		for (int i = 0; i < flatVals.length; i++)
			flatVals[i] = ThreadLocalRandom.current().nextFloat();

		DeviceJ device = DeviceJ.getDefaultDevice();
		device.setWaitForKernelFinish(true);
		ArrayJ in = device.createArray(DataType.FLOAT32, MemoryType.BUFFER, 3, 3, 2);
		in.writeFromArray(flatVals);
		ImagePlus outputImp = ImageJConverters.copyArrayJToImagePlus(in);

		int c = 0;
		for (int z = 0; z < 2; z++) {
			outputImp.setPositionWithoutUpdate(1, 1 + z, 1);
			ImageProcessor outIp = outputImp.getProcessor();
			for (int y = 0; y < 3; y++) {
				for (int x = 0; x < 3; x++) {
					assertEquals(flatVals[c++], outIp.getPixelValue(x, y));
				}
			}
		}
	}

	@Test
	public void testPullInt() {

		int[] flatVals = new int[18];
		for (int i = 0; i < flatVals.length; i++)
			flatVals[i] = ThreadLocalRandom.current().nextInt();

		DeviceJ device = DeviceJ.getDefaultDevice();
		device.setWaitForKernelFinish(true);
		ArrayJ in = device.createArray(DataType.INT32, MemoryType.BUFFER, 3, 3, 2);
		in.writeFromArray(flatVals);
		ImagePlus outputImp = ImageJConverters.copyArrayJToImagePlus(in);

		int c = 0;
		for (int z = 0; z < 2; z++) {
			outputImp.setPositionWithoutUpdate(1, 1 + z, 1);
			ImageProcessor outIp = outputImp.getProcessor();
			for (int y = 0; y < 3; y++) {
				for (int x = 0; x < 3; x++) {
					assertEquals(flatVals[c++], outIp.getPixelValue(x, y));
				}
			}
		}
	}

	@Test
	public void testPullUint() {

		int[] flatVals = new int[18];
		for (int i = 0; i < flatVals.length; i++)
			flatVals[i] = (int) ThreadLocalRandom.current().nextLong(0, 0xffffffffL);

		DeviceJ device = DeviceJ.getDefaultDevice();
		device.setWaitForKernelFinish(true);
		ArrayJ in = device.createArray(DataType.UINT32, MemoryType.BUFFER, 3, 3, 2);
		in.writeFromArray(flatVals);
		ImagePlus outputImp = ImageJConverters.copyArrayJToImagePlus(in);

		int c = 0;
		for (int z = 0; z < 2; z++) {
			outputImp.setPositionWithoutUpdate(1, 1 + z, 1);
			ImageProcessor outIp = outputImp.getProcessor();
			for (int y = 0; y < 3; y++) {
				for (int x = 0; x < 3; x++) {
					final long flatVal = flatVals[c++] & 0xffffffffL;
					assertEquals(flatVal, outIp.getPixelValue(x, y));
				}
			}
		}
	}

	@Test
	public void testPullShort() {

		short[] flatVals = new short[18];
		for (int i = 0; i < flatVals.length; i++)
			flatVals[i] = (short) ThreadLocalRandom.current().nextInt(Short.MIN_VALUE, Short.MAX_VALUE + 1);

		DeviceJ device = DeviceJ.getDefaultDevice();
		device.setWaitForKernelFinish(true);
		ArrayJ in = device.createArray(DataType.INT16, MemoryType.BUFFER, 3, 3, 2);
		in.writeFromArray(flatVals);
		ImagePlus outputImp = ImageJConverters.copyArrayJToImagePlus(in);

		int c = 0;
		for (int z = 0; z < 2; z++) {
			outputImp.setPositionWithoutUpdate(1, 1 + z, 1);
			ImageProcessor outIp = outputImp.getProcessor();
			for (int y = 0; y < 3; y++) {
				for (int x = 0; x < 3; x++) {
					float val = outIp.getPixelValue(x, y);
					assertEquals(flatVals[c++], val > MAX_INT16 ? val - MAX_UINT16 : val);
				}
			}
		}
	}

	@Test
	public void testPullUshort() {

		short[] flatVals = new short[18];
		for (int i = 0; i < flatVals.length; i++)
			flatVals[i] = (short) ThreadLocalRandom.current().nextInt(0, MAX_UINT16);

		DeviceJ device = DeviceJ.getDefaultDevice();
		device.setWaitForKernelFinish(true);
		ArrayJ in = device.createArray(DataType.UINT16, MemoryType.BUFFER, 3, 3, 2);
		in.writeFromArray(flatVals);
		ImagePlus outputImp = ImageJConverters.copyArrayJToImagePlus(in);

		int c = 0;
		for (int z = 0; z < 2; z++) {
			outputImp.setPositionWithoutUpdate(1, 1 + z, 1);
			ImageProcessor outIp = outputImp.getProcessor();
			for (int y = 0; y < 3; y++) {
				for (int x = 0; x < 3; x++) {
					short val = flatVals[c++];
					assertEquals(val < 0 ? MAX_UINT16 + val : val, outIp.getPixelValue(x, y));
				}
			}
		}
	}

	@Test
	public void testPullByte() {

		byte[] flatVals = new byte[18];
		for (int i = 0; i < flatVals.length; i++)
			flatVals[i] = (byte) ThreadLocalRandom.current().nextInt(Byte.MIN_VALUE, Byte.MAX_VALUE + 1);

		DeviceJ device = DeviceJ.getDefaultDevice();
		device.setWaitForKernelFinish(true);
		ArrayJ in = device.createArray(DataType.INT8, MemoryType.BUFFER, 3, 3, 2);
		in.writeFromArray(flatVals);
		ImagePlus outputImp = ImageJConverters.copyArrayJToImagePlus(in);

		int c = 0;
		for (int z = 0; z < 2; z++) {
			outputImp.setPositionWithoutUpdate(1, 1 + z, 1);
			ImageProcessor outIp = outputImp.getProcessor();
			for (int y = 0; y < 3; y++) {
				for (int x = 0; x < 3; x++) {
					float val = outIp.getPixelValue(x, y);
					assertEquals(flatVals[c++], val > MAX_INT8 ? val - MAX_UINT8 : val);
				}
			}
		}
	}

	@Test
	public void testPullUbyte() {

		byte[] flatVals = new byte[18];
		for (int i = 0; i < flatVals.length; i++)
			flatVals[i] = (byte) ThreadLocalRandom.current().nextInt(0, MAX_UINT8);

		DeviceJ device = DeviceJ.getDefaultDevice();
		device.setWaitForKernelFinish(true);
		ArrayJ in = device.createArray(DataType.UINT8, MemoryType.BUFFER, 3, 3, 2);
		in.writeFromArray(flatVals);
		ImagePlus outputImp = ImageJConverters.copyArrayJToImagePlus(in);

		int c = 0;
		for (int z = 0; z < 2; z++) {
			outputImp.setPositionWithoutUpdate(1, 1 + z, 1);
			ImageProcessor outIp = outputImp.getProcessor();
			for (int y = 0; y < 3; y++) {
				for (int x = 0; x < 3; x++) {
					byte val = flatVals[c++];
					assertEquals(val < 0 ? MAX_UINT8 + val : val, outIp.getPixelValue(x, y));
				}
			}
		}
	}
}
