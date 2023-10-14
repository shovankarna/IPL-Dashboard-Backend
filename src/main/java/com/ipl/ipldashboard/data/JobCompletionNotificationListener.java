package com.ipl.ipldashboard.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;

@Component
public class JobCompletionNotificationListener implements JobExecutionListener {

    private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

    private final JdbcTemplate jdbcTemplate;

    public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
        System.out.println("JobCompletionNotificationListener Called");
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public void afterJob(JobExecution jobExecution) {
        System.out.println("afterJob Called");
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("!!! JOB FINISHED! Time to verify the results");

            jdbcTemplate.query("SELECT team1, team2, date FROM match",
                    (rs, row) -> "Team1: " + rs.getString(1) + "Team2: " + rs.getString(2) + "Date: " + rs.getString(3))
                    .forEach(str -> System.out.println(str));
        }
    }
}