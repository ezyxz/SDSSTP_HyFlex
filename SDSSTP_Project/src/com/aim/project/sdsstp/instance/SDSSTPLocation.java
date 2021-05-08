package com.aim.project.sdsstp.instance;

/**
 * 
 * @author Warren G. Jackson
 * @since 26/03/2021
 *
 */
public class SDSSTPLocation {

	private final double x;
	
	private final double y;

	public SDSSTPLocation(double x, double y) {

		this.x = x;
		this.y = y;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
}
