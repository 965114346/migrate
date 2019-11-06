package com.zyy.migrate.listener;

import lombok.extern.slf4j.Slf4j;
import org.easybatch.core.listener.RecordReaderListener;
import org.easybatch.core.record.Record;
import org.springframework.stereotype.Component;

/**
 * @author yangyang.zhang
 * @date 2019年11月04日21:03:58
 * 要侦听与读每条记录的事件，您需要注册一个RecordReaderListener接口实现
 */
@Slf4j
@Component
public class CommonRecordReaderListener implements RecordReaderListener {

    @Override
    public void beforeRecordReading() {

    }

    @Override
    public void afterRecordReading(Record record) {

    }

    @Override
    public void onRecordReadingException(Throwable throwable) {

    }
}
