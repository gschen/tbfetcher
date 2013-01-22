package edu.fudan.tbfetcher.dbaccess;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.apache.log4j.Logger;

public class SQLFactory {
	private static final Logger log = Logger.getLogger(SQLFactory.class);

	public static String getInsertSQLStr(Object data, String tableName) {

		String sqlStr = "";
		String className = data.getClass().getCanonicalName();
		try {
			Class cl = Class.forName(className);
			Field[] fieldArray = cl.getDeclaredFields();
			sqlStr += "insert into ";
			String fieldListStr = tableName + "(";
			String valueListStr = "values(";
			for (int i = 0; i < fieldArray.length; i++) {
				Field field = fieldArray[i];
				if (Modifier.isStatic(field.getModifiers())) {
					continue;
				}
				field.setAccessible(true);
				Object fieldValue = field.get(data);
				if (null == fieldValue) {
					continue;
				}
				if (field.getType().equals(String.class)) {
					fieldValue = fieldValue.toString().replaceAll("'", "''");
					fieldValue = "'" + fieldValue + "'";
				}
				if (field.getType().equals(boolean.class)) {
					if (Boolean.TRUE.equals(fieldValue)) {
						fieldValue = "1";
					} else {
						fieldValue = "0";
					}
				}
				String fieldName = field.getName();
				fieldListStr += fieldName + ", ";
				valueListStr += fieldValue + ", ";
			}
			int lastIndex = fieldListStr.lastIndexOf(",");
			if (-1 != lastIndex) {
				fieldListStr = fieldListStr.substring(0, lastIndex) + ")";
			} else {
				fieldListStr += ")";
			}
			lastIndex = valueListStr.lastIndexOf(",");
			if (-1 != lastIndex) {
				valueListStr = valueListStr.substring(0, lastIndex) + ")";
			} else {
				valueListStr += ")";
			}
			sqlStr += fieldListStr + valueListStr;
		} catch (Exception e) {
			log.error("SQL Factory Error: ", e);
		}
		return sqlStr;
	}
}
