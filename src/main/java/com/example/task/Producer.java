package com.example.task;

import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Producer {

	@Autowired
	private JobQueue jobQueue;
	@Autowired
	private ScheduledExecutorService concurrentTaskScheduler;

	private AtomicLong atomicLong = new AtomicLong();

	@PostConstruct
	public void postConstructWorks() {
		Random rn = new Random();
		concurrentTaskScheduler.scheduleAtFixedRate(creator(rn), 2L, 1L, TimeUnit.SECONDS);

	}

	private Runnable creator(Random rn) {
		return () -> {
			int prio = rn.nextInt(10);
			JobItem j = new JobItem(atomicLong.getAndIncrement(), prio, "any message");
			jobQueue.getQueue().add(j);
			System.out.println("Put to queue:" + j.getId() + " with prio:" + j.getPrio());
		};
	}

	@PreDestroy
	public void preDestroyWorks() {
		this.concurrentTaskScheduler.shutdown();
	}
}
