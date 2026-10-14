import net.clesperanto.core.ArrayJ;
import net.clesperanto.core.DataType;
import net.clesperanto.core.DeviceJ;
import net.clesperanto.core.MemoryType;
import net.clesperanto.kernels.Tier1;
import org.junit.jupiter.api.Test;

import java.nio.IntBuffer;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestAbsolute extends AbstractDeviceTest {

    @Test
    public void testAbsolute() {
        DeviceJ device = DeviceJ.getDefaultDevice();
        device.setWaitForKernelFinish(true);

        ArrayJ in = device.createArray(DataType.INT32, MemoryType.BUFFER, 2, 2);
        in.fillMemory(-1);
        ArrayJ out = device.createArray(DataType.INT32, MemoryType.BUFFER, 2, 2);
        out.fillMemory(-1);
        Tier1.absolute(device, in, out);

        int[] result = new int[4];
        IntBuffer resultBuff = IntBuffer.wrap(result);
        out.readToBuffer(resultBuff);

        assertEquals(1, Arrays.stream(result).min().getAsInt());
        assertEquals(1, Arrays.stream(result).max().getAsInt());
        assertEquals(1, Arrays.stream(result).average().getAsDouble());
    }

    @Test
    public void testAbsolute1() {
        DeviceJ device = DeviceJ.getDefaultDevice();
        device.setWaitForKernelFinish(true);
        ArrayJ in = device.createArray(DataType.INT32, MemoryType.BUFFER, 2, 2);
        in.fillMemory(-1);
        ArrayJ out = Tier1.absolute(device, in, null);

        int[] result = new int[4];
        IntBuffer resultBuff = IntBuffer.wrap(result);
        out.readToBuffer(resultBuff);

        for (int val : result)
            assertEquals(1, val);
        in = null;
        out = null;
    }
}
