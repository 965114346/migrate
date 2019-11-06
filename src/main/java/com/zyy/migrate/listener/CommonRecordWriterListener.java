package com.zyy.migrate.listener;

import lombok.extern.slf4j.Slf4j;
import org.easybatch.core.listener.RecordWriterListener;
import org.easybatch.core.record.Batch;
import org.springframework.stereotype.Component;

/**
 * @author yangyang.zhang
 * @date 2019年11月04日21:03:58
 * 要侦听与写每条记录的事件，您需要注册一个RecordWriterListener接口实现
 */
@Slf4j
@Component
public class CommonRecordWriterListener implements RecordWriterListener {

    @Override
    public void beforeRecordWriting(Batch batch) {

    }

    @Override
    public void afterRecordWriting(Batch batch) {

    }

    @Override
    public void onRecordWritingException(Batch batch, Throwable throwable) {

    }
}
