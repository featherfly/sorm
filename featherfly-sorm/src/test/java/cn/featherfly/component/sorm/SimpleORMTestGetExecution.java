
package cn.featherfly.component.sorm;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import cn.featherfly.common.lang.RandomUtils;
import cn.featherfly.common.lang.UUIDGenerator;
import cn.featherfly.component.sorm.vo.School;
import cn.featherfly.component.sorm.vo.Teacher;

/**
 * 
 */
public class SimpleORMTestGetExecution {
    
    @org.testng.annotations.Test
    public void test() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/sorm");
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUsername("root");
        dataSource.setPassword("yi");
        SimpleORM<Teacher> sorm = new SimpleORM<Teacher>(Teacher.class, new JdbcTemplate(dataSource) , "sorm");
        Teacher t = new Teacher();
        String id = UUIDGenerator.generateUUID32(); 
        t.setId(id);
        t.setClassName("className_" + RandomUtils.getRandomInt(90));
        School school = new School();
        school.setId("1");
        t.setSchool(school);
        sorm.save(t);
        
        t = sorm.get(id);
        System.out.println(t.getId());
        System.out.println(t.getClassName());
        System.out.println(t.getSchool().getId());
        t.setClassName("className_" + RandomUtils.getRandomInt(1000));
        sorm.update(t);
    }
}
