package com.zyy.migrate.reader;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbutils.QueryRunner;
import org.easybatch.core.reader.RecordReader;
import org.easybatch.core.record.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@Slf4j
@Component
public class CommonJdbcRecordReader implements RecordReader {

    @Qualifier("sourceDataSource")
    @Autowired
    private DataSource dataSource;

    private Connection connection;
    private QueryRunner queryRunner;

    @Override
    public void open() throws Exception {
        this.connection = dataSource.getConnection();
        Statement statement = this.connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
    }

    @Override
    public Record readRecord() throws Exception {
        return null;
    }

    @Override
    public void close() throws Exception {

    }
}
