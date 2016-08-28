package com.xysoft.admin.service.impl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.xysoft.admin.service.UploadService;
import com.xysoft.common.ElementConst;
import com.xysoft.util.CommonUtil;
import com.xysoft.util.FileUtil;
import com.xysoft.util.JsonUtil;

@SuppressWarnings({"rawtypes", "unchecked"})
@Component
public class UploadServiceImpl implements UploadService {
	
	public String uploadFile(HttpServletRequest request) {
		//文件保存目录路径
		String savePath = FileUtil.getWebContent() + File.separator 
				+ ElementConst.File_Target_Dir + File.separator + ElementConst.File_Save_Dir;
		//文件保存目录URL
		String saveUrl  = FileUtil.speratorAnti 
				+ ElementConst.File_Target_Dir + FileUtil.speratorAnti + ElementConst.File_Save_Dir;
		//定义允许上传的文件扩展名
		Map<String, String> extMap = new HashMap<String, String>();
		extMap.put(ElementConst.Ext_Image, "gif,jpg,jpeg,png,bmp");
		extMap.put(ElementConst.Ext_Flash, "swf,flv");
		extMap.put(ElementConst.Ext_Media, "swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb,mp4");
		extMap.put(ElementConst.Ext_File, "doc,docx,xls,xlsx,ppt,htm,html,txt,zip,rar,gz,bz2,tif,pps,ppt,pptx,gif,jpg,jpeg,png,bmp,swf,flv,swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb");
		//最大文件大小
		long maxSize = 52428800;
		//判断是否存在文件
		if(!ServletFileUpload.isMultipartContent(request)){
			return getError("请选择文件");
		}
		//检查目录
		File uploadDir = new File(savePath);
		if(!uploadDir.isDirectory()){
			return getError("上传目录不存在");
		}
		//检查目录写权限
		if(!uploadDir.canWrite()){
			return getError("上传目录没有写权限");
		}
		String dirName = request.getParameter("dir");
		if (dirName == null) {
			dirName = ElementConst.Ext_Image;
		}
		if(!extMap.containsKey(dirName)){
			return getError("目录名不正确");
		}
		//创建文件夹
		savePath += File.separator + dirName + FileUtil.speratorAnti;
		saveUrl += FileUtil.speratorAnti + dirName + FileUtil.speratorAnti;
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
		
		MultipartHttpServletRequest mulrequest = (MultipartHttpServletRequest) request;
		Iterator itr = mulrequest.getFileNames();
		while (itr.hasNext()) {
			FileItem item = ((CommonsMultipartFile)mulrequest.getFileMap().get((String)itr.next())).getFileItem();
			String fileName = item.getName();
			if (!item.isFormField()) {
				//检查文件大小
				if(item.getSize() > maxSize){
					return getError("上传文件大小超过限制");
				}
				//检查扩展名
				String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
				if(!Arrays.<String>asList(extMap.get(dirName).split(",")).contains(fileExt)){
					return getError("上传文件扩展名是不允许的扩展名。\n只允许" + extMap.get(dirName) + "格式");
				}
				String newFileName = CommonUtil.getUUID() + "." + fileExt;
				try{
					File uploadedFile = new File(savePath, newFileName);
					item.write(uploadedFile);
				}catch(Exception e){
					return getError("上传文件失败");
				}

				Map<String, Object> map = new HashMap<String, Object>();
				map.put("error", 00);
				map.put("url", saveUrl + newFileName);
				JSONObject json = JSONObject.fromObject(map);
				return json.toString();
			}
		}
		return getError("上传失败");
	}

	private String getError(String message) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("error", 1);
		map.put("message", message);
		JSONObject json = JSONObject.fromObject(map);
		return json.toString();
	}

	@Override
	public String saveFile(CommonsMultipartFile upload) {
		if (upload.getSize() > 0) {
			String fileUrl = FileUtil.uploadFile(upload);
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("filename", upload.getOriginalFilename());
			return JsonUtil.toRes("上传成功", fileUrl, map);
		}
		return JsonUtil.toResOfFail("上传失败");
	}

	
