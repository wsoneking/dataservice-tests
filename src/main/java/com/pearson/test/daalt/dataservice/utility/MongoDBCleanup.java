package com.pearson.test.daalt.dataservice.utility;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

public class MongoDBCleanup {
	public static void main(String[] args) throws Exception {
		ServerAddress server = new ServerAddress("10.252.3.186");
		MongoClient client = new MongoClient(server);
		DB db = client.getDB("admin");
		DBCollection collection = db.getCollection("TestEngineDB");
		DBCursor cursor = collection.find();
		
		while(cursor.hasNext()) {
			BasicDBObject obj = (BasicDBObject) cursor.next();
			String opColl = obj.getString("collection");
			int days = obj.getInt("days_retained");
			
			Calendar cutoff = new GregorianCalendar();
			cutoff.add(Calendar.DATE, -10*days);
			
			BasicDBObject query = new BasicDBObject();
			query.put("date", new BasicDBObject("$lte", cutoff.getTime()));
			
			DBCollection toClean = db.getCollection(opColl);
			System.out.println("Collection " + opColl + " - " + toClean.find().count() + " records, " + toClean.find(query).count() + " to be removed");
			toClean.remove(query);
			db.command(new BasicDBObject("compact", opColl));
		}
		
		client.close();
		System.out.println("Cleanup Complete");


	}
}
