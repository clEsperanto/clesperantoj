package net.clesperanto.core;

import net.clesperanto._internals.jclic.DTypeJ;

public enum DataType {
    INT8(DTypeJ.INT8, MemoryJ.BYTE),
    UINT8(DTypeJ.UINT8, MemoryJ.BYTE),
    INT16(DTypeJ.INT16, MemoryJ.SHORT),
    UINT16(DTypeJ.UINT16, MemoryJ.SHORT),
    INT32(DTypeJ.INT32, MemoryJ.INT),
    UINT32(DTypeJ.UINT32, MemoryJ.INT),
    FLOAT32(DTypeJ.FLOAT, MemoryJ.FLOAT);

    private final DTypeJ dTypeJ;
    private final MemoryJ<?, ?> memory;

    DataType(DTypeJ dTypeJ, MemoryJ<?, ?> memory) {
        this.dTypeJ = dTypeJ;
        this.memory = memory;
    }

    public DTypeJ getRaw() {
        return dTypeJ;
    }

    public MemoryJ<?, ?> memory() {
        return memory;
    }

    public static DataType fromDType(final DTypeJ dtype) {
        for (DataType t : values()) {
            if (t.getRaw().value == dtype.value) {
                return t;
            }
        }
        throw new IllegalArgumentException("Unexpected DTypeJ: " + dtype);
    }
}
