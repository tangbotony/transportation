package com.instituteofsoftware.operation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.instituteofsoftware.bean.Pline;
import com.instituteofsoftware.util.Util;

public class Output2File {
	public static StringBuffer result = new StringBuffer();
	public static ArrayList<Integer> staticF = new ArrayList<>();
	public static ArrayList<Integer> relatF = new ArrayList<>();
	public static Map<String, Pline> mapPline = new HashMap<String, Pline>();
	public static Map<String, ArrayList<Integer>> predictRoad = new HashMap<String, ArrayList<Integer>>();
	public static void wirte()
	{
		//HandleData.staticFlow.put(temp1.getPline().getRoadID(),pline);
		//Map<String, ArrayList<Integer>> roadStatistics
		ArrayList<Integer> staticFlow;
		ArrayList<Integer> relativelyFlow;
		for(String key : SignallingStatistics.roadStatistics.keySet())
		{
			if(HandleData.staticFlow.get(key)==null)
				continue;
			staticFlow = HandleData.staticFlow.get(key).getForward();
			relativelyFlow = SignallingStatistics.roadStatistics.get(key);
			if(staticFlow==null || relativelyFlow==null || staticFlow.size()!=24 || !isNoZero(staticFlow) ||
					relativelyFlow.size()!=24 || !isNoZero(relativelyFlow))
				continue;

			for(int i=0;i<24;i++)
			{
				staticF.add(staticFlow.get(i));
				relatF.add(relativelyFlow.get(i));
				result.append(relativelyFlow.get(i)+"\t"+staticFlow.get(i));
				result.append("\n");
			}
			String temp[] = fitting(staticFlow, relativelyFlow).split("@");
			result.append(temp[0]);
			result.append("\n");
			Pline pline = HandleData.staticFlow.get(key);
			String abs[] = temp[1].split("#");
			pline.setA(Double.parseDouble(abs[0]));
			pline.setB(Double.parseDouble(abs[1]));
			mapPline.put(pline.getRoadID(), pline);
		}
		Util.write2File("result.txt", result.toString());
		predict();
		
	}
	private static void predict() {
		System.out.println("开始预测：");
		StringBuffer re = new StringBuffer();
		StringBuffer empty = new StringBuffer();
		ArrayList<Integer> staticFlow=null;
		ArrayList<Integer> relativelyFlow=null;
		for(String key : SignallingStatistics.allRoad.keySet())//取出所有的道路
		{
			if(mapPline.get(key) != null)//证明这条路段不需要预测
				continue;
			relativelyFlow = SignallingStatistics.roadStatistics.get(key);
			if(relativelyFlow!=null)
			{
				staticFlow = new ArrayList<Integer>();
				for(int i=0;i<relativelyFlow.size();i++)
					staticFlow.add(0);
				
				Pline upPline = getUpPline(key);
				staticFlow=getFlow(upPline,relativelyFlow,staticFlow,false);
				Pline downPLine=getDownPline(key);
				staticFlow=getFlow(downPLine,relativelyFlow,staticFlow,true);
				re.append(SignallingStatistics.allRoad.get(key)+"\n");
				for(int i=0;i<relativelyFlow.size();i++)
					re.append(relativelyFlow.get(i)+"\t"+staticFlow.get(i)+"\n");
				predictRoad.put(key, staticFlow);
			}else
			{
				empty.append(SignallingStatistics.allRoad.get(key) +"\n");
			}
			
		}
		Util.write2File("empty.txt", empty.toString());
		Util.write2File("predict.txt", re.toString());
		System.out.println("预测结束");
		System.out.println("预测道路数："+predictRoad.size());
		System.out.println("相对流量的道路数："+SignallingStatistics.roadStatistics.size());
	}
	//添加预测流量
	private static ArrayList<Integer> getFlow(Pline pline, ArrayList<Integer> relativelyFlow,
			ArrayList<Integer> staticFlow,boolean isSecond) {
		if(pline==null)
			return staticFlow;
		double a = pline.getA();
		double b = pline.getB();
		ArrayList<Integer> result = new ArrayList<>();
		System.out.println("恢复函数:"+"y = " + a + " * x + " + b);
		for(int i=0;i<relativelyFlow.size();i++)
		{
			double pre = relativelyFlow.get(i)*a+b;
			result.add((int)(isSecond?((pre+staticFlow.get(i))/2):(pre)));
		}
		return result;
	}
	private static Pline getDownPline(String key) {
		Pline pline = null;
		String road = key.split("#")[0];
		ArrayList<Pline> roadPline = HandleData.allRoadInformation.get(road);
		for(int i=0;i<roadPline.size();i++)
		{
			if(roadPline.get(i).getRoadID().equals(key))//找到，开始回溯
			{
				for(int j=i+1;j<roadPline.size();j++)
				{
					if(mapPline.get(roadPline.get(j).getRoadID())!=null)
					{
						pline = mapPline.get(roadPline.get(j).getRoadID());
						break;
					}
				}
				return pline;
			}
		}
		return pline;
	}
	private static Pline getUpPline(String key) {
		Pline pline = null;
		String road = key.split("#")[0];
		ArrayList<Pline> roadPline = HandleData.allRoadInformation.get(road);
		for(int i=0;i<roadPline.size();i++)
		{
			if(roadPline.get(i).getRoadID().equals(key))//找到，开始回溯
			{
				for(int j=i-1;j>=0;j--)
				{
					if(mapPline.get(roadPline.get(j).getRoadID())!=null)
					{
						pline = mapPline.get(roadPline.get(j).getRoadID());
						break;
					}
				}
				return pline;
			}
		}
		return pline;
	}
	public static String fitting(ArrayList<Integer> x,ArrayList<Integer> y)
	{
		int n = 0;
		@SuppressWarnings("unused")
		double sumx = 0.0, sumy = 0.0, sumx2 = 0.0;
		while (n < x.size()) {
			sumx += x.get(n);
			sumx2 += x.get(n) * x.get(n);
			sumy += y.get(n);
			n++;
		}
		// 求平均数
		double xbar = sumx / n;
		double ybar = sumy / n;
		// 计算系数
		@SuppressWarnings("unused")
		double xxbar = 0.0, yybar = 0.0, xybar = 0.0;
		for (int i = 0; i < n; i++) {
			xxbar += (x.get(i)- xbar) * (x.get(i) - xbar);
			yybar += (y.get(i) - ybar) * (y.get(i) - ybar);
			xybar += (x.get(i) - xbar) * (y.get(i) - ybar);
		}
		double beta1 = xybar / xxbar;
		double beta0 = ybar - beta1 * xbar;
		return "y = " + beta1 + " * x + " + beta0+"@"+beta1+"#"+beta0;
	}
	public static double cal() {
		double sum=0;
		for(int i=0;i<staticF.size();i++)
		{
			sum = sum + ((111.157109 + 0.046332 * relatF.get(i))-staticF.get(i))*((111.157109 + 0.046332 * relatF.get(i))-staticF.get(i));
		}
		System.out.println("sum:"+sum);
		System.out.println("staticF.size():"+staticF.size());
		return Math.sqrt(sum/(staticF.size()-2));
	}
	//	public static void main(String[] args) {
	//		staticF.add(1);
	//		relatF.add(1);
	//		staticF.add(2);
	//		relatF.add(3);
	//		staticF.add(3);
	//		relatF.add(4);
	//		System.out.println(cal());
	//	}
	private static boolean isNoZero(ArrayList<Integer> staticFlow) {
		boolean flag = true;
		for(Integer inter : staticFlow)
		{
			if(inter==0)
			{
				flag = false;
				break;
			}
		}
		return flag;
	}
}
