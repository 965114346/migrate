package com.zyy.migrate.listener;

import lombok.extern.slf4j.Slf4j;
import org.easybatch.core.listener.PipelineListener;
import org.easybatch.core.record.Record;
import org.springframework.stereotype.Component;

/**
 * @author yangyang.zhang
 * @date 2019年11月04日21:03:58
 * 处理管道侦听器
 * 要侦听与加工每条记录的事件，您需要注册一个PipelineListener接口实现
 */
@Slf4j
@Component
public class CommonPipelineListener implements PipelineListener {

    @Override
    public Record beforeRecordProcessing(Record record) {
        return null;
    }

    @Override
    public void afterRecordProcessing(Record record, Record record1) {

    }

    @Override
    public void onRecordProcessingException(Record record, Throwable throwable) {

    }
}
