package com.zyy.migrate;

import com.zyy.migrate.factory.JdbcRecordReaderFactoryBean;
import com.zyy.migrate.factory.JdbcRecordWriterFactoryBean;
import com.zyy.migrate.model.Article;
import com.zyy.migrate.model.BlackList;
import com.zyy.migrate.model.Sort;
import com.zyy.migrate.processor.ArticleRecordProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.easybatch.core.job.*;
import org.easybatch.core.listener.JobListener;
import org.easybatch.core.mapper.RecordMapper;
import org.easybatch.core.reader.RecordReader;
import org.easybatch.core.writer.StandardOutputRecordWriter;
import org.easybatch.jdbc.*;
import org.easybatch.tools.reporting.HtmlJobReportFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.sql.DataSource;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author yangyang.zhang
 * @date 2019年11月01日11:01:00
 *
 * API
 * https://github.com/j-easy/easy-batch/wiki
 */
@Slf4j
@Configuration
public class MigrateApplication {

    @Qualifier("sourceDataSource")
    @Autowired
    private DataSource sourceDataSource;

    @Qualifier("targetDataSource")
    @Autowired
    private DataSource targetDataSource;

    @Autowired
    private JdbcRecordReaderFactoryBean recordReaderFactoryBean;

    @Autowired
    private JdbcRecordWriterFactoryBean recordWriterFactoryBean;


    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring/application.xml");
        Map<String, Job> jobMap = applicationContext.getBeansOfType(Job.class);
        JobExecutor executor = applicationContext.getBean(JobExecutor.class);

        List<JobReport> list = new ArrayList<>();
        for (Map.Entry<String, Job> entry : jobMap.entrySet()) {
            //同步
            JobReport jobReport = executor.execute(entry.getValue());
            list.add(jobReport);
        }

        JobReport[] reports = new JobReport[list.size()];
        list.toArray(reports);


        DefaultJobReportMerger jobReportMerger = applicationContext.getBean(DefaultJobReportMerger.class);
        //合并报告
        JobReport jobReport = jobReportMerger.mergerReports(reports);
        HtmlJobReportFormatter reportFormatter = applicationContext.getBean(HtmlJobReportFormatter.class);
        String s = reportFormatter.formatReport(jobReport);

        FileUtils.write(new File("report/mergerJobReport.html"), s);

        applicationContext.close();
    }

    public Job sortJob(JobListener jobListener) {

        return JobBuilder.aNewJob().named("sortJob")
                .jobListener(jobListener)
                .reader(recordReaderFactoryBean.build(Sort.class))
                //.writer(new JdbcRecordWriter(targetDataSource, sortInsert, sortPsp))
                //.writer(new StandardOutputRecordWriter())
                .writer(recordWriterFactoryBean.build(Sort.class))
                .build();
    }

    @Bean
    public Job job(ArticleRecordProcessor processor, JobListener jobListener) {

        // article
        String query = "SELECT posts.id,posts.post_author, posts.post_title, posts.post_excerpt, posts.post_password, posts.post_content,posts.post_date,posts.post_modified,posts.comment_count FROM wp_posts posts WHERE posts.post_status = 'publish' AND posts.post_type = 'post' and posts.ID = 738 ORDER BY id DESC";
        String sql = "insert into article(id, sort_id, user_id, title, description, thumbnail, content, passed, read_type, create_time, update_time, article_extend, read_num, language_type) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        String[] fields = {"id", "userId", "title", "description", "articleExtend", "content", "create", "update", "readNum"};
        String[] insertFields = {"id", "sortId", "userId", "title", "description", "thumbnail", "content", "passed", "readType", "create", "update", "articleExtend", "readNum", "languageType"};
        PreparedStatementProvider psp = new BeanPropertiesPreparedStatementProvider(Article.class, insertFields);

        JdbcRecordReader reader = new JdbcRecordReader(sourceDataSource, query);
        return JobBuilder.aNewJob()//.batchSize(1)
                .jobListener(jobListener)
                .reader(recordReaderFactoryBean.build(Article.class, "post_status = 'publish' AND post_type = 'post'"))
                .processor(processor)
                .writer(recordWriterFactoryBean.build(Article.class))
                //.reader(reader)
                //.mapper(new JdbcRecordMapper(Article.class, fields))
                //.processor(processor)
                //.writer(new JdbcRecordWriter(targetDataSource, sql, psp))
                //.writer(new StandardOutputRecordWriter())
                .build();
    }

    public Job blackList(JobListener jobListener) {
        String query = "select eosno, createTime from SellBlackList";
        String insert = "insert into t_black_list values (?, ?)";
        String[] fields = {"id", "createTime"};

        return JobBuilder.aNewJob().named("blacklist Job")
                .batchSize(1)
                .jobListener(jobListener)
                .reader(new JdbcRecordReader(sourceDataSource, query))
                .mapper(new JdbcRecordMapper(BlackList.class, fields))
                .writer(new JdbcRecordWriter(targetDataSource, insert, new BeanPropertiesPreparedStatementProvider(BlackList.class, fields)))
                .build();
    }

}
