package com.example.task.business;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.task.entity.JobItem;
import com.example.task.persistency.JobsRepository;

@Component
public class Producer {

	@Autowired
	private ScheduledExecutorService concurrentTaskScheduler;

	@Autowired
	private JobsRepository jobRepository;

	@PostConstruct
	public void postConstructWorks() {
		Random rn = new Random();
		concurrentTaskScheduler.scheduleAtFixedRate(() -> persist(rn), 2L, 500L, TimeUnit.MILLISECONDS);
	}

	@Transactional
	private void persist(Random rn) {
		int prio = rn.nextInt(10);
		JobItem j = new JobItem();
		j.setMessage("any");
		j.setPrio(prio);
		jobRepository.save(j);
		System.out.println("Put to db:" + j.getId() + " with prio:" + j.getPrio());
	}

}
