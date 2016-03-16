package com.instituteofsoftware.bean;

import java.util.ArrayList;

public class Pline {
	private double a;
	private double b;
	private String roadID;
	private String startNILink;
	private String endNILink;
	private String direction;
	private double dis;
	private Point2 startPoint;
	private Point2 endPoint;
	private String containNILink;
	private ArrayList<Integer> forward;
	private ArrayList<Integer> reverse;
	
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
	public String getRoadID() {
		return roadID;
	}
	public void setRoadID(String roadID) {
		this.roadID = roadID;
	}
	public ArrayList<Integer> getForward() {
		return forward;
	}
	public void setForward(ArrayList<Integer> forward) {
		this.forward = forward;
	}
	public ArrayList<Integer> getReverse() {
		return reverse;
	}
	public void setReverse(ArrayList<Integer> reverse) {
		this.reverse = reverse;
	}
	public double getA() {
		return a;
	}
	public void setA(double a) {
		this.a = a;
	}
	public double getB() {
		return b;
	}
	public void setB(double b) {
		this.b = b;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return roadID+"\t"+startNILink+"\t"+endNILink+"\t"+direction+"\t"+dis+"\t"+startPoint.toString()+"\t"+endPoint.toString()+"\t"+containNILink;
		//		return containNILink;
		//return startPoint.toString()+"\t"+endPoint.toString();
		//return startNILink+"\t"+endNILink+"\t"+dis+"";
	}
}
