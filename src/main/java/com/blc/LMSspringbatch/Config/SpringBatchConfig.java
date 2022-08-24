package com.blc.LMSspringbatch.Config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.PathResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import com.blc.LMSspringbatch.Model.CandidateModel;
import com.blc.LMSspringbatch.Repository.ICandidateRepository;

import lombok.AllArgsConstructor;


@Configuration
@EnableBatchProcessing
@AllArgsConstructor
public class SpringBatchConfig {

	
    private JobBuilderFactory jobBuilderFactory;

    private StepBuilderFactory stepBuilderFactory;

    private ICandidateRepository candidateRepo;


    @Bean
    public FlatFileItemReader<CandidateModel> reader() {
        FlatFileItemReader<CandidateModel> itemReader = new FlatFileItemReader<>();
        itemReader.setResource(new FileSystemResource("C:\\Users\\Dell\\Downloads\\LMS-spring-batch\\src\\main\\resources\\Candidates.csv"));
   //     itemReader.setName("csvReader");
        itemReader.setLinesToSkip(1);
        itemReader.setLineMapper(lineMapper());
        System.out.println("HELLo....................");
        return itemReader;
    }

    private LineMapper<CandidateModel> lineMapper() {
        DefaultLineMapper<CandidateModel> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
    //    lineTokenizer.setStrict(false);
        lineTokenizer.setNames("cicId","fullName","email","mobileNum","hiredDate","degree","aggrPer","city","state","preferredJobLocation","status","passedOutYear","creatorUser","candidateStatus");
        
        BeanWrapperFieldSetMapper<CandidateModel> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(CandidateModel.class);

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;

    }

    @Bean
    public CandidateProcessor processor() {
        return new CandidateProcessor();
    }

    @Bean
    public RepositoryItemWriter<CandidateModel> writer() {
        RepositoryItemWriter<CandidateModel> writer = new RepositoryItemWriter<>();
        writer.setRepository(candidateRepo);
        writer.setMethodName("save");
        
        return writer;
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("csv-step").<CandidateModel, CandidateModel>chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public Job runJob() {
        return jobBuilderFactory.get("importcandidates")
                .flow(step1()).end().build();

    }

    @Bean
    public TaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();
        asyncTaskExecutor.setConcurrencyLimit(10);
        return asyncTaskExecutor;
    }
}
