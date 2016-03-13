package com.instituteofsoftware.bean;

//G15	1884	5	922	2302020	118.09851	24.64742277
public class VehicleInsp {
	private String coilID; //线圈ID
	private String controllerID; //控制器ID
	private String channel; //线圈所在车道
	private String placeKM; //桩号
	private String roadLine; //路线编号
	private Point2 gpsPoint;//线圈的GPS点
	private Pline pline;
	 
	public VehicleInsp(String line) {
		//G15	1889	4	918	2131000	119.2758129	25.56903821
		String temp[] = line.split("\t");
		this.roadLine = temp[0];
		this.placeKM = temp[1];
		this.channel = temp[2];
		this.controllerID = temp[3];
		this.coilID = temp[4];
		this.gpsPoint = new Point2(Double.parseDouble(temp[5]),Double.parseDouble(temp[6]));
	}
	public String getCoilID() {
		return coilID;
	}
	public void setCoilID(String coilID) {
		this.coilID = coilID;
	}
	public String getControllerID() {
		return controllerID;
	}
	public void setControllerID(String controllerID) {
		this.controllerID = controllerID;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getPlaceKM() {
		return placeKM;
	}
	public void setPlaceKM(String placeKM) {
		this.placeKM = placeKM;
	}
	public String getRoadLine() {
		return roadLine;
	}
	public void setRoadLine(String roadLine) {
		this.roadLine = roadLine;
	}
	public Point2 getGpsPoint() {
		return gpsPoint;
	}
	public void setGpsPoint(Point2 gpsPoint) {
		this.gpsPoint = gpsPoint;
	}
	public Pline getPline() {
		return pline;
	}
	public void setPline(Pline pline) {
		this.pline = pline;
	}
	@Override
	public String toString() {
		return roadLine+"\t"+placeKM+"\t"+channel+"\t"+controllerID+"\t"+coilID+"\t"+gpsPoint+"\t"+pline;
	}
}
