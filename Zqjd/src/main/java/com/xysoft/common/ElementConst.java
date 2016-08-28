package com.xysoft.common;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import com.xysoft.util.FileUtil;

public class ElementConst {
	/**
	 * 语言国际化.
	 */
	public static final String Lang_English = "en";
	public static final String Land_Chinese = "zh";
	
	public static final String Ext_Image = "image";
	public static final String Ext_File = "file";
	public static final String Ext_Flash = "flash";
	public static final String Ext_Media = "media";
	
	/**
	 * 文件保存位置.
	 */
	public static final String File_Target_Dir = "resource";
	
	public static final String File_Save_Dir = "common";
	
	public static final String File_Temp_Dir = "temp";
	
	/**
	 * 微信公众平台参数
	 */
	public static String Wx_Token = "";
	public static String Wx_AppId = "";
	public static String Wx_AppSecret = "";
	
	/**
	 * 服务器地址.
	 */
	public static String Service_Address = "";
	public static String solr_address = "";
	
	static {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(FileUtil.getRoot() + "/key.properties");
			Properties props = new Properties();
			props.load(fis);
			Wx_Token = props.getProperty("wx_token");
			Wx_AppId = props.getProperty("wx_appid");
			Wx_AppSecret = props.getProperty("wx_appsecret");
			Service_Address = props.getProperty("service_address");
			solr_address = props.getProperty("solr_address");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fis != null)
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
}
  