import net.clesperanto.core.ArrayJ;
import net.clesperanto.core.DataType;
import net.clesperanto.core.DeviceJ;
import net.clesperanto.core.MemoryType;
import net.clesperanto.imglib2.ImgLib2Converters;
import net.imglib2.Cursor;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.integer.*;
import net.imglib2.type.numeric.real.FloatType;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestPullImgLib2 extends AbstractDeviceTest {

	public final static long MAX_UINT32 = (long) Math.pow(2, 32);

	public final static int MAX_INT32 = Integer.MAX_VALUE;

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
		RandomAccessibleInterval<FloatType> outputImg = ImgLib2Converters.copyArrayJToImgLib2(in);
		Cursor<FloatType> outCursor = outputImg.cursor();

		int c = 0;
		while (outCursor.hasNext()) {
			outCursor.next();
			assertEquals(flatVals[c++], outCursor.get().get());
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
		RandomAccessibleInterval<IntType> outputImg = ImgLib2Converters.copyArrayJToImgLib2(in);
		Cursor<IntType> outCursor = outputImg.cursor();

		int c = 0;
		while (outCursor.hasNext()) {
			outCursor.next();
			assertEquals(flatVals[c++], outCursor.get().get());
		}
	}

	@Test
	public void testPullUint() {

		int[] flatVals = new int[18];
		for (int i = 0; i < flatVals.length; i++)
			flatVals[i] = Integer.MAX_VALUE + ThreadLocalRandom.current().nextInt(0, 100);

		DeviceJ device = DeviceJ.getDefaultDevice();
		device.setWaitForKernelFinish(true);
		ArrayJ in = device.createArray(DataType.UINT32, MemoryType.BUFFER, 3, 3, 2);
		in.writeFromArray(flatVals);
		RandomAccessibleInterval<UnsignedIntType> outputImg = ImgLib2Converters.copyArrayJToImgLib2(in);
		Cursor<UnsignedIntType> outCursor = outputImg.cursor();

		int c = 0;
		while (outCursor.hasNext()) {
			outCursor.next();
			long val = flatVals[c++];
			assertEquals(val < 0 ? MAX_UINT32 + val : val, outCursor.get().get());
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
		RandomAccessibleInterval<ShortType> outputImg = ImgLib2Converters.copyArrayJToImgLib2(in);
		Cursor<ShortType> outCursor = outputImg.cursor();

		int c = 0;
		while (outCursor.hasNext()) {
			outCursor.next();
			short val = flatVals[c++];
			assertEquals(val, outCursor.get().get());
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
		RandomAccessibleInterval<UnsignedShortType> outputImg = ImgLib2Converters.copyArrayJToImgLib2(in);
		Cursor<UnsignedShortType> outCursor = outputImg.cursor();

		int c = 0;
		while (outCursor.hasNext()) {
			outCursor.next();
			short val = flatVals[c++];
			assertEquals(val < 0 ? MAX_UINT16 + val : val, outCursor.get().get());
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
		RandomAccessibleInterval<ByteType> outputImg = ImgLib2Converters.copyArrayJToImgLib2(in);
		Cursor<ByteType> outCursor = outputImg.cursor();

		int c = 0;
		while (outCursor.hasNext()) {
			outCursor.next();
			short val = flatVals[c++];
			assertEquals(val, outCursor.get().get());
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
		RandomAccessibleInterval<UnsignedByteType> outputImg = ImgLib2Converters.copyArrayJToImgLib2(in);
		Cursor<UnsignedByteType> outCursor = outputImg.cursor();

		int c = 0;
		while (outCursor.hasNext()) {
			outCursor.next();
			short val = flatVals[c++];
			assertEquals(val < 0 ? MAX_UINT8 + val : val, outCursor.get().get());
		}
	}
}
