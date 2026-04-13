package com.mysql.base.constant;

/**
 * 缓存常量类
 * 
 * @author zhang
 * @since 2026-04-13
 */
public final class CacheConstants {

    private CacheConstants() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * Student相关缓存key前缀
     */
    public static final String STUDENT_KEY_PREFIX = "student:";
    
    /**
     * Student信息缓存key
     */
    public static final String STUDENT_INFO_KEY = STUDENT_KEY_PREFIX + "info:id:";
    
    /**
     * Student列表缓存key
     */
    public static final String STUDENT_LIST_KEY = STUDENT_KEY_PREFIX + "list:";
    
    /**
     * Student缓存名称
     */
    public static final String STUDENT_CACHE_NAME = "studentCache";
    
    /**
     * 默认缓存过期时间（秒）：30分钟
     */
    public static final long DEFAULT_EXPIRE_TIME = 1800;
    
    /**
     * 空值缓存过期时间（秒）：5分钟
     */
    public static final long NULL_EXPIRE_TIME = 300;
}
