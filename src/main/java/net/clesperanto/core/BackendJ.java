package net.clesperanto.core;


import net.clesperanto._internals.jclic._BackendJ;

/**
 * Class that contains the methods to interact and change the type of backend used for Clesperanto.
 */
public class BackendJ {

	/**
	 * Set the type of backend that Clesperanto is going to use.
	 * @param backend
	 * 	the type of backend that wants to be used. It should be either "cuda" or "opencl",
	 * 	if it is anything else it will be set to "opencl"
	 */
    public static void setBackend(String backend) {
    	_BackendJ.setBackend(backend);
    }
}
