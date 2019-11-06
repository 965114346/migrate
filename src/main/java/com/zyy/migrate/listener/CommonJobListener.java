package com.zyy.migrate.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.easybatch.core.job.JobParameters;
import org.easybatch.core.job.JobReport;
import org.easybatch.core.listener.JobListener;
import org.easybatch.tools.reporting.HtmlJobReportFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

/**
 * @author yangyang.zhang
 * @date 2019年11月04日21:03:58
 * 要侦听与工作相关的事件，您需要注册一个JobListener接口实现
 */
@Slf4j
@Component
public class CommonJobListener implements JobListener {

    @Autowired
    private HtmlJobReportFormatter jobReportFormatter;

    @Override
    public void beforeJobStart(JobParameters jobParameters) {

    }

    @Override
    public void afterJobEnd(JobReport jobReport) {
        String report = jobReportFormatter.formatReport(jobReport);
        try {
            FileUtils.write(new File("report/" + jobReport.getJobName() + ".html"), report);
        } catch (IOException e) {
            log.error("{}", e);
        }
    }
}
