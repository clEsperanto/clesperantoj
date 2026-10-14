package net.clesperanto;

import net.clesperanto.core.ArrayJ;
import net.clesperanto.core.DeviceJ;
import net.clesperanto.core.MemoryType;
import net.clesperanto.imglib2.ImgLib2Converters;
import net.clesperanto.kernels.Tier1;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.type.numeric.real.FloatType;

import java.util.Arrays;

public class Imglib2ClesperantoJExample {

    public static void main(String[] args) {
        System.out.println("Hello World! Native Java");
        System.out.println(System.getProperty("java.library.path"));

        DeviceJ currentDevice = DeviceJ.getDefaultDevice();

        // create a image with 3x3x2 pixels in imglib2
        float data[] = new float[3 * 3 * 2];
        float out[] = new float[3 * 3 * 2];
        Arrays.fill(data, -5);
        Arrays.fill(out, -1);
        Img<FloatType> input_img = ArrayImgs.floats(data, new long[] { 3, 3, 2 });
        Img<FloatType> output_img = ArrayImgs.floats(out, new long[] { 3, 3, 2 });

        ArrayJ input = ImgLib2Converters.copyImgLib2ToArrayJ(input_img, currentDevice, MemoryType.BUFFER);
        ArrayJ output = Tier1.absolute(currentDevice, input, null);

        System.out.println("Input:" + input.toString());
        System.out.println("Output:" + output.toString());

        output_img = ImgLib2Converters.copyArrayJToImgLib2(output);
        output_img.cursor().forEachRemaining(t -> System.out.println(t.get()));

        Float sum = Tier1.sumOfAllPixels(currentDevice, output);
        System.out.println("Sum of all pixels: " + sum);
    }

}
