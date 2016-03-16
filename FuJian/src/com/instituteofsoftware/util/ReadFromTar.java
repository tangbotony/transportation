package com.instituteofsoftware.util;

import java.io.File;
import java.util.ArrayList;



public class ReadFromTar {

	public static void main(String[] args) {
		//F:\Data\20151125
//		unpressData("20151125");
	}
	public static long lastT = 0;
	public static int lineNum = 12;
	public static String unzippath ;
	public static String TransportFtp;
	/**
	 * 获取lastT到App.T之间的所有数据
	 * @return
	 */
	public static void unpressData(String day) {
		//F:\Data\20151112
		TransportFtp = "F:\\Data\\"+day+"\\";
		unzippath = "F:\\Data\\unzip\\"+day+"\\";
		File f = new File(unzippath);
		if(!f.exists())f.mkdirs();
		lastT = TimeFormat.parse(day+" 00:00:00").getTime()/1000;
		for (long j = lastT;j<=lastT + 60*60*24;j+=60) {
			@SuppressWarnings("static-access")
			long t = new TimeFormat().format12(j*1000);
			for (int i = 4; i <= lineNum; i++)
			{
				String nameHead = null;
				if (i < 10)
					nameHead = "mc_xm_00" + i + "_";
				else if (i < 100)
					nameHead = "mc_xm_0" + i + "_";
				
				String rawname = nameHead + t + "00_001.txt.tar.gz";
				File f1 = new File(TransportFtp+rawname);
				if(!f1.exists()||!f1.canRead())
					continue;
				File f2 = new File(unzippath + nameHead + t	+ "00_001.txt");
				new Untargz().readtar(TransportFtp + rawname, unzippath);
				
				if(!f2.exists())
					continue;
				new File(unzippath + nameHead + t+ "00.chk").delete();
			}
		}
	}
	public static ArrayList<String> unpressData(String day,String star,String end) {
		unzippath = "F:\\Data\\unzip\\"+day+"\\";
		ArrayList<String> result = new ArrayList<>();
		System.out.println(day+" "+star+":00:00");
		lastT = TimeFormat.parse(day+" "+star+":00:00").getTime()/1000;
		System.out.println(end);
		for (long j = lastT;j<lastT + 60*60*Double.parseDouble(end);j+=60) {
			@SuppressWarnings("static-access")
			long t = new TimeFormat().format12(j*1000);
			for (int i = 4; i <= lineNum; i++)
			{
				String nameHead = null;
				if (i < 10)
					nameHead = "mc_xm_00" + i + "_";
				else if (i < 100)
					nameHead = "mc_xm_0" + i + "_";
				
				String fileName = unzippath + nameHead + t	+ "00_001.txt";
				result.add(fileName);
			}
		}
		return result;
	}
}
