package com.xryb.zhtc.util;

import java.lang.reflect.Field;

import spark.annotation.Auto;

/**
 * 解析auto注解，完成依赖注入
 * @author wf
 */
public class ParseAutoUtil {
	
	/**
	 * 解析注解Auto
	 */
	public static Object autowired(Class<?> cls) {
		Object obj = null;
		try {
			obj = cls.newInstance();
			Field[] fields = cls.getDeclaredFields();
			Auto auto = null;
			Object diObj = null;
			for (Field fid : fields) {
				auto = fid.getAnnotation(Auto.class);
				if (auto != null) {
					Class<?> diClass = auto.name();
					if (diClass == null)
						throw new Exception("It's must set DICalss!");
					if (check(diClass))
						diObj = autowired(diClass);
					else
						diObj = diClass.newInstance();
					fid.setAccessible(true);
					fid.set(obj, diObj);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

	/**
	 * 检查字段是否含有注解Auto
	 */
	public static boolean check(Class<?> cls) {
		Field[] fields = cls.getDeclaredFields();
		for (Field f : fields) {
			if (null != f.getAnnotation(Auto.class))
				return true;
		}
		return false;
	}

}
