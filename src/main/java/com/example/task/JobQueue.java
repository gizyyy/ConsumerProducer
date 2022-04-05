package com.example.task;

import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
@Getter
public class JobQueue {

	private PriorityBlockingQueue<JobItem> queue;

	@PostConstruct
	public void constructQueue() {
		Comparator<JobItem> prioSorter = Comparator.comparing(JobItem::getPrio).reversed();
		this.queue = new PriorityBlockingQueue<JobItem>(10, prioSorter);
	}

}
