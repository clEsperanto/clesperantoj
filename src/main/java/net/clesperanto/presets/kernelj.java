package net.clesperanto.presets;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.annotation.Platform;
import org.bytedeco.javacpp.annotation.Properties;
import org.bytedeco.javacpp.tools.InfoMap;
import org.bytedeco.javacpp.tools.InfoMapper;

@Properties(inherit = jclic.class, value = {
        @Platform(compiler = { "cpp17" }, define = { "SHARED_PTR_NAMESPACE std" }, include = {
                "kernelj.hpp" }, link = { "JCLIc" })
}, target = "net.clesperanto._internals.kernelj")

public class kernelj implements InfoMapper {
    static {
        Loader.load();
    }

    public void map(InfoMap infoMap) {

    }
}
