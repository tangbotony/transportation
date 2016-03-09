package com.instituteofsoftware.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class FileUtil {
	String fileName = null;
	BufferedReader reader = null; 
	BufferedWriter writer = null;
	ObjectOutputStream objOut = null;
	ObjectInputStream objIn = null;
	
	boolean append = false;
	
	public FileUtil(String fileName) {
		super();
		this.fileName = fileName;
	}
	public FileUtil(String fileName,boolean append) {
		super();
		this.fileName = fileName;
		this.append = append;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public void close(){
		try {
			if(this.reader!=null)
				this.reader.close();
			if(this.writer!=null)
				this.writer.close();
			if(this.objOut!=null)
				this.objOut.close();
			if(this.objIn!=null)
				this.objIn.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//��ȡ�ļ���һ�ζ�һ��
	public String readLine(){
		if(this.reader == null){
			try {
				this.reader = new BufferedReader(new FileReader(this.fileName));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (@SuppressWarnings("hiding") IOException e) {
				e.printStackTrace();
			}
		}
		try {
			return this.reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	public void writeLine(String str){
		if(this.writer == null){
			try {
				if(append) 
					this.writer = new BufferedWriter(new FileWriter(this.fileName,true));
				else
					this.writer = new BufferedWriter(new FileWriter(this.fileName));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			this.writer.write(str);
			this.writer.newLine();
			this.writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//���ļ���д�����
	public void writeObj(Object obj){
		try {
			this.objOut = new ObjectOutputStream(new FileOutputStream(this.fileName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			objOut.writeObject(obj);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//���ļ��ж�ȡ����
	public Object readObj(){
		Object obj = null;
		try {
			this.objIn = new ObjectInputStream(new FileInputStream(this.fileName));
			obj=objIn.readObject();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return obj;
	}
	public static void main(String[] args) {
		FileUtil out = new FileUtil("test.txt",true);
		out.writeLine("adasf");
	}
	
	
	
	
	
}
