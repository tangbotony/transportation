package com.instituteofsoftware.bean;

public class Signalling {
	//cell_id 	Lac_id	 net_type(1:2G,2:3G;3:4G)	 longitude	latitude
	private String roadId;//G70
	private String cellId;
	private String lacId;
	private String netType;
	private Point2 point;
	private Pline pline1;
	private Pline pline2;
	public String getCellId() {
		return cellId;
	}
	public Signalling(String temp) {
		String ts[] = temp.split("\t");
		this.roadId = ts[0];
		this.cellId = ts[1];
		this.lacId = ts[2];
		this.netType = ts[3];
		this.point = new Point2(Double.parseDouble(ts[4]), Double.parseDouble(ts[5]));
	}
	public String getRoadId() {
		return roadId;
	}
	public void setRoadId(String roadId) {
		this.roadId = roadId;
	}
	public void setCellId(String cellId) {
		this.cellId = cellId;
	}
	public String getLacId() {
		return lacId;
	}
	public void setLacId(String lacId) {
		this.lacId = lacId;
	}
	public String getNetType() {
		return netType;
	}
	public void setNetType(String netType) {
		this.netType = netType;
	}
	public Point2 getPoint() {
		return point;
	}
	public void setPoint(Point2 point) {
		this.point = point;
	}
	public Pline getPline1() {
		return pline1;
	}
	public void setPline1(Pline pline1) {
		this.pline1 = pline1;
	}
	public Pline getPline2() {
		return pline2;
	}
	public void setPline2(Pline pline2) {
		this.pline2 = pline2;
	}
	@Override
	public String toString() {
		return roadId+"\t"+cellId+"\t"+lacId+"\t"+netType+"\t"+point+"\t"+pline1+"#######"+pline2;
	}
	
}
