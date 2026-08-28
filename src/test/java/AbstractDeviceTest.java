import net.clesperanto.core.DeviceJ;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;

/**
 * Base class for tests that require a GPU/OpenCL device.
 *
 * Some CI environments do not have any GPU or OpenCL/CUDA backend available.
 * Rather than failing the build in that case, tests extending this class are
 * skipped (reported as "aborted", not failed) with a warning explaining why.
 */
public abstract class AbstractDeviceTest {

    @BeforeAll
    public static void checkDeviceAvailable() {
        boolean deviceAvailable;
        try {
            deviceAvailable = !DeviceJ.getAvailableDevices().isEmpty();
        } catch (Throwable t) {
            deviceAvailable = false;
        }

        if (!deviceAvailable) {
            System.err.println("WARNING: No GPU/OpenCL device or backend found. Skipping test.");
        }

        Assumptions.assumeTrue(deviceAvailable, "No GPU/OpenCL device or backend found. Skipping test.");
    }
}
