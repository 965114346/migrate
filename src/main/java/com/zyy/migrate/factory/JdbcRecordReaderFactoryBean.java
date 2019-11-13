package com.zyy.migrate.factory;

import com.zyy.migrate.annotation.ColumnMapping;
import com.zyy.migrate.annotation.TableMapping;
import com.zyy.migrate.sql.Sql;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.easybatch.core.reader.RecordReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class JdbcRecordReaderFactoryBean {

    @Autowired
    private ApplicationContext applicationContext;

    public RecordReader build(Class<?> clazz) {
        return build(clazz, "");
    }

    public RecordReader build(Class<?> clazz, String where) {
        TableMapping annotation = clazz.getAnnotation(TableMapping.class);
        String sourceTable = annotation.sourceTable();

        List<String> select = new ArrayList<>();
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            ColumnMapping columnMapping = field.getAnnotation(ColumnMapping.class);
            String sourceColumn = columnMapping.sourceColumn();

            if (StringUtils.isNotBlank(sourceColumn)) {
                select.add(sourceColumn);
            }
        }

        Sql.Builder builder = new Sql.Builder(clazz);
        if (StringUtils.isNotBlank(where)) {
            builder.where(where);
        }
        Sql build = builder.table(sourceTable).select(select).build();
        log.info("Sql: {}", build);
        return applicationContext.getBean(RecordReader.class, build);
    }
}
