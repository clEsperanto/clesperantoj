package net.clesperanto.core;

import net.clesperanto._internals.jclic.MTypeJ;

public enum MemoryType {
    BUFFER(MTypeJ.BUFFER),
    IMAGE(MTypeJ.IMAGE);

    private final MTypeJ mTypeJ;

    MemoryType(MTypeJ mTypeJ) {
        this.mTypeJ = mTypeJ;
    }

    public MTypeJ getRaw() {
        return mTypeJ;
    }

    public static MemoryType fromMType(final MTypeJ mtype) {
        for (MemoryType t : values()) {
            if (t.getRaw().value == mtype.value) {
                return t;
            }
        }
        throw new IllegalArgumentException("Unexpected MTypeJ: " + mtype);
    }
}
