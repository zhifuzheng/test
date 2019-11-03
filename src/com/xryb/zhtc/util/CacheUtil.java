package com.xryb.zhtc.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import dbengine.dao.EntityDao;
import dbengine.dao.SqlDao;
import dbengine.util.DESUtil;

import javolution.util.FastMap;

/**
 * 缂撳瓨宸ュ叿绫伙紝灏嗕竴浜涚粡甯歌皟鐢ㄧ殑鏁版嵁鏀惧叆缂撳瓨涓�
 * @author hshzh
 * 
 */
public class CacheUtil {
	/**
	 * 鍩虹缂撳瓨锛屽瓨澶ч儴浠介�氱敤缂撳瓨瀵硅薄锛堝瀛楀吀琛紝甯哥敤鍙傛暟绛夛級
	 */
	private final static FastMap<String, Object> baseCache = new FastMap<String, Object>().shared();	
	/**
	 * 浠ょ墝瀵硅薄鏁版嵁缂撳瓨(鐧婚檰鍚庣殑淇℃伅锛宔cinfo,userinfo,roleinfo,menuinfo)
	 */
	private final static FastMap<String, Object> tokenCache = new FastMap<String, Object>().shared();	
	/**
	 * 浠ょ墝鏃堕棿缂撳瓨(璁剧疆瀛樺湪浠ょ墝鏃剁殑褰撳墠璇锋眰鏃堕棿锛屼负鍚庣画璁剧疆鎿嶄綔杩囨湡鍋氬噯澶�,token涓虹櫥闄嗗悕,value涓虹櫥闄嗘椂鐨勬椂闂村璞�)
	 */
	private final static FastMap<String, Object> tokenTime = new FastMap<String, Object>().shared();
	/**
	 * 瀛樻斁鐭俊楠岃瘉鐮佷俊鎭�(key鎵嬫満鍙�,value涓洪獙璇佺爜瀛楃涓�)
	 */
	private final static FastMap<String,String> smsPassCodeCache = new FastMap<String,String>().shared();
	/**
	 * 瀛樻斁鐐规枃绔犺禐璇勮鏁扮殑淇℃伅(numberComments璇勮鏁�,pointPraise鍒嗕韩鏁�,numberComments鐐硅禐鏁�,circleUUID鍦堝瓙琛╱uid)
	 */
	private final static FastMap<String,Object> circlePassCodeCache = new FastMap<String,Object>().shared();
	/**
	 * 瀛樻斁鐐圭粰浜鸿禐璇勮鏁扮殑淇℃伅(numberComments璇勮鏁�,pointPraise鍒嗕韩鏁�,numberComments鐐硅禐鏁�,circleUUID鍦堝瓙琛╱uid)
	 */
	private final static FastMap<String,Object>	reviewPassCodeCache = new FastMap<String,Object>().shared();
	/**
	 * 瀛樻斁娑堟伅鏁版嵁搴撴暟鎹簱鐨勭偣璧�
	 */
	private final static FastMap<String,Object>	PariseScreService = new FastMap<String,Object>().shared();
	/**
	 * 新闻动态的缓存
	 */
	private final static FastMap<String,Object> videoCache = new FastMap<String,Object>().shared();
	/**
	 * 视频教程缓存
	 */
	private final static FastMap<String,Object> videolookCache = new FastMap<String,Object>().shared();
	/**
	 * 跑团打卡的缓存
	 * */
	private final static FastMap<String, Object> bigshot = new FastMap<String,Object>().shared();
	/**
	 * 
	 * 获取大咖评论表的内容
	 * 
	 * */
	private final static FastMap<String, Object> runningbigplb = new FastMap<String,Object>().shared();
	/**
	 * 鏀剧煭淇￠獙璇佺爜淇℃伅鏃堕棿缂撳瓨(璁剧疆瀛樺湪鏀剧煭淇￠獙璇佺爜淇℃伅鏃剁殑褰撳墠璇锋眰鏃堕棿锛屼负鍚庣画璁剧疆鎿嶄綔杩囨湡鍋氬噯澶�,mobile涓虹櫥闄嗘墜鏈哄彿,value涓虹敓鎴愰獙璇佺爜鏃剁殑鏃堕棿瀵硅薄)
	 */
	private final static FastMap<String, Object> smsPassCodeTime = new FastMap<String, Object>().shared();
	/**
	 * 存放敏感字的缓存(SENSITIVETYPE分布类型,SENSITIVETOPIC针对对象,SENSITIVEWORDS关键词汇,id敏感字id)
	 */
	private final static FastMap<String,Object>	GlossaryifList = new FastMap<String,Object>().shared();
	/**
	 * 杞挱鍥剧墖鐗堟湰鍙风紦瀛�
	 */
	private final static FastMap<String, Object> imgEdition = new FastMap<String,Object>().shared();
	/**
	 * 
	 * 获取跑团表的缓存
	 * 
	 * */
	private final static FastMap<String, Object> runningbain = new FastMap<String,Object>().shared();
	/**
	 * 竞赛模块中地址省市县三级数据缓存
	 */
	private final static FastMap<String, Object> jsAddressCache = new FastMap<String,Object>().shared();
	
