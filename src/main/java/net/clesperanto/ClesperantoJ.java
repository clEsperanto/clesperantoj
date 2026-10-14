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
package net.clesperanto;

import net.clesperanto.core.*;
import net.clesperanto.kernels.Tier1;

import java.util.Arrays;
import java.util.List;

public class ClesperantoJ {

    public static void main(String[] args) {
        System.out.println("Hello World! Native Java");

        System.out.println(System.getProperty("java.library.path"));

        BackendJ.setBackend("opencl");
        // BackendJ.setBackend("cuda");

        List<String> deviceList = DeviceJ.getAvailableDevices();
        for (int i = 0; i < deviceList.size(); i++) {
            System.out.println(deviceList.get(i) + " is available");
        }

        DeviceJ currentDevice = DeviceJ.getDefaultDevice();
        System.out.println(currentDevice.getInfo());

        ArrayJ input = currentDevice.createArray(DataType.FLOAT32, MemoryType.BUFFER, 3, 3, 2);

        float data[] = new float[3 * 3 * 2];
        float out[] = new float[3 * 3 * 2];
        Arrays.fill(data, -5);
        Arrays.fill(out, -1);

        input.writeFromArray(data);

        ArrayJ output = Tier1.absolute(currentDevice, input, null);
        output.readToArray(out);

        for (int i = 0; i < out.length; i++) {
            System.out.println("out[" + i + "] = " + out[i]);
        }

    }

}
