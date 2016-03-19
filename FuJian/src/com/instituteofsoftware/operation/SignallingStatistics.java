package com.instituteofsoftware.operation;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.instituteofsoftware.bean.Pline;
import com.instituteofsoftware.bean.Point2;
import com.instituteofsoftware.bean.RecoderSignalling;
import com.instituteofsoftware.bean.Signalling;
import com.instituteofsoftware.util.FileUtil;
import com.instituteofsoftware.util.ReadFromTar;
import com.instituteofsoftware.util.Util;

public class SignallingStatistics {
	/*
	 * 开始预测：
预测结束
预测道路数：196
	 */
	public static int minSignalling = 4;
	public static int empty=0;//小于
	public static int can=0;//最终可用数据
	public static int timegap = 4;//时间间隔4小时
	public static int notOnRoad =0;
	public static ArrayList<Signalling> allSignallings = new ArrayList<>();//所有的基站
	public static Map<String, String> cellMatching = new HashMap<String,String>();//key:cellId#lacId; 根据key得到匹配的道路id
	public static Map<String,ArrayList<Signalling>> roalSignalling = new HashMap<>();//key: roadID  G70#startID#endID 得到这段路上的基站
	public static Map<String, Pline> allRoad = new HashMap<>();//key key: roadID  G70#startID#endID
	public static Map<String, ArrayList<RecoderSignalling>> signallingStatistics = new HashMap<>();//存放每一个用户的信令数据，key:MD5加密电话号码
	public static Map<String,Signalling> mapSignalling = new HashMap<>();//key  cell_id#Lac_id
	public static String dataPath="F:/Data/unzip/20151112";
	public static Map<String, ArrayList<Integer>> roadStatistics = new HashMap<String, ArrayList<Integer>>();//信令统计，按照路段的id来取 key  cell_id#Lac_id
	
