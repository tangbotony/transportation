package com.instituteofsoftware.bean;

import java.util.Date;

import com.instituteofsoftware.util.Util;

public class Recoder {
	public String roadID;//G70
	public String coilID; //线圈ID
    public String status; //设备状态，0正常、其他异常
    public int forward1; //一类车正向流量
    public int reverse1; //一类车逆向流量
    public String avgSpeed1; //一类车平均速度
    public String occupancy1; //一类车时间占有率
    public int forward2; //二类车……
    public int reverse2;
    public String avgSpeed2;
    public String occupancy2;
    public int forward3; //三类车……
    public int reverse3;
    public String avgSpeed3;
    public String occupancy3;
    public int forward4; //四类车……
    public int reverse4;
    public String avgSpeed4;
    public String occupancy4;
    public int forward5; //五类车……
    public int reverse5;
    public String avgSpeed5;
    public String occupancy5;
    public Date gatherTime;//采集时间
	public Recoder(String recoder) {
		String recoders[] = recoder.split("\t");
		this.roadID = recoders[0];
		this.coilID = recoders[1];
		this.status = recoders[2];
		this.forward1 = Integer.parseInt(recoders[4]);
		this.reverse1 = Integer.parseInt(recoders[5]);
		this.avgSpeed1 = recoders[3];
		this.occupancy1 = recoders[6];
		this.forward2 = Integer.parseInt(recoders[8]);
		this.reverse2 = Integer.parseInt(recoders[9]);
		this.avgSpeed2 = recoders[7];
		this.occupancy2 = recoders[10];
		this.forward3 = Integer.parseInt(recoders[12]);
		this.reverse3 = Integer.parseInt(recoders[13]);
		this.avgSpeed3 = recoders[11];
		this.occupancy3 = recoders[14];
		this.forward4 = Integer.parseInt(recoders[16]);
		this.reverse4 =Integer.parseInt(recoders[17]);
		this.avgSpeed4 = recoders[15];
		this.occupancy4 = recoders[18];
		this.forward5 = Integer.parseInt(recoders[20]);
		this.reverse5 = Integer.parseInt(recoders[21]);
		this.avgSpeed5 = recoders[19];
		this.occupancy5 = recoders[22];
		this.gatherTime = Util.getDateByString(recoders[23]);
	}
	@Override
	public String toString() {
		return roadID+"\t"+coilID+"\t"+status;
	}
}
