package org.support.generator.plugins;

import java.util.Set;
import java.util.TreeSet;

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.AbstractJavaMapperMethodGenerator;

public class CustomJavaMapperMethodGenerator extends AbstractJavaMapperMethodGenerator {

	@Override
	public void addInterfaceElements(Interface interfaze) {
		//原生方法的删除，都不要
		interfaze.getMethods().clear();
		//引包
		Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
		importedTypes.add(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Param"));
		importedTypes.add(new FullyQualifiedJavaType("java.util.Map"));
		importedTypes.add(new FullyQualifiedJavaType("java.util.List"));
		importedTypes.add(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Mapper"));
		importedTypes.add(new FullyQualifiedJavaType("org.springframework.stereotype.Repository"));
		importedTypes.add(new FullyQualifiedJavaType(introspectedTable.getBaseRecordType()));
		interfaze.addImportedTypes(importedTypes);
		interfaze.addAnnotation("@Mapper");
		interfaze.addAnnotation("@Repository");
		//方法新增
		addInterfaceInsert(interfaze);
		addInterfaceInsertBatch(interfaze);
		addInterfaceGetByKey(interfaze);
		addInterfaceUpdate(interfaze);
		addInterfaceFind(interfaze);
		addInterfaceFindCount(interfaze);
	}

	private void addInterfaceUpdate(Interface interfaze) {
		// 创建方法对象
		Method method = new Method();
		// 设置该方法为public
		method.setVisibility(JavaVisibility.PUBLIC);
		// 设置返回类型是List
		FullyQualifiedJavaType returnType = new FullyQualifiedJavaType("Integer");
		// 方法对象设置返回类型对象
		method.setReturnType(returnType);
		// 设置方法名称为我们在IntrospectedTable类中初始化的 “selectByObject”
		method.setName("update");

		// 设置参数类型是对象
		FullyQualifiedJavaType parameterType;
		parameterType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
		// 为方法添加参数，变量名称record
		method.addParameter(new Parameter(parameterType, "entity")); //$NON-NLS-1$
		addMapperAnnotations(interfaze, method);
		context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
		if (context.getPlugins().clientSelectByPrimaryKeyMethodGenerated(method, interfaze, introspectedTable)) {
			interfaze.addMethod(method);
		}
	}

	private void addInterfaceGetByKey(Interface interfaze) {
		// 创建方法对象
		Method method = new Method();
		// 设置该方法为public
		method.setVisibility(JavaVisibility.PUBLIC);
		// 设置返回类型是List
		FullyQualifiedJavaType returnType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
		// 方法对象设置返回类型对象
		method.setReturnType(returnType);
		// 设置方法名称为我们在IntrospectedTable类中初始化的 “selectByObject”
		method.setName("getByKey");

		// 设置参数类型是对象
		FullyQualifiedJavaType parameterType;
		parameterType = new FullyQualifiedJavaType("@Param(\"key\") Long key, @Param(\"corpid\") String corpid");
		// 为方法添加参数，变量名称record
		method.addParameter(new Parameter(parameterType, "")); //$NON-NLS-1$
		addMapperAnnotations(interfaze, method);
		context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
		if (context.getPlugins().clientSelectByPrimaryKeyMethodGenerated(method, interfaze, introspectedTable)) {
			interfaze.addMethod(method);
		}
	}

	private void addInterfaceFindCount(Interface interfaze) {
		// 创建方法对象
		Method method = new Method();
		// 设置该方法为public
		method.setVisibility(JavaVisibility.PUBLIC);
		// 设置返回类型是List
		FullyQualifiedJavaType returnType = new FullyQualifiedJavaType("Integer");
		// 方法对象设置返回类型对象
		method.setReturnType(returnType);
		// 设置方法名称为我们在IntrospectedTable类中初始化的 “selectByObject”
		method.setName("getEntitysCount");

		// 设置参数类型是对象
		FullyQualifiedJavaType parameterType;
		parameterType = new FullyQualifiedJavaType("@Param(\"param\") Map<String,Object> param");
		// 为方法添加参数，变量名称record
		method.addParameter(new Parameter(parameterType, "param")); //$NON-NLS-1$
		//
		addMapperAnnotations(interfaze, method);
		context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
		if (context.getPlugins().clientSelectByPrimaryKeyMethodGenerated(method, interfaze, introspectedTable)) {
			interfaze.addMethod(method);
		}
	}

	private void addInterfaceInsertBatch(Interface interfaze) {
		// 创建方法对象
		Method method = new Method();
		// 设置该方法为public
		method.setVisibility(JavaVisibility.PUBLIC);
		// 设置返回类型是List
		FullyQualifiedJavaType returnType = new FullyQualifiedJavaType("void");
		// 方法对象设置返回类型对象
		method.setReturnType(returnType);
		// 设置方法名称为我们在IntrospectedTable类中初始化的 “selectByObject”
		method.setName("insertBatch");

		// 设置参数类型是对象
		FullyQualifiedJavaType parameterType;
		parameterType = new FullyQualifiedJavaType("List<" + introspectedTable.getBaseRecordType() + ">");
		method.addParameter(new Parameter(parameterType, "list")); //$NON-NLS-1$
		//
		addMapperAnnotations(interfaze, method);
		context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
		if (context.getPlugins().clientSelectByPrimaryKeyMethodGenerated(method, interfaze, introspectedTable)) {
			interfaze.addMethod(method);
		}
	}

	private void addInterfaceInsert(Interface interfaze) {
		// 创建方法对象
		Method method = new Method();
		// 设置该方法为public
		method.setVisibility(JavaVisibility.PUBLIC);
		// 设置返回类型是List
		FullyQualifiedJavaType returnType = new FullyQualifiedJavaType("Long");
		// 方法对象设置返回类型对象
		method.setReturnType(returnType);
		// 设置方法名称为我们在IntrospectedTable类中初始化的 “selectByObject”
		method.setName("insert");

		// 设置参数类型是对象
		FullyQualifiedJavaType parameterType;
		parameterType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
		method.addParameter(new Parameter(parameterType, "entity")); //$NON-NLS-1$
		//
		addMapperAnnotations(interfaze, method);
		context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
		if (context.getPlugins().clientSelectByPrimaryKeyMethodGenerated(method, interfaze, introspectedTable)) {
			interfaze.addMethod(method);
		}
	}

	private void addInterfaceFind(Interface interfaze) {
		// 创建方法对象
		Method method = new Method();
		// 设置该方法为public
		method.setVisibility(JavaVisibility.PUBLIC);
		// 设置返回类型是List
		FullyQualifiedJavaType returnType = new FullyQualifiedJavaType("List<" + introspectedTable.getBaseRecordType() + ">");
		// 方法对象设置返回类型对象
		method.setReturnType(returnType);
		// 设置方法名称为我们在IntrospectedTable类中初始化的 “selectByObject”
		method.setName("findEntitys");

		// 设置参数类型是对象
		FullyQualifiedJavaType parameterType;
		parameterType = new FullyQualifiedJavaType("@Param(\"param\") Map<String,Object> param");
		// import参数类型对象
		// 为方法添加参数，变量名称record
		method.addParameter(new Parameter(parameterType, "param")); //$NON-NLS-1$
		//
		addMapperAnnotations(interfaze, method);
		context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
		if (context.getPlugins().clientSelectByPrimaryKeyMethodGenerated(method, interfaze, introspectedTable)) {
			interfaze.addMethod(method);
		}
	}
	


	public void addMapperAnnotations(Interface interfaze, Method method) {
	}
}
