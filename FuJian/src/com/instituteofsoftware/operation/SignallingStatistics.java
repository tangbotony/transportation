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
	 * ��ʼԤ�⣺
Ԥ�����
Ԥ���·����196
	 */
	public static int minSignalling = 4;
	public static int empty=0;//С��
	public static int can=0;//���տ�������
	public static int timegap = 4;//ʱ����4Сʱ
	public static int notOnRoad =0;
	public static ArrayList<Signalling> allSignallings = new ArrayList<>();//���еĻ�վ
	public static Map<String, String> cellMatching = new HashMap<String,String>();//key:cellId#lacId; ����key�õ�ƥ��ĵ�·id
	public static Map<String,ArrayList<Signalling>> roalSignalling = new HashMap<>();//key: roadID  G70#startID#endID �õ����·�ϵĻ�վ
	public static Map<String, Pline> allRoad = new HashMap<>();//key key: roadID  G70#startID#endID
	public static Map<String, ArrayList<RecoderSignalling>> signallingStatistics = new HashMap<>();//���ÿһ���û����������ݣ�key:MD5���ܵ绰����
	public static Map<String,Signalling> mapSignalling = new HashMap<>();//key  cell_id#Lac_id
	public static String dataPath="F:/Data/unzip/20151112";
	public static Map<String, ArrayList<Integer>> roadStatistics = new HashMap<String, ArrayList<Integer>>();//����ͳ�ƣ�����·�ε�id��ȡ key  cell_id#Lac_id
	
	public static void signallingStatistics()
	{
		getAllRoad();
		dealSignallingToRoad();//�����еĻ�վ��ȡ�����б�
		matching();//�����еĻ�վ�͵�·����ƥ��
		sort();//��ÿһ�ε�·�ϵ�gps����
		statistics();
	}
	@SuppressWarnings("deprecation")
	private static void statistics()
	{
		for(int i=0;i<((24*60)/HandleData.timeGap);i++)
		{
			
			System.out.println("��"+i+"��Сʱ�Ŀ�ʼ����");
			ArrayList<String> filesName = ReadFromTar.unpressData(HandleData.fileDate, i<10? "0"+i : i+"", HandleData.timeGap/60+"");
			System.out.println(filesName.size());
			System.out.println(filesName);
			for(String file : filesName){//�ϲ�ͬһ���û�������
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
			System.out.println("��"+i+"��Сʱ�Ĵ��������");
			int count = 0;
			System.out.println("�ܹ�������"+signallingStatistics.size());
			System.out.println("ͳ�ƿ�ʼ....");
			for(Iterator<String> iterator = signallingStatistics.keySet().iterator(); iterator.hasNext();) {
				String string = (String) iterator.next();
				ArrayList<RecoderSignalling> list = signallingStatistics.get(string);
				if(list.size()>=minSignalling)	
				{
					int starIndex = 0;//�������������һ����ƥ��·�ε����ݵ�����
					int endIndex =0;//���������������һ����ƥ��·�ε����ݵ�����
					int countSignalling = 0;//��¼�����������������ж��ٸ������ܹ�����ƥ��
					Signalling starSignalling = null;//��ȡ���ʼʱ�Ķ�Ӧ�Ļ�վ����
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
					Signalling endSignalling = null;//��ȡ�������ʱ��Ӧ�Ļ�վ����
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
						System.out.println("���û������ã�"+list);
						empty++;
						continue;
					}
					can++;
					Pline tempPline = starSignalling.getPline1();//�õ������Ӧ�Ļ�վ�����������ӳ���·��
					Point2 startPoint = tempPline.getStartPoint();//��ȡ����·��һ���˵�
					String direction;
					if(Util.GetDistance(startPoint,starSignalling.getPoint())<Util.GetDistance(startPoint,endSignalling.getPoint()))
					{
						//֤���͵�·tempPlineͬ��
						direction = tempPline.getDirection();
					}else
					{
						//֤���͵�·temPlline����
						direction = tempPline.getDirection().equals("0")?"1":"0";//ȡ����
					}
					if(countSignalling>=minSignalling && list.get(endIndex).getTimeStamp().getHours()-list.get(starIndex).getTimeStamp().getHours()<=timegap)//�ж��Ƿ���·�ϣ�����·�ϾͲ�����
					{
						for(int j=0;j<list.size();j++)
						{
							Signalling tempSig = mapSignalling.get(list.get(j).getCellID()+"#"+list.get(j).getLac());//�õ�����������Ӧ�Ļ�վ
							if(tempSig!=null)//ȥ���յ�
							{
								Pline pl = tempSig.getPline1().getDirection().equals(direction)?tempSig.getPline1():tempSig.getPline2();//ȡ��ͬ���·��
								count(pl,i);//��pl���·��һ,��Ӧ�ľ��ǵ�i��Сʱ
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
			System.out.println("ͳ�ƽ���");
			System.out.println("���˵�������"+count);
			System.out.println("��������"+signallingStatistics.size());
			System.out.println("����"+count/signallingStatistics.size());
			System.out.println("����4�������������棬��"+empty+"�����ݲ����ã�");
			System.out.println("����4�������������棬��"+can+"�����ݿ��ã�");
			System.out.println("��"+notOnRoad+"���˲���·��");
			signallingStatistics = new HashMap<>();
		}
	}
	private static void count(Pline pl, int i) {//����Ӧ·�μ���
		ArrayList<Integer> list;
		if(roadStatistics.get(pl.getRoadID())==null){
			list = new ArrayList<>();
		}else{
			list = roadStatistics.get(pl.getRoadID());
		}
		for(int j=list.size();j<i+1;j++)//�����λ����Ҫ����
			list.add(0);
		list.add(i, list.get(i)+1);
		list.remove(i+1);
		roadStatistics.put(pl.getRoadID(), list);
	}
	private static void sort() {
		System.out.println("��ʼ����");
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
					System.out.println("�����쳣");
					System.exit(0);
				}
				result.add(tempSig);
				list.remove(tempSig);
			}
		}
		System.out.println("�������");
	}
	public static void matching()//�����еĻ�վ�͵�·����ƥ��
	{
		System.out.println("��ʼƥ��");
		for(int i=0;i<allSignallings.size();i++)
		{
			Signalling signalling = allSignallings.get(i);
			signalling.setPline1(getPlineBySig(signalling,false));//�õ������ͶӰ·��
			signalling.setPline2(getPlineBySig(signalling,true));//�õ������ڶ�����ͶӰ·��
			mapSignalling.put(signalling.getCellId()+"#"+signalling.getLacId(), signalling);
		}
		System.out.println("��վƥ�����");
	}
	/*
	 * flag = true �����ҵ������ڶ�����·
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
				if((Util.GetDistance(projectivePoint, signalling.getPoint())/1000)<curDis)//���Ͼ���Լ��
				{
					if(flag)//����Ѱ����һ�ߵĵ�·
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
		if(tempPline==null)//��һ��������߶�
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

	public static void dealSignallingToRoad()//�����еĻ�վ��ȡ�����б�
	{
		System.out.println("��ȡ��վ����");
		FileUtil fileUtil = new FileUtil("cellInfo-201510.txt");
		String cellInfo = fileUtil.readLine();
		while(cellInfo!=null)
		{
			Signalling signalling = new Signalling(cellInfo);
			if(!(signalling.getRoadId().equals("G319")||signalling.getRoadId().equals("G324")))
				allSignallings.add(signalling);
			cellInfo = fileUtil.readLine();
		}
		System.out.println("��ȡ����");
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
