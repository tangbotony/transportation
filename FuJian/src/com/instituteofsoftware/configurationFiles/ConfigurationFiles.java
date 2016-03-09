package com.instituteofsoftware.configurationFiles;

import com.instituteofsoftware.util.MongoDB4CRUD;

public class ConfigurationFiles {
	public static final double RADIUS=200;
	public static final double AVERAGE=0;
	public static final double VARIANCE = 20;
	public static final String IPADDRESS = "133.133.134.98";
	public static final double PRECISION = 1.0E-6;//���ͶӰ��ʱ����
	public static final int GAP = 1;//����������ļ�������ڲ���ͶӰ��ʱ���õı���
	public static final int LIMIT = 3;//������Һ�ѡ���������Բ鵽�Ĺ�·
	
	public static final int MONGODBPORT = 27017;
	public static final int FILESIZE = 6392;//��ֵ���ڴ���ԭʼGPS����ʱ������ļ���С������ļ�С�����ֵ������Ҫ���� 6K
	public static MongoDB4CRUD mongoDB4CRUDTaxiInformation = new MongoDB4CRUD(ConfigurationFiles.IPADDRESS, ConfigurationFiles.MONGODBPORT, "fujian", "oldRoadInformation");
	//roadInformation
	public static MongoDB4CRUD mongoDB4CRUDRoadInformation = new MongoDB4CRUD(ConfigurationFiles.IPADDRESS, ConfigurationFiles.MONGODBPORT, "fujian", "roadInformation");

	public static double FLAG = -1;
}
