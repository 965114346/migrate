package com.zyy.migrate.model;

import com.zyy.migrate.annotation.ColumnMapping;
import com.zyy.migrate.annotation.TableMapping;
import lombok.Data;

import java.math.BigInteger;

@Data
@TableMapping(sourceTable = "wp_terms", targetTable = "sort")
public class Sort {

    @ColumnMapping(sourceColumn = "term_id", targetColumn = "id")
    private BigInteger id;

    @ColumnMapping(sourceColumn = "name", targetColumn = "sort_name")
    private String name;
}
