package com.example.task.persistency;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.task.entity.JobItem;

@Repository
public interface JobsRepository extends JpaRepository<JobItem, Long> {
	List<JobItem> findByProgressIs(boolean progress);

}
