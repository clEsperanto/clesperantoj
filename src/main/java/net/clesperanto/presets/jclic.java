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
                new Info("AffineTransformJ")
                        .javaNames("_AffineTransformJ"));
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
                        "cle::transform::AffineTransform",
                        "toArrayJVector",
                        "std::shared_ptr<cle::Array>")
                        .skip());
    }
}
