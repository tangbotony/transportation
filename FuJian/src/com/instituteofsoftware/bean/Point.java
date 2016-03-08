package com.instituteofsoftware.bean;

public class Point {
	private String pointId;//longitude+latitude构建出来的
	private String date;//这个点创造的实际日期，项目需要的属性，大家可以不用管
	private double longitude;//经度
	private double latitude;//纬度
	private String isHavePeople;//项目需要的属性，大家可以不用管
	private String roadNum;//项目需要的属性，大家可以不用管
	private double ObservProbability;//项目需要的属性，大家可以不用管
	private int position;//记录这个点在该路里面是第几个点,因为一条路是由点集构成的，我需要知道这个点在这条路的那个位置
	private int direction;
	private double speed;
	public Point() {
		super();
	}
	public Point(String pointId)
	{
		this.pointId = pointId;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getIsHavePeople() {
		return isHavePeople;
	}
	public void setIsHavePeople(String isHavePeople) {
		this.isHavePeople = isHavePeople;
	}
	public String getRoadNum() {
		return roadNum;
	}
	public void setRoadNum(String roadNum) {
		this.roadNum = roadNum;
	}
	
	public double getObservProbability() {
		return ObservProbability;
	}
	public void setObservProbability(double observProbability) {
		ObservProbability = observProbability;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	
	public String getPointId() {
		return pointId;
	}
	public void setPointId(String pointId) {
		this.pointId = pointId;
	}
	
	public int getDirection() {
		return direction;
	}
	public void setDirection(int direction) {
		this.direction = direction;
	}
	public double getSpeed() {
		return speed;
	}
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	@Override
	public String toString() {
		return date+"	"+longitude + "	" + latitude+"	"+speed+"	"+direction+"	"+isHavePeople+"	"+roadNum;
		//return longitude + "	" + latitude;
	}
	public String toPline() {
		return "["+longitude+","+latitude+"]";

	}
}
