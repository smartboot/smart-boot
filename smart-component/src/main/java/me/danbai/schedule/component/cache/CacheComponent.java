package me.danbai.schedule.component.cache;

import java.util.List;

/**
 * 缓存组件
 * @author 三刀
 * @version v0.1 2015年11月07日 下午11:09 Seer Exp.
 */
public interface CacheComponent {

    /**
     * 针对没有方法参数的接口，清除缓存
     *  <pre>
     *  针对方法没有使用注解@CacheKey
     * </pre>
     * @param prefix
     * @param staticKey 可以为空
     */
    public void remove(PrefixEnum prefix, String staticKey);

    /**
     * 清除缓存
     * <pre>
     *  针对方法有使用注解@CacheKey
     * </pre>
     *
     * @param prefix
     * @param cacheKeys
     */
    public void remove(PrefixEnum prefix,  List<String> cacheKeys);
}
