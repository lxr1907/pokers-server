package yuelj.utils.alipay.config;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *版本：3.3
 *日期：2012-08-10
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
	
 *提示：如何获取安全校验码和合作身份者ID
 *1.用您的签约支付宝账号登录支付宝网站(www.alipay.com)
 *2.点击“商家服务”(https://b.alipay.com/order/myOrder.htm)
 *3.点击“查询合作者身份(PID)”、“查询安全校验码(Key)”

 *安全校验码查看时，输入支付密码后，页面呈灰色的现象，怎么办？
 *解决方法：
 *1、检查浏览器配置，不让浏览器做弹框屏蔽设置
 *2、更换浏览器或电脑，重新登录查询。
 */

public class AlipayConfig {
	
	//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	// 合作身份者ID，以2088开头由16位纯数字组成的字符串
	public static String partner = "2088021579511894";
	// 商户的私钥
	public static String private_key = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAK73gm1dM4aGy3JxxjAckxNeE97lHEh6aWjqtcxByU73TxBtfyUsemMT2NSPZ+z99nsv2GAR4Y56ej7yJ31haFjKSmimMWkDg2Es6xnwMucKCWEXovnv+BsaEnz+gAbCBeCY5vkaTPFia1QyJN08F1HjIi14FNFZ3gLtFzU2LF7ZAgMBAAECgYBrPaeCstKHDQ2b2uJ30LvQ7iPNxa39OpJrTRka1eO2c9zRG9wHg+iL27lWJ4OyfcLapGvCDpOqiaVt+Wx86/xREzmQmrDMa0+MrgHhtjp+hemlahfqYxMpISFdC5A0dQ0KjQSLCEcE9sfH08yDO/kRwZQMNrzpKGE/L6ARTpluUQJBAOgOsOp7m7hvHQUIwx3FJUa8XnRUKSZ5PDwF2KtmLTiZbBnQZvdU1QG5IZryQtkRZWjrmEi6NGJTRkTy3cxhLO0CQQDBBOQohx/1LTxqU4OUUmLYmtyXovzyOPACVAGnAdyC64E8X82zU0nz7a+3QijXTecoetOzJ5qOW6/zIDzVUmgdAkB12qA3NjfL34KZf+24PNvFpP1Dj0qm44OX6FA2HFpzGFgydAkXK9pSWbebfdY9b/xU4PIiXY8lMK3Lbpz+o4uxAkEArkFaMfR7t37VUjB/U8JlFqQIjqVIsu0QUSnCLCIUD+XJUNY1ALYqx5tHzaxe7rGYBQWOZelVMTw5RsdN5FB3SQJATB1acP9y7I1lo5pqoJ7TWJcHVapqOn2Ae3iRdEzThhhExxTawNNph+WIUJLnizgy3q13Mk+XaxOwaoz79iJ2nQ==";
	
	// 支付宝的公钥，无需修改该值
	public static String ali_public_key  = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

	//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
	

	// 调试用，创建TXT日志文件夹路径
	public static String log_path = "D:\\";

	// 字符编码格式 目前支持 gbk 或 utf-8
	public static String input_charset = "utf-8";
	
	// 签名方式 不需修改
	public static String sign_type = "RSA";

}
