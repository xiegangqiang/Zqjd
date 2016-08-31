package com.xysoft.util;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import com.xysoft.common.ElementConst;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
/**
 * EXCEL工具类，用于导入导出
 * @author 谢刚强
 *
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class ExcelUtil {
	
	private static String savePath;
	
	private static final String FILE_TYPE = ".xls";
	
	static {
	    savePath = FileUtil.getWebContent() + File.separator +  ElementConst.File_Target_Dir + File.separator + ElementConst.File_Temp_Dir + File.separator;
		File saveDirFile = new File(savePath);
		if( !saveDirFile.exists() ){
			saveDirFile.mkdirs();
		}
	}
	
	/**
	 * 创建EXCEL文件供前台下载
	 * @param filename 解压后生成Excel文件名称
	 * @param title 表头(自定义)
	 * @param objs 要导出文件的对象数据集合
	 * @return
	 */
	public static  String  createExcel (String filename, String[] title, String[] field, List<Object> objs) {
		File file = new File(savePath + filename + FILE_TYPE);
		String downDir = "";
		try {
			file.createNewFile();
			downDir = FileUtil.speratorAnti + ElementConst.File_Target_Dir + FileUtil.speratorAnti + ElementConst.File_Temp_Dir + FileUtil.speratorAnti + filename + FILE_TYPE;
			FileOutputStream os = new FileOutputStream(file);
			// 创建可以写入的Excel工作薄(默认运行生成的文件在tomcat/bin下 )
			WritableWorkbook wwb = Workbook.createWorkbook(os);
			// 生成工作表,(name:sheetName,参数0表示这是第一页)
			WritableSheet sheet = wwb.createSheet(filename, 0);
			
			//反射机制根据前台传来的属性名称与实体名称判断，从而调用不同的get方法构造出excle的数据集合
			List<Map> lists = new ArrayList<Map>();
				for (Object obj : objs) {
					Map<String, String> excle = new HashMap<String, String>();
					for (int i = 0; i < field.length; i++) {
					Class<Object> clz = (Class<Object>) obj.getClass();
					Field[] fields = clz.getDeclaredFields();
					for (int j = 0; j < fields.length; j++) {
						if( field[i].equals( fields[j].getName() ) ){
							Method method = obj.getClass().getMethod("get" + getMethodName(fields[j].getName()));
							if(method.invoke(obj) != null){
								excle.put(field[i], method.invoke(obj).toString());
							}else excle.put(field[i], "");
						}
					}
				}
				lists.add(excle);
			}
			
			//设置表头，以文件名为表头
			sheet.mergeCells(0, 0, lists.get(0).size()-1, 0);
			//表头单元格样式
			WritableCellFormat wcf1 = new WritableCellFormat(new WritableFont(WritableFont.createFont("宋体"), 20, WritableFont.BOLD));
			//表头设置居中 
			wcf1.setAlignment(Alignment.CENTRE); 
	        //表头设置边框线 
			wcf1.setBorder(Border.ALL, BorderLineStyle.THIN); 
			Label lb = new Label(0, 0, filename, wcf1);
			sheet.addCell(lb);
			
			
			//标题栏单元格样式
			WritableCellFormat wcf2 = new WritableCellFormat(new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD));
			//标题栏设置居中 
			wcf2.setAlignment(Alignment.CENTRE); 
	        //标题栏设置边框线 
			wcf2.setBorder(Border.ALL, BorderLineStyle.THIN); 
			// 开始写入第一行(即标题栏)
			for (int i = 0; i < title.length; i++) {
				// 用于写入文本内容到工作表中去
				Label label = null;
				// 在Label对象的构造中指明单元格位置(参数依次代表列数、行数、内容 )
				label = new Label(i, 1, title[i], wcf2);
				// 将定义好的单元格添加到工作表中
				sheet.setColumnView(i, 15);
				sheet.addCell(label);
			}
			
			//内容单元格样式
			WritableCellFormat wcf3 = new WritableCellFormat();
			//内容设置居中 
			wcf3.setAlignment(Alignment.CENTRE); 
	        //内容设置边框线 
			wcf3.setBorder(Border.ALL, BorderLineStyle.THIN); 
			// 开始写入内容
			for (int row = 0; row < lists.size(); row++) {
				// 获取一条(一行)记录
				Map map = (HashMap) lists.get(row);
				// 数据是文本时(用label写入到工作表中)
				for (int col = 0; col < map.size(); col++) {
					if(map.containsKey(field[col])){
						String listvalue = (String) map.get(field[col]).toString();
						Label label = null;
						label = new Label(col, row + 2, listvalue, wcf3);
						sheet.addCell(label);
					}
				}
				
			}
			wwb.write();
			// 关闭文件
			wwb.close();
			// 关闭输出流
			os.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return downDir;
	}

	private static String getMethodName(String fildeName) throws Exception{
		byte[] items = fildeName.getBytes();
		items[0] = (byte) ((char) items[0] - 'a' + 'A');
		return new String(items);
	}
	
	
	/**
	 * 读取EXCEL文件，用于导入
	 * @param excel 要读取的EXCEL文件
	 */
	public static List<Object> readExcel(CommonsMultipartFile excel, Class<?> clz) {
		if(!excel.isEmpty()){
			String originalName = excel.getOriginalFilename();//文件原名称
			String fileExt = originalName.substring(originalName.lastIndexOf("."));//后缀
			String newFileName = CommonUtil.getUUID() + fileExt;
			if( "xls,xlsx".indexOf( fileExt.substring(1) )  == -1 ) {
				return null;
			}
			try {
				//Class<Object> clz = (Class<Object>) object.getClass();
				List<Object> objects = new ArrayList<Object>();
				File temp = new File(savePath + newFileName);//保存路径
				if(temp.exists()) temp.delete();//是否存在，存在就删除该文件
				FileCopyUtils.copy(excel.getBytes(), temp);//保存在临时文件夹
				Workbook book = Workbook.getWorkbook(temp);
				Sheet sheet = book.getSheet(0);//第一个工作表
				//Cell cell = null;
				int rows = sheet.getRows();//总行数
				int cols = sheet.getColumns();//总列数
				for (int i = 2; i < rows; i++) {
					Object obj = clz.newInstance();
					for (int j = 0; j < cols; j++) {
						 Class<?> fieldType = clz.getDeclaredField( sheet.getCell(j, 0).getContents().trim() ).getType();
						 Method method = clz.getMethod("set" + getMethodName( sheet.getCell(j, 0).getContents().trim() ), fieldType);
						 String cellVal = sheet.getCell(j, i).getContents().trim();//单元格的值
						 //判断参数类型
						 if( "class java.lang.String".equals(fieldType.toString()) ) {
							 method.invoke(obj, cellVal);
						 }//字符串类型
						 if( "class java.lang.Integer".equals(fieldType.toString()) ) {
							 method.invoke(obj, Integer.valueOf( cellVal ));
						 }//Integer类型
						 if( "class java.lang.Double".equals(fieldType.toString()) ) {
							 method.invoke(obj, Double.valueOf(cellVal));
						 }//Double类型
						 if( "class java.lang.Float".equals(fieldType.toString()) ){
							 method.invoke(obj, Float.valueOf(cellVal));
						 }//Float类型
						 if( "class java.lang.Boolean".equals(fieldType.toString()) ) {
							 if( "true".equals(cellVal)){
								 method.invoke(obj, true);
							 }else {
								 method.invoke(obj, false);
							 }
						 }//Boolean类型
						 if( "class java.util.Date".equals(fieldType.toString()) ) {
							 method.invoke(obj, DateUtil.toDate(cellVal, "yyyyMMddHHmmss"));
						 }//Date类型
						 if( "class java.lang.Short".equals(fieldType.toString()) ) {
							 method.invoke(obj, Short.valueOf(cellVal));
						 }//Short类型
					}
					objects.add(obj);
				}
				book.close();
				temp.delete();
				return objects;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			return null;
		}
		return null;
	}
	
	
}















