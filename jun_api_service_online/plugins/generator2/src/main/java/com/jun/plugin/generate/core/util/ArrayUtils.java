package com.jun.plugin.generate.core.util;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.util.StringUtils;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Api("数组工具类")
public class ArrayUtils {

	@SuppressWarnings("unchecked")
	@ApiOperation("任意对象转为集合(ArrayList)")
	public static List<Object> toList(@ApiParam("任意对象") Object object) {
		List<Object> list = new ArrayList<>();
		if (object instanceof Collection)
			list.addAll((Collection<Object>) object);
		else if (object != null)
			if (object.getClass().isArray())
				list.addAll(ArrayUtils.as((Object[]) object));
			else
				list.add(object);
		return list;
	}

	@SuppressWarnings("unchecked")
	@ApiOperation("对象数组转为集合(ArrayList)")
	public static <T> List<T> as(@ApiParam("对象数组") T... array) {
		List<T> list = new ArrayList<T>(array.length);
		for (T t : array)
			list.add(t);
		return list;
	}

	@SuppressWarnings("unchecked")
	@ApiOperation("对象数组转为集合(HashSet)")
	public static <T> Set<T> asHashSet(@ApiParam("对象数组") T... array) {
		Set<T> set = new HashSet<T>();
		for (T t : array)
			set.add(t);
		return set;
	}

	@SuppressWarnings("unchecked")
	@ApiOperation("对象数组转为集合(TreeSet)")
	public static <T> Set<T> asTreeSet(@ApiParam("对象数组") T... array) {
		Set<T> set = new TreeSet<T>();
		for (T t : array)
			set.add(t);
		return set;
	}

	@ApiOperation("返回对象数组")
	public static Object[] asObject(@ApiParam("字符串数组") Object... array) {
		return array;
	}

	@ApiOperation("返回字符串数组")
	public static String[] asString(@ApiParam("字符串数组") String... array) {
		return array;
	}

	@ApiOperation("字符串数组转为整数数组")
	public static Integer[] asInteger(@ApiParam("字符串数组") String... array) {
		Integer[] result = new Integer[array.length];
		for (int i = 0; i < array.length; i++)
			result[i] = Integer.parseInt(array[i]);
		return result;
	}

	@ApiOperation("字符串数组转为长整数数组")
	public static Long[] asLong(@ApiParam("字符串数组") String... array) {
		Long[] result = new Long[array.length];
		for (int i = 0; i < array.length; i++)
			result[i] = Long.parseLong(array[i]);
		return result;
	}

	@ApiOperation("字符串数组转为Double数组")
	public static Double[] asDouble(@ApiParam("字符串数组") String... array) {
		Double[] result = new Double[array.length];
		for (int i = 0; i < array.length; i++)
			result[i] = Double.parseDouble(array[i]);
		return result;
	}

	@ApiOperation("字符串数组转为BigDecimal数组")
	public static BigDecimal[] asBigDecimal(@ApiParam("字符串数组") String... array) {
		BigDecimal[] result = new BigDecimal[array.length];
		for (int i = 0; i < array.length; i++)
			result[i] = new BigDecimal(array[i]);
		return result;
	}

	@SuppressWarnings("unchecked")
	@ApiOperation("字符串数组转为枚举数组")
	public static <T extends Enum<T>> T[] asEnum(@ApiParam("枚举类型") Class<T> type, @ApiParam("字符串数组") String... array) {
		T[] ts = (T[]) Array.newInstance(type, array.length);
		for (int i = 0; i < array.length; i++)
			ts[i] = Enum.valueOf(type, array[i]);
		return ts;
	}

	@ApiOperation("将集合或数组合并为以逗号分隔的字符串")
	public static String join(@ApiParam("对象集合") Object collection) {
		return join(collection, ",");
	}

