[![Build Status](https://github.com/clEsperanto/clesperantoj_prototype/actions/workflows/build.yaml/badge.svg)](https://github.com/clEsperanto/clesperantoj_prototype/actions/workflows/build.yaml)
[![Image.sc Forum](https://img.shields.io/badge/dynamic/json.svg?label=forum&amp;url=https%3A%2F%2Fforum.image.sc%2Ftags%2Fclesperanto.json&amp;query=%24.topic_list.tags.0.topic_count&amp;colorB=green&amp;&amp;suffix=%20topics&amp;logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAOCAYAAAAfSC3RAAABPklEQVR42m3SyyqFURTA8Y2BER0TDyExZ+aSPIKUlPIITFzKeQWXwhBlQrmFgUzMMFLKZeguBu5y+//17dP3nc5vuPdee6299gohUYYaDGOyyACq4JmQVoFujOMR77hNfOAGM+hBOQqB9TjHD36xhAa04RCuuXeKOvwHVWIKL9jCK2bRiV284QgL8MwEjAneeo9VNOEaBhzALGtoRy02cIcWhE34jj5YxgW+E5Z4iTPkMYpPLCNY3hdOYEfNbKYdmNngZ1jyEzw7h7AIb3fRTQ95OAZ6yQpGYHMMtOTgouktYwxuXsHgWLLl+4x++Kx1FJrjLTagA77bTPvYgw1rRqY56e+w7GNYsqX6JfPwi7aR+Y5SA+BXtKIRfkfJAYgj14tpOF6+I46c4/cAM3UhM3JxyKsxiOIhH0IO6SH/A1Kb1WBeUjbkAAAAAElFTkSuQmCC)](https://forum.image.sc/tag/clesperanto)
[![GitHub stars](https://img.shields.io/github/stars/clEsperanto/clesperantoj_prototype?style=social)](https://github.com/clEsperanto/clesperantoj_prototype)
[![GitHub forks](https://img.shields.io/github/forks/clEsperanto/clesperantoj_prototype?style=social)](https://github.com/clEsperanto/clesperantoj_prototype)

# clEsperantoJ

clEsperantoJ is the Java library of [clEsperanto] - a multi-language framework for GPU-accelerated image processing.
It relies on a familly of [OpenCL kernels] originated from [CLIJ], and rely on the C++ [CLIc] library as a processing backend.  

Current release only concerne OpenCL backends. CUDA and Metal backends requires rebuild from sources.

## Install

### Pre-build jar

A pre-build jar compatible for Windows, MacOS, and Ubuntu, is deployed on the `maven.scijava` [repository manager](https://maven.scijava.org/#nexus-search;quick~clesperantoj). It can be downloaded directly from the website or integrated to your project as a dependency using `maven`.

### Build from source

This is necessary for contribution. Please refere to the [instructions](./BUILD.md) page.

## Contribution

This is an open-source project, contribution and feedback are very welcome. Do not hesitate to contact us for more information.

## Acknowledgements
This project was supported by the Deutsche Forschungsgemeinschaft under Germany’s Excellence Strategy – EXC2068 - Cluster of Excellence "Physics of Life" of TU Dresden.
This project has been made possible in part by grant number [2021-237734 (GPU-accelerating Fiji and friends using distributed CLIJ, NEUBIAS-style, EOSS4)](https://chanzuckerberg.com/eoss/proposals/gpu-accelerating-fiji-and-friends-using-distributed-clij-neubias-style/) from the Chan Zuckerberg Initiative DAF, an advised fund of the Silicon Valley Community Foundation.


[clEsperanto]: http://clesperanto.net/
[OpenCL kernels]: https://github.com/clEsperanto/clij-opencl-kernels/tree/clesperanto_kernels
[CLIJ]: http://clij.github.io/
[CLIc]: https://github.com/clEsperanto/CLIc
[community guidelines]: https://clij.github.io/clij2-docs/community_guidelines
[github issue]: https://github.com/clEsperanto/pyclesperanto/issues
[image.sc forum]: https://forum.image.sc/
