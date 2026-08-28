#!/usr/bin/env bash
# Scripts to build and install native C++ libraries
# Adapted from https://github.com/bytedeco/javacpp-presets
set -eu

# C++ backend for CLIc: OPENCL (default), CUDA, or METAL
if [[ -z "${CLE_BACKEND:-}" ]]; then
    CLE_BACKEND="OPENCL"
fi
echo "Using CLE_BACKEND=${CLE_BACKEND}"

if [[ -z "$PLATFORM" ]]; then
    pushd ..
    bash cppbuild.sh "$@" clic
    popd
    exit
fi

case $PLATFORM in
    linux-x86_64)
        $CMAKE -DCMAKE_BUILD_TYPE=Release \
               -DCMAKE_INSTALL_PREFIX="../../../lib/linux64/" \
               -DCLE_BACKEND=${CLE_BACKEND} ..
            #    -DCMAKE_CXX_COMPILER="/usr/bin/g++" \
            #    -DCMAKE_CUDA_HOST_COMPILER="/usr/bin/g++" \
            #    -DOPENCL_INCLUDE_DIR="/usr/local/cuda/include/" \
		# -DCLFFT_LIBRARY_DIR="/opt/OpenCL/clFFT-2.12.2-Linux-x64/lib64/" ..
        make -j$(nproc)
        make install
        ;;
    macosx-*)
        # the following line might not be necessary if make would be properly installed in the path
        # CMAKE=/Applications/CMake.app/Contents/bin/cmake

        $CMAKE -DCMAKE_BUILD_TYPE=Release \
               -DCMAKE_INSTALL_PREFIX="../../../lib/macosx/" \
               -DCLE_BACKEND=${CLE_BACKEND} ..
        #        -DCMAKE_CXX_COMPILER="g++" \
        #        -DCMAKE_CUDA_HOST_COMPILER="g++" \
		# -DCLFFT_LIBRARY_DIR="/Users/haase/Downloads/clfft-2.12.2-h83d4a3d_1/lib" ..
        make -j$(sysctl -n hw.ncpu)
        make install
        ;;
    windows-x86_64)
        $CMAKE -G"NMake Makefiles" \
		       -DCMAKE_BUILD_TYPE=Release \
               -DCMAKE_INSTALL_PREFIX="../../../lib/win64/"  \
               -DCLE_BACKEND=${CLE_BACKEND} ..
            #   -DOpenCL_INCLUDE_DIR="C:/Program Files (x86)/OCL_SDK_Light/include" \
            #   -DOpenCL_LIBRARY="C:/Program Files (x86)/OCL_SDK_Light/lib/x86_64/opencl.lib" ..
            #    -DCLIC_INCLUDE_DIR="../../../CLIc_prototype/clic/include/core" \
            #    -DOCLCL_INCLUDE_DIR="../../../CLIc_prototype/thirdparty/opencl/ocl-clhpp/include" \
		    #    -DCLFFT_LIBRARY_DIR="C:/OpenCL/clFFT-2.12.2-Windows-x64/lib64/import/" ..
        nmake
        nmake install
        ;;
    *)
        echo "Error: Platform \"$PLATFORM\" is not supported"
        ;;
esac
