package org.support.generator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.springframework.stereotype.Component;


@Component
public class MybatisGenerator {

	private static File configFile;
	private static String doMainPackage;
	private static String modelPackage;
	private static String modelImplPackage;
	static {
		String path = System.getProperty("user.dir").concat("/src/main/resources/generator/generatorConfiguration.xml");
		System.out.println(path);
		configFile = new File(path);

		Properties prop = new Properties();
		InputStream in = Object.class.getResourceAsStream("/generator/config.properties");
		try {
			prop.load(in);
			MybatisGenerator.doMainPackage = prop.getProperty("target.package").trim();
			MybatisGenerator.modelPackage = prop.getProperty("model.package").trim();
			MybatisGenerator.modelImplPackage = prop.getProperty("modelImpl.package").trim();
		} catch (IOException e) {
			System.out.println("配置文件读取错误");
		}
	}
	
	public static void main(String[] args)
			throws IOException,
					XMLParserException,
					InvalidConfigurationException,
					SQLException,
					InterruptedException {
		
		if(!configFile.exists()) {
			System.out.println("配置文件不存在");
			return;
		}
		
		createPackage();
		
		List<String> warnings = new ArrayList<String>();
        boolean overwrite = true;
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(configFile);
        //手动提供数据库连接jar包
		config.addClasspathEntry(System.getProperty("user.dir") + "/src/main/mysql-connector-java-5.1.38.jar");
        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
        myBatisGenerator.generate(null);
	}

	private static void createPackage() {
		String root = System.getProperty("user.dir")+"/src/main/java/" + doMainPackage.replaceAll("\\.","/");
		System.out.println(root);
		File rootFiles = new File(root);
		if(!rootFiles.exists()){
		   System.out.println("创建情况："+rootFiles.mkdirs());
		}else{
		   System.out.println("存在目录："+rootFiles.getAbsolutePath());
		}

		String modelRoot = System.getProperty("user.dir")+"/src/main/java/" + modelPackage.replaceAll("\\.","/");
		System.out.println(modelRoot);
		File modelRootFiles = new File(modelRoot);
		if(!modelRootFiles.exists()){
			System.out.println("创建情况："+modelRootFiles.mkdirs());
		}else{
			System.out.println("存在目录："+modelRootFiles.getAbsolutePath());
		}

		String implRoot = System.getProperty("user.dir")+"/src/main/java/" + modelImplPackage.replaceAll("\\.","/");
		System.out.println(implRoot);
		File implRootFiles = new File(implRoot);
		if(!implRootFiles.exists()){
			System.out.println("创建情况："+implRootFiles.mkdirs());
		}else{
			System.out.println("存在目录："+implRootFiles.getAbsolutePath());
		}
	}
}
