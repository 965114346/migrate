package com.zyy.migrate.writer;

import com.zyy.migrate.annotation.ColumnMapping;
import com.zyy.migrate.sql.Sql;
import com.zyy.migrate.util.IOUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.easybatch.core.record.Batch;
import org.easybatch.core.record.Record;
import org.easybatch.core.writer.RecordWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

@Slf4j
@Component
@Scope("prototype")
public class AnnotationRecordWriter implements RecordWriter {

    private Sql sql;
    private String insertSql;
    private Connection connection;

    public AnnotationRecordWriter(Sql sql) {
        this.sql = sql;
    }

    @Qualifier("targetDataSource")
    @Autowired
    private DataSource dataSource;

    @Override
    public void open() throws Exception {
        connection = dataSource.getConnection();
        connection.setAutoCommit(false);

        insertSql = parseSql(sql);
        log.info("insert sql: {}", insertSql);
    }

    @Override
    public void writeRecords(Batch batch) throws Exception {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(insertSql);

            Iterator<Record> iterator = batch.iterator();
            while (iterator.hasNext()) {
                Object payload = iterator.next().getPayload();
                Field[] fields = payload.getClass().getDeclaredFields();

                for (int i = 0; i < fields.length; i++) {
                    Field field = fields[i];
                    field.setAccessible(true);

                    try {
                        Object o = field.get(payload);

                        if (Objects.isNull(o)) {
                            ColumnMapping annotation = field.getAnnotation(ColumnMapping.class);
                            o = annotation.defaultInsertValue();
                            log.info("Field:{} defaultValue:{}", field.getName(), o);
                        }
                        statement.setObject(i + 1, o);
                    } catch (Exception e) {
                        log.info("{}", e);
                    }
                }
                log.info("insert: {}", payload);
                try {
                    statement.addBatch();
                } catch (Exception e) {
                    log.info("{}", e);
                }
            }

            statement.executeBatch();
            connection.commit();
            log.info("Transaction committed");
        }  catch (SQLException e) {
            log.error("Unable to commit transaction ", e);
            connection.rollback();
            log.info("Transaction rolled back");
        } finally {
            IOUtils.close(statement);
        }
    }

    @Override
    public void close() throws Exception {
        IOUtils.close(connection);
    }

    private String parseSql(Sql sql) {
        StringBuilder builder = new StringBuilder();

        String table = sql.getTable();
        List<String> insert = sql.getInsert();

        builder.append("insert into ").append(table);

        if (Objects.nonNull(insert) && insert.size() > 0) {
            builder.append("(").append(StringUtils.join(insert, ",")).append(")");
        }

        List<String> questionList = new ArrayList<>();
        for (int i = 0; i < insert.size(); i++) {
            questionList.add("?");
        }
        builder.append(" values(").append(StringUtils.join(questionList, ",")).append(")");

        return builder.toString();
    }
}
