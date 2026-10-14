package net.clesperanto;

import net.clesperanto.core.AffineTransform;
import net.clesperanto.core.ArrayJ;
import net.clesperanto.core.DeviceJ;
import net.clesperanto.core.MemoryType;
import net.clesperanto.imglib2.ImgLib2Converters;
import net.clesperanto.kernels.Transform;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.type.numeric.real.FloatType;

import java.util.Arrays;

public class Imglib2ClesperantoJExampleTransform {

    public static void main(String[] args) {
        System.out.println("Hello World! Native Java");
        System.out.println(System.getProperty("java.library.path"));

        DeviceJ currentDevice = DeviceJ.getDefaultDevice();

        // create a image with 3x3x2 pixels in imglib2
        float data[] = new float[3 * 3 * 2];
        Arrays.fill(data, -5);
        Img<FloatType> input_img = ArrayImgs.floats(data, new long[] { 3, 3, 2 });

        ArrayJ input = ImgLib2Converters.copyImgLib2ToArrayJ(input_img, currentDevice, MemoryType.BUFFER);

        // build an affine transform (translate then rotate around the z axis) and apply it
        AffineTransform transform = new AffineTransform();
        transform.translate(1, 1, 0);
        transform.rotateAroundZAxis(45);

        ArrayJ transformed = Transform.affineTransform(currentDevice, input, null, transform, true, false);

        System.out.println("Transformed:" + transformed.toString());
        Img<FloatType> transformed_img = ImgLib2Converters.copyArrayJToImgLib2(transformed);
        transformed_img.cursor().forEachRemaining(t -> System.out.println(t.get()));
    }

}
