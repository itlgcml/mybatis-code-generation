package org.support.generator.plugins;

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.internal.types.JavaTypeResolverDefaultImpl;

import java.sql.Types;

/**
 * 数据库类型到JAVA类型映射
 * 通过标签指定，如：<javaTypeResolver type="cn.aesop.extend.resolver.MyJavaTypeResolverDefaultImpl"/>
 * @author Aesop(chao_c_c @ 163.com)
 * @date 2019/10/21 17:08
 */
public class MyJavaTypeResolverDefaultImpl extends JavaTypeResolverDefaultImpl {
    public MyJavaTypeResolverDefaultImpl() {
        super();
        //把数据库的 TINYINT 映射成 Integer
        super.typeMap.put(Types.TINYINT, new JdbcTypeInformation("TINYINT", new FullyQualifiedJavaType(Integer.class.getName())));
        super.typeMap.put(Types.DECIMAL, new JdbcTypeInformation("DECIMAL", new FullyQualifiedJavaType(Double.class.getName())));
        super.typeMap.put(Types.SMALLINT, new JdbcTypeInformation("SMALLINT", new FullyQualifiedJavaType(Integer.class.getName())));
        super.typeMap.put(12, new JdbcTypeInformation("TEXT", new FullyQualifiedJavaType(String.class.getName())));
    }
}
