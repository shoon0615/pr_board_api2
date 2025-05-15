/**
 * packageName  : com.crud.domain.config.logger
 * fileName     : P6SpyConfig
 * author       : SangHoon
 * date         : 2025-01-07
 * description  : p6Spy 라이브러리 log 가독성 향상
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-01-07          SangHoon             최초 생성
 */
package com.crud.domain.config.logger;

import com.p6spy.engine.logging.Category;
import com.p6spy.engine.spy.P6SpyOptions;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.engine.jdbc.internal.FormatStyle;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ClassUtils;

import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Configuration
public class P6SpyConfig implements MessageFormattingStrategy {

    @PostConstruct
    public void setLogMessageFormat() {
        P6SpyOptions.getActiveInstance().setLogMessageFormat(this.getClass().getName());
    }

    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql, String url) {
        sql = formatSql(category, sql);
//        return String.format("[%s] | %d ms | \n%s %s", category, elapsed, stackTrace(), formatSql(category, sql));
        return String.format("[%s] | %d ms | %s%s %s", category, elapsed, System.lineSeparator(), stackTrace(), formatSql(category, sql));
    }

    private String stackTrace() {
        return Stream.of(new Throwable().getStackTrace())
                .filter(t -> t.toString().startsWith("com.side_project") && !t.toString().contains(
                        ClassUtils.getUserClass(this).getName()))
                .map(StackTraceElement::toString)
                .collect(Collectors.joining("\n"));
    }

    private String formatSql(String category, String sql) {
        if (sql != null && !sql.trim().isEmpty() && Category.STATEMENT.getName().equals(category)) {
            String trimmedSQL = sql.trim().toLowerCase(Locale.ROOT);
            if (trimmedSQL.startsWith("create") || trimmedSQL.startsWith("alter") || trimmedSQL.startsWith("comment")) {
                sql = FormatStyle.DDL.getFormatter().format(sql);
            } else {
                sql = FormatStyle.BASIC.getFormatter().format(sql);
            }
//            return stackTrace() + sql;
            return sql;
        }
        return sql;
    }

}