package com.instituteofsoftware.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.instituteofsoftware.bean.Pline;
import com.instituteofsoftware.bean.Point2;


public class Util {
	private static final double EARTH_RADIUS = 6378137;
	/**
	 * ��������GPS�㣨���ȣ�γ�ȣ�����������֮��ľ���
	 * @param start���
	 * @param end �յ�
	 * @return ���ؾ���
	 */
	
	private static double rad(double d)
	{
		return d * Math.PI / 180.0;
	}
	public static double GetDistance(double lng1, double lat1, double lng2, double lat2)
	{
		double radLat1 = rad(lat1);
		double radLat2 = rad(lat2);
		double a = radLat1 - radLat2;
		double b = rad(lng1) - rad(lng2);
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) + 
				Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000) / 10000;
		return s;
	}
	/*
	 * �����ʽ�ó��ľ����ǰ��׼���
	 */
	public static double GetDistance(Point2 pointStar,Point2 pointEnd)
	{
		//longitude ���� x;  latitude γ�� y;
		double lng1=0, lat1=0,  lng2=0, lat2=0;
		lng1 = pointStar.getX();
		lat1 = pointStar.getY();
		lng2 = pointEnd.getX();
		lat2 = pointEnd.getY();
		double radLat1 = rad(lat1);
		double radLat2 = rad(lat2);
		double a = radLat1 - radLat2;
		double b = rad(lng1) - rad(lng2);
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) + 
				Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000) / 10000;
		return s;
	}

	//��ȡ�������ϵ�ͶӰ��
	public static Point2 getProjPoint(Pline pline, Point2 point) {
		Point2 p0 = new Point2();
		Point2 p1 = pline.getStartPoint();
		Point2 p2 = pline.getEndPoint();
		Point2 p3 = point;
		// k = |P0-P1|/|P2-P1| = ( (v1*v2)/|P2-P1| ) / |P2-P1| = (P3 - P1) * (P2   - P1) / (|P2 - P1| * |P2 - P1|)
		double k = ((p3.getX()-p1.getX())*(p2.getX()-p1.getX())+(p3.getY()-p1.getY())*(p2.getY()-p1.getY()))/
				((p2.getX()-p1.getX())*(p2.getX()-p1.getX())+(p2.getY()-p1.getY())*(p2.getY()-p1.getY()));
		p0.setX(k*(p2.getX()-p1.getX())+p1.getX());
		p0.setY(k*(p2.getY()-p1.getY())+p1.getY());
		return p0;
	}
	
	
	
	public static String getCurTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//�������ڸ�ʽ
		return df.format(new Date());// new Date()Ϊ��ȡ��ǰϵͳʱ��

	}
	public static Date getDateByString(String trmp)
	{
		SimpleDateFormat sdf= new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		try {
			return sdf.parse(trmp);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public static void createFile(String fileName) {
		File ceateFile =new File(fileName);    
		//����ļ��в������򴴽�    
		if(!ceateFile.exists())      
		{
		    try {
				ceateFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
//	public static void createTxt(String previousFileName, String fileName) {
//		String result =Main.resultFileAdd+"/"+previousFileName+"/"+fileName;
//		String process =Main.resultFileAdd+"/"+previousFileName+"#"+ConfigurationFiles.PROCESSFILE+"/"+fileName;
//		File resultFileName = new File(result);
//		File processFileName = new File(process);
//		try {
//			resultFileName.createNewFile();
//			
//			if(ConfigurationFiles.ISCREATELOG)
//			{
//				processFileName.createNewFile();//������־�ļ�
//			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}	
//	}
	
	
	
	
	//��һ���ļ�׷������
	public static void appendMethodB(String fileName, String content,boolean flag) {
		if(!flag)
		{
			return;
		}
		System.out.println(content);
		try {  
			//��һ��д�ļ��������캯���еĵڶ�������true��ʾ��׷����ʽд�ļ�  
			FileWriter writer = new FileWriter(fileName, true);  
			writer.write(content+"\n");  
			writer.close();  
		} catch (IOException e) {  
			e.printStackTrace();  
		} 
	}
	public static void write2File(String fileName,String content)
	{
		FileWriter writer;
		try {
			writer = new FileWriter(fileName, false);
			writer.write(content);  
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
