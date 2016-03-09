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
		db = mg.getDB(dBName);//��ȡtemp DB�����Ĭ��û�д�����mongodb���Զ�����
		collections = db.getCollection(collectionName);		//��ȡusers DBCollection�����Ĭ��û�д�����mongodb���Զ�����
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
	 * <b>function:</b> ��ѯ��������
	 * @author hoojo
	 * @createDate 2011-6-2 ����03:22:40
	 */
	public void queryAll() {
		print("��ѯusers���������ݣ�");
		//db�α�
		DBCursor cur = collections.find();
		while (cur.hasNext()) {
			print(cur.next());
		}
	}

	public void add() {
		//�Ȳ�ѯ��������
		queryAll();
		print("count: " + collections.count());

		DBObject user = new BasicDBObject();
		user.put("name", "hoojo");
		user.put("age", 24);
		//users.save(user)���棬getN()��ȡӰ������
		//print(users.save(user).getN());

		//��չ�ֶΣ���������ֶΣ���Ӱ����������
		user.put("sex", "��");
		print(collections.save(user).getN());

		//��Ӷ������ݣ�����Array����
		print(collections.insert(user, new BasicDBObject("name", "tom")).getN());

		//���List����
		List<DBObject> list = new ArrayList<DBObject>();
		list.add(user);
		DBObject user2 = new BasicDBObject("name", "lucy");
		user.put("age", 22);
		list.add(user2);
		//���List����
		print(collections.insert(list).getN());

		//��ѯ�����ݣ������Ƿ���ӳɹ�
		print("count: " + collections.count());
		queryAll();
	}


	public void remove() {
		queryAll();
		print("ɾ��id = 4de73f7acd812d61b4626a77��" + collections.remove(new BasicDBObject("_id", new ObjectId("4de73f7acd812d61b4626a77"))).getN());
		print("remove age >= 24: " + collections.remove(new BasicDBObject("age", new BasicDBObject("$gte", 24))).getN());
	}




	public void modify() {
		print("�޸ģ�" + collections.update(new BasicDBObject("_id", new ObjectId("4dde25d06be7c53ffbd70906")), new BasicDBObject("age", 99)).getN());
		print("�޸ģ�" + collections.update(
				new BasicDBObject("_id", new ObjectId("4dde2b06feb038463ff09042")), 
				new BasicDBObject("age", 121),
				true,//������ݿⲻ���ڣ��Ƿ����
				false//�����޸�
				).getN());
		print("�޸ģ�" + collections.update(
				new BasicDBObject("name", "haha"), 
				new BasicDBObject("name", "dingding"),
				true,//������ݿⲻ���ڣ��Ƿ����
				true//falseֻ�޸ĵ�һ�죬true����ж����Ͳ��޸�
				).getN());

		//�����ݿⲻ���ھͲ��޸ġ���������ݣ����������ݾͲ��޸�
		//print("�޸Ķ�����" + coll.updateMulti(new BasicDBObject("_id", new ObjectId("4dde23616be7c19df07db42c")), new BasicDBObject("name", "199")));
	}








	public void query() {
		//��ѯ����
		//queryAll();

		//��ѯid = 4de73f7acd812d61b4626a77
		print("find id = 4de73f7acd812d61b4626a77: " + collections.find(new BasicDBObject("_id", new ObjectId("4de73f7acd812d61b4626a77"))).toArray());

		//��ѯage = 24
		

		queryAll();
	}






	public void testOthers() {
		DBObject user = new BasicDBObject();
		user.put("name", "hoojo");
		user.put("age", 24);

		//JSON ����ת��        
		print("serialize: " + JSON.serialize(user));
		//�����л�
		print("parse: " + JSON.parse("{ \"name\" : \"hoojo\" , \"age\" : 24}"));

		print("�ж�temp Collection�Ƿ����: " + db.collectionExists("temp"));

		//��������ھʹ���
		if (!db.collectionExists("temp")) {
			DBObject options = new BasicDBObject();
			options.put("size", 20);
			options.put("capped", 20);
			options.put("max", 20);
			print(db.createCollection("account", options));
		}

		//����dbΪֻ��
		db.setReadOnly(true);

		//ֻ������д������
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