	public static void signallingStatistics()
	{
		getAllRoad();
		dealSignallingToRoad();//将所有的基站读取加入列表
		matching();//将所有的基站和道路进行匹配
		sort();//给每一段道路上的gps排序
		statistics();
	}
	@SuppressWarnings("deprecation")
	private static void statistics()
	{
		for(int i=0;i<((24*60)/HandleData.timeGap);i++)
		{
			
			System.out.println("第"+i+"个小时的开始处理！");
			ArrayList<String> filesName = ReadFromTar.unpressData(HandleData.fileDate, i<10? "0"+i : i+"", HandleData.timeGap/60+"");
			System.out.println(filesName.size());
			System.out.println(filesName);
			for(String file : filesName){//合并同一个用户的数据
				if(!new File(file).exists())
					continue;
				FileUtil fileUtil = new FileUtil(file);
				String temp = fileUtil.readLine();
				while(temp!=null)
				{
					RecoderSignalling recoderSignalling = new RecoderSignalling(temp);
					ArrayList<RecoderSignalling> tempList;
					if(signallingStatistics.get(recoderSignalling.getTmsy())==null){
						tempList = new ArrayList<>();
					}else{
						tempList = signallingStatistics.get(recoderSignalling.getTmsy());
					}
					tempList.add(recoderSignalling);
					signallingStatistics.put(recoderSignalling.getTmsy(),tempList);
					temp = fileUtil.readLine();
				}
			}
			System.out.println("第"+i+"个小时的处理结束！");
			int count = 0;
			System.out.println("总共人数："+signallingStatistics.size());
			System.out.println("统计开始....");
			for(Iterator<String> iterator = signallingStatistics.keySet().iterator(); iterator.hasNext();) {
				String string = (String) iterator.next();
				ArrayList<RecoderSignalling> list = signallingStatistics.get(string);
				if(list.size()>=minSignalling)	
				{
					int starIndex = 0;//信令数据里面第一个能匹配路段的数据的索引
					int endIndex =0;//信令数据里面最后一个能匹配路段的数据的索引
					int countSignalling = 0;//记录这条信令数据里面有多少个信令能够进行匹配
					Signalling starSignalling = null;//获取信令开始时的对应的基站数据
					for(int m=0;m<list.size();m++)
					{
						if(mapSignalling.get(list.get(m).getCellID()+"#"+list.get(m).getLac())!=null)
						{
							starSignalling = mapSignalling.get(list.get(m).getCellID()+"#"+list.get(m).getLac());
							starIndex = m;
							countSignalling++;
							break;
						}
						
					}
					Signalling endSignalling = null;//获取信令结束时对应的基站数据
					for(int m=starIndex+1;m<list.size();m++)
					{
						if(mapSignalling.get(list.get(m).getCellID()+"#"+list.get(m).getLac())!=null)
						{
							endSignalling = mapSignalling.get(list.get(m).getCellID()+"#"+list.get(m).getLac());
							endIndex = m;
							countSignalling++;
						}
						
					}
					if(starSignalling == null || endSignalling==null)
					{
						System.out.println("index:"+starIndex);
						System.out.println("list.size():"+list.size());
						System.out.println("该用户不可用："+list);
						empty++;
						continue;
					}
					can++;
					Pline tempPline = starSignalling.getPline1();//得到信令对应的基站的最近的那条映射的路段
					Point2 startPoint = tempPline.getStartPoint();//获取这条路的一个端点
					String direction;
					if(Util.GetDistance(startPoint,starSignalling.getPoint())<Util.GetDistance(startPoint,endSignalling.getPoint()))
					{
						//证明和道路tempPline同向
						direction = tempPline.getDirection();
					}else
					{
						//证明和道路temPlline反向
						direction = tempPline.getDirection().equals("0")?"1":"0";//取反向
					}
					if(countSignalling>=minSignalling && list.get(endIndex).getTimeStamp().getHours()-list.get(starIndex).getTimeStamp().getHours()<=timegap)//判断是否在路上，不在路上就不处理
					{
						for(int j=0;j<list.size();j++)
						{
							Signalling tempSig = mapSignalling.get(list.get(j).getCellID()+"#"+list.get(j).getLac());//得到该信令所对应的基站
							if(tempSig!=null)//去除空的
							{
								Pline pl = tempSig.getPline1().getDirection().equals(direction)?tempSig.getPline1():tempSig.getPline2();//取出同向的路段
								count(pl,i);//给pl这段路加一,对应的就是第i个小时
							}
						}
					}else
					{
						notOnRoad++;
					}
				}else
				{
					count++;
				}
			}
			System.out.println("统计结束");
			System.out.println("过滤的人数："+count);
			System.out.println("总人数："+signallingStatistics.size());
			System.out.println("比例"+count/signallingStatistics.size());
			System.out.println("大于4个信令数据里面，有"+empty+"个数据不可用！");
			System.out.println("大于4个信令数据里面，有"+can+"个数据可用！");
			System.out.println("有"+notOnRoad+"个人不在路上");
			signallingStatistics = new HashMap<>();
		}
	}
	private static void count(Pline pl, int i) {//给对应路段计数
		ArrayList<Integer> list;
		if(roadStatistics.get(pl.getRoadID())==null){
			list = new ArrayList<>();
		}else{
			list = roadStatistics.get(pl.getRoadID());
		}
		for(int j=list.size();j<i+1;j++)//不足的位数需要补足
			list.add(0);
		list.add(i, list.get(i)+1);
		list.remove(i+1);
		roadStatistics.put(pl.getRoadID(), list);
	}
	private static void sort() {
		System.out.println("开始排序");
		for (Iterator<String> iterator = roalSignalling.keySet().iterator(); iterator.hasNext();) {
			String key = (String) iterator.next();
			Pline pline = allRoad.get(key);
			ArrayList<Signalling> list = roalSignalling.get(key);
			int size = list.size();
			ArrayList<Signalling> result = new ArrayList<>();
			for(int i=0;i<size;i++)
			{
				double dis = Double.MAX_VALUE;
				Signalling tempSig=null;
				for(int j=0;j<list.size();j++)
				{
					double temp = Util.GetDistance(pline.getStartPoint(), list.get(j).getPoint());
					if(temp<dis)
					{
						dis = temp;
						tempSig = list.get(j);
					}
				}
				if(tempSig==null)
				{
					System.out.println("程序异常");
					System.exit(0);
				}
				result.add(tempSig);
				list.remove(tempSig);
			}
		}
		System.out.println("排序结束");
	}
	public static void matching()//将所有的基站和道路进行匹配
	{
		System.out.println("开始匹配");
		for(int i=0;i<allSignallings.size();i++)
		{
			Signalling signalling = allSignallings.get(i);
			signalling.setPline1(getPlineBySig(signalling,false));//得到最近的投影路段
			signalling.setPline2(getPlineBySig(signalling,true));//得到倒数第二近的投影路段
			mapSignalling.put(signalling.getCellId()+"#"+signalling.getLacId(), signalling);
		}
		System.out.println("基站匹配结束");
	}
	/*
	 * flag = true 代表找到离他第二近的路
	 */
	private static Pline getPlineBySig(Signalling signalling,boolean flag) {
		Pline tempPline = null;
		ArrayList<Pline> plines = HandleData.allRoadInformation.get(signalling.getRoadId());
		if(plines==null)
		{
			System.out.println(signalling);
			System.exit(0);
		}
		double curDis=Double.MAX_VALUE;
		for(int i=0;i<plines.size();i++)
		{
			Point2 projectivePoint = Util.getProjPoint(plines.get(i),signalling.getPoint());
			if(projectivePoint.getX()>plines.get(i).getStartPoint().getX() && projectivePoint.getX()<plines.get(i).getEndPoint().getX()
					|| projectivePoint.getX()>plines.get(i).getEndPoint().getX() && projectivePoint.getX()<plines.get(i).getStartPoint().getX())
			{
				if((Util.GetDistance(projectivePoint, signalling.getPoint())/1000)<curDis)//加上距离约束
				{
					if(flag)//代表寻找另一边的道路
					{
						if(!plines.get(i).getRoadID().equals(signalling.getPline1().getRoadID()))
						{
							tempPline = plines.get(i);
							curDis = Util.GetDistance(projectivePoint, signalling.getPoint())/1000;
						}
					}else
					{
						tempPline = plines.get(i);
						curDis = Util.GetDistance(projectivePoint, signalling.getPoint())/1000;
					}
				}
			}
		}
		if(tempPline==null)//找一个最近的线段
		{
			curDis=Double.MAX_VALUE;
			for(int i=0;i<plines.size();i++)
			{
				double start =Util.GetDistance(plines.get(i).getStartPoint(), signalling.getPoint());
				double end = Util.GetDistance(plines.get(i).getEndPoint(), signalling.getPoint());
				if( start<curDis || end<curDis)
				{
					curDis = start>end ? end : start;
					tempPline = plines.get(i);
				}
			}
		}
		if(roalSignalling.get(tempPline.getRoadID())==null)
		{
			ArrayList<Signalling> sig = new ArrayList<>();
			sig.add(signalling);
			roalSignalling.put(tempPline.getRoadID(), sig);
 		}else
 		{
 			ArrayList<Signalling> sig = roalSignalling.get(tempPline.getRoadID());
			sig.add(signalling);
			roalSignalling.put(tempPline.getRoadID(), sig);
 		}
		return tempPline;
	}

	public static void dealSignallingToRoad()//将所有的基站读取加入列表
	{
		System.out.println("读取基站数据");
		FileUtil fileUtil = new FileUtil("cellInfo-201510.txt");
		String cellInfo = fileUtil.readLine();
		while(cellInfo!=null)
		{
			Signalling signalling = new Signalling(cellInfo);
			if(!(signalling.getRoadId().equals("G319")||signalling.getRoadId().equals("G324")))
				allSignallings.add(signalling);
			cellInfo = fileUtil.readLine();
		}
		System.out.println("读取结束");
	}
	private static void getAllRoad() {
		for(int i=0;i<HandleData.roads.length;i++)
		{
			ArrayList<Pline> temp = HandleData.allRoadInformation.get(HandleData.roads[i]);
			for (Iterator<Pline> iterator = temp.iterator(); iterator.hasNext();) {
				Pline pline = (Pline) iterator.next();
				allRoad.put(pline.getRoadID(), pline);
			}
		}
	}
}