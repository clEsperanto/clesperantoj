/*-
 * #%L
 * Java wrapper for Clesperanto
 * %%
 * Copyright (C) 2022 - 2026 Robert Haase, MPI CBG and Stephane Rigaud, Institut Pasteur
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
