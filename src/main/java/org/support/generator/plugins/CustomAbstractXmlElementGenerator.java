package org.support.generator.plugins;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

import java.util.List;


public class CustomAbstractXmlElementGenerator extends AbstractXmlElementGenerator {

	@Override
	public void addElements(XmlElement parentElement) {
		// 增加compareParamColumns
		createInsert(parentElement);
		createInsertBatch(parentElement);
		createGetByKey(parentElement);
		createUpdate(parentElement);
		createUpdateBatch(parentElement);
		createFind(parentElement);
		createFindCount(parentElement);
		createInclude(parentElement);

	}

	private void createUpdateBatch(XmlElement parentElement) {
		String updateBatchSQL = "update " + introspectedTable.getFullyQualifiedTableNameAtRuntime() + " SET ";
		TextElement selectText = new TextElement(updateBatchSQL);
		XmlElement find = new XmlElement("update");
		find.addAttribute(new Attribute("parameterType", "java.util.List"));
		find.addAttribute(new Attribute("id", "updateBatch"));
		find.addElement(selectText);
		// 最外层XmlElement
		XmlElement trimXmlElement = new XmlElement("trim");
		trimXmlElement.addAttribute(new Attribute("prefix","set"));
		trimXmlElement.addAttribute(new Attribute("suffixOverrides",","));
		List<IntrospectedColumn> allColumnList = introspectedTable.getAllColumns();
		for (int i=0;i<allColumnList.size();i++){
			IntrospectedColumn introspectedColumn = allColumnList.get(i);
			String escapedColumnName = MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn);
			XmlElement tempXmlElement = new XmlElement("trim");
			tempXmlElement.addAttribute(new Attribute("prefix",  escapedColumnName+ " =case"));
			tempXmlElement.addAttribute(new Attribute("suffix", "end,"));
			XmlElement foreachXmlElement = new XmlElement("foreach");
			foreachXmlElement.addAttribute(new Attribute("collection", "list"));
			foreachXmlElement.addAttribute(new Attribute("item", "i"));
			foreachXmlElement.addAttribute(new Attribute("index", "index"));
			XmlElement ifXmlElement = new XmlElement("if");
			ifXmlElement.addAttribute(new Attribute("test", "i."+escapedColumnName));
			ifXmlElement.addElement(new TextElement("when id=#{id} then #i.{"+escapedColumnName+"}"));
			foreachXmlElement.addElement(ifXmlElement);
			tempXmlElement.addElement(foreachXmlElement);
			trimXmlElement.addElement(tempXmlElement);
		}
		trimXmlElement.addElement(new TextElement("where"));
		XmlElement whereForeach = new XmlElement("foreach");
		whereForeach.addAttribute(new Attribute("separator", "or"));
		whereForeach.addAttribute(new Attribute("collection", "list"));
		whereForeach.addAttribute(new Attribute("item", "i"));
		whereForeach.addAttribute(new Attribute("index", "index"));
		whereForeach.addElement(new TextElement("id=#{i.id}"));
		trimXmlElement.addElement(whereForeach);
		find.addElement(trimXmlElement);
		parentElement.addElement(find);
	}

	private void createUpdate(XmlElement parentElement) {
		// 公用select
		String sb = "update " + introspectedTable.getFullyQualifiedTableNameAtRuntime() + " SET ";
		TextElement selectText = new TextElement(sb);
		XmlElement find = new XmlElement("update");
		find.addAttribute(new Attribute("id", "update"));
//		find.addAttribute(new Attribute("parameterType", introspectedTable.getBaseRecordType()));
		find.addElement(selectText);
		int index = 0;
		for(IntrospectedColumn introspectedColumn : introspectedTable.getAllColumns()) {
			XmlElement ifElement = new XmlElement("if");
			if ("corpid".equals(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn))) {
				continue;
			}
			ifElement.addAttribute(new Attribute("test",introspectedColumn.getJavaProperty() + "!=null"));
			String str = "";
			if (index > 0) {
				str = ",";
			}
			index++;
			str += MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn) + " = #{" + introspectedColumn.getJavaProperty() + "}";
			ifElement.addElement(new TextElement(str));
			find.addElement(ifElement);
		}
		XmlElement where = new XmlElement("where");
		where.addElement(new TextElement("id = #{id}"));
		find.addElement(where);
		parentElement.addElement(find);
	}

	private void createGetByKey(XmlElement parentElement) {
		// 公用select
		String sb = "select * from " + introspectedTable.getFullyQualifiedTableNameAtRuntime();
		TextElement selectText = new TextElement(sb);

		// 公用include
		XmlElement where = new XmlElement("where");
		where.addElement(new TextElement("id = #{key}"));
		// 增加find
		XmlElement find = new XmlElement("select");
		find.addAttribute(new Attribute("id", "getByKey"));
		find.addAttribute(new Attribute("resultMap", "BaseResultMap"));
		find.addElement(selectText);
		find.addElement(where);
		parentElement.addElement(find);
	}

	private void createFindCount(XmlElement parentElement) {
		// 公用select
		String sb = "select count(1) from " +
				introspectedTable.getFullyQualifiedTableNameAtRuntime();
		TextElement selectText = new TextElement(sb);

		// 公用include
		XmlElement where = new XmlElement("where");
		XmlElement include = new XmlElement("include");
		include.addAttribute(new Attribute("refid", "compareParamColumns"));
		where.addElement(include);
		// 增加find
		XmlElement find = new XmlElement("select");
		find.addAttribute(new Attribute("id", "getEntitysCount"));
		find.addAttribute(new Attribute("resultType", "java.lang.Integer"));
//		find.addAttribute(new Attribute("parameterType", introspectedTable.getBaseRecordType()));
		find.addElement(selectText);
		find.addElement(where);
		parentElement.addElement(find);
	}

	private void createInsertBatch(XmlElement parentElement) {
		int index = 0;
		StringBuilder cloumnSb = new StringBuilder();
		StringBuilder valueSb = new StringBuilder();
		cloumnSb.append("	");
		valueSb.append("	");
		valueSb.append("(");
		for(IntrospectedColumn introspectedColumn : introspectedTable.getAllColumns()) {
			if (index > 0) {
				cloumnSb.append(",");
				valueSb.append(",");
			} else {
				index++;
			}
			cloumnSb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
			valueSb.append("#{obj.").append(introspectedColumn.getJavaProperty()).append("}");
		}
		String sb = "insert into " +
				introspectedTable.getFullyQualifiedTableNameAtRuntime() +
				"(";
		TextElement selectText = new TextElement(sb);
		XmlElement foreach = new XmlElement("foreach");
		foreach.addAttribute(new Attribute("collection","list"));
		foreach.addAttribute(new Attribute("item","obj"));
		foreach.addAttribute(new Attribute("index","index"));
		foreach.addAttribute(new Attribute("separator",","));
		valueSb.append(")");
		foreach.addElement(new TextElement(valueSb.toString()));
		XmlElement insert = new XmlElement("insert");
		insert.addAttribute(new Attribute("id","insertBatch"));
		insert.addAttribute(new Attribute("useGeneratedKeys","true"));
		insert.addAttribute(new Attribute("keyProperty","id"));
		insert.addElement(selectText);
		insert.addElement(new TextElement(cloumnSb.toString()));
		insert.addElement(new TextElement(") VALUES"));
		insert.addElement(foreach);
		parentElement.addElement(insert);
	}

	private void createInsert(XmlElement parentElement) {
		int index = 0;
		StringBuilder cloumnSb = new StringBuilder();
		StringBuilder valueSb = new StringBuilder();
		cloumnSb.append("	");
		valueSb.append("	");
		for(IntrospectedColumn introspectedColumn : introspectedTable.getAllColumns()) {
			if (index > 0) {
				cloumnSb.append(",");
				valueSb.append(",");
			} else {
				index++;
			}
			cloumnSb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
			valueSb.append("#{").append(introspectedColumn.getJavaProperty()).append("}");
		}
		String sb = "insert into " +
				introspectedTable.getFullyQualifiedTableNameAtRuntime() +
				"(";
		TextElement selectText = new TextElement(sb);
		XmlElement selectKey = new XmlElement("selectKey");
		selectKey.addAttribute(new Attribute("resultType","Long"));
		selectKey.addAttribute(new Attribute("keyProperty","id"));
		selectKey.addElement(new TextElement("SELECT LAST_INSERT_ID()"));
		XmlElement insert = new XmlElement("insert");
		insert.addAttribute(new Attribute("id","insert"));
		insert.addAttribute(new Attribute("parameterType",introspectedTable.getBaseRecordType()));
		insert.addElement(selectText);
		insert.addElement(new TextElement(cloumnSb.toString()));
		insert.addElement(new TextElement(") VALUES ("));
		insert.addElement(new TextElement(valueSb.toString()));
		insert.addElement(new TextElement(")"));
		insert.addElement(selectKey);
		parentElement.addElement(insert);
	}

	private void createFind(XmlElement parentElement) {
		// 公用select
		XmlElement choose = new XmlElement("choose");
		XmlElement when = new XmlElement("when");
		when.addElement(new TextElement("${param.columns}"));
		when.addAttribute(new Attribute("test","param.columns != null"));
		XmlElement otherwise = new XmlElement("otherwise");
		otherwise.addElement(new TextElement("*"));
		choose.addElement(when);
		choose.addElement(otherwise);
		// 公用include
		XmlElement where = new XmlElement("where");
		XmlElement include = new XmlElement("include");
		include.addAttribute(new Attribute("refid", "compareParamColumns"));
		where.addElement(include);
		// 增加find
		XmlElement find = new XmlElement("select");
		find.addAttribute(new Attribute("id", "findEntitys"));
		find.addAttribute(new Attribute("resultMap", "BaseResultMap"));
//		find.addAttribute(new Attribute("parameterType", introspectedTable.getBaseRecordType()));
		find.addElement(new TextElement("select"));
		find.addElement(choose);
		find.addElement(new TextElement("from " + introspectedTable.getFullyQualifiedTableNameAtRuntime()));
		find.addElement(where);

		XmlElement groupif = new XmlElement("if");
		groupif.addAttribute(new Attribute("test", "param.groupByStr != null"));
		groupif.addElement(new TextElement("group by ${param.groupByStr}"));

		XmlElement orderif = new XmlElement("if");
		orderif.addAttribute(new Attribute("test", "param.orderByStr != null"));
		orderif.addElement(new TextElement("order by ${param.orderByStr}"));

		XmlElement startif = new XmlElement("if");
		startif.addAttribute(new Attribute("test", "param.start!=null and param.pageNum!=null"));
		startif.addElement(new TextElement("LIMIT #{param.start},#{param.pageNum}"));

		XmlElement pageif = new XmlElement("if");
		pageif.addAttribute(new Attribute("test", "param.pageSize != null"));
		pageif.addElement(new TextElement("LIMIT #{param.pageSize}"));

		find.addElement(groupif);
		find.addElement(orderif);
		find.addElement(startif);
		find.addElement(pageif);

		parentElement.addElement(find);
	}

	private void createInclude(XmlElement parentElement) {
		XmlElement sql = new XmlElement("sql");
		sql.addAttribute(new Attribute("id", "compareParamColumns"));
		//在这里添加where条件
		StringBuilder sb = new StringBuilder();
		sql.addElement(new TextElement("1"));
		for(IntrospectedColumn introspectedColumn : introspectedTable.getAllColumns()) {
			XmlElement selectNotNullElement = new XmlElement("if"); //$NON-NLS-1$
			sb.setLength(0);
			sb.append("param.").append(introspectedColumn.getJavaProperty());
			sb.append("!=null");
			selectNotNullElement.addAttribute(new Attribute("test", sb.toString()));
			sb.setLength(0);
			// 添加and
			sb.append(" and ");
			// 添加别名t
			sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
			// 添加等号
			sb.append(" = ");
			sb.append("#{param.").append(introspectedColumn.getJavaProperty()).append("}");
			selectNotNullElement.addElement(new TextElement(sb.toString()));
			sql.addElement(selectNotNullElement);
		}
		parentElement.addElement(sql);
	}


}
