package com.zyy.migrate.factory;

import com.zyy.migrate.annotation.ColumnMapping;
import com.zyy.migrate.annotation.TableMapping;
import com.zyy.migrate.sql.Sql;
import lombok.extern.slf4j.Slf4j;
import org.easybatch.core.writer.RecordWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class JdbcRecordWriterFactoryBean {

    @Autowired
    private ApplicationContext applicationContext;

    public RecordWriter build(Class<?> clazz) {
        TableMapping annotation = clazz.getAnnotation(TableMapping.class);
        String targetTable = annotation.targetTable();

        List<String> insert = new ArrayList<>();
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            ColumnMapping columnMapping = field.getAnnotation(ColumnMapping.class);
            insert.add(columnMapping.targetColumn());
        }

        Sql build = new Sql.Builder(clazz).table(targetTable).insert(insert).build();
        log.info("Sql: {}", build);
        return applicationContext.getBean(RecordWriter.class, build);
    }
}
