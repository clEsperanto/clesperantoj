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
