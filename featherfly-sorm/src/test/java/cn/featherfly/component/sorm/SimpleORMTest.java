
package cn.featherfly.component.sorm;

import java.util.List;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import cn.featherfly.common.lang.RandomUtils;
import cn.featherfly.common.lang.UUIDGenerator;
import cn.featherfly.component.sorm.vo.School;
import cn.featherfly.component.sorm.vo.Teacher;

/**
 * 
 */
public class SimpleORMTest {
    
    public static final BasicDataSource DATA_SOURCE;
    static {
        DATA_SOURCE = new BasicDataSource();
        DATA_SOURCE.setUrl("jdbc:mysql://localhost:3306/sorm");
//        DATA_SOURCE.setUrl("jdbc:mysql://192.168.1.6:3306/sorm");
        DATA_SOURCE.setDriverClassName("com.mysql.jdbc.Driver");
        DATA_SOURCE.setUsername("root");
        DATA_SOURCE.setPassword("yi");
    }
    
	public static void main(String[] args) {		
		SimpleORM<Teacher> sorm = new SimpleORM<Teacher>(Teacher.class
		        , new JdbcTemplate(SimpleORMTest.DATA_SOURCE) , "sorm");
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

class Insert {
	public static void main(String[] args) {
		SimpleORM<Teacher> sorm = new SimpleORM<Teacher>(Teacher.class
		        , new JdbcTemplate(SimpleORMTest.DATA_SOURCE) , "sorm");
			for (int i = 0; i < 100; i++) {
			Teacher t = new Teacher();
			if (i<10) {
				t.setId("id_00"+i);	
			} else if (i< 100) {
				t.setId("id_0"+i);
			} else {
				t.setId("id_"+i);
			}
			
			t.setClassName("className_" + RandomUtils.getRandomInt(90));
			School school = new School();
			school.setId("1");
			t.setSchool(school);
			sorm.save(t);
		}
	}
}
class Delete {
	public static void main(String[] args) {
		SimpleORM<Teacher> sorm = new SimpleORM<Teacher>(Teacher.class
		        , new JdbcTemplate(SimpleORMTest.DATA_SOURCE) , "sorm");		
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
class Update {
	public static void main(String[] args) {
		SimpleORM<Teacher> sorm = new SimpleORM<Teacher>(Teacher.class
		        , new JdbcTemplate(SimpleORMTest.DATA_SOURCE) , "sorm");
		List<Teacher> ts = sorm.list(" 1=1", new Object[] {});
        if (!ts.isEmpty()) {
            Teacher t = ts.get(0);
            System.out.println(t.getId());
            System.out.println(t.getClassName());
            System.out.println(t.getSchool().getId());
            t.setClassName(null);
            sorm.update(t);
        }
	}
}
class Merge {
	public static void main(String[] args) {
		SimpleORM<Teacher> sorm = new SimpleORM<Teacher>(Teacher.class
		        , new JdbcTemplate(SimpleORMTest.DATA_SOURCE) , "sorm");
		Teacher t = sorm.get("id_");
		System.out.println(t.getId());
		System.out.println(t.getClassName());
		System.out.println(t.getSchool().getId());
		t.setClassName("className_merge_" + RandomUtils.getRandomString(4));
		sorm.merge(t);
	}
}
