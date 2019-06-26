
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
public class SimpleORMTestOracle {
	public static void main(String[] args) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:oracle:thin:@192.168.1.99:1521:orcl10g");
		dataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
		dataSource.setUsername("scbmptest");
		dataSource.setPassword("scbmptest");
		SimpleORM<Teacher> sorm = new SimpleORM<Teacher>(Teacher.class, new JdbcTemplate(dataSource), "SCBMPTEST");
		String id = UUIDGenerator.generateUUID32();
		Teacher t = new Teacher();
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

class DeleteOracle {
	public static void main(String[] args) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:oracle:thin:@192.168.1.99:1521:orcl10g");
		dataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
		dataSource.setUsername("scbmptest");
		dataSource.setPassword("scbmptest");
		SimpleORM<Teacher> sorm = new SimpleORM<Teacher>(Teacher.class, new JdbcTemplate(dataSource), "SCBMPTEST");
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
		
		sorm.delete(t);
		
		t = sorm.get(id);		
		System.out.println(t);
	}
}