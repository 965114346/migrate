package com.zyy.migrate.listener;

import lombok.extern.slf4j.Slf4j;
import org.easybatch.core.listener.BatchListener;
import org.easybatch.core.record.Batch;
import org.springframework.stereotype.Component;

/**
 * @author yangyang.zhang
 * @date 2019年11月04日21:03:58
 * 批处理侦听器
 */
@Slf4j
@Component
public class CommonBatchListener implements BatchListener {

    @Override
    public void beforeBatchReading() {

    }

    @Override
    public void afterBatchProcessing(Batch batch) {

    }

    @Override
    public void afterBatchWriting(Batch batch) {

    }

    @Override
    public void onBatchWritingException(Batch batch, Throwable throwable) {

    }
}
