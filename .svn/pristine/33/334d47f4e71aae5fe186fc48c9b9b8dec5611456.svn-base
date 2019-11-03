package com.xryb.zhtc.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.xryb.zhtc.util.AccountUtil;
import com.xryb.zhtc.util.DateTimeUtil;

public class JunitTest {
	
	@Test
	public void mapRemoveTest(){
		Map<String, String> map = new HashMap<>();
		map.put("key", "value");
		System.out.println(map.remove("key"));
	}
	
	@Test
	public void orderNoTest(){
		
		while(true){
			System.out.println(DateTimeUtil.formatDate(new Date(), "yyyyMMddHHmmssSSS")+createRandom());
		}
		
	}
	
	public static String createRandom() {
		StringBuffer str = new StringBuffer();
		for(int i = 0 ;i < 6 ;i++){
			str.append((int)(Math.random() * 9));
		}
		return str.toString();
	}
	
	@Test
	public void integerTest(){
		System.out.println("".equals(null));
		Double firstRatio = 0.01;
		Integer payment = new Integer(280);
		Integer result = (int) (payment*firstRatio);
		System.out.println(result);
	}
	
	@Test
	public void expireTest(){
		Date nowTime = new Date();
		Date expireTime = DateTimeUtil.parseDate("2019-06-30 17:03:03", "yyyy-MM-dd HH:mm:ss");
		System.out.println(nowTime.before(expireTime));
	}
	
	@Test
	public void accountTest() throws Exception{
		AccountUtil.newAccount("jdbcwrite", false, "4", "5d7defdbcfce4fb391d44bd5394b69c6", "优好采购", "18984530602", "5d7defdbcfce4fb391d44bd5394b69c6");
	}
	
	@Test
	public void listTest() throws Exception{
		List<String> list = new ArrayList<String>();
		list.add("1");
		list.add("2");
		list.add("3");
		list.add("4");
		list.add("5");
		list.add("6");
		for(String num : list){
			if(num.equals("2")){
				list.remove(num);
			}
		}
		for(String num : list){
			System.out.println(num);
		}
	}

}
