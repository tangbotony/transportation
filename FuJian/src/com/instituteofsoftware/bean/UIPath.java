package com.instituteofsoftware.bean;

import java.io.Serializable;

public class UIPath implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//(time,sLong,sLat,eLong,eLat,color)
	private double sLong;
	private double sLat;
	private double eLong;
	private double eLat;
	private String color;
	private double width;
	private int flow;
	public double getsLong() {
		return sLong;
	}
	public double getsLat() {
		return sLat;
	}
	public void setsLat(double sLat) {
		this.sLat = sLat;
	}
	public double geteLong() {
		return eLong;
	}
	public void seteLong(double eLong) {
		this.eLong = eLong;
	}
	public double geteLat() {
		return eLat;
	}
	public void seteLat(double eLat) {
		this.eLat = eLat;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public double getWidth() {
		return width;
	}
	public void setWidth(double width) {
		this.width = width;
	}
	public int getFlow() {
		return flow;
	}
	public void setFlow(int flow) {
		this.flow = flow;
	}
	public void setsLong(double sLong) {
		this.sLong = sLong;
	}
}
