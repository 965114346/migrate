package com.zyy.migrate.record;

import lombok.AllArgsConstructor;
import org.easybatch.core.record.Header;
import org.easybatch.core.record.Record;

@AllArgsConstructor
public class ObjectRecord implements Record<Object> {

    private Header header;
    private Object payload;

    @Override
    public Header getHeader() {
        return header;
    }

    @Override
    public Object getPayload() {
        return payload;
    }
}
