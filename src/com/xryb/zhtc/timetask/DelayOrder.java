package com.xryb.zhtc.timetask;

import java.util.Date;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import com.xryb.zhtc.util.DateTimeUtil;


/**
 * 订单定时任务
 * @author zzf
 */

public class DelayOrder implements Delayed{
	private String orderUUID;//订单UUID
	private long startTime;//开始时间:单位毫秒
	private long expire; //到期时间:单位毫秒
	private String orderInfo;//订单其他信息json方式保存
	private long dlayTime;//延迟时间:单位毫秒
	
	public DelayOrder() {
		
	}
	
	/**
	 * 构造方法
	 * @param orderUUID 订单UUID
	 * @param startTimeStr 开始时间字符串 格式为：yyyy-MM-dd HH:mm:ss
	 * @param secondsDelay 延迟时间 单位为秒
	 */
	public DelayOrder(String orderUUID, String startTimeStr, long secondsDelay) {
		super();
		this.orderUUID = orderUUID;
		this.startTime = DateTimeUtil.parseDate(startTimeStr, "yyyy-MM-dd HH:mm:ss").getTime();
		this.expire = startTime+(secondsDelay*1000);
		this.orderInfo = "";
	}
	
	
	
	/**
	 * 构造方法
	 * @param orderUUID 订单UUID
	 * @param startTimeStr 开始时间字符串 格式为：yyyy-MM-dd HH:mm:ss
	 * @param orderInfo 订单其他信息json方式保存
	 * @param secondsDelay 延迟时间 单位为秒
	 */
	public DelayOrder(String orderUUID, String startTimeStr,String orderInfo, long secondsDelay) {
		super();
		this.orderUUID = orderUUID;
		this.startTime = DateTimeUtil.parseDate(startTimeStr, "yyyy-MM-dd HH:mm:ss").getTime();
		this.expire = startTime+(secondsDelay*1000);
		this.orderInfo = orderInfo;
	}
	

	@Override
	public int compareTo(Delayed o) {
		return (int)(this.getDelay(TimeUnit.MILLISECONDS)-o.getDelay(TimeUnit.MILLISECONDS));
	}
	
	@Override
	public long getDelay(TimeUnit unit) {
		return unit.convert(this.expire-System.currentTimeMillis(), TimeUnit.MILLISECONDS);
	}

	public String getOrderUUID() {
		return orderUUID;
	}

	public void setOrderUUID(String orderUUID) {
		this.orderUUID = orderUUID;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getExpire() {
		return expire;
	}

	public void setExpire(long expire) {
		this.expire = expire;
	}

	public String getOrderInfo() {
		return orderInfo;
	}

	public void setOrderInfo(String orderInfo) {
		this.orderInfo = orderInfo;
	}

	public long getDlayTime() {
		return dlayTime;
	}

	public void setDlayTime(long dlayTime) {
		this.dlayTime = dlayTime;
	}
	

}
