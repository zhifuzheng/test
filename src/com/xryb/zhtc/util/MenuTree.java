package com.xryb.zhtc.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xryb.zhtc.entity.MenuInfo;



/**
 * 生成菜单树
 * @author hshzh
 *
 */
public class MenuTree {
	/**
	 * 创建菜单树(json格式)
	 * 
	 * @param OList
	 *            群组列表
	 * @param isCheckGroupids
	 *            已选择的节点
	 * @param showTopNode
	 *            是否显示顶级节点 true显示 false不显示
	 * @param topId
	 *            顶级节点 id
	 * @return
	 */
	public Object createTreeJson(List<MenuInfo> menuList,String[] isCheckGroupids, boolean showTopNode, String topCode) {
		if (menuList == null || menuList.size() < 1) {
			// 参数错误
			return null;
		}
		StringBuilder treeStr = new StringBuilder();
		try {
			String checkedGroupStr = "";
			if (!showTopNode) {
				// 不需要生成顶级节点,只需要生成下级节点
				List<TreeBean> treeList = new ArrayList<TreeBean>();
				getChildJson(menuList, isCheckGroupids, topCode, treeList);
				return treeList;
			} else {
				TreeBean treeBean = new TreeBean();
				for (MenuInfo menuinfo : menuList) {
					checkedGroupStr = "";
					if (topCode.equals(String.valueOf(menuinfo.getMenuCode()))) {
						treeBean.setId(menuinfo.getMenuCode()+ "");// id
						treeBean.setpId(menuinfo.getMenuParentCode());//pid
						treeBean.setChecked(false);// 是否选中
						treeBean.setText(menuinfo.getMenuName());// 名称
						treeBean.setIconCls("");// 图标
						//treeBean.setState("open");// 开启状态
						//treeBean.setState("closed");// 关闭状态
						if (isCheckGroupids != null	&& isCheckGroupids.length > 0) {
							for (String CheckStr : isCheckGroupids) {
								if (CheckStr.equals(String.valueOf(menuinfo.getMenuUUID()))) {
									treeBean.setChecked(true);
									break;
								}
							}
						}
						Map<String, String> attributes = new HashMap();
						attributes.put("menuId", menuinfo.getId() + "");// 菜单id
						attributes.put("menuName", menuinfo.getMenuName());// 菜单名称
						attributes.put("menuCode", menuinfo.getMenuCode());// 菜单代码
						attributes.put("menuHaveLowerNode",menuinfo.getMenuHaveLowerNode());// 是否有子节点
						attributes.put("menuParentCode",menuinfo.getMenuParentCode());// 父节点编号
						attributes.put("menuType", menuinfo.getMenuType());// 菜单类型id
						attributes.put("menuPermission",menuinfo.getMenuPermission());// 菜单权限标识(如"system.role"表示系统级角色权限，"system.user"用户权限)
						attributes.put("menuUrl", menuinfo.getMenuUrl()+"");// 菜单连接地址（无连接地址用#号表示）
						attributes.put("menuUUID",menuinfo.getMenuUUID());// uuid
						treeBean.setAttributes(attributes);
						// 生成顶级节点
						if ("1".equals(menuinfo.getMenuHaveLowerNode())) {
							// 有子节点
							treeBean.setState("closed");// 关闭状态
							List<TreeBean> treeList = new ArrayList<TreeBean>();
							treeBean.setChildren(treeList);
							getChildJson(menuList, isCheckGroupids, topCode,treeList);
						}
						return treeBean;
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return treeStr.toString();
	}

	/**
	 * 子节点树(json格式)
	 * 
	 * @param menuList
	 *            组织机构信息
	 * @param isCheckGroupids
	 *            选中的ID,存在则有多选框
	 * @param menuid
	 *            节点id
	 * @return
	 */
	private void getChildJson(List<MenuInfo> menuList, String[] isCheckGroupids,
			String menucode, List<TreeBean> treeList) {
		if (menuList == null || menuList.size() < 1 || menucode == null || "".equals(menucode)) {
			return;
		}
		StringBuilder treeStr = new StringBuilder();
		String checkedGroupStr = "";
		for (MenuInfo menuinfo : menuList) {
			checkedGroupStr = "";
			if (menucode.equals(menuinfo.getMenuParentCode() + "")) {
				TreeBean treeBean = new TreeBean();
				treeBean.setId(menuinfo.getMenuCode()+ "");// id
				treeBean.setpId(menuinfo.getMenuParentCode());//pid
				treeBean.setChecked(false);// 是否选中
				treeBean.setText(menuinfo.getMenuName());// 名称
				treeBean.setIconCls("");// 图标
				//treeBean.setState("open");// 开启状态
				//treeBean.setState("closed");// 关闭状态
				if (isCheckGroupids != null
						&& isCheckGroupids.length > 0) {
					for (String CheckStr : isCheckGroupids) {
						if (CheckStr.equals(String.valueOf(menuinfo.getMenuUUID()))) {
							treeBean.setChecked(true);
							break;
						}
					}
				}
				Map<String, String> attributes = new HashMap();
				attributes.put("menuId", menuinfo.getId() + "");// 菜单id
				attributes.put("menuName", menuinfo.getMenuName());// 菜单名称
				attributes.put("menuCode", menuinfo.getMenuCode());// 菜单代码
				attributes.put("menuHaveLowerNode",menuinfo.getMenuHaveLowerNode());// 是否有子节点
				attributes.put("menuParentCode",menuinfo.getMenuParentCode());// 父节点编号
				attributes.put("menuType", menuinfo.getMenuType());// 菜单类型id
				attributes.put("menuPermission",menuinfo.getMenuPermission());// 菜单权限标识(如"system.role"表示系统级角色权限，"system.user"用户权限)
				attributes.put("menuUrl", menuinfo.getMenuUrl()+"");// 菜单连接地址（无连接地址用#号表示）
				attributes.put("menuUUID",menuinfo.getMenuUUID());// uuid
				treeBean.setAttributes(attributes);
				treeList.add(treeBean);
				// 生成顶级节点
				if ("1".equals(menuinfo.getMenuHaveLowerNode())) {
					treeBean.setState("closed");// 关闭状态
					// 有子节点
					List<TreeBean> treeListN = new ArrayList<TreeBean>();
					treeBean.setChildren(treeListN);
					getChildJson(menuList, isCheckGroupids, menuinfo.getMenuCode()+ "", treeListN);
				}
			}
		}
	}
}
