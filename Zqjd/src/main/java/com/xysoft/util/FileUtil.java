package com.xysoft.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.fileupload.FileItem;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.xysoft.common.ElementConst;

public class FileUtil {
	/**
	 * 反斜杆.
	 */
	public static final String speratorAnti = "/";
	
	/**
	 * 获取WebContent根目录地址.
	 */
	public static String getWebContent() {
		String path = FileUtil.class.getResource("/").getPath();
		path = path.substring(1);
		File file = new File(path);
		return "/"+file.getParentFile().getParent();
	}
	
	/**
	 * 获取WEB-INF目录地址.
	 */
	public static String getRoot() {
		String path = FileUtil.class.getResource("/").getPath();
		path = path.substring(1);
		File file = new File(path);
		return "/"+file.getParent();
	}
	
	/**
	 * 删除文件.
	 */
	public static void deleteFile(String strDir, String fileName)
	{
		String targetPath = FileUtil.getWebContent()+ strDir;
		File file = new File(targetPath + File.separator + fileName);
		if (file.exists()) file.delete();
	}
	
	/**
	 * 保存文件(上传).
	 */
	public static String uploadFile(CommonsMultipartFile upload) {
		//文件保存目录路径
		String savePath = FileUtil.getWebContent() + File.separator 
				+ ElementConst.File_Target_Dir + File.separator + ElementConst.File_Save_Dir;
		//文件保存目录URL
		String saveUrl  = FileUtil.speratorAnti 
				+ ElementConst.File_Target_Dir + FileUtil.speratorAnti + ElementConst.File_Save_Dir;
		//定义允许上传的文件扩展名
		Map<String, String> extMap = new HashMap<String, String>();
		extMap.put(ElementConst.Ext_Image, "gif,jpg,jpeg,png,bmp");
		
		//创建文件夹
		savePath += File.separator + "image" + FileUtil.speratorAnti;
		saveUrl += FileUtil.speratorAnti + "image" + FileUtil.speratorAnti;
		File saveDirFile = new File(savePath);
		if (!saveDirFile.exists()) {
			saveDirFile.mkdirs();
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		String ymd = sdf.format(new Date());
		savePath += ymd + "/";
		saveUrl += ymd + "/";
		File dirFile = new File(savePath);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}
		FileItem item = upload.getFileItem();
		String fileName = item.getName();
		//检查扩展名
		String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
		String newFileName = CommonUtil.getUUID() + "." + fileExt;
		try{
			File uploadedFile = new File(savePath, newFileName);
			item.write(uploadedFile);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return saveUrl + newFileName;
	}
}
