package spark.render;

/**
 * 页面展现器接口
 * @author xd
 *
 * @param <T>
 */
public interface ISparkRender<T> {
	public T render(Object o);
	public T parse(String json,Class<?> c);
}