	/**
	 * 鍒ゆ柇鏁版嵁搴撴ā寮忔槸鍚﹁閿佸畾鏄閿佸畾锛岄粯璁ゆ病鏈夐攣瀹�
	 */
	public boolean MyCirRivIsLock = false;
	/**
	 * 鍒ゆ柇鏁版嵁搴撴ā寮忔槸鍚﹁閿佸畾鏄閿佸畾锛岄粯璁ゆ病鏈夐攣瀹�
	 */
	public boolean MyCirRivIsLockDz = false;
	
	/**
	 * 
	 * 判断是否有人在浏览进行上锁
	 * 
	 * */
	public boolean browseIs = false;
	/**
	 * 
	 * 判断是否有人在视频教程浏览进行上锁
	 * 
	 * */
	public boolean videolook = false;
	/**
	 * 
	 * 判断是否有人进行阅读时候进行上锁
	 * 
	 * */
	public boolean lookbrowse = false;
	/**
	 * 
	 * 判断是否有人进行阅读时候进行上锁
	 * 
	 * */
	public boolean bfvideo = false;
	/**
	 * 判断大咖热度是否上说
	 * 
	 * */
	public boolean bigshots = false;
	/**
	 * 点赞数量是否
	 * 
	 * */
	public boolean runningbigpls = false;
	/**
	 * 
	 * 对大咖表的点赞
	 * 
	 * */
	public boolean runningbigshotnumber = false;
	/**
	 * 点赞数量是否
	 * 
	 * */
	public boolean runningbigplsno = false;
	/**
	 * 
	 * 对大咖表的点赞
	 * 
	 * */
	public boolean runningbigshotnumberno = false;
	
	private final static CacheUtil instance = new CacheUtil();
	
	
	private CacheUtil() {
		
	}
	
	public static CacheUtil getInstance() {
		return instance;
	}
	
	/**
	 * 鑾峰彇閫氱敤缂撳瓨瀵硅薄瀵硅薄
	 * 
	 * @return
	 */
	public FastMap<String, Object> getBaseCache() {
		return baseCache;
	}
    /**
     * 鑾峰彇浠ょ墝瀵硅薄鏁版嵁缂撳瓨瀵硅薄
     * @return
     */
	public FastMap<String, Object> getTokenCache() {
		return tokenCache;
	}
	
