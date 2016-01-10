package me.danbai.schedule.component.cache;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import net.vinote.smartboot.integration.cache.CacheClient;

import java.util.List;

/**
 * 缓存组件,某些业务在完成修改类操作后可调用该组件对缓存进行清理
 * @author 三刀
 * @version v0.1 2015年11月07日 下午11:15 Seer Exp.
 */
public class CacheComponentImpl implements CacheComponent {

    @Autowired
    private CacheClient cacheClient;
    @Override
    public void remove(PrefixEnum prefix, String staticKey) {
        if (prefix == null) {
            throw new IllegalArgumentException("缓存前缀不能为空");
        }
        cacheClient.remove(prefix.getPrefix()+(StringUtils.isBlank(staticKey)?"":CacheInterceptor.KEY_SEPERATOR+staticKey));
    }

    @Override
    public void remove(PrefixEnum prefix, List<String> cacheKeys) {
        if(CollectionUtils.isNotEmpty(cacheKeys)){
            for(String key:cacheKeys){
                remove(prefix,key);
            }
        }
    }
}
