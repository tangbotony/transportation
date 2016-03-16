package com.instituteofsoftware.operation;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.instituteofsoftware.bean.Pline;
import com.instituteofsoftware.bean.Point2;
import com.instituteofsoftware.bean.VehicleInsp;
import com.instituteofsoftware.util.FileUtil;
import com.instituteofsoftware.util.Util;

public class HandleData {
	public static String fileDate="20151112";
	public static String filePath="F:/Data/unzip/"+fileDate+"/";
	public static String recoderFileName=fileDate+".txt";
	public static int countSize = 0;
	public static int timeGap = 60;
	public static Map<String, List<Point2>> map = new HashMap<String, List<Point2>>();// ring road;
	public static Map<String, List<Point2>> ringRoadMap = new HashMap<String, List<Point2>>();
	public static Map<String, Boolean> ringRoadPoint = new HashMap<String, Boolean>();
	public static Map<String, ArrayList<Pline>> allRoadInformation = new HashMap<String, ArrayList<Pline>>();//存放每一条路的所有信息, key:G70
	public static Map<String, ArrayList<VehicleInsp>> allVehicleInsps = new HashMap<String, ArrayList<VehicleInsp>>();//存放每一条路的车检器
	public static Map<String, Pline> staticFlow = new HashMap<String,Pline>();//key: G70#starID#endID
	public static StringBuffer error = new StringBuffer();
	public static double maxDis = 4;
	public static boolean isRecoder = false;
	public static String files[] ={"G3.txt","G15.txt","G1501.txt","G1514.txt","G70.txt","G72.txt","G76.txt","S35.txt","G25.txt"};
	public static String roads[] ={"G3","G15","G1501","G1514","G70","G72","G76","S35","G25"};
	
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
		divideVehicleInspection();
//		Util.write2File("err.txt", error.toString())；
		StatisticsData.statis();
		SignallingStatistics.signallingStatistics();
		Output2File.wirte();
	}
	
	private static void divideVehicleInspection() {
		FileUtil fileUtil = new FileUtil("vehicleinspection0324.txt");
		String temp = fileUtil.readLine();
		StringBuffer buffer = new StringBuffer();
		while(temp!=null)
		{
			VehicleInsp vel = new VehicleInsp(temp);
			if(allVehicleInsps.get(vel.getRoadLine())==null)
				allVehicleInsps.put(vel.getRoadLine(), new ArrayList<VehicleInsp>());
			ArrayList<VehicleInsp> tempList = allVehicleInsps.get(vel.getRoadLine());
			vel.setPline(getPlineBy(vel,false));
			if(vel.getPline()!=null)
			{
				if(vel.getChannel().equals("1") || vel.getChannel().equals("3") || vel.getChannel().equals("5") || vel.getChannel().equals("7"))//代表的是正方向
				{
					if(!vel.getPline().getDirection().equals("0"))
					{
						Pline pline1 =getPlineBy(vel,true);
						if(pline1!=null)
							vel.setPline(pline1);//获取对面的道路来作为投影路段
					}
				}else
				{
					if(!vel.getPline().getDirection().equals("1"))
					{
						Pline pline1 =getPlineBy(vel,true);
						if(pline1!=null)
							vel.setPline(getPlineBy(vel,true));
					}
				}
				tempList.add(vel);
				allVehicleInsps.put(vel.getRoadLine(),tempList);
				buffer.append(vel.toString());
				buffer.append("\n");
			}
//			vel.setPline2(getPlineBy(vel,true));查判断
			if(vel.getPline()==null)
			{
				error.append(vel);
				error.append("\n");
			}
			temp = fileUtil.readLine();
		}
		Util.write2File("test.txt", buffer.toString());
	}

	private static Pline getPlineBy(VehicleInsp vel,boolean flag) {
		if(flag == true && vel.getPline()==null)//代表第一次道路匹配为空，第二次不需要在进行匹配
			return null;
		Pline tempPline = null;
		ArrayList<Pline> plines = allRoadInformation.get(vel.getRoadLine());
		if(plines==null)
		{
			System.out.println(vel);
			System.exit(0);
		}
		double curDis=Double.MAX_VALUE;
		for(int i=0;i<plines.size();i++)
		{
			Point2 projectivePoint = Util.getProjPoint(plines.get(i),vel.getGpsPoint());
			if(projectivePoint.getX()>plines.get(i).getStartPoint().getX() && projectivePoint.getX()<plines.get(i).getEndPoint().getX()
					|| projectivePoint.getX()>plines.get(i).getEndPoint().getX() && projectivePoint.getX()<plines.get(i).getStartPoint().getX())
			{
				if((Util.GetDistance(projectivePoint, vel.getGpsPoint())/1000)<curDis)//加上距离约束
				{
					if(flag)//代表寻找另一边的道路
					{
						if(!plines.get(i).getRoadID().equals(vel.getPline().getRoadID()))
						{
							tempPline = plines.get(i);
							curDis = Util.GetDistance(projectivePoint, vel.getGpsPoint())/1000;
						}
					}else
					{
						tempPline = plines.get(i);
						curDis = Util.GetDistance(projectivePoint, vel.getGpsPoint())/1000;
					}
				}
			}
		}
		if( !flag && curDis>maxDis)//第二条线不需要加入距离判断，程序能够运行到这，证明第一条能够匹配
		{
			tempPline=null;
		}
		return tempPline;
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
		String fileNames[] = fileName.split("\\.");
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
					tempPline.setRoadID(fileNames[0]+"#"+tempPline.getStartNILink()+"#"+tempPline.getEndNILink());
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
						tempPline.setDis(Double.parseDouble(lineInformation[2])+tempPline.getDis());
						if(tempPline.getDis()<2)
						{
							tempPline.setContainNILink(tempPline.getContainNILink()+"\t"+lineInformation[0]);
						}else
						{
							tempPline.setEndNILink(lineInformation[0]);
							tempPline.setContainNILink(tempPline.getContainNILink()+"\t"+lineInformation[0]);
							tempPline.setEndPoint(new Point2(Double.parseDouble(lineInformation[5]), Double.parseDouble(lineInformation[6])));
							tempPline.setRoadID(fileNames[0]+"#"+tempPline.getStartNILink()+"#"+tempPline.getEndNILink());
							allRoad.add(tempPline);
							tempPline = null;
						}
					}
				}
			}
			beforPline = temp;
			temp = road.readLine();
		}
		StringBuffer re = new StringBuffer();
		countSize = countSize +allRoad.size();
		for(int i=0;i<allRoad.size();i++)
		{
			re.append(allRoad.get(i).toString());
			re.append("\n");
		}
		FileWriter writer;
		try {
			Util.createFile(fileNames[0]+"_new.txt");
			writer = new FileWriter(fileNames[0]+"_new.txt", false);
			writer.write(re.toString());  
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		allRoadInformation.put(fileNames[0], allRoad);
		return allRoad;
	}
}