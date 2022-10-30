package org.support.generator.plugins;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TemplateEntity {
    /**
     * model接口包路径
     */
    private String modelPackage;

    /**
     * model接口包路径
     */
    private String importModelPackage;

    /**
     * model生成目录
     */
    private String modelCreatePackage;

    /**
     * modelImpl生成目录
     */
    private String modelImplCreatePackage;

    /**
     * modelClass
     */
    private String modelClass;

    /**
     * 接口名称
     */
    private String modelName;

    /**
     * 实体class
     */
    private String entityClass;

    /**
     * 实体路径
     */
    private String entityPackage;

    /**
     * model实现类包路径
     */
    private String modelImplPackage;

    /**
     * model实现类名称
     */
    private String modelImplName;

    /**
     * dao层路径
     */
    private String daoPackage;

    /**
     * dao class
     */
    private String daoClass;

    /**
     * dao名称
     */
    private String daoName;
}
