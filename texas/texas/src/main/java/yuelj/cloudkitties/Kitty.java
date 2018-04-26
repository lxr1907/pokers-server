package yuelj.cloudkitties;

import java.math.BigDecimal;

public class Kitty {
	private String name;
	private int id;
	private BigDecimal price;
	private int coldown;
	private int sex;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public int getColdown() {
		return coldown;
	}

	public void setColdown(int coldown) {
		this.coldown = coldown;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}
}
