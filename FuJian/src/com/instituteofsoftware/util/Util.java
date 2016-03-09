package com.instituteofsoftware.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


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

	
	
	
	
	
	public static String getCurTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//�������ڸ�ʽ
		return df.format(new Date());// new Date()Ϊ��ȡ��ǰϵͳʱ��

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
	
	
}