    /**
     * 鑾峰彇浠ょ墝鏃堕棿缂撳瓨瀵硅薄
     * @return
     */
	public FastMap<String, Object> getTokenTime() {
		return tokenTime;
	}
	/**
	 * 璁剧疆浠ょ墝鐢熸垚鏃堕棿
	 * @param loginName
	 * @param date
	 * @return
	 */
	public boolean setTokenTime(String token,Long date ){
		if(token==null || "".equals(token) || date==null || "".equals(date)){
			return false;
		}
		tokenTime.put(token,date);
		return true;
	}
	/**
	 * 璁剧疆鐧婚檰璐﹀彿浠ょ墝map瀵硅薄
	 * @param token 缂撳瓨Map涓殑key
	 * @param map 瀛樻斁鐧婚檰鍚庣殑鐩稿叧淇℃伅(鏉冮檺瀛楃涓层�佸叕鍙告垨鍗曚綅瀵硅薄銆佺敤鎴峰璞★紙鍙负绌猴級銆佷細鍛樺璞★紙鍙负绌猴級)
	 * @return
	 */
	public boolean setTokenObject(String token,Map<String,Object> map){
		System.out.println("璁剧疆浠ょ墝瀵硅薄token涓�:"+token);
		System.out.println("瀵硅薄涓猴細"+map);
		if(token!=null && !"".equals(token) && map!=null){
			tokenCache.put(token, map);
			return true;
		}
		return false;
	}
	/**
	 * 绉婚櫎鐧婚檰淇℃伅缂撳瓨瀵硅薄锛屽寘鎷护鐗岀敓鎴愭椂闂�
	 * @param token
	 * @return
	 */
	public boolean deleteTokenObject(String token){
		if(token == null || "".equals(token) || "null".equals(token)){
			return false;
		}
		//绉婚櫎鐧婚檰瀵硅薄鍏�
		tokenCache.remove(token);
		//绉婚櫎鐧婚檰鏃堕棿
		tokenTime.remove(token);
		return true;
	}
	/**
	 *鑾峰彇浠ょ墝瀵硅薄
	 * @param token
	 * @return
	 */
	public Map<String,Object> getTokenObject(String token){
		if(token==null || "".equals(token)){
			return null;
		}
		return (Map<String,Object>)tokenCache.get(token);
	}
	/**
	 * 璁剧疆鐭俊楠岃瘉鐮佺紦瀛�
	 * @param mobile
	 * @param passCode
	 * @return
	 */
	public boolean setSmsPassCode(String mobile,String passCode){
		smsPassCodeCache.put(mobile, passCode);
		return true;
	}
	
	/**
	 * 鑾峰彇鐭俊楠岃瘉鐮佺紦瀛�
	 * @param mobile
	 * @return
	 */
	public String getSmsPassCode(String mobile){
		return smsPassCodeCache.get(mobile);
	}
	
	/**
	 * 绉婚櫎楠岃瘉鐮�
	 * @param token
	 * @return
	 */
	public boolean deleteSmsPassCode(String token){
		smsPassCodeCache.remove(token);
		return true;
	}
	
