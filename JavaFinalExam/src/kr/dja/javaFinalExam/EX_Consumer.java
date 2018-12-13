package kr.dja.javaFinalExam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import kr.dja.javaMidtermExam.Consumer;

public class EX_Consumer
{
	public final int id;
	public final String gender;
	public final int age;
	public final int occupation;
	public final String cityCat;
	public final int currentCityYears;
	public final boolean isMarital;
	
	private ArrayList<Product> _products;
	public List<Product> products;
	
	public EX_Consumer(int id, String gender, int age, int occupation, String cityCat, int currentCityYears, boolean isMarital)
	{
		this.id = id;
		this.gender = gender;
		this.age = age;
		this.occupation = occupation;
		this.cityCat = cityCat;
		this.currentCityYears = currentCityYears;
		this.isMarital = isMarital;
		
		this._products = new ArrayList<Product>();
		this.products = Collections.unmodifiableList(this._products);
	}
	
	public synchronized void addProduct(String id, int price)
	{
		this._products.add(new Product(id, price));
	}
	
	public int getAvgPrice()
	{
		int sum = 0;
		for(int i = 0; i < this._products.size(); ++i)
		{
			sum += this._products.get(i).price;
		}
		return sum / this._products.size();
	}
	
	@Override
	public String toString()
	{
		StringBuffer buf = new StringBuffer();
		buf.append("id:");
		buf.append(this.id);
		buf.append(" gender:");
		buf.append(this.gender);
		buf.append(" age:");
		buf.append(this.age);
		buf.append(" occupation: ");
		buf.append(this.occupation);
		buf.append(" cityCat: ");
		buf.append(this.cityCat);
		buf.append(" currentCityYears: ");
		buf.append(this.currentCityYears);
		buf.append(" isMarital: ");
		buf.append(this.isMarital);
		
		return buf.toString();
	}
}

class Product
{
	public final String id;
	public final int price;
	
	public Product(String id, int price)
	{
		this.id = id;
		this.price = price;
	}
}