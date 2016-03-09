package com.instituteofsoftware.util;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.util.JSON;


public class MongoDB4CRUD {

	private Mongo mg = null;
	private DB db;
	private DBCollection collections;

	
	public MongoDB4CRUD() {
		super();
	}

	public MongoDB4CRUD(String ipAddress,int port,String dBName,String collectionName) {
		try {
			mg = new Mongo(ipAddress,port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		}
		db = mg.getDB(dBName);//获取temp DB；如果默认没有创建，mongodb会自动创建
		collections = db.getCollection(collectionName);		//获取users DBCollection；如果默认没有创建，mongodb会自动创建
	}

	public void destory() {
		if (mg != null)
			mg.close();
		mg = null;
		db = null;
		collections = null;
		System.gc();
	}

	
	
	public void print(Object o) {
		System.out.println(o);
	}




	/**
	 * <b>function:</b> 查询所有数据
	 * @author hoojo
	 * @createDate 2011-6-2 下午03:22:40
	 */
	public void queryAll() {
		print("查询users的所有数据：");
		//db游标
		DBCursor cur = collections.find();
		while (cur.hasNext()) {
			print(cur.next());
		}
	}

	public void add() {
		//先查询所有数据
		queryAll();
		print("count: " + collections.count());

		DBObject user = new BasicDBObject();
		user.put("name", "hoojo");
		user.put("age", 24);
		//users.save(user)保存，getN()获取影响行数
		//print(users.save(user).getN());

		//扩展字段，随意添加字段，不影响现有数据
		user.put("sex", "男");
		print(collections.save(user).getN());

		//添加多条数据，传递Array对象
		print(collections.insert(user, new BasicDBObject("name", "tom")).getN());

		//添加List集合
		List<DBObject> list = new ArrayList<DBObject>();
		list.add(user);
		DBObject user2 = new BasicDBObject("name", "lucy");
		user.put("age", 22);
		list.add(user2);
		//添加List集合
		print(collections.insert(list).getN());

		//查询下数据，看看是否添加成功
		print("count: " + collections.count());
		queryAll();
	}


	public void remove() {
		queryAll();
		print("删除id = 4de73f7acd812d61b4626a77：" + collections.remove(new BasicDBObject("_id", new ObjectId("4de73f7acd812d61b4626a77"))).getN());
		print("remove age >= 24: " + collections.remove(new BasicDBObject("age", new BasicDBObject("$gte", 24))).getN());
	}




	public void modify() {
		print("修改：" + collections.update(new BasicDBObject("_id", new ObjectId("4dde25d06be7c53ffbd70906")), new BasicDBObject("age", 99)).getN());
		print("修改：" + collections.update(
				new BasicDBObject("_id", new ObjectId("4dde2b06feb038463ff09042")), 
				new BasicDBObject("age", 121),
				true,//如果数据库不存在，是否添加
				false//多条修改
				).getN());
		print("修改：" + collections.update(
				new BasicDBObject("name", "haha"), 
				new BasicDBObject("name", "dingding"),
				true,//如果数据库不存在，是否添加
				true//false只修改第一天，true如果有多条就不修改
				).getN());

		//当数据库不存在就不修改、不添加数据，当多条数据就不修改
		//print("修改多条：" + coll.updateMulti(new BasicDBObject("_id", new ObjectId("4dde23616be7c19df07db42c")), new BasicDBObject("name", "199")));
	}








	public void query() {
		//查询所有
		//queryAll();

		//查询id = 4de73f7acd812d61b4626a77
		print("find id = 4de73f7acd812d61b4626a77: " + collections.find(new BasicDBObject("_id", new ObjectId("4de73f7acd812d61b4626a77"))).toArray());

		//查询age = 24
		

		queryAll();
	}






	public void testOthers() {
		DBObject user = new BasicDBObject();
		user.put("name", "hoojo");
		user.put("age", 24);

		//JSON 对象转换        
		print("serialize: " + JSON.serialize(user));
		//反序列化
		print("parse: " + JSON.parse("{ \"name\" : \"hoojo\" , \"age\" : 24}"));

		print("判断temp Collection是否存在: " + db.collectionExists("temp"));

		//如果不存在就创建
		if (!db.collectionExists("temp")) {
			DBObject options = new BasicDBObject();
			options.put("size", 20);
			options.put("capped", 20);
			options.put("max", 20);
			print(db.createCollection("account", options));
		}

		//设置db为只读
		db.setReadOnly(true);

		//只读不能写入数据
		db.getCollection("test").save(user);
	}

	public Mongo getMg() {
		return mg;
	}

	public void setMg(Mongo mg) {
		this.mg = mg;
	}

	public DB getDB() {
		return db;
	}

	public void setDB(DB db) {
		this.db = db;
	}

	public DBCollection getCollections() {
		return collections;
	}

	public void setCollections(DBCollection collections) {
		this.collections = collections;
	}
}
