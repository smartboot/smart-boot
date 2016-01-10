package me.danbai.schedule.component.cache;

import java.lang.annotation.*;

/**
 * 拼凑缓存Key
 *
 * @author 三刀
 * @version v0.1 2015年11月06日 下午1:46 Seer Exp.
 */
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheKey {

    /** 缓存字段，必须具有getter方法, 为空则直接去当前参数的toString(), 当前参数为空则为"" */
    String[] fields() default {};

}
