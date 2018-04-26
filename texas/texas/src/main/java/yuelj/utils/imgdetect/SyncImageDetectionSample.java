package yuelj.utils.imgdetect;

import java.util.Arrays;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.green.model.v20161216.ImageDetectionRequest;
import com.aliyuncs.green.model.v20161216.ImageDetectionResponse;
import com.aliyuncs.green.model.v20161216.ImageDetectionResponse.ImageResult.IllegalResult;
import com.aliyuncs.green.model.v20161216.ImageDetectionResponse.ImageResult.OcrResult;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

import yuelj.constants.ParamsAndURL;

/**
 * 同步图片检测结果调用示例，调用会实时返回检测结果
 */
public class SyncImageDetectionSample {
	public static String imgUrl = "https://fdjimg.oss-cn-hangzhou.aliyuncs.com/175";

	public static void main(String[] args) {
		// 请替换成你自己的accessKeyId、accessKeySecret
		IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou",
				(String) ParamsAndURL.getParam("Green_accessKeyId"),
				(String) ParamsAndURL.getParam("Green_accessKeySecret"));
		IAcsClient client = new DefaultAcsClient(profile);
		ImageDetectionRequest imageDetectionRequest = new ImageDetectionRequest();
		/**
		 * 是否同步调用 false: 同步
		 */
		imageDetectionRequest.setAsync(false);
		/**
		 * 同步图片检测支持多个场景, 但不建议一次设置太多场景: porn: 黄图检测 ocr: ocr文字识别 illegal: 暴恐敏感检测
		 * ad: 图片广告 sensitiveFace: 指定人脸 qrcode: 二维码
		 */
		// imageDetectionRequest.setScenes(Arrays.asList("porn", "ocr",
		// "illegal"));
		imageDetectionRequest.setScenes(Arrays.asList("ocr"));
		imageDetectionRequest.setConnectTimeout(4000);
		imageDetectionRequest.setReadTimeout(4000);
		/**
		 * 同步图片检测一次只支持单张图片进行检测
		 */
		imageDetectionRequest.setImageUrls(Arrays.asList(imgUrl));
		try {
			ImageDetectionResponse imageDetectionResponse = client.getAcsResponse(imageDetectionRequest);
			System.out.println(JSON.toJSONString(imageDetectionResponse));
			if ("Success".equals(imageDetectionResponse.getCode())) {
				List<ImageDetectionResponse.ImageResult> imageResults = imageDetectionResponse.getImageResults();
				if (imageResults != null && imageResults.size() > 0) {
					// 同步图片检测只有一个返回的ImageResult
					ImageDetectionResponse.ImageResult imageResult = imageResults.get(0);
					// porn场景对应的检测结果放在pornResult字段中
					// ocr场景对应的检测结果放在ocrResult字段中
					// illegal场景对应的检测结果放在illegalResult字段中
					// ad场景对应的检测结果放在adResult字段中
					// sensitiveFace场景对应的检测结果放在sensitiveFaceResult字段中
					// qrcode场景对应的检测结果放在qrcodeResult字段中
					// 请按需获取
					/**
					 * 黄图检测结果
					 */
					ImageDetectionResponse.ImageResult.PornResult pornResult = imageResult.getPornResult();
					if (pornResult != null) {
						/**
						 * 绿网给出的建议值, 0表示正常，1表示色情，2表示需要review
						 */
						Integer label = pornResult.getLabel();
						/**
						 * 黄图分值, 0-100
						 */
						Float rate = pornResult.getRate();
						// 根据业务情况来做处理
					}
					// 其他的场景的类似向黄图检测结果一样去get结果，进行处理
					/**
					 * ocr识别结果
					 */
					OcrResult ocrResult = imageResult.getOcrResult();
					if (ocrResult != null) {
						List<String> texts = ocrResult.getText();
						if (texts != null && texts.size() > 0) {
							for (String text : texts) {
								System.out.println(text);
							}
						}
					}
					IllegalResult illegalResult = imageResult.getIllegalResult();
					if (illegalResult != null) {
						/**
						 * 绿网给出的建议值, 0表示正常，1表示暴恐敏感，2表示需要review
						 */
						illegalResult.getLabel();
						/**
						 * 分值, 0-100
						 */
						illegalResult.getRate();
					}
				}
			} else {
				/**
				 * 检测失败
				 */
			}
		} catch (ServerException e) {
			e.printStackTrace();
		} catch (ClientException e) {
			e.printStackTrace();
		}
	}
}
