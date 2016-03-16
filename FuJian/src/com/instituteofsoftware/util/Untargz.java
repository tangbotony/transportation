package com.instituteofsoftware.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

import org.kamranzafar.jtar.TarEntry;
import org.kamranzafar.jtar.TarInputStream;

/**
 * 
 * @author Jason Li 2014-5 不再把文件都解压出来，全部在操作数据流
 */
public class Untargz {

	public void readtar(String tarFile, String destFolder) {
		File ss = new File(tarFile);
		TarInputStream tis = null;
		try {
			tis = new TarInputStream(new GZIPInputStream(
					new BufferedInputStream(new FileInputStream(ss))));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		TarEntry entry;

		try {
			while ((entry = tis.getNextEntry()) != null) {
				int count;
				byte data[] = new byte[204800];

				FileOutputStream fos = new FileOutputStream(new File(destFolder
						+ entry.getName()));
				BufferedOutputStream dest = new BufferedOutputStream(fos);

				while ((count = tis.read(data)) != -1) {
					dest.write(data, 0, count);
				}
				dest.flush();
				dest.close();
			}
			tis.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Untargz ungz = new Untargz();
		ungz.readtar("ftpdata/mc_xm_001_20141229172700_001.txt.tar.gz",
				"ftpunpress/");
	}
}