package kr.dja.javaFinalExam.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import kr.dja.javaFinalExam.EX_Consumer;

class ConsumerGroup
{
	public final int freqAvg;
	public final int priceAvg;
	public final List<EX_Consumer> list;
	
	public ConsumerGroup(int freqAvg, int priceAvg, ArrayList<EX_Consumer> list)
	{
		this.freqAvg = freqAvg;
		this.priceAvg = priceAvg;
		this.list = Collections.unmodifiableList(list);
	}
}