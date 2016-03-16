package com.instituteofsoftware.operation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import com.instituteofsoftware.bean.Pline;
import com.instituteofsoftware.bean.Recoder;
import com.instituteofsoftware.bean.VehicleInsp;
import com.instituteofsoftware.util.FileUtil;
import com.instituteofsoftware.util.Util;

public class StatisticsData {
	public static void statis()
	{
		System.out.println("开始统计");
		String fileName = HandleData.recoderFileName;
		FileUtil fileUtil = new FileUtil(fileName);
		Date baseDate = getBaseDateByFileName(fileName);
		String temp = fileUtil.readLine();
		int count=0;
		StringBuffer noUse = new StringBuffer();
		while(temp != null)
		{
			Recoder recoder = new Recoder(temp);
			if(!recoder.status.equals("0"))//数据不可用
			{
				noUse.append(temp);
				noUse.append("\n");
				temp = fileUtil.readLine();
				continue;
			}
			VehicleInsp vehicleInsp = null;
			if(getTimegap(baseDate, recoder.gatherTime)<(count+1)*HandleData.timeGap)
			{
				ArrayList<VehicleInsp>  vehicles= HandleData.allVehicleInsps.get(recoder.roadID);
				for(int i=0;i<vehicles.size();i++)
				{
					if(vehicles.get(i).getCoilID().equals(recoder.coilID))
					{
						vehicleInsp = vehicles.get(i);
						int forCars = recoder.forward1>0?recoder.forward1:0+recoder.forward2>0?recoder.forward2:0+recoder.forward3>0?recoder.forward3:0+recoder.forward4>0?recoder.forward4:0+recoder.forward5>0?recoder.forward5:0;
						int revCars= recoder.reverse1>0?recoder.reverse1:0+recoder.reverse2>0?recoder.reverse2:0+recoder.reverse3>0?recoder.reverse3:0+recoder.reverse4>0?recoder.reverse4:0+recoder.reverse5>0?recoder.reverse5:0;
						ArrayList<Integer> forward = vehicleInsp.getForward();
						ArrayList<Integer> reverse = vehicleInsp.getReverse();
						System.out.println("count"+count);
						if(forward.size()<count+1)
						{
							for(int j=forward.size();j<=count+1;j++)
							{
								forward.add(0);
								reverse.add(0);
							}
						}
						forward.add(count,forward.get(count)+forCars);
						forward.remove(count+1);
						vehicleInsp.setForward(forward);
						reverse.add(count,reverse.get(count)+revCars);
						reverse.remove(count+1);
						vehicleInsp.setReverse(reverse);
						vehicles.add(i,vehicleInsp);//把更改后的数据加入到原来的数组中，然后在移除多余的
						vehicles.remove(i++);
					}
				}
			}else
			{
				count++;
				continue;
			}
			if(vehicleInsp==null)
			{
				noUse.append(temp);
				noUse.append("\n");
			}
			temp = fileUtil.readLine();
		}
		for(int i=0;i<HandleData.roads.length;i++)//把同一条路上的车检器检测的数据合并
		{
			ArrayList<VehicleInsp> tempVehicleInsp = HandleData.allVehicleInsps.get(HandleData.roads[i]);//拿到对应道路上的车检器
			for(int j=0;j<tempVehicleInsp.size();j++)
			{
				VehicleInsp temp1 = tempVehicleInsp.get(j);
				if(HandleData.staticFlow.get(temp1.getPline().getRoadID())==null)
				{
					Pline pline = temp1.getPline();
					pline.setForward(temp1.getForward());
					pline.setReverse(temp1.getReverse());
					HandleData.staticFlow.put(temp1.getPline().getRoadID(),pline);
				}else
				{
					Pline pline = HandleData.staticFlow.get(temp1.getPline().getRoadID());
					pline.setForward(merge(temp1.getForward(),pline.getForward()));
					pline.setReverse(merge(temp1.getReverse(),pline.getReverse()));
					HandleData.staticFlow.put(temp1.getPline().getRoadID(),pline);
				}
			}
		}
		int countZero =0;
		for (Iterator<String> iterator =  HandleData.staticFlow.keySet().iterator(); iterator.hasNext();) {
			String type = (String) iterator.next();
			int zero = 0;
			ArrayList<Integer> temp1 = HandleData.staticFlow.get(type).getForward();
			if(temp1==null ||temp1.size()==0)
				countZero++;
			for(int i=0;i<temp1.size();i++)
			{
				if(temp1.get(i)==0)
					zero++;
			}
			zero =zero+(count+1)-temp1.size();
			System.out.println(HandleData.staticFlow.get(type).getRoadID()+"\t"+zero+"\t"+HandleData.staticFlow.get(type).getStartPoint()+"\t"+HandleData.staticFlow.get(type).getEndPoint()+"\t"+HandleData.staticFlow.get(type).getForward());
		}
		System.out.println("道路条数:"+HandleData.staticFlow.size()+";道路为空的条数:"+countZero);
		
		Util.write2File("nouse.txt", noUse.toString());
	}
	private static ArrayList<Integer> merge(ArrayList<Integer> staticFlow1, ArrayList<Integer> staticFlow2) {
		ArrayList<Integer> result = new ArrayList<>();
		int size1 = staticFlow1.size();
		int size2 = staticFlow2.size();
		for(int i=0;i<(size1>size2?size1:size2);i++)
		{
			result.add((i<size1?staticFlow1.get(i):0)+(i<size2?staticFlow2.get(i):0));
		}
		return result;
	}
	private static Date getBaseDateByFileName(String fileName) {
		SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date start=null;
		try {
			start = sdf.parse(fileName.substring(0,4)+"-"+fileName.substring(4, 6)+"-"+fileName.substring(6,8)+" 00:00:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return start;
	}
	private static long getTimegap(Date start,Date end) {
		return(end.getTime() - start.getTime())/(1000*60);
	}
}
