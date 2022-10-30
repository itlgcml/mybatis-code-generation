package org.support.generator.plugins;

import com.alibaba.fastjson.JSONObject;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.AbstractJavaMapperMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 自定义插件
 * 增加
 * 1. find
 * 2. list
 * 3. pageList
 * 
 * 参考
 * @see org.mybatis.generator.plugins.ToStringPlugin
 * @author My
 */
public class CustomPlugin extends PluginAdapter {

	private static String modelPackage;
	private static String modelImplPackage;
	static {


		Properties prop = new Properties();
		InputStream in = Object.class.getResourceAsStream("/generator/config.properties");
		try {
			prop.load(in);
			CustomPlugin.modelPackage = prop.getProperty("model.package").trim();
			CustomPlugin.modelImplPackage = prop.getProperty("modelImpl.package").trim();
		} catch (IOException e) {
			System.out.println("配置文件读取错误");
		}
	}

	public boolean validate(List<String> warnings) {
		return true;
	}

	public TemplateEntity templateEntity = new TemplateEntity();
	
	@Override
	public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
		AbstractXmlElementGenerator elementGenerator = new CustomAbstractXmlElementGenerator();
		elementGenerator.setContext(context);
		elementGenerator.setIntrospectedTable(introspectedTable);
		XmlFilter.filterElements(document.getRootElement());
		elementGenerator.addElements(document.getRootElement());
		createModel(document,introspectedTable);

		return super.sqlMapDocumentGenerated(document, introspectedTable);
	}




	@Override
	public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		AbstractJavaMapperMethodGenerator methodGenerator = new CustomJavaMapperMethodGenerator();
		methodGenerator.setContext(context);
		methodGenerator.setIntrospectedTable(introspectedTable);
		methodGenerator.addInterfaceElements(interfaze);
		return super.clientGenerated(interfaze, topLevelClass, introspectedTable);
	}

	private void createModel(Document document, IntrospectedTable introspectedTable) {
		String table = introspectedTable.getFullyQualifiedTableNameAtRuntime();
		//系统表去除saas关键词
		if (table.contains("saas")) {
			table = table.replaceAll("saas", "");
		}
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(table);
		table = camel(stringBuffer).toString();
		String tablePrefix = table.substring(2,3).toUpperCase() + table.substring(3);
		String lowTablePrefix = table.substring(2,3).toLowerCase() + table.substring(3);

		templateEntity.setModelPackage(modelPackage);
		templateEntity.setImportModelPackage(modelPackage + "." + tablePrefix + "Model");
		templateEntity.setModelCreatePackage(context.getJavaClientGeneratorConfiguration().getTargetProject() + "/" + modelPackage.replaceAll("\\.","/") + "/");
		templateEntity.setModelImplCreatePackage(context.getJavaClientGeneratorConfiguration().getTargetProject() + "/" + modelImplPackage.replaceAll("\\.","/") + "/");
		templateEntity.setModelClass(tablePrefix + "Model");
		templateEntity.setModelName(lowTablePrefix + "Model");
		templateEntity.setDaoPackage(document.getRootElement().getAttributes().get(0).getValue());
		templateEntity.setDaoClass(tablePrefix + "Dao");
		templateEntity.setDaoName(lowTablePrefix + "Dao");
		templateEntity.setModelImplPackage(modelImplPackage);
		templateEntity.setModelImplName(tablePrefix + "ModelImpl");
		templateEntity.setEntityPackage(context.getJavaModelGeneratorConfiguration().getTargetPackage() + "." + tablePrefix + "Entity");
		templateEntity.setEntityClass(tablePrefix + "Entity");


		Configuration cfg = new Configuration();
		//指定模板所在的classpath目录
		cfg.setClassForTemplateLoading(CustomPlugin.class, "/template");
		try {
			//指定模板
			Template t = cfg.getTemplate("ModelTemplate");
			String filePath = System.getProperty("user.dir") + "/" + templateEntity.getModelCreatePackage() + templateEntity.getModelClass() + ".java";
			File file = new File(filePath);
			generateFreemarkerFile(file,t,templateEntity);

			//指定模板
			Template t2 = cfg.getTemplate("ModelImplTemplate");
			String filePath2 = System.getProperty("user.dir") + "/" + templateEntity.getModelImplCreatePackage() + templateEntity.getModelImplName() + ".java";
			File file2 = new File(filePath2);
			generateFreemarkerFile(file2,t2,templateEntity);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void generateFreemarkerFile(File file , Template t, TemplateEntity s){
		try {
			//java文件的生成目录
			FileOutputStream fos = new FileOutputStream(file);
			t.process(s, new OutputStreamWriter(fos, "utf-8"));
			fos.flush();
			fos.close();
		} catch  (IOException e) {
			e.printStackTrace();
		} catch  (TemplateException e) {
			e.printStackTrace();
		}
	}

	public static StringBuffer camel(StringBuffer str) {
		//利用正则删除下划线，把下划线后一位改成大写
		Pattern pattern = Pattern.compile("_(\\w)");
		Matcher matcher = pattern.matcher(str);
		StringBuffer sb = new StringBuffer(str);
		if(matcher.find()) {
			sb = new StringBuffer();
			//将当前匹配子串替换为指定字符串，并且将替换后的子串以及其之前到上次匹配子串之后的字符串段添加到一个StringBuffer对象里。
			//正则之前的字符和被替换的字符
			matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
			//把之后的也添加到StringBuffer对象里
			matcher.appendTail(sb);
		}else {
			return sb;
		}
		return camel(sb);
	}

}
