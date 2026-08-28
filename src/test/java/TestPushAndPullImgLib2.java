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

import net.clesperanto.core.ArrayJ;
import net.clesperanto.core.DeviceJ;
import net.clesperanto.core.MemoryType;
import net.clesperanto.imglib2.ImgLib2Converters;
import net.imglib2.Cursor;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.type.numeric.integer.*;
import net.imglib2.type.numeric.real.FloatType;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestPushAndPullImgLib2 extends AbstractDeviceTest {

	public final static long MAX_UINT32 = (long) Math.pow(2, 32);

	public final static int MAX_INT32 = Integer.MAX_VALUE;

	public final static int MAX_UINT16 = 65536;

	public final static int MAX_INT16 = 65536 / 2 - 1;

	public final static int MAX_UINT8 = 256;

	public final static int MAX_INT8 = 256 / 2 - 1;

	@Test
	public void testImgLib2PushAndPullFloat() {
		float[] flatVals = new float[18];
		for (int i = 0; i < flatVals.length; i++)
			flatVals[i] = ThreadLocalRandom.current().nextFloat();
		RandomAccessibleInterval<FloatType> inputImg = ArrayImgs.floats(flatVals, new long[] { 3, 3, 2 });

		DeviceJ device = DeviceJ.getDefaultDevice();
		device.setWaitForKernelFinish(true);
		ArrayJ in = ImgLib2Converters.copyImgLib2ToArrayJ(inputImg, device, MemoryType.BUFFER);
		RandomAccessibleInterval<FloatType> outputImg = ImgLib2Converters.copyArrayJToImgLib2(in);

		Cursor<FloatType> inCursor = inputImg.cursor();
		Cursor<FloatType> outCursor = outputImg.cursor();

		int c = 0;
		while (inCursor.hasNext()) {
			inCursor.next();
			outCursor.next();
			assertEquals(inCursor.get().get(), outCursor.get().get());
			assertEquals(flatVals[c++], outCursor.get().get());
		}
	}

	@Test
	public void testImgLib2PushAndPullInt() {
		int[] flatVals = new int[18];
		for (int i = 0; i < flatVals.length; i++)
			flatVals[i] = ThreadLocalRandom.current().nextInt();
		RandomAccessibleInterval<IntType> inputImg = ArrayImgs.ints(flatVals, new long[] { 3, 3, 2 });

		DeviceJ device = DeviceJ.getDefaultDevice();
		device.setWaitForKernelFinish(true);
		ArrayJ in = ImgLib2Converters.copyImgLib2ToArrayJ(inputImg, device, MemoryType.BUFFER);
		RandomAccessibleInterval<IntType> outputImg = ImgLib2Converters.copyArrayJToImgLib2(in);

		Cursor<IntType> inCursor = inputImg.cursor();
		Cursor<IntType> outCursor = outputImg.cursor();

		int c = 0;
		while (inCursor.hasNext()) {
			inCursor.next();
			outCursor.next();
			assertEquals(inCursor.get().get(), outCursor.get().get());
			assertEquals(flatVals[c++], outCursor.get().get());
		}
	}

	@Test
	public void testImgLib2PushAndPullUint() {
		int[] flatVals = new int[18];
		for (int i = 0; i < flatVals.length; i++)
			flatVals[i] = Integer.MAX_VALUE + ThreadLocalRandom.current().nextInt(0, 100);
		RandomAccessibleInterval<UnsignedIntType> inputImg = ArrayImgs.unsignedInts(flatVals, new long[] { 3, 3, 2 });

		DeviceJ device = DeviceJ.getDefaultDevice();
		device.setWaitForKernelFinish(true);
		ArrayJ in = ImgLib2Converters.copyImgLib2ToArrayJ(inputImg, device, MemoryType.BUFFER);
		RandomAccessibleInterval<UnsignedIntType> outputImg = ImgLib2Converters.copyArrayJToImgLib2(in);

		Cursor<UnsignedIntType> inCursor = inputImg.cursor();
		Cursor<UnsignedIntType> outCursor = outputImg.cursor();

		int c = 0;
		while (inCursor.hasNext()) {
			inCursor.next();
			outCursor.next();
			long val = flatVals[c++];
			assertEquals(inCursor.get().get(), outCursor.get().get());
			assertEquals(val < 0 ? val + MAX_UINT32 : val, outCursor.get().get());
		}
	}

	@Test
	public void testImgLib2PushAndPullShort() {
		short[] flatVals = new short[18];
		for (int i = 0; i < flatVals.length; i++)
			flatVals[i] = (short) ThreadLocalRandom.current().nextInt(Short.MIN_VALUE, Short.MAX_VALUE + 1);

		RandomAccessibleInterval<ShortType> inputImg = ArrayImgs.shorts(flatVals, new long[] { 3, 3, 2 });

		DeviceJ device = DeviceJ.getDefaultDevice();
		device.setWaitForKernelFinish(true);
		ArrayJ in = ImgLib2Converters.copyImgLib2ToArrayJ(inputImg, device, MemoryType.BUFFER);
		RandomAccessibleInterval<ShortType> outputImg = ImgLib2Converters.copyArrayJToImgLib2(in);

		Cursor<ShortType> inCursor = inputImg.cursor();
		Cursor<ShortType> outCursor = outputImg.cursor();

		int c = 0;
		while (inCursor.hasNext()) {
			inCursor.next();
			outCursor.next();
			assertEquals(inCursor.get().get(), outCursor.get().get());
			assertEquals(flatVals[c++], outCursor.get().get());
		}
	}

	@Test
	public void testImgLib2PushAndPullUshort() {

		short[] flatVals = new short[18];
		for (int i = 0; i < flatVals.length; i++)
			flatVals[i] = (short) ThreadLocalRandom.current().nextInt(0, MAX_UINT16);

		RandomAccessibleInterval<UnsignedShortType> inputImg = ArrayImgs.unsignedShorts(flatVals,
				new long[] { 3, 3, 2 });

		DeviceJ device = DeviceJ.getDefaultDevice();
		device.setWaitForKernelFinish(true);
		ArrayJ in = ImgLib2Converters.copyImgLib2ToArrayJ(inputImg, device, MemoryType.BUFFER);
		RandomAccessibleInterval<UnsignedShortType> outputImg = ImgLib2Converters.copyArrayJToImgLib2(in);

		Cursor<UnsignedShortType> inCursor = inputImg.cursor();
		Cursor<UnsignedShortType> outCursor = outputImg.cursor();

		int c = 0;
		while (inCursor.hasNext()) {
			inCursor.next();
			outCursor.next();
			int val = flatVals[c++];
			assertEquals(inCursor.get().get(), outCursor.get().get());
			assertEquals(val < 0 ? MAX_UINT16 + val : val, outCursor.get().get());
		}
	}

	@Test
	public void testImgLib2PushAndPullByte() {
		byte[] flatVals = new byte[18];
		for (int i = 0; i < flatVals.length; i++)
			flatVals[i] = (byte) ThreadLocalRandom.current().nextInt(Byte.MIN_VALUE, Byte.MAX_VALUE + 1);

		RandomAccessibleInterval<ByteType> inputImg = ArrayImgs.bytes(flatVals, new long[] { 3, 3, 2 });

		DeviceJ device = DeviceJ.getDefaultDevice();
		device.setWaitForKernelFinish(true);
		ArrayJ in = ImgLib2Converters.copyImgLib2ToArrayJ(inputImg, device, MemoryType.BUFFER);
		RandomAccessibleInterval<ByteType> outputImg = ImgLib2Converters.copyArrayJToImgLib2(in);

		Cursor<ByteType> inCursor = inputImg.cursor();
		Cursor<ByteType> outCursor = outputImg.cursor();

		int c = 0;
		while (inCursor.hasNext()) {
			inCursor.next();
			outCursor.next();
			assertEquals(inCursor.get().get(), outCursor.get().get());
			assertEquals(flatVals[c++], outCursor.get().get());
		}
	}

	@Test
	public void testImgLib2PushAndPullUbyte() {
		byte[] flatVals = new byte[18];
		for (int i = 0; i < flatVals.length; i++)
			flatVals[i] = (byte) ThreadLocalRandom.current().nextInt(0, MAX_UINT8);

		RandomAccessibleInterval<UnsignedByteType> inputImg = ArrayImgs.unsignedBytes(flatVals, new long[] { 3, 3, 2 });

		DeviceJ device = DeviceJ.getDefaultDevice();
		device.setWaitForKernelFinish(true);
		ArrayJ in = ImgLib2Converters.copyImgLib2ToArrayJ(inputImg, device, MemoryType.BUFFER);
		RandomAccessibleInterval<UnsignedByteType> outputImg = ImgLib2Converters.copyArrayJToImgLib2(in);

		Cursor<UnsignedByteType> inCursor = inputImg.cursor();
		Cursor<UnsignedByteType> outCursor = outputImg.cursor();

		int c = 0;
		while (inCursor.hasNext()) {
			inCursor.next();
			outCursor.next();
			int val = flatVals[c++];
			assertEquals(inCursor.get().get(), outCursor.get().get());
			assertEquals(val < 0 ? MAX_UINT8 + val : val, outCursor.get().get());
		}
	}
}
