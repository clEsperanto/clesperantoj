package net.clesperanto.core;

/*
 * TODO think about possible new methods
 *
 * - method to modify the data inside the array from this class
 * - access a certain position of the array
 * - Do we check here if the size of the destination array is compatible for the method {@link #copyDataTo(ArrayJ)}
 *
 */

import net.clesperanto._internals.jclic._ArrayJ;

import java.util.Objects;

/**
 * Class to interact with the arrays allocated on the GPU by ClesperantoJ
 */
public class ArrayJ {

    private final _ArrayJ _arrayj;

    private final DataType dataType;
    private final MemoryType memoryType;
    private final DeviceJ device;

    private final long width;
    private final long height;
    private final long depth;

    private final int numDimensions;

    // TODO replace by method static ArrayJ wrapRaw(_ArrayJ)
    public ArrayJ(final _ArrayJ _arrayj) {
        this._arrayj = _arrayj;

        dataType = DataType.fromDType(_arrayj.dtype());
        memoryType = MemoryType.fromMType(_arrayj.mtype());
        device = new DeviceJ(_arrayj.device());

        width = _arrayj.getWidth();
        height = _arrayj.getHeight();
        depth = _arrayj.getDepth();

        numDimensions = _arrayj.getDimension();
    }

    // // TODO: remove after adapting Tier methods
    // public ArrayJ(final _ArrayJ _arrayj, Object IGNORED_just_so_that_Tier_methods_work_unmodified_REMOVE_LATER) {
    //     this(_arrayj);
    // }

    // TODO: make package-private?
    public ArrayJ(final long width, final long height, final long depth, final int numDimensions,
                  final DeviceJ device, final DataType dataType, final MemoryType memoryType) {
        checkDimensions(width, height, depth, numDimensions);
        this._arrayj = _ArrayJ.create(width, height, depth, numDimensions, dataType.getRaw(), memoryType.getRaw(), device.getRaw());
        this.dataType = dataType;
        this.memoryType = memoryType;
        this.device = device;
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.numDimensions = numDimensions;
    }

    private static void checkDimensions(
            final long width, final long height, final long depth, final int numDimensions)
            throws IllegalArgumentException {
        if (width <= 0)
            throw new IllegalArgumentException("width must be greater than zero");
        if (height <= 0)
            throw new IllegalArgumentException("height must be greater than zero");
        if (depth <= 0)
            throw new IllegalArgumentException("depth must be greater than zero");
        if (numDimensions <= 0 || numDimensions > 3)
            throw new IllegalArgumentException("numDimensions must be between 1 and 3");
        if (numDimensions < 3 && depth != 1)
            throw new IllegalArgumentException("1D and 2D images must have depth==1");
        if (numDimensions < 2 && height != 1)
            throw new IllegalArgumentException("1D images must have height==1");
    }

    // TODO: rather do int... varargs for size at the end
    //       and probably as DeviceJ::createArray(DataType, MemoryType, int...)
    public ArrayJ(final int[] size, final DeviceJ device, final DataType dataType, final MemoryType memoryType) {
        this(size[0], size.length > 1 ? size[1] : 1, size.length > 2 ? size[2] : 1, size.length, device, dataType, memoryType);
    }

    public ArrayJ(final long[] size, final DeviceJ device, final DataType dataType, final MemoryType memoryType) {
        this(size[0], size.length > 1 ? size[1] : 1, size.length > 2 ? size[2] : 1, size.length, device, dataType, memoryType);
    }

    /**
     * @return the width of the array
     */
    public long width() {
        return width;
    }

    /**
     * @return the height of the array
     */
    public long height() {
        return height;
    }

    /**
     * @return the depth of the array
     */
    public long depth() {
        return depth;
    }

    /**
     * @return the number of dimensions of the array
     */
    public int numDimensions() {
        return numDimensions;
    }

    /**
     * @return the data type of the array
     */
    public DataType dataType() {
        return dataType;
    }

    /**
     * Get the type of object that the array represents (either an 'image' or a 'buffer').
     *
     * @return the memory type of the array
     */
    public MemoryType memoryType() {
        return memoryType;
    }

    /**
     * @return the device object that represents where the arrayJ is loaded
     */
    public DeviceJ device() {
        return device;
    }

    /**
     * Fill the array in the GPU with the wanted value.
     * All the positions in the array will be set to the same value
     *
     * @param value the value that every position in the array will adopt
     */
    public void fillMemory(float value) {
        _arrayj.fillMemory(value);
    }

    /*
     * Copy the data from this array to the destination array {@code dst}.
     *
     * @param dst
     * 	the array into which the current array will be copied
     */
    public void copyDataTo(ArrayJ dst) {
        _arrayj.copyDataTo(dst._arrayj);
    }

    /**
     * @return the raw object that is going to be sent to the native Clesperanto library. Without Java wrappers
     */
    public _ArrayJ getRaw() {
        return _arrayj;
    }

    /**
     * TODO javadoc
     *
     * @param data a primitive array matching the datatype of this array
     */
    public void readToArray(Object data) {
        dataType.memory().readToArray(this, data, 0, 0, 0, width, height, depth);
    }

    /**
     * TODO javadoc
     *
     * @param data a {@code Buffer} matching the datatype of this array
     */
    public void readToBuffer(Object data) {
        dataType.memory().readToBuffer(this, data, 0, 0, 0, width, height, depth);
    }

    /**
     * TODO javadoc
     *
     * @param data a primitive array matching the datatype of this array
     */
    public void writeFromArray(Object data) {
        dataType.memory().writeFromArray(this, data, 0, 0, 0, width, height, depth);
    }

    /**
     * TODO javadoc
     *
     * @param data a {@code Buffer} matching the datatype of this array
     */
    public void writeFromBuffer(Object data) {
        dataType.memory().writeFromBuffer(this, data, 0, 0, 0, width, height, depth);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArrayJ)) return false;
        ArrayJ arrayJ = (ArrayJ) o;
        return Objects.equals(_arrayj, arrayJ._arrayj);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(_arrayj);
    }

    /**
     * @return a string representation of the array information
     */
    @Override
    public String toString() {
        return "cle::Array[(" + width + ", " + height + ", " + depth + "), dtype=" + dataType + ", mtype=" + memoryType + "]";
    }
}
