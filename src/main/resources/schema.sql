DROP TABLE IF EXISTS jobs;


CREATE TABLE IF NOT EXISTS jobs(
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  prio INTEGER,
  progress BOOLEAN,
  message VARCHAR(100) NOT NULL);


create INDEX jobs_indx on jobs(progress);