//	public String getManagerFiles(HttpServletRequest request, String dir, String path) {
//		String rootPath = "";
//		//根目录URL，可以指定绝对路径，比如 http://www.yoursite.com/attached/
//		String rootUrl  = request.getContextPath() + "/resource/";
//		//图片扩展名
//		String[] fileTypes = new String[]{"gif", "jpg", "jpeg", "png", "bmp"};
//		
//		if (dir != null) {
//			if(!Arrays.<String>asList(new String[]{"image", "flash", "media", "file"}).contains(dir)){
//				return "Invalid Directory name.";
//			}
//			//dir = dir.split(",")[0];
//		}
//		//根据path参数，设置各路径和URL
//		path = request.getParameter("path") != null ? request.getParameter("path") : "";
//		if ("".equals(path) || "space/".equals(path)) {
//			path = "space/" + dir;
//		} else {
//			path = path.replaceAll("//", "/");
//		}
//		String currentPath = rootPath + path;
//		String currentUrl = rootUrl + path;
//		String currentDirPath = path;
//		String moveupDirPath = "";
//		if (!"".equals(path)) {
//			String str = currentDirPath.substring(0, currentDirPath.length() - 1);
//			moveupDirPath = str.lastIndexOf("/") >= 0 ? str.substring(0, str.lastIndexOf("/") + 1) : "";
//		}
//		
//		//排序形式，name or size or type
//		String order = request.getParameter("order") != null ? request.getParameter("order").toLowerCase() : "name";
//
//		//不允许使用..移动到上一级目录
//		if (path.indexOf("..") >= 0) {
//			return "Access is not allowed.";
//		}
//		
//		rootPath = path;
//		if (!"".equals(path) && path.endsWith("/")) {
//			rootPath = path.substring(0, path.length() - 1);
//		}
//		Space spacek = this.spaceDao.getSpaceByMarkcode(rootPath);
//		List<Space> spaces = new ArrayList<Space>();
//		if (spacek != null) {
//			spaces = spacek.getChildren();
//		}
//		
//		if (!"".equals(path) && path.endsWith("/")) {
//			rootPath = rootPath + "/";
//		}
//		
//		List<Hashtable> fileList = new ArrayList<Hashtable>();
//		for (Space space : spaces) {
//			Hashtable<String, Object> hash = new Hashtable<String, Object>();
//			if(!space.getIsLeaf()) {
//				hash.put("is_dir", true);
//				hash.put("has_file", true);
//				hash.put("filesize", 0L);
//				hash.put("is_photo", false);
//				hash.put("filetype", "");
//			} else {
//				String fileExt = space.getName().substring(space.getName().lastIndexOf(".") + 1).toLowerCase();
//				hash.put("is_dir", false);
//				hash.put("has_file", false);
//				hash.put("filesize", Long.valueOf(space.getLength()));
//				hash.put("is_photo", Arrays.<String>asList(fileTypes).contains(fileExt));
//				hash.put("filetype", fileExt);
//			}
//			hash.put("titlename", space.getName());
//			hash.put("filename", space.getMarkcode().substring(space.getMarkcode().lastIndexOf("/")));
//			hash.put("datetime", DateUtil.toStrYyyyMmDdHhMm(space.getModifyDate()));
//			fileList.add(hash);
//		}
//		
//		if ("size".equals(order)) {
//			Collections.sort(fileList, new SizeComparator());
//		} else if ("type".equals(order)) {
//			Collections.sort(fileList, new TypeComparator());
//		} else {
//			Collections.sort(fileList, new NameComparator());
//		}
//		
//		Map<String, Object> resObj = new HashMap<String, Object>();
//		resObj.put("moveup_dir_path", moveupDirPath);
//		resObj.put("current_dir_path", currentPath);
//		resObj.put("current_url", currentUrl);
//		resObj.put("total_count", fileList.size());
//		resObj.put("file_list", fileList);
//		return JsonUtil.toStringFromObject(resObj);
//	}
//	
//	public class NameComparator implements Comparator {
//		public int compare(Object a, Object b) {
//			Hashtable hashA = (Hashtable)a;
//			Hashtable hashB = (Hashtable)b;
//			if (((Boolean)hashA.get("is_dir")) && !((Boolean)hashB.get("is_dir"))) {
//				return -1;
//			} else if (!((Boolean)hashA.get("is_dir")) && ((Boolean)hashB.get("is_dir"))) {
//				return 1;
//			} else {
//				return ((String)hashA.get("filename")).compareTo((String)hashB.get("filename"));
//			}
//		}
//	}
//	public class SizeComparator implements Comparator {
//		public int compare(Object a, Object b) {
//			Hashtable hashA = (Hashtable)a;
//			Hashtable hashB = (Hashtable)b;
//			if (((Boolean)hashA.get("is_dir")) && !((Boolean)hashB.get("is_dir"))) {
//				return -1;
//			} else if (!((Boolean)hashA.get("is_dir")) && ((Boolean)hashB.get("is_dir"))) {
//				return 1;
//			} else {
//				if (((Long)hashA.get("filesize")) > ((Long)hashB.get("filesize"))) {
//					return 1;
//				} else if (((Long)hashA.get("filesize")) < ((Long)hashB.get("filesize"))) {
//					return -1;
//				} else {
//					return 0;
//				}
//			}
//		}
//	}
//	public class TypeComparator implements Comparator {
//		public int compare(Object a, Object b) {
//			Hashtable hashA = (Hashtable)a;
//			Hashtable hashB = (Hashtable)b;
//			if (((Boolean)hashA.get("is_dir")) && !((Boolean)hashB.get("is_dir"))) {
//				return -1;
//			} else if (!((Boolean)hashA.get("is_dir")) && ((Boolean)hashB.get("is_dir"))) {
//				return 1;
//			} else {
//				return ((String)hashA.get("filetype")).compareTo((String)hashB.get("filetype"));
//			}
//		}
//	}
}
