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
package net.clesperanto.core;

import net.clesperanto._internals.jclic;

import java.nio.*;
import java.util.function.IntFunction;

public final class MemoryJ<A, B extends Buffer> {

    public static final MemoryJ<byte[], ByteBuffer> BYTE = new MemoryJ<>(
            byte[].class, jclic._MemoryJ::readToByte, jclic._MemoryJ::writeFromByte, byte[]::new,
            ByteBuffer.class, jclic._MemoryJ::readToByte, jclic._MemoryJ::writeFromByte, ByteBuffer::allocateDirect);
    public static final MemoryJ<short[], ShortBuffer> SHORT = new MemoryJ<>(
            short[].class, jclic._MemoryJ::readToShort, jclic._MemoryJ::writeFromShort, short[]::new,
            ShortBuffer.class, jclic._MemoryJ::readToShort, jclic._MemoryJ::writeFromShort, MemoryJ::allocateShortBuffer);
    public static final MemoryJ<int[], IntBuffer> INT = new MemoryJ<>(
            int[].class, jclic._MemoryJ::readToInt, jclic._MemoryJ::writeFromInt, int[]::new,
            IntBuffer.class, jclic._MemoryJ::readToInt, jclic._MemoryJ::writeFromInt, MemoryJ::allocateIntBuffer);
    public static final MemoryJ<float[], FloatBuffer> FLOAT = new MemoryJ<>(
            float[].class, jclic._MemoryJ::readToFloat, jclic._MemoryJ::writeFromFloat, float[]::new,
            FloatBuffer.class, jclic._MemoryJ::readToFloat, jclic._MemoryJ::writeFromFloat, MemoryJ::allocateFloatBuffer);

    private final Class<A> arrayClass;
    private final MemoryReadFunction<A> readToArray;
    private final MemoryWriteFunction<A> writeFromArray;
    private final IntFunction<A> createArray;

    private final Class<B> bufferClass;
    private final MemoryReadFunction<B> readToBuffer;
    private final MemoryWriteFunction<B> writeFromBuffer;
    private final IntFunction<B> createBuffer;

    private MemoryJ(
            final Class<A> arrayClass,
            final MemoryReadFunction<A> readToArray,
            final MemoryWriteFunction<A> writeFromArray,
            final IntFunction<A> createArray,
            final Class<B> bufferClass,
            final MemoryReadFunction<B> readToBuffer,
            final MemoryWriteFunction<B> writeFromBuffer,
            final IntFunction<B> createBuffer
    ) {
        this.arrayClass = arrayClass;
        this.readToArray = readToArray;
        this.writeFromArray = writeFromArray;
        this.createArray = createArray;
        this.bufferClass = bufferClass;
        this.readToBuffer = readToBuffer;
        this.writeFromBuffer = writeFromBuffer;
        this.createBuffer = createBuffer;
    }

    @SuppressWarnings("unchecked")
    public void readToArray(ArrayJ arrayj, Object data, long origin_x, long origin_y, long origin_z, long width, long height, long depth) {
        if (!(arrayClass.isInstance(data)))
            throw new IllegalArgumentException("Data is not of type " + arrayClass.getSimpleName());
        readToArray.read(arrayj.getRaw(), (A) data, origin_x, origin_y, origin_z, width, height, depth);
    }

    @SuppressWarnings("unchecked")
    public void readToBuffer(ArrayJ arrayj, Object data, long origin_x, long origin_y, long origin_z, long width, long height, long depth) {
        if (!(bufferClass.isInstance(data)))
            throw new IllegalArgumentException("Data is not of type " + bufferClass.getName());
        readToBuffer.read(arrayj.getRaw(), (B) data, origin_x, origin_y, origin_z, width, height, depth);
    }

    @SuppressWarnings("unchecked")
    public void writeFromArray(ArrayJ arrayj, Object data, long origin_x, long origin_y, long origin_z, long width, long height, long depth) {
        if (!(arrayClass.isInstance(data)))
            throw new IllegalArgumentException("Data is not of type " + arrayClass.getSimpleName());
        writeFromArray.write(arrayj.getRaw(), (A) data, origin_x, origin_y, origin_z, width, height, depth);
    }

    @SuppressWarnings("unchecked")
    public void writeFromBuffer(ArrayJ arrayj, Object data, long origin_x, long origin_y, long origin_z, long width, long height, long depth) {
        if (!(bufferClass.isInstance(data)))
            throw new IllegalArgumentException("Data is not of type " + bufferClass.getName());
        writeFromBuffer.write(arrayj.getRaw(), (B) data, origin_x, origin_y, origin_z, width, height, depth);
    }

    public A createArray(final int numElements) {
        return createArray.apply(numElements);
    }

    public B createBuffer(final int numElements) {
        return createBuffer.apply(numElements);
    }

    @FunctionalInterface
    private interface MemoryReadFunction<T> {
        void read(jclic._ArrayJ array, T data, long origin_x, long origin_y, long origin_z, long width, long height, long depth);
    }

    @FunctionalInterface
    private interface MemoryWriteFunction<T> {
        void write(jclic._ArrayJ array, T data, long origin_x, long origin_y, long origin_z, long width, long height, long depth);
    }

    private static ShortBuffer allocateShortBuffer(final int n) {
        final long numBytes = 2L * n;
        if (numBytes > Integer.MAX_VALUE)
            return ShortBuffer.allocate(n);
        else
            return ByteBuffer.allocateDirect((int) numBytes).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer();
    }

    private static IntBuffer allocateIntBuffer(final int n) {
        final long numBytes = 4L * n;
        if (numBytes > Integer.MAX_VALUE)
            return IntBuffer.allocate(n);
        else
            return ByteBuffer.allocateDirect((int) numBytes).order(ByteOrder.LITTLE_ENDIAN).asIntBuffer();
    }

    private static FloatBuffer allocateFloatBuffer(final int n) {
        final long numBytes = 4L * n;
        if (numBytes > Integer.MAX_VALUE)
            return FloatBuffer.allocate(n);
        else
            return ByteBuffer.allocateDirect((int) numBytes).order(ByteOrder.LITTLE_ENDIAN).asFloatBuffer();
    }
}
