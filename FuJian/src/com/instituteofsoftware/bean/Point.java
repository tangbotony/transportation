package com.instituteofsoftware.bean;

public class Point {
	private String pointId;//longitude+latitude����������
	private String date;//����㴴���ʵ�����ڣ���Ŀ��Ҫ�����ԣ���ҿ��Բ��ù�
	private double longitude;//����
	private double latitude;//γ��
	private String isHavePeople;//��Ŀ��Ҫ�����ԣ���ҿ��Բ��ù�
	private String roadNum;//��Ŀ��Ҫ�����ԣ���ҿ��Բ��ù�
	private double ObservProbability;//��Ŀ��Ҫ�����ԣ���ҿ��Բ��ù�
	private int position;//��¼������ڸ�·�����ǵڼ�����,��Ϊһ��·���ɵ㼯���ɵģ�����Ҫ֪�������������·���Ǹ�λ��
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
