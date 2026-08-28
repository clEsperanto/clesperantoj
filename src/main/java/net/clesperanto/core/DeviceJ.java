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
import java.util.List;
import java.util.Objects;

import net.clesperanto._internals.jclic._DeviceJ;
import net.clesperanto._internals.jclic._StringVector;

/**
 * Class to interact with the divide that is going to be used to do the
 * operations
 */
public class DeviceJ {

	private final _DeviceJ _deviceJ;

	/**
	 * Constructor that initializes the default device
	 * IMPORTANT: Does not initialize the backend.
	 *
	 *
	 */
	private DeviceJ() {
		_deviceJ = new _DeviceJ();
	}

	/**
	 * Constructor that initializes the wanted device
	 * IMPORTANT: Does not initialize the backend.
	 *
	 * @param deviceName
	 *                   the full or partial name of the device that wants to be initialized,
	 *                   if multiple devices fit the name, the first of the list is initialized.
	 * 					 e.g. "NVIDIA RTX 4090" or "NVIDIA" or "RTX"
	 * @param deviceType
	 *                   the type of device that wants to be initialized, "cpu" or "gpu". If any type works,
	 *                   the argument should be "all"
	 */
	private DeviceJ(String deviceName, String deviceType) {
		Objects.requireNonNull(deviceName, "The device name cannot be null");
		Objects.requireNonNull(deviceType, "The device type cannot be null, if any device type works, use \"all\"");
		_deviceJ = new _DeviceJ();
		_deviceJ.setDevice(deviceName, deviceType);
	}

	DeviceJ(_DeviceJ _deviceJ) {
		this._deviceJ = _deviceJ;
	}

	/**
	 * Get the first device available. By default this method tries to use an OpenCL
	 * backend.
	 * If not OpenCL backend is available the method will fail.
	 * 
	 * @return the default device where Clesperanto operations can be done.
	 */
	public static DeviceJ getDefaultDevice() {
		BackendJ.setBackend("");
		return new DeviceJ();
	}

	/**
	 * Get the first device available and select the wanted backend.
	 * If the wanted backend is not available, the method will fall back to OpenCL.
	 * And if OpenCL is not available either the method will fail.
	 * 
	 * @param backend
	 *                the type of backend that wants to be used. It should be either
	 *                "cuda" or "opencl",
	 *                if it is anything else it will be set to "opencl"
	 * @return the default device where Clesperanto operations can be done.
	 */
	public static DeviceJ getDefaultDevice(String backend) {
		BackendJ.setBackend(backend);
		return new DeviceJ();
	}

	/**
	 * Get the wanted device by its name and device type. Initialize the device with
	 * openCL backend.
	 * If not OpenCL backend is available the method will fail.
	 *
	 * @param deviceName
	 *                   the full or partial name of the device that wants to be initialized,
	 *                   if multiple devices fit the name, the first of the list is initialized.
	 * 					 e.g. "NVIDIA RTX 4090" or "NVIDIA" or "RTX"
	 * @param deviceType
	 *                   the type of device that wants to be initialized, "cpu" or "gpu". If any type works,
	 *                   the argument should be "all"
	 * @return the wanted device where Clesperanto operations can be done.
	 */
	public static DeviceJ getDeviceWithDefaultBackend(String deviceName, String deviceType) {
		BackendJ.setBackend("");
		return new DeviceJ(deviceName, deviceType);
	}

	/**
	 * Get the wanted device by its name and device type. Initialize the device with
	 * the wanted backend. If the backend is not available it will fallback to
	 * OpenCl backend.
	 * If OpenCL backend is not available the method will fail.
	 *
	 * @param deviceName
	 *                   the full or partial name of the device that wants to be initialized,
	 *                   if multiple devices fit the name, the first of the list is initialized.
	 * 					 e.g. "NVIDIA RTX 4090" or "NVIDIA" or "RTX"
	 * @param deviceType
	 *                   the type of device that wants to be initialized, "cpu" or "gpu". If any type works,
	 *                   the argument should be "all"
	 * @param backend
	 *                   the type of backend that wants to be used. It should be
	 *                   either "cuda" or "opencl",
	 *                   if it is anything else it will be set to "opencl"
	 * @return the wanted device where Clesperanto operations can be done.
	 */
	public static DeviceJ getDevice(String deviceName, String deviceType, String backend) {
		BackendJ.setBackend(backend);
		return new DeviceJ(deviceName, deviceType);
	}

