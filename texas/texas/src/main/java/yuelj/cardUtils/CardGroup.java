package yuelj.cardUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class CardGroup {
	/**
	 * 获取0到52的list代表一副牌
	 * 
	 * @return
	 */
	public static ArrayList<Integer> getInitialCards() {
		ArrayList<Integer> cardsList = new ArrayList<Integer>();
		for (int i = 0; i < 52; i++) {
			cardsList.add(i);
		}
		return cardsList;
	}

	/**
	 * 获取随机卡组,范围0到52，0到3代表黑A红A梅A方A
	 * 
	 * @return
	 */
	public static ArrayList<Integer> getRandomCards() {
		ArrayList<Integer> src = getInitialCards();
		ArrayList<Integer> cardsList = new ArrayList<Integer>();
		while (src.size() > 0) {
			int size = src.size();
			Random r = new Random();
			int index = r.nextInt(size);

			cardsList.add(src.get(index));
			src.remove(index);
		}
		return cardsList;
	}

	public static void main(String[] args) {

		ArrayList<Integer> countList = new ArrayList<>();
		for (int i = 0; i < 52; i++) {
			countList.add(0);
		}
		for (int i = 0; i < 100000; i++) {
			ArrayList<Integer> cardsList = getRandomCards();
			List<Integer> p1cards=cardsList.subList(0, 3);
			cardsList.remove(0);
			cardsList.remove(0);
			cardsList.remove(0);
			List<Integer> pcards=cardsList.subList(0, 3);
			countList.set(pcards.get(0), countList.get(pcards.get(0)) + 1);
		}
		Arrays.asList(countList).forEach(times -> {
			System.out.println(times);
		});
	}
}
