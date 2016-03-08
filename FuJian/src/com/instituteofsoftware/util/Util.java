package com.instituteofsoftware.util;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.instituteofsoftware.bean.Point;
import com.mongodb.DBObject;

public class Util {
	private static final double EARTH_RADIUS = 6378137;
	/**
	 * ��������GPS�㣨���ȣ�γ�ȣ�����������֮��ľ���
	 * @param start���
	 * @param end �յ�
	 * @return ���ؾ���
	 */
	public static double calcuDistByCoordinate(Point start,Point end){
		//System.out.println("["+start.getLongitude()+","+start.getLatitude()+"]["+end.getLongitude()+","+end.getLatitude()+"]");
		return GetDistance(start.getLongitude(),start.getLatitude(),end.getLongitude(),end.getLatitude());
	}
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

	
	//����DBObject�õ���ļ���
	public static ArrayList<Point> getTrajectoryByDBObject(DBObject dbObject) {
		
		ArrayList<Point> trajectory = new ArrayList<Point>();
		String []pointString=dbObject.get("PLine").toString().split("]");
		int length = pointString.length;
		String temp[];
		String roadNum = dbObject.get("ID").toString();
		Point tempPoint;
		for (int i = 0; i < length; i++) {
			String string = pointString[i].trim();
			string = string.substring(3, string.length());
			temp = string.split(",");
			tempPoint = new Point();
			tempPoint.setRoadNum(roadNum);
			tempPoint.setLongitude(Double.parseDouble(temp[0].trim()));
			tempPoint.setLatitude(Double.parseDouble(temp[1].trim()));
			tempPoint.setPosition(i);
			trajectory.add(tempPoint);
		}
		return trajectory;
	}
	public static double calDisByArrayList(ArrayList<Point> points)
	{
		double dis=0.0;
		for(int i=0;i<points.size()-1;i++)
		{
			dis = dis + Util.calcuDistByCoordinate(points.get(i), points.get(i+1));
		}
		return dis/1000;
	}
	//ѡ������������point�����������
	public static ArrayList<Point> getMinPointList(Point point,ArrayList<Point> points)
	{
		ArrayList<Point> result = new ArrayList<Point>();
		if(points.size()<3)
		{
			result = points;
		}else
		{
			int position=0;//������¼����������������·�����λ��
			double disc=Util.calcuDistByCoordinate(point, points.get(0));
			double temp=0;
			for(int i=1;i<points.size();i++)
			{
				temp = Util.calcuDistByCoordinate(point, points.get(i));
				if(temp<disc)
				{
					position = i;
					disc = temp;
				}
			}
			result.add(points.get(position));//������ĵ����
			//�����ڶ����ĵ㣬��ʵ��������position�����߲���
			if(position!=0)
			{
				if(position+1<points.size())
				{
					if(Util.calcuDistByCoordinate(point, points.get(position-1))<Util.calcuDistByCoordinate(point, points.get(position+1)))
					{
						result.add(points.get(position-1));
					}else
					{
						result.add(points.get(position+1));
					}
				}else
				{
					result.add(points.get(position-1));
				}
			}else
			{
				result.add(points.get(position+1));
			}

		}
		return result;
	}
	public static boolean isEqual(Point point,Point point2) {
		boolean isEqual = false;
		if(point.getLatitude()==point2.getLatitude()&&point.getLongitude()==point2.getLongitude())
		{
			isEqual = true;
		}
		return isEqual;
	}
	
	
	public static ArrayList<Point> arrayListAddArrayList(ArrayList<Point> arrayList,ArrayList<Point> arrayList1)
	{
		for(int i=0;i<arrayList1.size();i++)
		{
			arrayList.add(arrayList1.get(i));
		}
		return arrayList;
	}
	public static int getBreakPoint(Point starP, ArrayList<Point> tempStarPoint) {
		int breakpoint=0;
		for(int i=0;i<tempStarPoint.size();i++)
		{
			if(Util.isEqual(starP, tempStarPoint.get(i)))
			{
				breakpoint = i;
				break;
			}
		}
		return breakpoint;
	}
	public static ArrayList<Point> getLeftArrayList(int breakpoint,ArrayList<Point> tempStarPoint) {
		ArrayList<Point> resultList = new ArrayList<Point>();
		for(int i=0;i<=breakpoint;i++)
		{
			resultList.add(tempStarPoint.get(i));
		}
		return resultList;
	}
	public static ArrayList<Point> getRightArrayList(int breakpoint,ArrayList<Point> tempStarPoint) {
		ArrayList<Point> resultList = new ArrayList<Point>();
		for(int i=breakpoint;i<tempStarPoint.size();i++)
		{
			resultList.add(tempStarPoint.get(i));
		}
		return resultList;
	}
	
	
	public static String getCurTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//�������ڸ�ʽ
		return df.format(new Date());// new Date()Ϊ��ȡ��ǰϵͳʱ��

	}
//	public static void createFile(String fileName) {
//		File ceateFile =new File(Main.resultFileAdd+"/"+fileName);    
//		//����ļ��в������򴴽�    
//		if  (!ceateFile .exists()  && !ceateFile .isDirectory())      
//		{
//		    ceateFile .mkdir();
//		}
//	}
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
	//����һ���㲹��ڶ��������Ϣ
	public static void setOtherInformation(Point pointOne,Point pointTwo)
	{
		pointOne.setDate(pointTwo.getDate());
		pointOne.setDirection(pointTwo.getDirection());
		pointOne.setSpeed(pointTwo.getSpeed());
		pointOne.setIsHavePeople(pointTwo.getIsHavePeople());
	}
	
	
	
	
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
