package com.zyy.migrate.util;

import java.io.Closeable;
import java.io.IOException;
import java.util.Objects;

/**
 * @author yangyang.zhang
 * @date 2019/4/3 21:27
 */
public class IOUtils {

    public static void close(AutoCloseable closeable) {
        if (Objects.nonNull(closeable)) {
            try {
                closeable.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
