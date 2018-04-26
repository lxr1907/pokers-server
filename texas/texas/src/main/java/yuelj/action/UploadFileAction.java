package yuelj.action;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import yuelj.utils.NextIdDao;
import yuelj.utils.OSSUpload;
import yuelj.utils.imgdetect.ImageHelper;
/**
 * 上传文件
 * @author lxr
 *
 */
@Controller
public class UploadFileAction extends BaseAction {
	private static final long serialVersionUID = -1896915260152387341L;
	private HttpServletRequest request;
	@Autowired
	private NextIdDao nextId;

	public void setServletRequest(HttpServletRequest req) {
		this.request = req;
	}

	private List<File> uploadify;// 这里的"fileName"一定要与表单中的文件域名相同
	private List<String> fileNameContentType;// 格式同上"fileName"+ContentType
	private List<String> fileNameFileName;// 格式同上"fileName"+FileName
	private String savePath;// 文件上传后保存的路径

	private File uploadSingle;// app上传

	public File getUploadSingle() {
		return uploadSingle;
	}

	public void setUploadSingle(File uploadSingle) {
		this.uploadSingle = uploadSingle;
	}

	public List<File> getUploadify() {
		return uploadify;
	}

	public void setUploadify(List<File> uploadify) {
		this.uploadify = uploadify;
	}

	public List<String> getFileNameContentType() {
		return fileNameContentType;
	}

	public void setFileNameContentType(List<String> fileNameContentType) {
		this.fileNameContentType = fileNameContentType;
	}

	public List<String> getFileNameFileName() {
		return fileNameFileName;
	}

	public void setFileNameFileName(List<String> fileNameFileName) {
		this.fileNameFileName = fileNameFileName;
	}

	@SuppressWarnings("deprecation")
	public String getSavePath() {
		return request.getRealPath(savePath);
	}

	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}

	public void uploadImg() throws Exception {
		File dir = new File(getSavePath());
		if (!dir.exists()) {
			dir.mkdirs();
		}
		List<File> files = getUploadify();
		if (files == null && uploadSingle != null) {
			files = new ArrayList<File>();
			files.add(uploadSingle);
		}
		if (files == null || files.size() == 0) {
			setResponseResult(0, "uploadSingle为空！");
			return;
		}
		if (files.size() == 1) {
			OSSUpload ossUp = new OSSUpload();
			String id = nextId.getNextId();
			// 上传缩略图
			ossUp.putObject(OSSUpload.IMG_BUCKET, id, files.get(0));
			File smallImg = ImageHelper.scaleImage(files.get(0), 0.6, "jpg");
			ossUp.putObject(OSSUpload.IMG_BUCKET, id + "_small", smallImg);
			setResponseResult(1, id, id);
		} else {
			setResponseResult(0, "参数为空！");
		}
	}
}
