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

package net.clesperanto.presets;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.annotation.Platform;
import org.bytedeco.javacpp.annotation.Properties;
import org.bytedeco.javacpp.presets.javacpp;
import org.bytedeco.javacpp.tools.Info;
import org.bytedeco.javacpp.tools.InfoMap;
import org.bytedeco.javacpp.tools.InfoMapper;

//
// @Properties(inherit = javacpp.class, value = {
// 		@Platform(compiler = { "cpp17", "-framework OpenCL" }, define = { "SHARED_PTR_NAMESPACE std" }, include = {
// 				"clesperantoj.hpp" }, includepath = {
// 						"/usr/local/cuda-12.3/targets/x86_64-linux/include" }, linkpath = {
// 								"/usr/local/cuda-12.3/targets/x86_64-linux/lib" }, link = { "JCLIc", "OpenCL" })
// }, target = "net.clesperanto.wrapper.clesperantoj")

@Properties(
        inherit = javacpp.class,
        value = {
                @Platform(value = "windows",
                        include = "clesperantoj.hpp", link = {"JCLIc", "Shell32"}),
                @Platform(value = {"linux", "macosx"},
                        include = "clesperantoj.hpp", link = "JCLIc")},
        target = "net.clesperanto._internals.jclic")
public class jclic implements InfoMapper {
    static {
        Loader.load();
    }

    public void map(InfoMap infoMap) {

        infoMap.put(
                new Info("std::size_t")
                        .cast()
                        .valueTypes("long")
                        .pointerTypes("SizeTPointer"));
        infoMap.put(
                new Info("std::string")
                        .annotations("@StdString")
                        .valueTypes("String", "BytePointer")
                        .pointerTypes("@Cast({\"char*\", \"std::string*\"}) BytePointer"));
        infoMap.put(
                new Info("std::vector<std::string>")
                        .pointerTypes("_StringVector")
                        .define());
        infoMap.put(
                new Info("std::vector<float>")
                        .pointerTypes("_FloatVector")
                        .define());
        infoMap.put(
                new Info("std::vector<int>")
                        .pointerTypes("_IntVector")
                        .define());
        infoMap.put(
                new Info("std::unordered_map<std::string,std::vector<float> >")
                        .pointerTypes("_FloatVectorMap")
                        .define());
        infoMap.put(
                new Info("std::vector<ArrayJ>")
                        .pointerTypes("_ArrayJVector")
                        .define());
        infoMap.put(
                new Info("ArrayJ")
                        .javaNames("_ArrayJ"));
        infoMap.put(
                new Info("BackendJ")
                        .javaNames("_BackendJ"));
        infoMap.put(
                new Info("DeviceJ")
                        .javaNames("_DeviceJ"));
        infoMap.put(
                new Info("MemoryJ")
                        .javaNames("_MemoryJ"));
        infoMap.put(
                new Info("UtilsJ")
                        .javaNames("_UtilsJ"));
        infoMap.put(
                new Info("DTypeJ")
                        .enumerate());
        infoMap.put(
                new Info("MTypeJ")
                        .enumerate());
        infoMap.put(
                new Info(
                        "cle::Array",
                        "cle::Device",
                        "cle::BackendManager",
                        "cle::Backend",
                        "toArrayJVector",
                        "std::shared_ptr<cle::Array>")
                        .skip());
    }
}
