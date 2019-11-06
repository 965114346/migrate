package com.zyy.migrate.validator;

import lombok.extern.slf4j.Slf4j;
import org.easybatch.core.record.Record;
import org.easybatch.core.validator.RecordValidator;
import org.springframework.stereotype.Component;

/**
 * @author yangyang.zhang
 * @date 2019年11月05日11:38:26
 * 验证器
 */
@Slf4j
@Component
public class CommonRecordValidator implements RecordValidator {

    @Override
    public Record processRecord(Record record) throws Exception {
        // 返回null或抛出异常不通过验证
        return record;
    }
}
