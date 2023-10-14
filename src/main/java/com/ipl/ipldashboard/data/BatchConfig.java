package com.ipl.ipldashboard.data;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import com.ipl.ipldashboard.model.Match;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    private final String[] FIELD_NAMES = new String[] {
            "id", "season", "city", "date", "team1", "team2", "toss_winner", "toss_decision", "result", "dl_applied",
            "winner", "win_by_runs", "win_by_wickets", "player_of_match", "venue", "umpire1", "umpire2", "umpire3"
    };

    // @Autowired
    // public JobBuilder jobBuilder;

    // @Autowired
    // public StepBuilder stepBuilder;

    @Bean
    FlatFileItemReader<MatchInput> reader() {
        System.out.println("FlatFileItemReader Called");
        return new FlatFileItemReaderBuilder<MatchInput>()
                .name("matchItemReader")
                .resource(new ClassPathResource("match_info_data.csv"))
                .delimited()
                .names(FIELD_NAMES)
                .fieldSetMapper(new BeanWrapperFieldSetMapper<MatchInput>() {
                    {
                        setTargetType(MatchInput.class);
                    }
                })
                .build();
    }

    @Bean
    MatchDataProcessor processor() {
         System.out.println("MatchDataProcessor Called");
        return new MatchDataProcessor();
    }

    @Bean
    JdbcBatchItemWriter<Match> writer(DataSource dataSource) {
        System.out.println("JdbcBatchItemWriter Called");
        return new JdbcBatchItemWriterBuilder<Match>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO match (id,season,city,date,team1,team2,toss_winner,toss_decision,result,dl_applied,winner,win_by_runs,win_by_wickets,player_of_match,venue,umpire1,umpire2,umpire3)"
                        +
                        "VALUES (:id, :season, :city, :date, :team1, :team2, :tossWinner, :tossDecision, :result, :dlApplied, :matchWinner, :winByRuns, :winByWickets, :playerOfMatch, :venue, :umpire1, :umpire2, :umpire3)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    Job importUserJob(JobRepository jobRepository,
            JobCompletionNotificationListener listener, Step step1) {
                  System.out.println("importUserJob Called");
        return new JobBuilder("importUserJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }

    @Bean
    Step step1(JobRepository jobRepository,
            PlatformTransactionManager transactionManager, JdbcBatchItemWriter<Match> writer) {
                 System.out.println("step1 Called");
        return new StepBuilder("step1", jobRepository)
                .<MatchInput, Match>chunk(10, transactionManager)
                .reader(reader())
                .processor(processor())
                .writer(writer)
                .build();
    }
}
