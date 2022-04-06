package com.example.task.business;

import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.task.entity.JobItem;
import com.example.task.persistency.JobsRepository;

@Component
public class Consumer {

	@Autowired
	private JobQueue jobQueue;

	@Autowired
	private ScheduledExecutorService concurrentTaskScheduler;

	@Autowired
	private JobsRepository jobRepository;

	@PostConstruct
	public void postConstructWorks() {
		concurrentTaskScheduler.scheduleAtFixedRate(() -> {
			try {
				runTheJob();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}, 2L, 100L, TimeUnit.MILLISECONDS);
	}

	@Transactional
	private void runTheJob() throws InterruptedException {
		JobItem peek = jobQueue.getQueue().peek();
		if (Objects.isNull(peek))
			return;
		JobItem taken = jobQueue.getQueue().take();
		System.out.println("Taken from queue:" + taken.getId() + " with prio:" + taken.getPrio());
		jobRepository.delete(taken);
	}

	@PreDestroy
	public void preDestroyWorks() {
		this.concurrentTaskScheduler.shutdown();
	}

}
