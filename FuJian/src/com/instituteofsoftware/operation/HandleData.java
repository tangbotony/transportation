package com.instituteofsoftware.operation;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.instituteofsoftware.bean.Pline;
import com.instituteofsoftware.bean.Point2;
import com.instituteofsoftware.util.FileUtil;
import com.instituteofsoftware.util.Util;

public class HandleData {
	public static Map<String, List<Point2>> map = new HashMap<String, List<Point2>>();// ring road;
	public static Map<String, List<Point2>> ringRoadMap = new HashMap<String, List<Point2>>();
	public static Map<String, Boolean> ringRoadPoint = new HashMap<String, Boolean>();
	public static boolean isRecoder = false;
	public static String files[] ={"G3.txt","G15.txt","G1501.txt","G1514.txt","G70.txt","G72.txt","G76.txt","S35.txt","G25.txt",};
	public static void main(String[] args) {
		isRecoder = false;
		getLinkGPS(map,"Rfujian.mif","Rfujian.mid");//读取正常的道路
		isRecoder = true;
		getLinkGPS(ringRoadMap,"fujian_ic&jct.MIF","fujian_ic&jct.MID");//读取匝道
		isRecoder = false;
		for(int i=0;i<files.length;i++)
		{
			System.out.println("第"+(i+1)+"个文件");
			getNewRoadSE(files[i]);
//			divideRoad(files[i]);
		}
	}
	
