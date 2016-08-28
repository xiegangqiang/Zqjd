package com.xysoft.support;

public class DataSourceContextHolder {
    public static final String MSQL_DATA_SOURCE = "mysqldataSource";  
    public static final String SQL_DATA_SOURCE = "sqldataSource";  
      
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();  
      
    public static void setDBType(String dbType) {  
        contextHolder.set(dbType);  
    }  
      
    public static String getDBType() {  
        return contextHolder.get();  
    }  
      
    public static void clearDBType() {  
        contextHolder.remove();  
    }  
}
