package com.example.task;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class JobItem {
	private Long id;
	private Integer prio;
	private String message;
}
