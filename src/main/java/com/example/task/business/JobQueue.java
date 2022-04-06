package com.example.task.business;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.task.entity.JobItem;
import com.example.task.persistency.JobsRepository;

import lombok.Getter;

@Component
@Getter
public class JobQueue {

	private PriorityBlockingQueue<JobItem> queue;

	@Autowired
	private JobsRepository jobRepository;

	@Autowired
	private ScheduledExecutorService concurrentTaskScheduler;

	@PostConstruct
	public void constructQueue() {
		Comparator<JobItem> prioSorter = Comparator.comparing(JobItem::getPrio).reversed();
		this.queue = new PriorityBlockingQueue<JobItem>(10, prioSorter);
		concurrentTaskScheduler.scheduleWithFixedDelay(() -> feed(), 0, 4L, TimeUnit.SECONDS);
	}

	@Transactional
	private void feed() {
		List<JobItem> findAllNotInProgress = jobRepository.findByProgressIs(false);
		if (Objects.isNull(findAllNotInProgress)) {
			return;
		}

		findAllNotInProgress.stream().forEach(k -> {
			System.out.println("Reading from table and putting to queue:" + k.getId());
			k.setProgress(true);
		});

		queue.addAll(findAllNotInProgress);
	}

}
