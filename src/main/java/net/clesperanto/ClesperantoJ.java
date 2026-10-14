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
