package com.mysql.base.util;

import lombok.extern.slf4j.Slf4j;

import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.binding.MapperMethod.MethodSignature;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;

@Slf4j
public class MybatisSqlUtil {

    public static String showSql(SqlSession sqlSession, Class mapperInterface, String methodName, Object[] params) {
        Configuration configuration = sqlSession.getConfiguration();
        MappedStatement ms = configuration.getMappedStatement(mapperInterface.getName() + "." + methodName);

        Method sqlMethod = null;

        // find method equals methodName
        for (Method method : mapperInterface.getDeclaredMethods()) {
            if (method.getName().equals(methodName)) {
                sqlMethod = method;
                break;
            }
        }
        if (sqlMethod == null) {
            throw new RuntimeException("mapper method is not found");
        }

        Class<?>[] parameterTypes = sqlMethod.getParameterTypes();
        for (Class<?> parameterType : parameterTypes) {
            String name = parameterType.getName();
            try {
                // 首先获取无参构造函数
                parameterType.getConstructor();
            } catch (NoSuchMethodException e) {
                log.warn("{} 没有无参构造函数,重新获取构造函数", parameterType.getName());
                for (Constructor<?> constructor : parameterType.getConstructors()) {
                }
            }
        }

        MapperMethod.MethodSignature method = new MethodSignature(configuration, mapperInterface, sqlMethod);
        Object paramObject = method.convertArgsToSqlCommandParam(params);
        BoundSql boundSql = ms.getBoundSql(paramObject);
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
        if (!CollectionUtils.isEmpty(parameterMappings) && parameterObject != null) {
            TypeHandlerRegistry typeHandlerRegistry = configuration
                    .getTypeHandlerRegistry();
            if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                sql = sql.replaceFirst("\\?",
                        Matcher.quoteReplacement(getParameterValue(parameterObject)));
            } else {
                MetaObject metaObject = configuration.newMetaObject(parameterObject);
                for (ParameterMapping parameterMapping : parameterMappings) {
                    String propertyName = parameterMapping.getProperty();
                    if (metaObject.hasGetter(propertyName)) {
                        Object obj = metaObject.getValue(propertyName);
                        sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(obj)));
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        Object obj = boundSql.getAdditionalParameter(propertyName);
                        sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(obj)));
                    } else {
                        sql = sql.replaceFirst("\\?", "missing");
                    }
                }
            }
        }
        return sql;
    }

    /**
     * if param's type is `String`,add single quotation<br>
     *
     * if param's type is `datetime`,convert to string and quote <br>
     */
    private static String getParameterValue(Object obj) {
        String value = null;
        if (obj instanceof String) {
            value = "'" + obj.toString() + "'";
        } else if (obj instanceof Date) {
            DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
            value = "'" + formatter.format(new Date()) + "'";
        } else if (obj instanceof LocalDateTime) {
            value = "\'" + ((LocalDateTime) obj).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "\'";
        } else {
            if (obj != null) {
                value = obj.toString();
            } else {
                value = "";
            }

        }
        return value;
    }
}
