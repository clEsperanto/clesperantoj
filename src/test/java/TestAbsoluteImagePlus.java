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
