package com.example.task;

import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Consumer {

	@Autowired
	private JobQueue jobQueue;

	@Autowired
	private ScheduledExecutorService concurrentTaskScheduler;

	@PostConstruct
	public void postConstructWorks() {
		concurrentTaskScheduler.scheduleAtFixedRate(consumer(), 0, 2L, TimeUnit.SECONDS);
	}

	private Runnable consumer() {
		return () -> {
			try {
				JobItem taken = jobQueue.getQueue().take();
				System.out.println("Taken from queue:" + taken.getId() + " with prio:" + taken.getPrio());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		};
	}

	@PreDestroy
	public void preDestroyWorks() {
		this.concurrentTaskScheduler.shutdown();
	}
}