	@ApiOperation("将集合或数组合并为以分隔符分隔的字符串")
	public static String join(@ApiParam("对象集合") Object collection, @ApiParam("分隔符") String separator) {
		StringBuilder builder = new StringBuilder();
		for (Object object : toList(collection)) {
			builder.append(object);
			builder.append(separator);
		}
		if (builder.length() > 0)
			builder.delete(builder.length() - separator.length(), builder.length());
		return builder.toString();
	}

	@ApiOperation("比较目标集合中是否包含源集合中的对象")
	public static boolean hasContains(@ApiParam("源集合") Collection<?> src, @ApiParam("目标集合") Collection<?> dest) {
		for (Object o : dest)
			if (src.contains(o))
				return true;
		return false;
	}

	@ApiOperation("将对象集合转换为对象Map")
	public static <T, K> Map<K, T> toMap(@ApiParam("集合") Collection<T> collection,
			@ApiParam("Map键值lambda表达式") Function<T, K> keyMapper) {
		return toMap(collection, keyMapper, item -> item);
	}

	/**
	 * @param collection
	 * @param keyMapper   E::getName 或 e->e.getName()
	 * @param valueMapper E::getName 或 e->e.getName() 或
	 *                    e->MapUtils.as("name",e.getName)
	 * @return
	 */
	@ApiOperation("将对象集合转换为对象Map")
	public static <T, K, V> Map<K, V> toMap(@ApiParam("集合") Collection<T> collection,
			@ApiParam("Map键值lambda表达式") Function<T, K> keyMapper,
			@ApiParam("新对象lambda表达式") Function<T, V> valueMapper) {
		return collection.stream().collect(Collectors.toMap(keyMapper, valueMapper));
	}

	@ApiOperation("将对象集合转换为新对象集合")
	public static <T, N> List<N> map(@ApiParam("集合") Collection<T> collection,
			@ApiParam("新对象lambda表达式") Function<T, N> mapper) {
		return collection.stream().map(mapper).collect(Collectors.toList());
	}

	@ApiOperation("将集合进行过滤")
	public static <T> List<T> filter(@ApiParam("集合") Collection<T> collection,
			@ApiParam("过滤条件lambda表达式") Predicate<T> predicate) {
		return collection.stream().filter(predicate).collect(Collectors.toList());
	}

	@ApiOperation("返回集合是否包含")
	public static <T> boolean contains(@ApiParam("集合") Collection<T> collection,
			@ApiParam("包含条件lambda表达式") Predicate<T> predicate) {
		return collection.stream().anyMatch(predicate);
	}

	@ApiOperation("将集合构建为树集合")
	public static <T, K> void buildTree(@ApiParam("集合") Collection<T> collection,
			@ApiParam("key值lambda表达式") Function<T, K> keyMapper,
			@ApiParam("parent值lambda表达式") Function<T, K> parentMapper,
			@ApiParam("获取子集合lambda表达式") Function<T, Collection<T>> getChildrenMapper,
			@ApiParam("设置子集合lambda表达式") BiConsumer<T, Collection<T>> setChildrenMapper) {
		Map<K, T> map = toMap(collection, item -> {
			Collection<T> children = getChildrenMapper.apply(item);
			if (children == null) {
				children = new ArrayList<>();
				setChildrenMapper.accept(item, children);
			}
			return keyMapper.apply(item);
		});
		for (Iterator<T> iterator = collection.iterator(); iterator.hasNext();) {
			T t = iterator.next();
			T parentT = map.get(parentMapper.apply(t));
			if (parentT != null) {
				Collection<T> children = getChildrenMapper.apply(parentT);
				children.add(t);
				iterator.remove();
			}
		}
	}

	public static void buildTree(Collection<Map> collection, String key,
			@ApiParam("parent属性名称") String parent) {
		String childrenKey = "children";
		buildTree(collection, item -> item.get(key), item -> item.get(parent),
				item -> getValue(item,childrenKey, new ArrayList<>()), null);
	}

	public static <T> T getValue(Map map, String key, T defaultValue) {
		T value = (T) map.get(key);
		if (StringUtils.isEmpty(value)) {
			value = defaultValue;
			map.put(key, value);
		}
		return value;
	}

}
