package net.vinote.smartboot.service.test;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import net.vinote.smartboot.integration.cache.CacheClient;

public class CacheClientTest extends AbstractUnitTest{

	@Autowired
	private CacheClient cacheClient;
	@Test
	public void testA(){
		int[] a=new int[1024];
		for(int i=0;i<a.length;i++){
			a[i]=i;
		}
		cacheClient.putObject("1111111", a, 60);
		int[] q=cacheClient.getObject("1111111");
		for(int i=0;i<q.length;i++){
			System.out.println(q[i]);
		}
	}
}
