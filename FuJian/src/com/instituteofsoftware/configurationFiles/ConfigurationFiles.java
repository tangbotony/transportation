package com.instituteofsoftware.configurationFiles;

import com.instituteofsoftware.util.MongoDB4CRUD;

public class ConfigurationFiles {
	public static final double RADIUS=200;
	public static final double AVERAGE=0;
	public static final double VARIANCE = 20;
	public static final String IPADDRESS = "133.133.134.98";
	public static final double PRECISION = 1.0E-6;//求解投影的时候用
	public static final int GAP = 1;//定义两个点的间隔，用于查找投影点时候用的变量
	public static final int LIMIT = 3;//定义查找候选点是最多可以查到的公路
	
	public static final int MONGODBPORT = 27017;
	public static final int FILESIZE = 6392;//该值是在处理原始GPS数据时定义的文件大小，如果文件小于这个值，则不需要处理 6K
	public static MongoDB4CRUD mongoDB4CRUDTaxiInformation = new MongoDB4CRUD(ConfigurationFiles.IPADDRESS, ConfigurationFiles.MONGODBPORT, "fujian", "oldRoadInformation");
	//roadInformation
	public static MongoDB4CRUD mongoDB4CRUDRoadInformation = new MongoDB4CRUD(ConfigurationFiles.IPADDRESS, ConfigurationFiles.MONGODBPORT, "fujian", "roadInformation");

	public static double FLAG = -1;
}