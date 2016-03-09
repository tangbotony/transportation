package com.instituteofsoftware.bean;

public class Pline {
	private String startNILink;
	private String endNILink;
	private String direction;
	private double dis;
	private Point2 startPoint;
	private Point2 endPoint;
	private String containNILink;
	
	public Pline() {
		super();
	}
	public String getStartNILink() {
		return startNILink;
	}
	public void setStartNILink(String startNILink) {
		this.startNILink = startNILink;
	}
	public String getEndNILink() {
		return endNILink;
	}
	public void setEndNILink(String endNILink) {
		this.endNILink = endNILink;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public double getDis() {
		return dis;
	}
	public void setDis(double dis) {
		this.dis = dis;
	}
	public Point2 getStartPoint() {
		return startPoint;
	}
	public void setStartPoint(Point2 startPoint) {
		this.startPoint = startPoint;
	}
	public Point2 getEndPoint() {
		return endPoint;
	}
	public void setEndPoint(Point2 endPoint) {
		this.endPoint = endPoint;
	}
	public String getContainNILink() {
		return containNILink;
	}
	public void setContainNILink(String containNILink) {
		this.containNILink = containNILink;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return startNILink+"\t"+endNILink+"\t"+direction+"\t"+dis+"\t"+startPoint.toString()+"\t"+endPoint.toString()+"\t"+containNILink;
		//		return containNILink;
		//return startPoint.toString()+"\t"+endPoint.toString();
		//return startNILink+"\t"+endNILink+"\t"+dis+"";
	}
}
