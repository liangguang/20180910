package com.lgp.utils.system;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CountDownLatchTestUtil {

	static CountDownLatch latch = new CountDownLatch(1);
	
	static CountDownLatch downLatch = new CountDownLatch(10);
	
	static ExecutorService service = Executors.newCachedThreadPool();

	public static void main(String[] args) {
		final long start = System.currentTimeMillis();
		
		for (int i = 0; i < 10; i++) {
			final int index = i+1;
				Runnable runnable = new Runnable() {
					
					@Override
					public void run() {
						
						try {
							latch.await();
							long end = System.currentTimeMillis();
							System.out.println(Thread.currentThread().getName()+"===已鸣枪，第"+index+"位运动员出发,反应时间："+(end-start)+"ms");
							Thread.sleep((long) (Math.random() * 10000));
							end = System.currentTimeMillis();
							System.out.println(Thread.currentThread().getName()+"===第"+index+"位运动员完成,用时"+(end-start)+"ms");
							downLatch.countDown();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
				};
				service.execute(runnable);
		}
		try {
			System.out.println("鸣枪开始！");
			latch.countDown();
			
			downLatch.await();
			System.out.println("比赛结束");
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		service.shutdown();
	}
	
	
}
