package JDBC_Connection;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import connection.URL;
import dao.Dao;
import domain.Index;

public class App {

	public static void main(String[] args) throws Exception {
		if (args.length != 4) {
			throw new IllegalArgumentException("Must be four arguments with one space between every two arguments");
		}

		URL url = new URL();

		url.setData(args[0]);
		url.setHost(args[1]);
		url.setPort(args[2]);
		url.setDatabaseName(args[3]);

		String NAME = "miscout";
		String PASSWORD = "";

		String URL = null;
		String classForname = null;
		String database = null;
		String part1 = null;
		String part2 = null;
		String part3 = null;
		String part4 = null;

		if ("sqlserver".equals(args[0])) {
			database = args[3];
			part1 = "CREATE NONCLUSTERED INDEX [";
			part2 = "] ON [dbo].[";
			part3 = "] ([";
			part4 = ") \nGO";

			// "jdbc:sqlserver://miscoutserver2015-test:1444;database=MiscoutBPU;integratedSecurity=false"
			URL = "jdbc:" + url.getData() + "://" + url.getHost() + ":" + url.getPort() + ";databace="
					+ url.getDatabaseName() + ";integratedSecurity=false";
			classForname = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

		} else if ("postgresql".equals(args[0])) {
			database = args[3];
			part1 = "CREATE INDEX ix_";
			part2 = " ON msc.";
			part3 = " USING btree (";
			part4 = ");";

			// "jdbc:postgresql://uapc1276:5432/miscout"
			URL = "jdbc:" + url.getData() + "://" + url.getHost() + ":" + url.getPort() + "/" + url.getDatabaseName();
			classForname = "org.postgresql.Driver";
		} else {
			throw new Exception("Database must be sqlserver or postgresql");
		}

		try {

			Class.forName(classForname);

			Connection conn = DriverManager.getConnection(URL, NAME, PASSWORD);

			DatabaseMetaData databaseMetadata = conn.getMetaData();

			ResultSet result = databaseMetadata.getTables(database, null, null, new String[] { "TABLE" });

			List<String> lines = new ArrayList<>();

			while (result.next()) {
				String primaryTable = result.getString(3);

				ResultSet result2 = databaseMetadata.getImportedKeys(database, null, primaryTable);

				while (result2.next()) {
					String columnName = result2.getString(8);

					ResultSet result3 = databaseMetadata.getIndexInfo(database, null, primaryTable, false, false);
					Map<String, Index> m = new HashMap<>();

					while (result3.next()) {

						String indexName = result3.getString(6);
						String referenceColumn = result3.getString(9);
						if (referenceColumn == null)
							continue;

						Dao.map(m, primaryTable, indexName, referenceColumn);
					}
					result3.close();

					boolean found = Dao.iterate(m, columnName);
					if (!found) {
						ResultSet result4 = databaseMetadata.getPrimaryKeys(database, null, primaryTable);

						Map<String, Index> mpk = new HashMap<>();

						while (result4.next()) {

							String pkName = result4.getString(6);
							String referenceColumnPK = result4.getString(4);
							if (referenceColumnPK == null)
								continue;

							Dao.map(m, primaryTable, pkName, referenceColumnPK);

						}
						result4.close();

						found = Dao.iterate(mpk, columnName);
					}

					if (!found) {

						String message = part1 + primaryTable.substring(0, Math.min(primaryTable.length(), 38)) + "_"
								+ columnName + part2 + primaryTable + part3 + columnName + part4;
						lines.add(message);
						System.out.println(message);

					}
				}
				result2.close();
			}

			Path file = Paths.get("Indexes.txt");
			Files.write(file, lines, StandardCharsets.UTF_8);

			result.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
