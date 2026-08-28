/*-
 * #%L
 * Java wrapper for Clesperanto
 * %%
 * Copyright (C) 2022 - 2025 Robert Haase, MPI CBG and Stephane Rigaud, Institut Pasteur
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
/*
 * Copyright 2024 Stéphane Rigaud, Robert Haase, Institut Pasteur Paris,
 * Max Planck Institute for Molecular Cell Biology and Genetics Dresden,
 * ScaDS.AI, Leipzig University
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package net.clesperanto.core;

import java.util.ArrayList;
import java.util.HashMap;

import net.clesperanto._internals.jclic._StringVector;
import net.clesperanto._internals.jclic._FloatVector;
import net.clesperanto._internals.jclic._IntVector;
import net.clesperanto._internals.jclic._ArrayJVector;
import net.clesperanto._internals.jclic._FloatVectorMap;
import net.clesperanto._internals.jclic._UtilsJ;

public class Utils {

	public static ArrayList<String> toArrayList(_StringVector vector) {
		ArrayList<String> arr = new ArrayList<String>((int) vector.size());
		for (int i = 0; i < vector.size(); i++) {
			arr.add(vector.get(i));
		}
		return arr;
	}

	public static ArrayList<Float> toArrayList(_FloatVector vector) {
		ArrayList<Float> arr = new ArrayList<Float>((int) vector.size());
		for (int i = 0; i < vector.size(); i++) {
			arr.add(vector.get(i));
		}
		return arr;
	}

	public static ArrayList<Integer> toArrayList(_IntVector vector) {
		ArrayList<Integer> arr = new ArrayList<Integer>((int) vector.size());
		for (int i = 0; i < vector.size(); i++) {
			arr.add(vector.get(i));
		}
		return arr;
	}

	public static ArrayList<ArrayJ> toArrayList(_ArrayJVector vector) {
		ArrayList<ArrayJ> arr = new ArrayList<ArrayJ>((int) vector.size());
		for (int i = 0; i < vector.size(); i++) {
			arr.add(new ArrayJ(vector.get(i)));
		}
		return arr;
	}

	public static HashMap<String, ArrayList<Float>> toHashMap(_FloatVectorMap map) {
		HashMap<String, ArrayList<Float>> hashMap = new HashMap<String, ArrayList<Float>>((int) map.size());
		ArrayList<String> keys = toArrayList(_UtilsJ.getKeys(map));
		for (String string : keys) {
			hashMap.put(string, toArrayList(map.get(string)));
		}
		return hashMap;
	}

	public static _FloatVectorMap toFloatVectorMap(HashMap<String, ArrayList<Float>> hashMap) {
		_FloatVectorMap map = new _FloatVectorMap();
		for (String key : hashMap.keySet()) {
			map.put(key, toFloatVector(hashMap.get(key)));
		}
		return map;
	}

	public static _FloatVector toFloatVector(ArrayList<Float> array) {
		Float[] floatArray = new Float[array.size()];
		floatArray = array.toArray(floatArray);
		_FloatVector vector = new _FloatVector(array.size());
		for (int i = 0; i < array.size(); i++) {
			vector.put(i, floatArray[i]);
		}
		return vector;
	}

	public static _IntVector toIntVector(ArrayList<Integer> array) {
		Integer[] intArray = new Integer[array.size()];
		intArray = array.toArray(intArray);
		_IntVector vector = new _IntVector(array.size());
		for (int i = 0; i < array.size(); i++) {
			vector.put(i, intArray[i]);
		}
		return vector;
	}


}