	/**
	 * 读取一个mif文件中下一个NILink的详细gps点，有效的保存在roadGPS中
	 * @param fin
	 */
	private static List<Point2> readMif(FileUtil fin){
		List<Point2> gps = new ArrayList<Point2>();
		String str;
		while((str = fin.readLine())!=null&&!str.startsWith("Pline")&&!str.startsWith("Line"));
		if(str.startsWith("Pline")){
			while((str = fin.readLine())!=null&&!str.contains("Pen")){
				String s[] = str.split(" ");
				double x = Double.valueOf(s[0]),y = Double.valueOf(s[1]);
				if(isRecoder)
					ringRoadPoint.put(x+"#"+y, true);
				gps.add(new Point2(x,y));
			}
		}
		else if(str.startsWith("Line")){
				String s[] = str.split(" ");
				double x1 = Double.valueOf(s[1]),y1 = Double.valueOf(s[2]);
				double x2 = Double.valueOf(s[3]),y2 = Double.valueOf(s[4]);
				gps.add(new Point2(x1,y1));
				gps.add(new Point2(x2,y2));
				if(isRecoder)
				{
					ringRoadPoint.put(x1+"#"+y1, true);
					ringRoadPoint.put(x2+"#"+y2, true);
				}
		}
		return gps;
	}
	/*
	 * 将mif文件和mid文件联系起来
	 */
	private static void getLinkGPS(Map<String, List<Point2>> map,String mif,String mid){
		FileUtil mifin = new FileUtil(mif);
		FileUtil midin = new FileUtil(mid);
		String str = null;
		while((str = midin.readLine())!=null){
			String s[] = str.split(",");
			String nilinkId = s[1].substring(1, s[1].length()-1);
			map.put(nilinkId, readMif(mifin));
		}
		mifin.close();
		midin.close();
	}
	public static void divideRoad(String fileName)
	{
		StringBuffer re = new StringBuffer();
		Map<Integer, String> newRoad = new HashMap<>();
		int index = 0;
		String tempRoad ="";
		FileUtil road = new FileUtil(fileName);
		String temp = road.readLine();
		int currentDirection = -1;
		System.out.println(ringRoadPoint.size());
		System.out.println("开始！！！！！！！！");
		while(temp!=null)
		{
			String line[] = temp.split("	");
			if(currentDirection == -1)
				currentDirection = Integer.parseInt(line[1]);
			else if(currentDirection != Integer.parseInt(line[1]))//判断是否换方向
			{
				System.out.println("换向###############");
				if(!tempRoad.equals(""))
				{
					newRoad.put(index, tempRoad);//把之前的加入，开始新的方向
					index++;//重新开始
					tempRoad = "";
				}
				currentDirection = Integer.parseInt(line[1]);//记录新的方向
			}
			if(tempRoad.equals(""))
				tempRoad = line[0];
			else
				tempRoad = tempRoad + "	" + line[0];
			if(ringRoadPoint.get(line[5]+"#"+line[6])!=null)
			{
				newRoad.put(index, tempRoad);
				index++;
				tempRoad="";
			}
			temp = road.readLine();
		}
		ArrayList<Point2> tempList;
		for(int i=0;i<index;i++)
		{
			System.out.println(newRoad.get(i));
			String line[] = newRoad.get(i).split("\t");
			tempList = new ArrayList<Point2>();
			for(int j=0;j<line.length;j++)
			{
				if(map.get(line[j])!=null)
					tempList.addAll(map.get(line[j]));
				else{
					System.out.println("i:"+i);
					System.out.println("index:"+index);
					System.out.println( newRoad.get(i));
					System.out.println(line[j]);
					System.exit(0);
				}
			}
			for(int m=0;m<tempList.size();m++)
			{
				re.append(tempList.get(m).toString());
				re.append("\t");
			}
			re.append("\n");
		}
		FileWriter writer;
		try {
			System.out.println(fileName);
			String fileNmaes[] = fileName.split("\\.");
			System.out.println(fileNmaes.length);
			Util.createFile(fileNmaes[0]+"_result.txt");
			writer = new FileWriter(fileNmaes[0]+"_result.txt", false);
			writer.write(re.toString());  
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	 * 获取新划分的路的起点和终点
	 */
	public static ArrayList<Pline> getNewRoadSE(String fileName)
	{
		ArrayList<Pline> allRoad = new ArrayList<Pline>();
		FileUtil road = new FileUtil(fileName);
		String temp = road.readLine();
		Pline tempPline = null;
		String beforPline="";
		while(temp!=null)
		{
			String lineInformation[] = temp.split("\t");
			if(tempPline == null)
			{
				tempPline = new Pline();
				tempPline.setStartNILink(lineInformation[0]);
				tempPline.setStartPoint(new Point2(Double.parseDouble(lineInformation[3]), Double.parseDouble(lineInformation[4])));
				tempPline.setDis(Double.parseDouble(lineInformation[2]));
				tempPline.setDirection(lineInformation[1]);
				tempPline.setContainNILink(lineInformation[0]);
			}else
			{
				if(!lineInformation[1].equals(tempPline.getDirection()))//换向
				{
					String befor[] = beforPline.split("\t");
					tempPline.setEndNILink(befor[0]);
					tempPline.setEndPoint(new Point2(Double.parseDouble(befor[5]), Double.parseDouble(befor[6])));
					allRoad.add(tempPline);
					tempPline = null;
					continue;
				}else
				{
					if(ringRoadPoint.get(lineInformation[5]+"#"+lineInformation[6])==null)//判断是不是分割点
					{
						tempPline.setDis(Double.parseDouble(lineInformation[2])+tempPline.getDis());
						tempPline.setContainNILink(tempPline.getContainNILink()+"\t"+lineInformation[0]);
					}else
					{
						tempPline.setEndNILink(lineInformation[0]);
						tempPline.setContainNILink(tempPline.getContainNILink()+"\t"+lineInformation[0]);
						tempPline.setEndPoint(new Point2(Double.parseDouble(lineInformation[5]), Double.parseDouble(lineInformation[6])));
						tempPline.setDis(Double.parseDouble(lineInformation[2])+tempPline.getDis());
						allRoad.add(tempPline);
						tempPline = null;
					}
				}
			}
			beforPline = temp;
			temp = road.readLine();
		}
		StringBuffer re = new StringBuffer();
		for(int i=0;i<allRoad.size();i++)
		{
			re.append(allRoad.get(i).toString());
			re.append("\n");
		}
		FileWriter writer;
		try {
			System.out.println(fileName);
			String fileNmaes[] = fileName.split("\\.");
			Util.createFile(fileNmaes[0]+"_new.txt");
			writer = new FileWriter(fileNmaes[0]+"_new.txt", false);
			writer.write(re.toString());  
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return allRoad;
	}
}



