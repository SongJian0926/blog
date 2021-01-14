package com.gframework.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.RandomAccess;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.springframework.lang.Nullable;

/**
 * 树状数据结构处理工具类.
 * 
 * @since 2.0.0
 * @author Ghwolf
 */
public abstract class TreeUtils {

	/**
	 * 树节点筛选器.
	 * <p>筛选一个树，仅保留指定id的节点极其父节点和他的子节点
	 * @param <T> 数节点类型
	 * @param <K> id类型
	 * @param tree 保留着所有顶级父节点的集合，需要可修改，因为节点变动会直接在list上进行变动。可以为null
	 * @param getId 获取id的方法
	 * @param getChild 获取子节点的方法
	 * @param filterIds 要保留的id，可以为null
	 */
	public static <T,K> void treeFilter(@Nullable List<T> tree,Function<T, K> getId,Function<T,List<T>> getChild,@Nullable List<K> filterIds){
		if (tree == null || tree.isEmpty() || filterIds == null || filterIds.isEmpty()) return ;
		treeFilter0(tree,getId,getChild,filterIds);
	}
	
	private static <T,K> boolean treeFilter0(List<T> tree,Function<T, K> getId,Function<T,List<T>> getChild,List<K> filterIds){
		
		boolean flag = false ;
		
		Iterator<T> iter = tree.iterator();
		while(iter.hasNext()) {
			T t = iter.next();
				
			if (filterIds.contains(getId.apply(t))) {
				flag = true ;
			} else {
				List<T> ch = getChild.apply(t);
				if (ch == null || ch.isEmpty()) {
					iter.remove();
					continue ;
				}
				boolean f = treeFilter0(ch,getId,getChild,filterIds);
				if (f) {
					flag = true ;
				} else {
					iter.remove();
				}
			}
		}
		
		return flag ;
	}
	
	
	// TODO 树节点删除器，待实现
//	public void treeDeleter(){};
	

	/**
	 * 将一个乱序的对象集合，转换为具有树状结构的
	 * <p>
	 * 仅仅当parentId为null时才会认为是父节点，如果找不到一个节点的parentId对应的数据，则会忽略此节点极其所有子节点。
	 * 同时你应当注意避免出现循环引用问题，虽然如果出现数据上的循环引用，不会造成此方法堵塞，但是会导致在使用返回结果时出现问题。
	 * <p>
	 * 时间复杂度为："n的累加"
	 * 
	 * @param <T> 集合对象类型
	 * @param <K> id类型
	 * @param list 集合对象，可以为null
	 * @param getId 获取id的方法
	 * @param getParentId 获取父级id方法
	 * @param setChild 设置子节点的方法
	 * @return 返回新的以及拥有树结构的集合，此集合仅包含父级节点
	 */
	public static <T, K> List<T> toTree(@Nullable List<T> list, Function<T, K> getId, Function<T, K> getParentId,
			BiConsumer<T, T> setChild) {
		if (list == null || list.isEmpty()) return Collections.emptyList();

		List<T> parentList = new ArrayList<>();
		if (list instanceof RandomAccess) {
			for (int x = 0; x < list.size(); x ++) {
				T t = list.get(x);
				K id = getId.apply(t);
				K pid = getParentId.apply(t);
				for (int y = x + 1; y < list.size(); y ++) {
					T t2 = list.get(y);
					K id2 = getId.apply(t2);
					K pid2 = getParentId.apply(t2);
					if (Objects.equals(id, pid2)) {
						setChild.accept(t, t2);
					} else if (Objects.equals(pid, id2)) {
						setChild.accept(t2, t);
					}
				}
				if (pid == null) {
					parentList.add(t);
				}
			}
		} else {
			List<T> arrList = new ArrayList<>(list.size());
			for (T t : list) {
				K id = getId.apply(t);
				K pid = getParentId.apply(t);
				for (int x = 0; x < arrList.size(); x ++) {
					T t2 = arrList.get(x);
					K id2 = getId.apply(t2);
					K pid2 = getParentId.apply(t2);
					if (Objects.equals(id, pid2)) {
						setChild.accept(t, t2);
					} else if (Objects.equals(pid, id2)) {
						setChild.accept(t2, t);
					}
				}
				arrList.add(t);
				if (pid == null) {
					parentList.add(t);
				}
			}
		}

		return parentList;
	}
}
