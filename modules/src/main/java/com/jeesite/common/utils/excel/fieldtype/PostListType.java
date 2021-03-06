/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.common.utils.excel.fieldtype;

import java.util.List;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.utils.SpringUtils;
import com.jeesite.modules.sys.entity.Post;
import com.jeesite.modules.sys.service.PostService;

/**
 * 字段类型转换
 * @author ThinkGem
 * @version 2015-03-24
 * @example fieldType = PostListType.class
 */
public class PostListType {

	private static PostService postService = SpringUtils.getBean(PostService.class);
	private static ThreadLocal<List<Post>> cache = new ThreadLocal<>();

	/**
	 * 获取对象值（导入）
	 */
	public static Object getValue(String val) {
		List<Post> postList = ListUtils.newArrayList();
		List<Post> allPostList = cache.get();
		if (allPostList == null){
			allPostList = postService.findList(new Post());
			cache.set(allPostList); // 不知道会不会引起内存泄露，先这样用着
		}
		for (String s : StringUtils.split(val, ",")) {
			for (Post e : allPostList) {
				if (StringUtils.trimToEmpty(s).equals(e.getPostName())) {
					postList.add(e);
				}
			}
		}
		return postList.size() > 0 ? postList : null;
	}

	/**
	 * 设置对象值（导出）
	 */
	public static String setValue(Object val) {
		if (val != null) {
			@SuppressWarnings("unchecked")
			List<Post> postList = (List<Post>) val;
			return ListUtils.extractToString(postList, "postName", ", ");
		}
		return "";
	}
}
