package yuelj.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;

import yuelj.constants.ParamsAndURL;
import yuelj.utils.imgdetect.ImageHelper;
import yuelj.utils.logs.SystemLog;

public class OSSUpload {
	private static String accessKeyId = (String) ParamsAndURL.getParam("OSSUpload_accessKeyId");
	private static String accessKeySecret = (String) ParamsAndURL.getParam("OSSUpload_accessKeySecret");
	// 以杭州为例
	private static String endpoint = (String) ParamsAndURL.getParam("OSSUpload_endpoint");
	public static String IMG_BUCKET = (String) ParamsAndURL.getParam("OSSUpload_IMG_BUCKET");
	// public static String urlpre =
	// "http://fdjimg.oss-cn-hangzhou.aliyuncs.com/";
	//https://fdjimg.oss-cn-hangzhou.aliyuncs.com/175

	public OSSClient getOSSClient() {
		// 初始化一个OSSClient
		OSSClient client = new OSSClient(endpoint, accessKeyId, accessKeySecret);

		// 下面是一些调用代码...
		return client;

	}

	public void putObject(String bucketName, String key, File file) throws IOException {

		// 初始化OSSClient
		OSSClient client = getOSSClient();

		InputStream content = new FileInputStream(file);

		// 创建上传Object的Metadata
		ObjectMetadata meta = new ObjectMetadata();

		// 必须设置ContentLength
		meta.setContentLength(file.length());

		// 上传Object.
		PutObjectResult result = client.putObject(bucketName, key, content, meta);

		// 打印ETag
		SystemLog.printlog(result.getETag());
	}

	public InputStream getObject(String bucketName, String key) {

		// 初始化OSSClient
		OSSClient client = getOSSClient();

		// 下载Object.
		OSSObject result = client.getObject(bucketName, key);
		return result.getObjectContent();
	}

	public InputStream getObject(String key) {
		InputStream in = getObject(IMG_BUCKET, key);
		return in;
	}

	/**
	 * 上传图片
	 * 
	 * @param id
	 * @param targetFile
	 * @throws IOException
	 */
	private String uploadImgInput(InputStream in) throws IOException {
		NextIdDao nextId = (NextIdDao) SpringUtil.getBean("NextIdDao");
		String id = nextId.getNextId();
		File f = createFile("./images/", id);
		inputstreamToFile(in, f);
		this.putObject(OSSUpload.IMG_BUCKET, id, f);
		if (f.exists()) {
			f.delete();
		}
		return id;
	}

	public static File createFile(String path, String fileName) {
		// path表示你所创建文件的路径
		File f = new File(path);
		if (!f.exists()) {
			f.mkdirs();
		}
		File file = new File(f, fileName);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return file;
	}

	/**
	 * 将图片id列表中的图片从oss获取，拼接为糖葫芦，上传后返回id
	 * 
	 * @param upics
	 * @return 拼接后的图片id
	 */
	public String uploadMergeImg(List<String> upics) {
		List<InputStream> piclist = new ArrayList<>();
		for (String i : upics) {
			piclist.add(getObject(i));
		}
		InputStream newIn = ImageHelper.mergeImages(piclist, 1);
		String id = "";
		try {
			if (newIn != null) {
				id = uploadImgInput(newIn);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return id;
	}

	public void inputstreamToFile(InputStream is, File file) {
		FileOutputStream fos = null;
		try {
			// 打开一个已存在文件的输出流
			fos = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		// 将输入流is写入文件输出流fos中
		int ch = 0;
		try {
			while ((ch = is.read()) != -1) {
				fos.write(ch);
			}
			fos.close();
			is.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}