	/**
	 *
	 * @return the name of this device
	 */
	public String getName() {
		return this._deviceJ.getName();
	}

	/**
	 *
	 * @return the info about this device
	 */
	public String getInfo() {
		return this._deviceJ.getInfo();
	}

	/**
	 *
	 * @param wait if true, the device will wait for the kernel to finish
	 */
	public void setWaitForKernelFinish(boolean wait) {
		this._deviceJ.setWaitForKernelFinish(wait);
	}

	/**
	 * Method that returns the available compatible devices on the computer.
	 * Devices types can be GPUs or CPUs.
	 *
	 * @return a list of the available devices in the computer
	 */
	public static List<String> getAvailableDevices() {
		_StringVector devices = _DeviceJ.getAvailableDevices();
		List<String> devicesList = new ArrayList<String>();
		for (int i = 0; i < devices.size(); i++) {
			devicesList.add(devices.get(i));
		}
		return devicesList;
	}

	/**
	 * Method that returns the available compatible devices of the given
	 * {@code deviceType} on the computer, e.g. "gpu", "cpu", "all".
	 * Using the {@code deviceType} "all" returns all the devices available, it is
	 * the same as using {@link #getAvailableDevices()}.
	 *
	 * @param deviceType the type of device to look for e.g. "gpu", "cpu", "all".
	 * @return a list of the available devices in the computer of the specific type
	 */
	public static List<String> getAvailableDevices(String deviceType) {
		Objects.requireNonNull(deviceType, "The device type cannot be null, if any device type works, use \"all\" or"
				+ " use the method \"DeviceJ.getAvailableDevices()\"");
		_StringVector devices = _DeviceJ.getAvailableDevices(deviceType);
		List<String> devicesList = new ArrayList<String>();
		for (int i = 0; i < devices.size(); i++) {
			devicesList.add(devices.get(i));
		}
		return devicesList;
	}

	/**
	 * Return the backend that the device is using.
	 * 
	 * @return the backend (opencl, cuda) that the device is using
	 * @throws RuntimeException if there is any error finding the backend
	 */
	public String getBackend() {
		String info = this.getInfo();

		int ind = info.indexOf(")");

		if (ind == -1)
			throw new RuntimeException("Unable to retrieve backend");

		return info.substring(1, ind).toLowerCase();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof DeviceJ))
			return false;
		DeviceJ deviceJ = (DeviceJ) o;
		return _deviceJ.equals(deviceJ._deviceJ);
		// NB: _DeviceJ.equals method is a native method overloading (not
		// overriding) Object.equals() Therefore, Objects.equals(_deviceJ,
		// deviceJ._deviceJ) will *not* work here!
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(_deviceJ);
	}

	@Override
	public String toString() {
		return "DeviceJ{\"" + getName() + "\"}";
	}

	public ArrayJ createArray(final DataType dataType, final MemoryType memoryType, final int... size) {
		final int n = size.length;
		final int w = n > 0 ? size[0] : 1;
		final int h = n > 1 ? size[1] : 1;
		final int d = n > 2 ? size[2] : 1;
		return new ArrayJ(w, h, d, n, this, dataType, memoryType);
	}

	public ArrayJ createArray(final DataType dataType, final MemoryType memoryType, final long... size) {
		final int n = size.length;
		final long w = n > 0 ? size[0] : 1;
		final long h = n > 1 ? size[1] : 1;
		final long d = n > 2 ? size[2] : 1;
		return new ArrayJ(w, h, d, n, this, dataType, memoryType);
	}

	/**
	 *
	 * @return the raw object that is going to be sent to the native Clesperanto
	 *         library. Without Java wrappers
	 */
	public _DeviceJ getRaw() {
		return this._deviceJ;
	}
}
