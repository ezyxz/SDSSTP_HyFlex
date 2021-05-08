package com.aim.project.sdsstp.interfaces;

import java.nio.file.Path;
import java.util.Random;

/**
 * 
 * @author Warren
 *
 */
public interface SDSSTPInstanceReaderInterface {

	/**
	 * 
	 * @param path
	 * @param random
	 * @return
	 */
	public SDSSTPInstanceInterface readSDSSTPInstance(Path path, Random random);
}
