package top.hellocode.common;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

public class CodeGenerator {


    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator generator = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setAuthor("HelloCode.");
        gc.setOpen(false);
        gc.setDateType(DateType.ONLY_DATE);
        gc.setSwagger2(true);
        gc.setIdType(IdType.AUTO);
        gc.setBaseColumnList(true);
        gc.setBaseResultMap(true);
        gc.setFileOverride(true);

        generator.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://192.168.36.128:3306/yqx1?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("lh18391794828");

        generator.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent("top.hellocode");
        generator.setPackageInfo(pc);

        //策略设置
        StrategyConfig strategyConfig = new StrategyConfig();
//        strategyConfig.setTablePrefix("test_");  //设置数据库表的前缀名称，模块名 = 数据库表名 - 前缀名  例如： User = t_user - t_
        strategyConfig.setRestControllerStyle(true);    //设置是否启用Rest风格
        // strategyConfig.setVersionFieldName("version");  //设置乐观锁字段名
         strategyConfig.setLogicDeleteFieldName("del_flag");  //设置逻辑删除字段名
        strategyConfig.setEntityLombokModel(true);  //设置是否启用lombok
        strategyConfig.setColumnNaming(NamingStrategy.underline_to_camel);      // 下划线转驼峰命名
        generator.setStrategy(strategyConfig);

        generator.execute();
    }
}