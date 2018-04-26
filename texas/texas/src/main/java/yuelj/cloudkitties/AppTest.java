package yuelj.cloudkitties;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.math.RandomUtils;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {

	static String urlBase = "http://cloudkitties.co/";
	static String urlLogin = urlBase + "member/index_do.php";
	static String urlMyKitties = urlBase + "plus/mykitties.php";

	// static int minPrice = 30;
	// static String email = "450002197@qq.com";
	// static String password = "Sunlin19971017";

	static int minPrice = 350;
	static int minMuPrice =350;
	static String email = "418982099@qq.com";
	static String password = "@Qq1907312";

	// static int minPrice = 5;
	// static String email = "1119495352@qq.com";
	// static String password = "mzl110";

	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public AppTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(AppTest.class);
	}

	/**
	 * Rigourous Test :-)
	 */
	public void testApp() {
		assertTrue(true);
	}

	public static void main(String[] args) {
		for (int i = 0; i < 999999999; i++) {
			email = "418982099@qq.com";
			password = "@Qq1907312";
			try {
				haha();
				Thread.sleep((RandomUtils.nextInt(2000) + 25000) * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void haha() {

		List<Kitty> ks = getKitties();
		// 母猫(可生育、估值在不低于minPrice)蓝色
		List<Kitty> ks0 = ks.stream().filter(k -> k.getSex() == 0).filter(k -> k.getColdown() == 0)
				.filter(k -> k.getPrice().compareTo(new BigDecimal(minPrice)) >= 0).collect(Collectors.toList());
		// 公猫(可生育)绿色
		List<Kitty> ks1 = ks.stream().filter(k -> k.getSex() == 1).filter(k -> k.getColdown() == 0)
				.filter(k -> k.getPrice().compareTo(new BigDecimal(minMuPrice)) >= 0).collect(Collectors.toList());

		// System.out.println(JSON.toJSONString(ks0));
		// System.out.println("---------------------");
		// System.out.println(JSON.toJSONString(ks1));
		// System.out.println("---------------------");

		int length = ks0.size() > ks1.size() ? ks1.size() : ks0.size();
		// length=1;
		int j = 0;
		int successTimes = 0;
		for (int i = 0; i < length; i++) {
			try {
				Thread.sleep((RandomUtils.nextInt(30) + 10) * 1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int a = birth(ks0.get(i).getId(), ks1.get(j).getId());
			j++;
			if (a > 0) {
				if (a == ks0.get(i).getId()) {
					// 母猫生育冷却中，公猫不变
					j--;
				} else {
					// 公猫生育冷却中，母猫不变
					i--;
				}
				System.out.println("生育冷却中：" + a);
			} else {
				successTimes++;
			}

		}

		System.out.println("生育成功次数：" + successTimes);
		System.out.println(new Date());
	}

	public static int birth(int id0, int id1) {
		String birthUrl = "http://cloudkitties.co/plus/breed.php?father=" + id1 + "&mother=" + id0;

		Map<String, String> cookies = getCookies();
		try {
			Document doc = Jsoup.connect(birthUrl).timeout(50000).cookies(cookies).get();
			int a = doc.toString().indexOf("生育冷却中");
			if (a > 0) {
				String b = doc.toString().substring(a - 10, a).split("ID")[1];
				// System.out.println("生育冷却中:"+b);
				return Integer.parseInt(b);
			}
			// System.out.println(doc.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static List<Kitty> getKitties() {

		List<Kitty> ks = new ArrayList<>();
		BigDecimal totalAmount = BigDecimal.ZERO;
		try {
			Map<String, String> cookies = getCookies();
			Document doc = Jsoup.connect(urlMyKitties).timeout(50000).cookies(cookies).get();

			Elements Kitties = doc.getElementsByClass("KittiesGrid-item");

			for (Element kitty : Kitties) {
				Elements idname = kitty.getElementsByAttributeValue("ondblclick", "ShowElement(this)");
				if (idname == null || idname.size() == 0) {
					continue;
				}
				int id = Integer.parseInt(idname.attr("id"));
				String name = kitty.getElementsByAttributeValue("ondblclick", "ShowElement(this)").text();
				BigDecimal price = new BigDecimal(kitty.getElementsByClass("KittyStatus-note").text().replace("Ξ", ""));
				String coldownText = kitty.getElementsByClass("KittyCard-coldown").text();
				// 生育冷却时间（秒），0表示可生育
				int coldown = "可生育".equals(coldownText) ? 0 : Integer.parseInt(coldownText.replaceAll("\\D", ""));
				int sex = kitty.getElementsByClass("KittyCard u-bg-alt-mintgreen KittyCard--responsive").size() == 0 ? 0
						: 1;

				totalAmount = totalAmount.add(price);
				Kitty k = new Kitty();
				k.setId(id);
				k.setName(name);
				k.setPrice(price);
				k.setColdown(coldown);
				k.setSex(sex);
				ks.add(k);
			}

			// 按价格降序排列
			ks.sort((Kitty h1, Kitty h2) -> h2.getPrice().compareTo(h1.getPrice()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("总估值：" + totalAmount);
		System.out.println("猫总数：" + ks.size());
		return ks;
	}

	public static Map<String, String> getCookies() {
		Map<String, String> cookies = null;
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("fmdo", "login");
		paramMap.put("dopost", "login");
		paramMap.put("gourl", urlBase);
		paramMap.put("email", email);
		paramMap.put("password", password);
		try {
			Connection.Response res = Jsoup.connect(urlLogin).data(paramMap).timeout(50000).method(Method.POST)
					.execute();
			cookies = res.cookies();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return cookies;
	}
}
