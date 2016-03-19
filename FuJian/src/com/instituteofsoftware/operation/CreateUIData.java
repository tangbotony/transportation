package com.instituteofsoftware.operation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.instituteofsoftware.bean.Pline;
import com.instituteofsoftware.bean.UIPath;
import com.instituteofsoftware.util.Util;

public class CreateUIData {
	public static ArrayList<ArrayList<UIPath>> data = new ArrayList<ArrayList<UIPath>>();//data.get(0)得到的是第一个小时的所有线段的集合
	public static Map<String,Pline> allStaticFlow = new HashMap<>();
	public static String color[] = {"#BB0000","#17BF00","#0000FF","#FFA500","#8B4513","#CD5C5C","#696969","#7B68EE","#DB7093"};
	public static Map<String,String> colorMap = new HashMap<>();
	public static int count=0;
	public static void exportData()
	{
		//静态流量数据：public static Map<String, Pline> staticFlow = new HashMap<String,Pline>();//key: G70#starID#endID
		//预测的静态流量：public static Map<String, ArrayList<Integer>> predictRoad = new HashMap<String, ArrayList<Integer>>();
		color();
		getAllStaticFlow();
		consolidate();
		System.out.println("道路数量："+allStaticFlow.size());
		System.out.println("count:"+count);
		deal();//根据样本设置颜色值和路得宽度
		Util.write2File("json.txt", JSON.toJSONString(data));
	}
	private static void color() {
		for(int i=0;i<color.length;i++)
		{
			colorMap.put(HandleData.roads[i], color[i]);
		}
	}
	private static void deal() {
		ArrayList<ArrayList<UIPath>> result = new ArrayList<ArrayList<UIPath>>();
		for(int i=0;i<data.size();i++)
		{
			ArrayList<UIPath> tempList = new ArrayList<>();
			ArrayList<UIPath> arrayList = data.get(i);
			double bigst=0.0;
			double small = Double.MAX_VALUE;
			for(int j=0;j<arrayList.size();j++)
			{
				if(arrayList.get(j).getFlow()<small)
					small = arrayList.get(j).getFlow();
				if(arrayList.get(j).getFlow()>bigst)
					bigst = arrayList.get(j).getFlow();
			}
			for(int m=0;m<arrayList.size();m++)
			{
				UIPath temp = arrayList.get(m);
				temp.setWidth((temp.getFlow()-small)/(bigst-small));
				tempList.add(temp);
			}
			result.add(tempList);
		}
		data = result;
	}
	private static void consolidate() {
		HashMap<Integer, ArrayList<UIPath>> tempMap  = new HashMap<>();
		for(String key : allStaticFlow.keySet())
		{
			Pline pline = allStaticFlow.get(key);
			ArrayList<Integer> forward = pline.getForward();
			if(forward.size()!=24)
			{
				System.out.println("该路流量大小："+forward.size());
				System.out.println(pline);
				System.exit(0);
			}
			for(int j=0;j<forward.size();j++)
			{
				UIPath path = new UIPath();
				path.setsLong(pline.getStartPoint().getX());//设置经度
				path.setsLat(pline.getStartPoint().getY());
				path.seteLong(pline.getEndPoint().getX());
				path.seteLat(pline.getEndPoint().getY());
				path.setFlow(forward.get(j));
				path.setColor(colorMap.get(pline.getRoadID().split("#")[0]));
				//将该点加入
				ArrayList<UIPath> tempList =null;
				if(tempMap.get(j)==null)
					tempList = new ArrayList<>();
				else
					tempList = tempMap.get(j);
				tempList.add(path);
				tempMap.put(j, tempList);
			}
		}
		int count=0;
		for(int i=0;i<24;i++)
		{
			count =count+tempMap.get(i).size();
			data.add(tempMap.get(i));
		}
		System.out.println("总共数据数："+count);
	}
	private static void getAllStaticFlow() {
		for(String key : HandleData.allRoadInformation.keySet())//得到所有的静态流量
		{
			ArrayList<Pline> road = HandleData.allRoadInformation.get(key);
			for(int i=0;i<road.size();i++)
			{
				Pline pline = road.get(i);
				if(HandleData.staticFlow.get(pline.getRoadID())!=null && HandleData.staticFlow.get(pline.getRoadID()).getForward()!=null && HandleData.staticFlow.get(pline.getRoadID()).getForward().size()==24)
				{
					pline.setForward(HandleData.staticFlow.get(road.get(i).getRoadID()).getForward());//设置静态流量，该静态流量是通过车间器统计得到的
				}else
				{
					if(Output2File.predictRoad.get(pline.getRoadID())!=null)//证明可以通过预测得到
					{
						pline.setForward(Output2File.predictRoad.get(pline.getRoadID()));
					}else//寻找相邻的路段的前向流量
					{
						count++;
						pline.setForward(findAdjFlow(pline.getRoadID()));
						if(pline.getForward()==null)
						{
							System.out.println(pline);
							System.out.println("程序异常，寻找空路没有找到相邻的路段");
							System.exit(0);
						}
					}
				}
				allStaticFlow.put(pline.getRoadID(), pline);
			}
		}
	}
	private static ArrayList<Integer> findAdjFlow(String key) {
		ArrayList<Integer> result=null;
		String road = key.split("#")[0];
		ArrayList<Pline> roadPline = HandleData.allRoadInformation.get(road);
		for(int i=0;i<roadPline.size();i++)
		{
			if(roadPline.get(i).getRoadID().equals(key))//找到，开始回溯
			{
				for(int j=i-1;j>=0;j--)
				{
					
					if(HandleData.staticFlow.get(roadPline.get(j).getRoadID())!=null || Output2File.predictRoad.get(roadPline.get(j).getRoadID())!=null)
					{
						result = HandleData.staticFlow.get(roadPline.get(j).getRoadID())!=null?HandleData.staticFlow.get(roadPline.get(j).getRoadID()).getForward():Output2File.predictRoad.get(roadPline.get(j).getRoadID());
						break;
					}
				}
				if(result==null)//向前展望
				{
					for(int j=i+1;j<roadPline.size();j++)
					{
						
						if(HandleData.staticFlow.get(roadPline.get(j).getRoadID())!=null || Output2File.predictRoad.get(roadPline.get(j).getRoadID())!=null)
						{
							result = HandleData.staticFlow.get(roadPline.get(j).getRoadID())!=null?HandleData.staticFlow.get(roadPline.get(j).getRoadID()).getForward():Output2File.predictRoad.get(roadPline.get(j).getRoadID());
							break;
						}
					}
				}
				return result;
			}
		}
		return result;
	}

}
