package com.instituteofsoftware.bean;

import java.util.Date;

import com.instituteofsoftware.util.Util;

public class RecoderSignalling {
	//F59248895A7D43B8FDBA59D7A1ECDC14	2015/11/12 23:29:40	22811	3526	01	12
	private String tmsy;
	private Date timeStamp;
	private String lac;
	private String cellID;
	private String eventID;
	private String lineId;
	public RecoderSignalling(String line) {
		String recods[] = line.split("\t");
		this.tmsy = recods[0];
		this.timeStamp = Util.getDateByString(recods[1]);
		this.lac = recods[2];
		this.cellID = recods[3];
		this.eventID = recods[4];
		this.lineId = recods[5];
	}
	public String getTmsy() {
		return tmsy;
	}
	public void setTmsy(String tmsy) {
		this.tmsy = tmsy;
	}
	public Date getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getLac() {
		return lac;
	}
	public void setLac(String lac) {
		this.lac = lac;
	}
	public String getCellID() {
		return cellID;
	}
	public void setCellID(String cellID) {
		this.cellID = cellID;
	}
	public String getEventID() {
		return eventID;
	}
	public void setEventID(String eventID) {
		this.eventID = eventID;
	}
	public String getLineId() {
		return lineId;
	}
	public void setLineId(String lineId) {
		this.lineId = lineId;
	}
	@SuppressWarnings("deprecation")
	@Override
	public String toString() {
		return timeStamp.getHours()+":"+timeStamp.getMinutes()+":"+timeStamp.getSeconds()+"\t"+lac+"\t"+cellID+"\t"+eventID+"\t"+lineId;
		//return timeStamp.getHours()+":"+timeStamp.getMinutes()+":"+timeStamp.getSeconds();
	}
}
