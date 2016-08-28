package com.xysoft.util;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
/**
 * 图片处理工具类
 * 功能：缩放图像，按比例缩放，按宽高缩放
 * @author 谢刚强
 */
public class ImageUtil {
	
		//WebContent根目录地址.
		private static final String webContent = FileUtil.getWebContent();

		/**
		 * 缩放图像（按比例缩放）
		 * @param srcImageFile 源图像文件地址
		 * @param scale 缩放比例
		 * @param flag 缩放选择:true 放大; false 缩小;
		 */
		public static String scale(String srcImageFile, int scale, boolean flag) {
			//网络路径
			if(srcImageFile.substring(0, 4).equals("http")){
				return srcImageFile;
			}
			String imgUrl = srcImageFile.replaceAll("/", "\\\\");
			String imgName = "scale_" + imgUrl.substring(imgUrl.lastIndexOf("\\")+1, imgUrl.length());
			String savePath = webContent + imgUrl.substring(0, imgUrl.lastIndexOf("\\")) + "\\" +  imgName;
			String srcImage = srcImageFile.substring(0, srcImageFile.lastIndexOf("/")) + "/" + imgName;
			File file = new File(savePath);
			if(file.exists()){
				return srcImage;
			}
			try {
				BufferedImage src = ImageIO.read(new File(webContent + srcImageFile)); // 读入文件
				int width = src.getWidth(); // 得到源图宽
				int height = src.getHeight(); // 得到源图长
				if (flag) {// 放大
					width = width * scale;
					height = height * scale;
				} else {// 缩小
					width = width / scale;
					height = height / scale;
				}
				Image image = src.getScaledInstance(width, height,
						Image.SCALE_DEFAULT);
				BufferedImage tag = new BufferedImage(width, height,
						BufferedImage.TYPE_INT_RGB);
				Graphics g = tag.getGraphics();
				g.drawImage(image, 0, 0, null); // 绘制缩小后的图
				g.dispose();
				ImageIO.write(tag, "JPEG", file);// 输出到文件流
			} catch (IOException e) {
				e.printStackTrace();
			}
			return srcImage;
		}

		/**
		     * 缩放图像（按高度和宽度缩放）
		     * @param srcImageFile 源图像文件地址
		     * @param height 缩放后的高度
		     * @param width 缩放后的宽度
		     * @param bb 比例不对时是否需要补白：true为补白; false为不补白;
		     */
		@SuppressWarnings("static-access")
		public final static String scaleByHW(String srcImageFile, int height, int width, boolean bb) {
			//网络路径
			if(srcImageFile.substring(0, 4).equals("http")){
				return srcImageFile;
			}
			String imgUrl = srcImageFile.replaceAll("/", "\\\\");
			String imgName = "scale_" + imgUrl.substring(imgUrl.lastIndexOf("\\")+1, imgUrl.length());
			String savePath = webContent + imgUrl.substring(0, imgUrl.lastIndexOf("\\")) + "\\" +  imgName;
			String srcImage = srcImageFile.substring(0, srcImageFile.lastIndexOf("/")) + "/" + imgName;
			File file = new File(savePath);
			if(file.exists()){
				return srcImage;
			}
			try {
				double ratio = 0.0; // 缩放比例
				File f = new File(webContent + srcImageFile);
				BufferedImage bi = ImageIO.read(f);
				Image itemp = bi.getScaledInstance(width, height, bi.SCALE_SMOOTH);
				// 计算比例
				if ((bi.getHeight() > height) || (bi.getWidth() > width)) {
					if (bi.getHeight() > bi.getWidth()) {
						ratio = (new Integer(height)).doubleValue() / bi.getHeight();
					} else {
						ratio = (new Integer(width)).doubleValue() / bi.getWidth();
					}
					AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(ratio, ratio), null);
					itemp = op.filter(bi, null);
				}
				if (bb) {//补白
					BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
					Graphics2D g = image.createGraphics();
					g.setColor(Color.white);
					g.fillRect(0, 0, width, height);
					if (width == itemp.getWidth(null))
						g.drawImage(itemp, 0, (height - itemp.getHeight(null)) / 2,
								itemp.getWidth(null), itemp.getHeight(null),
								Color.white, null);
					else
						g.drawImage(itemp, (width - itemp.getWidth(null)) / 2, 0,
								itemp.getWidth(null), itemp.getHeight(null),
								Color.white, null);
					g.dispose();
					itemp = image;
				}
				ImageIO.write((BufferedImage) itemp, "JPEG", file);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return srcImage;
		}
		
		 /**
	     * <b>function:</b> 可以设置图片缩放质量，并且可以根据指定的宽高缩放图片
	     * @author xgq
	     * @createDate 2015-9-10 上午11:01:27
	     * @param width 缩放的宽度
	     * @param height 缩放的高度
	     * @param quality 图片压缩质量，最大值是1； 使用枚举值：{@link ImageQuality}
	     *             Some guidelines: 0.75 high quality、0.5  medium quality、0.25 low quality
	     * @param savePath 保存目录
	     * @param targetImage 即将缩放的目标图片
	     * @return 图片保存路径、名称
	     * @throws IOException
	     */
	    public static String resize(String srcImageFile, int width, int height, Float quality) {
			//网络路径
			if(srcImageFile.substring(0, 4).equals("http")){
				return srcImageFile;
			}
			String imgUrl = srcImageFile.replaceAll("/", "\\\\");
			String imgName = "scale_" + imgUrl.substring(imgUrl.lastIndexOf("\\")+1, imgUrl.length());
			String savePath = webContent + imgUrl.substring(0, imgUrl.lastIndexOf("\\")) + "\\" +  imgName;
			String srcImage = srcImageFile.substring(0, srcImageFile.lastIndexOf("/")) + "/" + imgName;
			File file = new File(webContent + imgUrl);//源文件
			if(!file.exists()){
				return srcImage;
			}
			try {
				Image targetImage = ImageIO.read(file);
				width = Math.max(width, 1);
				height = Math.max(height, 1);
				BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
				image.getGraphics().drawImage(targetImage, 0, 0, width, height, null);
				
				FileOutputStream fos = new FileOutputStream(new File(savePath));
				JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(fos);
				
				JPEGEncodeParam encodeParam = JPEGCodec.getDefaultJPEGEncodeParam(image); 
				if (quality == null || quality <= 0) {
					quality = 1f;
				}
				/** 设置图片压缩质量 */  
				encodeParam.setQuality(quality, true);  
				encoder.encode(image, encodeParam);  
				
				image.flush();
				fos.flush();
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
	        return srcImage;
	    }
		
	
}