	/**
	 * 璁剧疆鐭俊楠岃瘉鐮佹椂闂�
	 * @param mobile
	 * @param date
	 * @return
	 */
	public boolean setSmsPassCodeTime(String mobile,Date date){
		smsPassCodeTime.put(mobile, date);
		return true;
	}
	/**
	 * 閫氳繃鏂规硶鍚嶅拰绫诲瀷璁块棶杩滅▼缂撳瓨鏁版嵁
	 * @param monthStr
	 * @param returnType 1Map锛�2String
	 * @return
	 */
	public String getHttpCache(String monthStr,String token){
		if(monthStr==null||token==null || "".equals(monthStr) || "".equals(token)){
			return null;
		}
		HttpConnectionUtil hcu = new HttpConnectionUtil();
		
		String url = ReadProperties.getValue("cacheServiceAddr")+"/admin/cache/"+monthStr;
		Map parameterMap = new HashMap();
		parameterMap.put("token", token);
		String returnStr = "";
		try {
			return hcu.doPost(url, parameterMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	//淇变箰閮ㄧ儹搴︾紦瀛�
	List<String> clubList = new ArrayList<>();
	public boolean clubReDu(String clubUUID) {
		clubList.add(clubUUID);
		return true;
	}
	public List<String> getClubReDu() {
		return clubList;
	}
	//娓呴櫎list
	public void claer() {
		clubList.clear();
	}
	
	//俱乐部详情新闻动态浏览量缓存
	List<String> newsBrowseList = new ArrayList<String>();
	public boolean setnewsBrowse(String newsUUID) {
		newsBrowseList.add(newsUUID);
		return true;
	}
	public List<String> getnewsBrowse(){
		return newsBrowseList;
	}
	//清除list
	public void newsBrowseListClaer() {
		newsBrowseList.clear();
	}
	
	//俱乐部详情新闻动态阅读缓存
	List<String> newsReadingList = new ArrayList<String>();
	public boolean setnewsReading(String newsUUID) {
		newsReadingList.add(newsUUID);
		return true;
	}
	public List<String> getnewsReading(){
		return newsReadingList;
	}
	//清除list
	public void newsnewsReadingClaer() {
		newsReadingList.clear();
	}
	
	//俱乐部详情党建活动浏览量缓存
	List<String> djBrowseList = new ArrayList<String>();
	public boolean setdjBrowse(String djUUID) {
		djBrowseList.add(djUUID);
		return true;
	}
	public List<String> getdjBrowse(){
		return djBrowseList;
	}
	//清除list
	public void djBrowseClaer() {
		djBrowseList.clear();
	}
	
	//俱乐部详情党建活动阅读量缓存
	List<String> djReadingList = new ArrayList<String>();
	public boolean setdjReading(String djUUID) {
		djReadingList.add(djUUID);
		return true;
	}
	public List<String> getdjReading(){
		return djReadingList;
	}
	//清除list
	public void djReadingClaer() {
		djReadingList.clear();
	}
	
	//俱乐部详情视频教程浏览量缓存
	List<Map<String, Object>> videoList = new ArrayList<Map<String, Object>>();
	public boolean setVideo(Map<String, Object> map) {
		videoList.add(map);
		//System.out.println("sssssss:"+videoList);
		return true;
	}
	public List<Map<String, Object>> getVideo(){
		return videoList;
	}
	//清除list
	public void videoListClaer() {
		videoList.clear();
	}
	
	//供需点赞缓存
	List<Map<String, Object>> giveList = new ArrayList<Map<String, Object>>();
	public boolean setGive(Map<String, Object> map) {
		giveList.add(map);
		return true;
	}
	public List<Map<String, Object>> getGive(){
		return giveList;
	}
	//根据key删除
	public void delKey(Object key) {
		//System.out.println("没清除之前："+LoveList);
		for(Map<String, Object> map : giveList) {
			for(String keys : map.keySet()) {
				if(keys.equals(key)) {
					map.remove(key);
				}
			}
		}
		//System.out.println("清除之后:"+LoveList);
	}
	//清除list
	public void giveListClaer() {
		giveList.clear();
	}
	
	/**
     * 获取省市县数据缓存对象
     * @return
     */
	public FastMap<String, Object> getJsAddressCache() {
		return jsAddressCache;
	}
	//设置竞赛模块省市县三级数据对象
	public boolean setJsAddrData(String level,Object addrObj) {
		if(!"".equals(level)&&level!=null&&addrObj!=null) {
			jsAddressCache.put(level, addrObj);
			return true;
		}
		return false;
	}
	//获取竞赛模块省市县三级数据对象
	public Object getAddrData(String level) {
		if(!"".equals(level)&&level!=null) {
			return jsAddressCache.get(level);
		}
		return null;
	}
	
	//智慧旅游浏览量缓存
	List<Object> lyBrowseList = new ArrayList<Object>();
	public void setLyBrowse(String uuid) {
		lyBrowseList.add(uuid);
	}
	public List<Object> getLyBrowse(){
		return lyBrowseList;
	}
	//清除lyBrowseList
	public void lyBrowseListClaer() {
		lyBrowseList.clear();
	}
	
	//智慧旅游评论数量缓存
	List<Object> lyCommentList = new ArrayList<Object>();
	public void setLyComment(String uuid) {
		lyCommentList.add(uuid);
	}
	public List<Object> getLyComment(){
		return lyCommentList;
	}
	//清除lyCommentList
	public void lyCommentListClaer() {
		lyCommentList.clear();
	}
	
	//供需收藏缓存
	List<String> collectionList = new ArrayList<>();
	public void setCollection(String uuid) {
		collectionList.add(uuid);
	}
	
	public List<String> getCollection() {
		return collectionList;
	}
	
	//清除list
	public void collectionListClaer() {
		collectionList.clear();
	}
}
