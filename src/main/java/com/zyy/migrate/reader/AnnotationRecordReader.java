package com.zyy.migrate.reader;

import com.zyy.migrate.annotation.ColumnMapping;
import com.zyy.migrate.record.ObjectRecord;
import com.zyy.migrate.sql.Sql;
import com.zyy.migrate.util.IOUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.easybatch.core.reader.RecordReader;
import org.easybatch.core.record.Header;
import org.easybatch.core.record.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@Scope("prototype")
public class AnnotationRecordReader implements RecordReader {

    private Sql sql;

    public AnnotationRecordReader(Sql sql) {
        this.sql = sql;
    }

    @Qualifier("sourceDataSource")
    @Autowired
    private DataSource dataSource;

    private Connection connection;
    private PreparedStatement statement;
    private ResultSet resultSet;
    private String dataSourceName;
    private long currentRecordNumber;
    private int maxRows;
    private int queryTimeout;
    private int fetchSize;

    @Override
    public void open() throws Exception {
        this.connection = dataSource.getConnection();

        String query = parseSql(sql);
        log.info("{}", query);

        PreparedStatement statement = connection.prepareStatement(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);

        if (this.maxRows >= 1) {
            statement.setMaxRows(this.maxRows);
        }

        if (this.fetchSize >= 1) {
            statement.setFetchSize(this.fetchSize);
        }

        if (this.queryTimeout >= 1) {
            statement.setQueryTimeout(this.queryTimeout);
        }
        this.statement = statement;
        this.resultSet = statement.executeQuery();
        this.dataSourceName = "sourceDataSource";
    }

    @Override
    public Record readRecord() throws Exception {
        if (resultSet.next()) {
            Header header = new Header(++this.currentRecordNumber, this.dataSourceName, new Date());

            Class<?> clazz = sql.getClazz();
            Object instance = clazz.newInstance();

            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                field.setAccessible(true);
                String sourceColumn = field.getAnnotation(ColumnMapping.class).sourceColumn();

                if (StringUtils.isNotBlank(sourceColumn)) {
                    Object column = resultSet.getObject(sourceColumn);
                    field.set(instance, column);
                }
            }

            return new ObjectRecord(header, instance);
        }
        return null;
    }

    @Override
    public void close() throws Exception {
        IOUtils.close(statement);
        IOUtils.close(connection);
    }

    private String parseSql(Sql sql) {
        StringBuffer buffer = new StringBuffer("select ");

        String select = StringUtils.join(sql.getSelect(), ",");
        String table = sql.getTable();
        String where = sql.getWhere();
        List<String> and = sql.getAnd();

        buffer.append(select).append(" from ").append(table);

        boolean flag = StringUtils.isNotBlank(where);
        if (flag) {
            buffer.append(" where ").append(where);
        }

        if (Objects.nonNull(and) && and.size() > 0) {
            if (!flag) {
                buffer.append(" where 1 = 1 ");
            }
            for (String s : and) {
                buffer.append(" and ").append(s);
            }
        }

        return buffer.toString();
    }
}
