
package cn.featherfly.component.sorm;

import java.sql.Types;

import org.apache.commons.dbcp.BasicDataSource;

import cn.featherfly.common.db.metadata.DatabaseMetadata;
import cn.featherfly.common.db.metadata.DatabaseMetadataManager;

/**
 * 
 */
public class MetadataTestOracle {
	public static void main(String[] args) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:oracle:thin:@192.168.1.99:1521:orcl10g");
		dataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
		dataSource.setUsername("bmp");
		dataSource.setPassword("bmp");
		DatabaseMetadata meta = DatabaseMetadataManager.getDefaultManager().create(dataSource, "BMP");
		System.out.println(meta.getTable("SYS_USER").getColumn("CREATE_TIME").getType());
		System.out.println(Types.DATE);
	}
}
