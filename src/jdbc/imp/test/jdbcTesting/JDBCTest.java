package jdbc.imp.test.jdbcTesting;

import java.io.File;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import jdbc.imp.driver.DBDriver;
import jdbc.imp.resultSet.DBResultSet;

import org.junit.Assert;
import org.junit.Test;

import java.sql.*;

import static org.junit.Assert.fail;


public class JDBCTest {
    private final String protocol = "xmldb";
    private final String tmp = System.getProperty("java.io.tmpdir");

    public static Class<?> getSpecifications() {
        return Driver.class;
    }

    private Connection createUseDatabase(final String databaseName) throws
            SQLException {
        final Driver driver = new DBDriver();
        final Properties info = new Properties();
        final File dbDir = new File(tmp + "/jdbc/" + Math.round((((float)
                Math.random()) * 100000)));
        info.put("path", dbDir.getAbsoluteFile());
        final Connection connection = driver.connect("jdbc:" + protocol +
                "://localhost", info);
        final Statement statement = connection.createStatement();
        statement.execute("CREATE DATABASE " + databaseName);
        statement.execute("USE " + databaseName);
        statement.close();
        return connection;
    }

    @Test
    public void testJDBCOne() throws SQLException {
        final Connection connection = createUseDatabase("School");
        try {
            final Statement statement = connection.createStatement();
            statement.execute("Create table Student (ID int, Name varchar, "
                    + "Grade float)");
            int count = statement.executeUpdate("INSERT INTO Student (ID, "
                    + "Name, Grade)"
                    + " VALUES (1 ,'Ahmed Khaled', 90.5)");
            Assert.assertEquals("Table Insertion did not return 1", 1, count);
            count = statement.executeUpdate("INSERT INTO Student (ID, Name, "
                    + "Grade)"
                    + " VALUES (2 ,'Ahmed El Naggar', 90.2)");
            Assert.assertEquals("Table Insertion did not return 1", 1, count);
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM"
                    + " Student");
            resultSet.next();
            Assert.assertEquals("Failed to get Correct Float Value",
                    90.5, resultSet.getFloat("Grade"), 0.0001);
        } catch (final SQLException e) {
            e.printStackTrace();
        }
        connection.close();
    }

    @Test
    public void testJDBCTwo() throws SQLException {
        final Connection connection = createUseDatabase("School");
        try {
            final Statement statement = connection.createStatement();
            statement.execute("Create table Student (ID int, Name varchar, "
                    + "Grade float)");
            int count = statement.executeUpdate("INSERT INTO Student (ID, "
                    + "Name, Grade)"
                    + " VALUES (1 ,'Ahmed Khaled', 90.5)");
            Assert.assertEquals("Table Insertion did not return 1", 1, count);
            count = statement.executeUpdate("INSERT INTO Student (ID, Name, "
                    + "Grade)"
                    + " VALUES (2 ,'Ahmed El Naggar', 90.2)");
            Assert.assertEquals("Table Insertion did not return 1", 1, count);
            count = statement.executeUpdate("INSERT INTO Student (ID, Name, "
                    + "Grade)"
                    + " VALUES (3 ,'Ahmed Walid', 90.5)");
            count = statement.executeUpdate("INSERT INTO Student (ID, Name, "
                    + "Grade)"
                    + " VALUES (4 ,'Anas Harby', 90.5)");
            final DBResultSet resultSet = (DBResultSet)
                    statement.executeQuery("SELECT * FROM Student WHERE ID > "
                            + "1");
            int numberOfMatches = 0;
            while (resultSet.next()) {
                numberOfMatches++;
            }
            Assert.assertEquals("Invalid Result Set Size", 3, numberOfMatches);
        } catch (final SQLException e) {
            e.printStackTrace();
        }
        connection.close();
    }

    @Test
    public void testJDBCThree() throws SQLException {
        final Connection connection = createUseDatabase("TestDB_Create");
        try {
            final Statement statement = connection.createStatement();
            statement
                    .execute("CREATE TABLE table_name13(column_name1 varchar,"
                            + " column_name2" +
                            " int, column_name3 varchar)");
            final int count1 = statement.executeUpdate(
                    "INSERT INTO table_name13(column_NAME1, COLUMN_name3, "
                            + "column_name2) " +
                            "VALUES ('value1', 'value3', 4)");
            Assert.assertEquals("Insert returned a number != 1", 1, count1);
            final boolean result1 = statement.execute(
                    "INSERT INTO table_name13(column_NAME1, column_name2, "
                            + "COLUMN_name3)" +
                            " VALUES ('value1', 4, 'value5')");
            Assert.assertFalse("Wrong return for insert record", result1);
            final int count3 = statement.executeUpdate(
                    "INSERT INTO table_name13(column_name1, COLUMN_NAME3, "
                            + "column_NAME2) " +
                            "VALUES ('value2', 'value4', 5)");
            Assert.assertEquals("Insert returned a number != 1", 1, count3);
            final int count4 = statement.executeUpdate(
                    "INSERT INTO table_name13(column_name1, COLUMN_NAME3, "
                            + "column_NAME2) " +
                            "VALUES ('value5', 'value6', 6)");
            Assert.assertEquals("Insert returned a number != 1", 1, count4);
            final boolean result3 = statement
                    .execute("SELECT * FROM table_name13" +
                            " ORDER BY column_name2 ASC, COLUMN_name3 DESC");
            Assert.assertTrue("Wrong return for select UNION existing "
                    + "records", result3);
            final ResultSet res2 = statement.getResultSet();

            res2.next();
            statement.close();
        } catch (final Throwable e) {
            e.printStackTrace();
        }
        connection.close();
    }

    @Test
    public void testJDBCFour() throws SQLException {
        final Connection connection = createUseDatabase("sqlDatabase");
        try {
            final Statement statement = connection.createStatement();
            statement.execute("Create table tb (ID int, Name varchar, Grade "
                    + "float, Birth date)");
            int count = statement.executeUpdate("INSERT INTO tb (ID, Name, "
                    + "Grade, birth)"
                    + " VALUES (-30, 'hello', -0.366, '2001-10-10')");
            Assert.assertEquals("Table Insertion did not return 1", 1, count);
            count = statement.executeUpdate("INSERT INTO tb"
                    + " VALUES (-2 ,'A spaced string', 101.00002, "
                    + "'0001-01-01')");
            Assert.assertEquals("Table Insertion did not return 1", 1, count);
            count = statement.executeUpdate("INSERT INTO tb"
                    + " VALUES (333 ,'a float is 003', 0.003, '8488-11-30')");
            Assert.assertEquals("Table Insertion did not return 1", 1, count);
            final ResultSet resultSet =
                    statement.executeQuery("SELECT * FROM tb WHERE birth != "
                            + "'1111-11-11'");
            int rows = 0;
            while (resultSet.next()) {
                rows++;
            }
            Assert.assertEquals("Invalid Result Set Size", 3, rows);
        } catch (final SQLException e) {
            e.printStackTrace();
        }
        connection.close();
    }

    @Test

    public void testJDBCFive() throws SQLException {

        final Connection connection = createUseDatabase("sqlDatabase");
        try {
            final Statement statement = connection.createStatement();
            statement.execute("Create table tb (ID int, Name varchar, GradE "
                    + "float, Birth date)");
            int count = statement.executeUpdate("INSERT INTO tb (ID, Name, "
                    + "GrAde, birth)"
                    + " VALUES (-30, 'hello', -0.366, '2001-10-10')");
            Assert.assertEquals("Table Insertion did not return 1", 1, count);
            count = statement.executeUpdate("INSERT INTO tb"
                    + " VALUES (-2 ,'A spaced string', 101.00002, "
                    + "'0001-01-01')");
            Assert.assertEquals("Table Insertion did not return 1", 1, count);
            count = statement.executeUpdate("INSERT INTO tb"
                    + " VALUES (333 ,'a float is .003', .003, '8488-11-30')");
            Assert.assertEquals("Table Insertion did not return 1", 1, count);
            final ResultSet resultSet = statement.executeQuery("select birth,"
                    + " gRAde, id from tb where "
                    + "birth > '0001-01-01' order by id");
            int rows = 0;
            while (resultSet.next()) {
                rows++;
            }
            Assert.assertEquals("Invalid Result Set Size", 2, rows);
            Assert.assertEquals(Types.DATE, resultSet.
                    getMetaData().getColumnType(resultSet.findColumn("BirTh")));
            Assert.assertEquals(Types.FLOAT, resultSet.
                    getMetaData().getColumnType(resultSet.findColumn("gRAdE")));
            Assert.assertEquals(Types.INTEGER, resultSet.
                    getMetaData().getColumnType(resultSet.findColumn("iD")));
            Assert.assertEquals(3, resultSet.getMetaData().getColumnCount());
            Assert.assertTrue(resultSet.getMetaData().getTableName(1)
                    .equalsIgnoreCase("Tb"));
            Assert.assertTrue(resultSet.getMetaData().
                    getColumnLabel(1).equalsIgnoreCase("birth"));
        } catch (final SQLException e) {
            e.printStackTrace();
        }
        connection.close();
    }

    @Test
    public void testInsertWithoutColumnNames() throws SQLException {
        final Connection connection = createUseDatabase("TestDB_Create");
        try {
            final Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE table_name3(column_name1 varchar,"
                    + " " +
                    "column_name2 int, column_name3 float)");
            final int count = statement.executeUpdate("INSERT INTO "
                    + "table_name3 " +
                    "VALUES ('value1', 3, 1.3)");
            Assert.assertEquals("Insert returned a number != 1", 1, count);
            statement.close();
        } catch (final Throwable e) {
            e.printStackTrace();
        }
        connection.close();
    }

    @Test
    public void testJDBCSix() throws SQLException {
        final Connection connection = createUseDatabase("TestDB_Create");
        try {
            final Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE table_name4(column_name1 varchar," +
                    " column_name2 int, column_name3 date)");
            final int count = statement.executeUpdate(
                    "INSERT INTO table_name4(column_NAME1, COLUMN_name3, "
                            + "column_name2)" +
                            " VALUES ('value1', '2011-01-25', 4)");
            Assert.assertEquals("Insert returned a number != 1", 1, count);
            statement.close();
        } catch (final Throwable e) {
            e.printStackTrace();
        }
        connection.close();
    }

    @Test
    public void testJDBCSeven() throws SQLException {
        final Connection connection = createUseDatabase("TestDB_Create");
        try {
            final Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE table_name5(column_name1 varchar,"
                    + " " +
                    "column_name2 int, column_name3 varchar)");
            statement.executeUpdate(
                    "INSERT INTO table_name5(invalid_column_name1, "
                            + "column_name3, " +
                            "column_name2) VALUES ('value1', 'value3', 4)");
            fail("Inserted with invalid column name!!");
            statement.close();
        } catch (final SQLException e) {
        } catch (final Throwable e) {
            e.printStackTrace();
        }
        connection.close();
    }

    @Test
    public void testJDBCEight() throws SQLException {
        final Connection connection = createUseDatabase("TestDB_Create");
        try {
            final Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE table_name7(column_name1 varchar,"
                    + " " +
                    "column_name2 int, column_name3 varchar)");
            final int count1 = statement.executeUpdate(
                    "INSERT INTO table_name7(column_NAME1, COLUMN_name3, "
                            + "column_name2)" +
                            " VALUES ('value1', 'value3', 4)");
            Assert.assertEquals("Insert returned a number != 1", 1, count1);
            final int count2 = statement.executeUpdate(
                    "INSERT INTO table_name7(column_NAME1, COLUMN_name3, "
                            + "column_name2)" +
                            " VALUES ('value1', 'value3', 4)");
            Assert.assertEquals("Insert returned a number != 1", 1, count2);
            final int count3 = statement.executeUpdate(
                    "INSERT INTO table_name7(column_name1, COLUMN_NAME3, "
                            + "column_NAME2)" +
                            " VALUES ('value2', 'value4', 5)");
            Assert.assertEquals("Insert returned a number != 1", 1, count3);
            final int count4 = statement.executeUpdate(
                    "UPDATE table_name7 SET column_name1='1111111111', " +
                            "COLUMN_NAME2=2222222, column_name3='333333333'");
            Assert.assertEquals("Updated returned wrong number", count1 +
                    count2 + count3, count4);
            statement.close();
        } catch (final Throwable e) {
            e.printStackTrace();
        }
        connection.close();
    }

    @Test
    public void testJDBCNine() throws SQLException {
        final Connection connection = createUseDatabase("TestDB_Create");
        try {
            final Statement statement = connection.createStatement();
            statement.execute(
                    "CREATE TABLE table_name8(column_name1 varchar, "
                            + "column_name2 int," +
                            " column_name3 date, column_name4 float)");

            final int count1 = statement.executeUpdate(
                    "INSERT INTO table_name8(column_NAME1, COLUMN_name3, "
                            + "column_name2, " +
                            "column_name4) VALUES ('value1', '2011-01-25', 3,"
                            + " 1.3)");
            Assert.assertEquals("Insert returned a number != 1", 1, count1);

            final int count2 = statement.executeUpdate(
                    "INSERT INTO table_name8(column_NAME1, COLUMN_name3, "
                            + "column_name2," +
                            " column_name4) VALUES ('value1', '2011-01-28', "
                            + "3456, 1.01)");
            Assert.assertEquals("Insert returned a number != 1", 1, count2);

            final int count3 = statement.executeUpdate(
                    "INSERT INTO table_name8(column_NAME1, COLUMN_name3, "
                            + "column_name2, " +
                            "column_name4) VALUES ('value2', '2011-02-11', "
                            + "-123, 3.14159)");
            Assert.assertEquals("Insert returned a number != 1", 1, count3);

            final int count4 = statement.executeUpdate(
                    "UPDATE table_name8 SET COLUMN_NAME2=222222, "
                            + "column_name3='1993-10-03'" +
                            " WHERE coLUmn_NAME1='value1'");
            Assert.assertEquals("Updated returned wrong number", count1 +
                    count2, count4);

            statement.close();
        } catch (final Throwable e) {
            e.printStackTrace();
        }
        connection.close();
    }

    @Test
    public void testJDBCTen() throws SQLException {
        final Connection connection = createUseDatabase("TestDB_Create");
        try {
            final Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE table_name9(column_name1 varchar,"
                    + " column_name2 int, " +
                    "column_name3 varchar)");
            final int count = statement.executeUpdate(
                    "UPDATE table_name9 SET column_name1='value1', "
                            + "column_name2=15, " +
                            "column_name3='value2'");
            Assert.assertEquals("Updated empty table retruned non-zero "
                    + "count!", 0, count);
            statement.close();
        } catch (final Throwable e) {
            e.printStackTrace();
        }

        try {
            final Statement statement = connection.createStatement();
            statement.executeUpdate(
                    "UPDATE wrong_table_name9 SET column_name1='value1'," +
                            " column_name2=15, column_name3='value2'");
            fail("Updated empty table retruned non-zero count!");
            statement.close();
        } catch (final SQLException e) {
        } catch (final Throwable e) {
            e.printStackTrace();
        }
        connection.close();
    }

    @Test
    public void testJDBCEleven() throws SQLException {
        final Connection connection = createUseDatabase("TestDB_Create");
        try {
            final Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE table_name10(column_name1 "
                    + "varchar," +
                    " column_name2 int, column_name3 date)");
            final int count1 = statement.executeUpdate(
                    "INSERT INTO table_name10(column_NAME1, COLUMN_name3, "
                            + "column_name2)" +
                            " VALUES ('value1', '2011-01-25', 4)");
            Assert.assertEquals("Insert returned a number != 1", 1, count1);
            final int count2 = statement.executeUpdate(
                    "INSERT INTO table_name10(column_NAME1, COLUMN_name3, "
                            + "column_name2) " +
                            "VALUES ('value1', '2011-01-28', 4)");
            Assert.assertEquals("Insert returned a number != 1", 1, count2);
            final int count3 = statement.executeUpdate(
                    "INSERT INTO table_name10(column_name1, COLUMN_NAME3, "
                            + "column_NAME2)" +
                            " VALUES ('value2', '2011-02-11', 5)");
            Assert.assertEquals("Insert returned a number != 1", 1, count3);
            final int count4 = statement.executeUpdate("DELETE From "
                    + "table_name10");
            Assert.assertEquals("Delete returned wrong number", 3, count4);
            statement.close();
        } catch (final Throwable e) {
            e.printStackTrace();
        }
        connection.close();
    }

    @Test
    public void testJDBCTwelve() throws SQLException {
        final Connection connection = createUseDatabase("TestDB_Create");
        try {
            final Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE table_name11(column_name1 "
                    + "varchar, " +
                    "column_name2 int, column_name3 DATE)");
            final int count1 = statement.executeUpdate(
                    "INSERT INTO table_name11(column_NAME1, COLUMN_name3, "
                            + "column_name2) " +
                            "VALUES ('value1', '2011-01-25', 4)");
            Assert.assertEquals("Insert returned a number != 1", 1, count1);
            final int count2 = statement.executeUpdate(
                    "INSERT INTO table_name11(column_NAME1, COLUMN_name3, "
                            + "column_name2)" +
                            " VALUES ('value1', '2013-06-30', 4)");
            Assert.assertEquals("Insert returned a number != 1", 1, count2);
            final int count3 = statement.executeUpdate(
                    "INSERT INTO table_name11(column_name1, COLUMN_NAME3, "
                            + "column_NAME2) " +
                            "VALUES ('value2', '2013-07-03', 5)");
            Assert.assertEquals("Insert returned a number != 1", 1, count3);
            final int count4 = statement.executeUpdate("DELETE From "
                    + "table_name11 " +
                    " WHERE coLUmn_NAME3>'2011-01-25'");
            Assert.assertEquals("Delete returned wrong number", 2, count4);
            statement.close();
        } catch (final Throwable e) {
            e.printStackTrace();
        }
        connection.close();
    }

    @Test
    public void testJDBCThirteen() throws SQLException {
        final Connection connection = createUseDatabase("TestDB_Create");
        try {
            final Statement statement = connection.createStatement();
            statement
                    .execute("CREATE TABLE table_name12(column_name1 varchar,"
                            + " " +
                            "column_name2 int, column_name3 varchar)");
            final int count1 = statement.executeUpdate(
                    "INSERT INTO table_name12(column_NAME1, COLUMN_name3, "
                            + "column_name2)" +
                            " VALUES ('value1', 'value3', 4)");
            Assert.assertEquals("Insert returned a number != 1", 1, count1);
            final int count2 = statement.executeUpdate(
                    "INSERT INTO table_name12(column_NAME1, COLUMN_name3, "
                            + "column_name2)" +
                            " VALUES ('value1', 'value3', 4)");
            Assert.assertEquals("Insert returned a number != 1", 1, count2);
            final int count3 = statement.executeUpdate(
                    "INSERT INTO table_name12(column_name1, COLUMN_NAME3, "
                            + "column_NAME2) " +
                            "VALUES ('value2', 'value4', 5)");
            Assert.assertEquals("Insert returned a number != 1", 1, count3);
            final int count4 = statement.executeUpdate(
                    "INSERT INTO table_name12(column_name1, COLUMN_NAME3, "
                            + "column_NAME2) " +
                            "VALUES ('value5', 'value6', 6)");
            Assert.assertEquals("Insert returned a number != 1", 1, count4);
            final ResultSet result = statement.executeQuery("SELECT * From "
                    + "table_name12");
            int rows = 0;
            while (result.next())
                rows++;
            Assert.assertNotNull("Null result retruned", result);
            Assert.assertEquals("Wrong number of rows", 4, rows);
            Assert.assertEquals("Wrong number of columns", 3, result
                    .getMetaData().getColumnCount());
            statement.close();
        } catch (final Throwable e) {
            e.printStackTrace();
        }
        connection.close();
    }

    @Test
    public void testJDBCFourteen() throws SQLException {
        final Connection connection = createUseDatabase("TestDB_Create");
        try {
            final Statement statement = connection.createStatement();
            statement
                    .execute("CREATE TABLE table_name13(column_name1 varchar,"
                            + " " +
                            "column_name2 int, column_name3 varchar)");
            final int count1 = statement.executeUpdate(
                    "INSERT INTO table_name13(column_NAME1, COLUMN_name3, "
                            + "column_name2)" +
                            " VALUES ('value1', 'value3', 4)");
            Assert.assertEquals("Insert returned a number != 1", 1, count1);
            final int count2 = statement.executeUpdate(
                    "INSERT INTO table_name13(column_NAME1, column_name2, "
                            + "COLUMN_name3) " +
                            "VALUES ('value1', 4, 'value3')");
            Assert.assertEquals("Insert returned a number != 1", 1, count2);
            final int count3 = statement.executeUpdate(
                    "INSERT INTO table_name13(column_name1, COLUMN_NAME3, "
                            + "column_NAME2) " +
                            "VALUES ('value2', 'value4', 5)");
            Assert.assertEquals("Insert returned a number != 1", 1, count3);
            final int count4 = statement.executeUpdate(
                    "INSERT INTO table_name13(column_name1, COLUMN_NAME3, "
                            + "column_NAME2) " +
                            "VALUES ('value5', 'value6', 6)");
            Assert.assertEquals("Insert returned a number != 1", 1, count4);
            final ResultSet result = statement.executeQuery("SELECT "
                    + "column_name1 FROM " +
                    "table_name13 WHERE coluMN_NAME2 < 5");
            int rows = 0;
            while (result.next())
                rows++;
            Assert.assertNotNull("Null result retruned", result);
            Assert.assertEquals("Wrong number of rows", 2, rows);
            Assert.assertEquals("Wrong number of columns", 1, result
                    .getMetaData().getColumnCount());
            statement.close();
        } catch (final Throwable e) {
            e.printStackTrace();
        }
        connection.close();
    }

    @Test
    public void testJDBCFifteen() throws SQLException {
        final Connection connection = createUseDatabase("TestDB_Create");
        try {
            final Statement statement = connection.createStatement();
            statement
                    .execute("CREATE TABLE table_name13(column_name1 varchar,"
                            + " column_name2 int," +
                            " column_name3 varchar)");
            final int count1 = statement.executeUpdate(
                    "INSERT INTO table_name13(column_NAME1, COLUMN_name3, "
                            + "column_name2) " +
                            "VALUES ('value1', 'value3', 4)");
            Assert.assertEquals("Insert returned a number != 1", 1, count1);
            final boolean result1 = statement.execute(
                    "INSERT INTO table_name13(column_NAME1, column_name2, "
                            + "COLUMN_name3)" +
                            " VALUES ('value1', 8, 'value3')");
            Assert.assertFalse("Wrong return from 'execute' for insert "
                    + "record", result1);
            final int count3 = statement.executeUpdate(
                    "INSERT INTO table_name13(column_name1, COLUMN_NAME3, "
                            + "column_NAME2)" +
                            " VALUES ('value2', 'value4', 5)");
            Assert.assertEquals("Insert returned a number != 1", 1, count3);
            final int count4 = statement.executeUpdate(
                    "INSERT INTO table_name13(column_name1, COLUMN_NAME3, "
                            + "column_NAME2)" +
                            " VALUES ('value5', 'value6', 6)");
            Assert.assertEquals("Insert returned a number != 1", 1, count4);

            final boolean result2 = statement.execute("SELECT column_name1 "
                    + "FROM table_name13 " +
                    "WHERE coluMN_NAME2 = 8");
            Assert.assertTrue("Wrong return for select existing records",
                    result2);

            final boolean result3 = statement.execute("SELECT column_name1 "
                    + "FROM table_name13" +
                    " WHERE coluMN_NAME2 > 100");
            Assert.assertFalse("Wrong return for select non existing "
                    + "records", result3);

            statement.close();
        } catch (final Throwable e) {
            e.printStackTrace();
        }
        connection.close();
    }

    @Test
    public void testJDBCSixteen() throws SQLException {
        final Connection connection = createUseDatabase("TestDB_Create");
        try {
            final Statement statement = connection.createStatement();
            statement
                    .execute("CREATE TABLE table_name13(column_name1 varchar,"
                            + " column_name2 " +
                            "int, column_name3 varchar)");
            final int count1 = statement.executeUpdate(
                    "INSERT INTO table_name13(column_NAME1, COLUMN_name3, "
                            + "column_name2) " +
                            "VALUES ('value1', 'value3', 4)");
            Assert.assertEquals("Insert returned a number != 1", 1, count1);
            final boolean result1 = statement.execute(
                    "INSERT INTO table_name13(column_NAME1, column_name2, "
                            + "COLUMN_name3) " +
                            "VALUES ('value1', 4, 'value5')");
            Assert.assertFalse("Wrong return for insert record", result1);
            final int count3 = statement.executeUpdate(
                    "INSERT INTO table_name13(column_name1, COLUMN_NAME3, "
                            + "column_NAME2) " +
                            "VALUES ('value2', 'value4', 5)");
            Assert.assertEquals("Insert returned a number != 1", 1, count3);
            final int count4 = statement.executeUpdate(
                    "INSERT INTO table_name13(column_name1, COLUMN_NAME3, "
                            + "column_NAME2) " +
                            "VALUES ('value5', 'value6', 6)");
            Assert.assertEquals("Insert returned a number != 1", 1, count4);

            final boolean result2 = statement.execute("SELECT DISTINCT "
                    + "column_name2 " +
                    "FROM table_name13");
            Assert.assertTrue("Wrong return for select existing records",
                    result2);
            final ResultSet res1 = statement.getResultSet();

            int rows = 0;
            while (res1.next())
                rows++;
            Assert.assertEquals("Wrong number of rows", 3, rows);

            final boolean result3 = statement
                    .execute("SELECT DISTINCT column_name2, column_name3 FROM"
                            + " " +
                            "table_name13 WHERE coluMN_NAME2 < 5");
            Assert.assertTrue("Wrong return for select existing records",
                    result3);
            final ResultSet res2 = statement.getResultSet();

            int rows2 = 0;
            while (res2.next())
                rows2++;
            Assert.assertEquals("Wrong number of rows", 2, rows2);

            statement.close();
        } catch (final Throwable e) {
            e.printStackTrace();
        }
        connection.close();
    }

    @Test
    public void testJDBCSeventeen() throws SQLException {
        final Connection connection = createUseDatabase("TestDB_Create");
        try {
            final Statement statement = connection.createStatement();
            statement
                    .execute("CREATE TABLE table_name13(column_name1 varchar,"
                            + " " +
                            "column_name2 int, column_name3 varchar)");
            final int count1 = statement.executeUpdate(
                    "INSERT INTO table_name13(column_NAME1, COLUMN_name3, "
                            + "column_name2)" +
                            " VALUES ('value1', 'value3', 4)");
            Assert.assertEquals("Insert returned a number != 1", 1, count1);
            final boolean result1 = statement.execute(
                    "INSERT INTO table_name13(column_NAME1, column_name2, "
                            + "COLUMN_name3)" +
                            " VALUES ('value1', 4, 'value5')");
            Assert.assertFalse("Wrong return for insert record", result1);
            final int count3 = statement.executeUpdate(
                    "INSERT INTO table_name13(column_name1, COLUMN_NAME3, "
                            + "column_NAME2)" +
                            " VALUES ('value2', 'value4', 5)");
            Assert.assertEquals("Insert returned a number != 1", 1, count3);
            final int count4 = statement.executeUpdate(
                    "INSERT INTO table_name13(column_name1, COLUMN_NAME3, "
                            + "column_NAME2)" +
                            " VALUES ('value5', 'value6', 6)");
            Assert.assertEquals("Insert returned a number != 1", 1, count4);

            final boolean result2 = statement.execute("ALTER TABLE "
                    + "table_name13 ADD " +
                    "column_name4 date");
            Assert.assertFalse("Wrong return for ALTER TABLE", result2);

            final boolean result3 = statement.execute("SELECT column_name4 "
                    + "FROM table_name13 " +
                    "WHERE coluMN_NAME2 = 5");
            Assert.assertTrue("Wrong return for select existing records",
                    result3);
            final ResultSet res2 = statement.getResultSet();
            int rows2 = 0;
            while (res2.next())
                rows2++;
            Assert.assertEquals("Wrong number of rows", 1, rows2);

            while (res2.previous())
                ;
            res2.next();

            Assert.assertNull("Retrieved date is not null", res2.getDate
                    ("column_name4"));

            statement.close();
        } catch (final Throwable e) {
            e.printStackTrace();
        }
        connection.close();
    }

    @Test
    public void testJDBCEighteen() throws SQLException {
        final Connection connection = createUseDatabase("TestDB_Create");
        try {
            final Statement statement = connection.createStatement();
            statement
                    .execute("CREATE TABLE table_name13(column_name1 varchar,"
                            + " column_name2 int," +
                            " column_name3 varchar)");
            final int count1 = statement.executeUpdate(
                    "INSERT INTO table_name13(column_NAME1, COLUMN_name3, "
                            + "column_name2) VALUES " +
                            "('value1', 'value3', 4)");
            Assert.assertEquals("Insert returned a number != 1", 1, count1);
            final boolean result1 = statement.execute(
                    "INSERT INTO table_name13(column_NAME1, column_name2, "
                            + "COLUMN_name3) VALUES " +
                            "('value1', 4, 'value5')");
            Assert.assertFalse("Wrong return for insert record", result1);
            final int count3 = statement.executeUpdate(
                    "INSERT INTO table_name13(column_name1, COLUMN_NAME3, "
                            + "column_NAME2) VALUES " +
                            "('value2', 'value4', 5)");
            Assert.assertEquals("Insert returned a number != 1", 1, count3);
            final int count4 = statement.executeUpdate(
                    "INSERT INTO table_name13(column_name1, COLUMN_NAME3, "
                            + "column_NAME2) VALUES " +
                            "('value5', 'value6', 6)");
            Assert.assertEquals("Insert returned a number != 1", 1, count4);

            final boolean result3 = statement
                    .execute("SELECT * FROM table_name13 ORDER BY "
                            + "column_name2 ASC, COLUMN_name3 DESC");
            Assert.assertTrue("Wrong return for select UNION existing "
                    + "records", result3);
            final ResultSet res2 = statement.getResultSet();
            int rows2 = 0;
            while (res2.next())
                rows2++;
            Assert.assertEquals("Wrong number of rows", 4, rows2);
            while (res2.previous()) ;
            res2.next();
            Assert.assertEquals("Wrong order of rows", 4, res2.getInt
                    ("column_name2"));
            Assert.assertEquals("Wrong order of rows", "value5",
                    res2.getString("column_name3"));

            res2.next();
            Assert.assertEquals("Wrong order of rows", 4, res2.getInt
                    ("column_name2"));
            Assert.assertEquals("Wrong order of rows", "value3",
                    res2.getString("column_name3"));

            res2.next();
            Assert.assertEquals("Wrong order of rows", 5, res2.getInt
                    ("column_name2"));

            statement.close();
        } catch (final Throwable e) {
            e.printStackTrace();
        }
        connection.close();
    }

    @Test
    public void testJDBCNineteen() throws SQLException {
        final Connection connection = createUseDatabase("TestDB_Create");
        try {
            final Statement statement = connection.createStatement();
            statement
                    .execute("CREATE TABLE table_name13(column_name1 varchar,"
                            + " column_name2 " +
                            "int, column_name3 varchar)");
            final int count1 = statement.executeUpdate(
                    "INSERT INTO table_name13(column_NAME1, COLUMN_name3, "
                            + "column_name2) " +
                            "VALUES ('value1', 'value3', 4)");
            Assert.assertEquals("Insert returned a number != 1", 1, count1);
            final boolean result1 = statement.execute(
                    "INSERT INTO table_name13(column_NAME1, column_name2, "
                            + "COLUMN_name3) " +
                            "VALUES ('value1', 4, 'value5')");
            Assert.assertFalse("Wrong return for insert record", result1);
            final int count3 = statement.executeUpdate(
                    "INSERT INTO table_name13(column_name1, COLUMN_NAME3, "
                            + "column_NAME2)" +
                            " VALUES ('value2', 'value4', 5)");
            Assert.assertEquals("Insert returned a number != 1", 1, count3);
            final int count4 = statement.executeUpdate(
                    "INSERT INTO table_name13(column_name1, COLUMN_NAME3, "
                            + "column_NAME2) " +
                            "VALUES ('value5', 'value6', 6)");
            Assert.assertEquals("Insert returned a number != 1", 1, count4);
            final boolean result3 = statement
                    .execute("SELECT * FROM table_name13 ORDER BY "
                            + "column_name2 ASC, COLUMN_name3 DESC");
            Assert.assertTrue("Wrong return for select UNION existing "
                    + "records", result3);
            final ResultSet res2 = statement.getResultSet();
            while (res2.next()) ;
            while (res2.previous()) ;
            Assert.assertTrue(res2.isBeforeFirst());
            res2.next();
            Assert.assertTrue(res2.isFirst());
            Assert.assertEquals("value1", res2.getString("colUmn_Name1"));
            Assert.assertEquals(4, res2.getInt("colUmn_Name2"));
            Assert.assertEquals("value5", res2.getString("colUmn_Name3"));
            res2.next();
            Assert.assertEquals("value1", res2.getString("colUmn_Name1"));
            Assert.assertEquals(4, res2.getInt("colUmn_Name2"));
            Assert.assertEquals("value3", res2.getString("colUmn_Name3"));
            res2.next();
            Assert.assertEquals("value2", res2.getString("colUmn_Name1"));
            Assert.assertEquals(5, res2.getInt("colUmn_Name2"));
            Assert.assertEquals("value4", res2.getString("colUmn_Name3"));
            res2.next();
            Assert.assertTrue(res2.isLast());
            Assert.assertEquals("value5", res2.getString("colUmn_Name1"));
            Assert.assertEquals(6, res2.getInt("colUmn_Name2"));
            Assert.assertEquals("value6", res2.getString("colUmn_Name3"));
            res2.next();
            Assert.assertTrue(res2.isAfterLast());
            while (res2.previous()) ;
            Assert.assertTrue(res2.isBeforeFirst());
            res2.next();
            Assert.assertTrue(res2.isFirst());
            Assert.assertEquals("value1", res2.getString(1));
            Assert.assertEquals(4, res2.getInt(2));
            Assert.assertEquals("value5", res2.getString(3));
            res2.next();
            Assert.assertEquals("value1", res2.getString(1));
            Assert.assertEquals(4, res2.getInt(2));
            Assert.assertEquals("value3", res2.getString(3));
            res2.next();
            Assert.assertEquals("value2", res2.getString(1));
            Assert.assertEquals(5, res2.getInt(2));
            Assert.assertEquals("value4", res2.getString(3));
            res2.next();
            Assert.assertTrue(res2.isLast());
            Assert.assertEquals("value5", res2.getString(1));
            Assert.assertEquals(6, res2.getInt(2));
            Assert.assertEquals("value6", res2.getString(3));
            res2.next();
            Assert.assertTrue(res2.isAfterLast());
            while (res2.previous()) ;
            while (res2.next()) ;
            while (res2.previous()) ;
            Assert.assertTrue(res2.isBeforeFirst());
            res2.next();
            Assert.assertTrue(res2.isFirst());
            res2.first();
            Assert.assertEquals("value1", res2.getObject(res2.findColumn
                    ("colUmn_Name1")));
            Assert.assertEquals(4, res2.getObject(res2.findColumn
                    ("colUmn_Name2")));
            Assert.assertEquals("value5", res2.getObject(res2.findColumn
                    ("colUmn_Name3")));
            res2.next();
            Assert.assertEquals("value1", res2.getObject(res2.findColumn
                    ("colUmn_Name1")));
            Assert.assertEquals(4, res2.getObject(res2.findColumn
                    ("colUmn_Name2")));
            Assert.assertEquals("value3", res2.getObject("colUmn_Name3"));
            res2.next();
            Assert.assertEquals("value2", res2.getObject("colUmn_Name1"));
            Assert.assertEquals(5, res2.getObject("colUmn_Name2"));
            Assert.assertEquals("value4", res2.getObject("colUmn_Name3"));
            res2.next();
            Assert.assertTrue(res2.isLast());
            Assert.assertEquals("value5", res2.getObject("colUmn_Name1"));
            Assert.assertEquals(6, res2.getObject("colUmn_Name2"));
            Assert.assertEquals("value6", res2.getObject("colUmn_Name3"));
            res2.next();
            Assert.assertTrue(res2.isAfterLast());
            res2.previous();
            Assert.assertEquals("value5", res2.getObject("colUmn_Name1"));
            Assert.assertEquals(6, res2.getObject("colUmn_Name2"));
            res2.absolute(1);
            Assert.assertEquals("value1", res2.getString(1));
            Assert.assertEquals(4, res2.getInt(2));
            Assert.assertEquals("value5", res2.getString(3));
            res2.next();
            Assert.assertEquals("value1", res2.getString(1));
            Assert.assertEquals(4, res2.getInt(2));
            Assert.assertEquals("value3", res2.getString(3));
            res2.next();
            Assert.assertEquals("value2", res2.getString(1));
            Assert.assertEquals(5, res2.getInt(2));
            Assert.assertEquals("value4", res2.getString(3));
            res2.next();
            Assert.assertTrue(res2.isLast());
            Assert.assertEquals("value5", res2.getString(1));
            Assert.assertEquals(6, res2.getInt(2));
            Assert.assertEquals("value6", res2.getString(3));
            res2.next();
            Assert.assertTrue(res2.isAfterLast());
            statement.close();
        } catch (final Throwable e) {
            e.printStackTrace();
        }
        connection.close();
    }

    @Test
    public void testJDBCTwenty() throws SQLException {
        final Connection connection = createUseDatabase("TestDB_Create");
        try {
            final Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE table_name1(column_name1 varchar,"
                    + " column_name2 " +
                    "int, column_name3 date)");
            statement.close();
        } catch (final Throwable e) {
            e.printStackTrace();
        }
        try {
            final Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE table_name1(column_name1 varchar,"
                    + " column_name2 int," +
                    " column_name3 date)");
            fail("Created existing table successfully!");
        } catch (final SQLException e) {

        } catch (final Throwable e) {
            e.printStackTrace();
        }

        try {
            final Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE incomplete_table_name1");
            fail("Create invalid table succeed");
        } catch (final SQLException e) {
        } catch (final Throwable e) {
            e.printStackTrace();
        }
        connection.close();
    }

    @Test
    public void testJDBCTwentyOne() throws SQLException {
        final Connection connection = createUseDatabase("School");
        try {
            final Statement statement = connection.createStatement();
            statement.execute("Create table Student (ID int, Name varchar, "
                    + "Grade float)");
            int count = statement.executeUpdate("INSERT INTO Student (ID, "
                    + "Name, Grade)"
                    + " VALUES (1 ,'Ahmed Khaled', 90.5)");
            Assert.assertEquals("Table Insertion did not return 1", 1, count);
            count = statement.executeUpdate("INSERT INTO Student (ID, Name, "
                    + "Grade)"
                    + " VALUES (2 ,'Ahmed El Naggar', 90.2)");
            Assert.assertEquals("Table Insertion did not return 1", 1, count);
            count = statement.executeUpdate("InseRT inTO Student valUES (2,"
                    + "'tolba',155.5)");
            Assert.assertEquals("Table Insertion did not return 1", 1, count);
            count = statement.executeUpdate("upDATE Student set id = 500 "
                    + "where grade > 10.5 ");
            Assert.assertEquals("Table Insertion did not return 1", 3, count);
            count = statement.executeUpdate("delETE from StUDENT wheRE grade "
                    + "< 100.0");
            Assert.assertEquals("Table Insertion did not return 1", 2, count);
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM"
                    + " Student");
            resultSet.next();
            Assert.assertEquals("Failed to get Correct Float Value",
                    155.5, resultSet.getFloat("Grade"), 0.0001);
        } catch (final SQLException e) {
            e.printStackTrace();
        }
        connection.close();
    }

    @Test
    public void testJDBCTwentyTwo() throws SQLException {
        final Connection connection = createUseDatabase("School");
        try {
            final Statement statement = connection.createStatement();
            statement.execute("Create table Student (ID int, Name varchar, "
                    + "Grade float)");
            int count = statement.executeUpdate("INSERT INTO Student (ID, "
                    + "Name, Grade)"
                    + " VALUES (1 ,'Ahmed Khaled', 90.5)");
            Assert.assertEquals("Table Insertion did not return 1", 1, count);
            count = statement.executeUpdate("INSERT INTO Student (ID, Name, "
                    + "Grade)"
                    + " VALUES (2 ,'Ahmed El Naggar', 90.2)");
            Assert.assertEquals("Table Insertion did not return 1", 1, count);
            count = statement.executeUpdate("InseRT inTO Student valUES (2,"
                    + "'tolba',155.5)");
            Assert.assertEquals("Table Insertion did not return 1", 1, count);
            count = statement.executeUpdate("InseRT inTO Student valUES (800,"
                    + "'tolba fam',125.5)");
            Assert.assertEquals("Table Insertion did not return 1", 1, count);
            count = statement.executeUpdate("InseRT inTO Student valUES (88,"
                    + "'Bars',1889.5)");
            Assert.assertEquals("Table Insertion did not return 1", 1, count);
            count = statement.executeUpdate("InseRT inTO Student valUES (2,"
                    + "'Naggor',855.5)");
            Assert.assertEquals("Table Insertion did not return 1", 1, count);
            count = statement.executeUpdate("InseRT inTO Student valUES "
                    + "(2007,' ehYaEtchy ',1577.5)");
            Assert.assertEquals("Table Insertion did not return 1", 1, count);
            count = statement.executeUpdate("InseRT inTO Student valUES "
                    + "(2828,'eh ya Barry',15588.5)");
            Assert.assertEquals("Table Insertion did not return 1", 1, count);

            count = statement.executeUpdate("upDATE Student set id = 500 "
                    + "where grade > 10.5 ");
            Assert.assertEquals("Table Insertion did not return 1", 8, count);
            count = statement.executeUpdate("delETE from StUDENT wheRE grade "
                    + "< 100.0");
            Assert.assertEquals("Table Insertion did not return 1", 2, count);
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM"
                    + " Student");
            resultSet.next();
            Assert.assertEquals("Failed to get Correct Float Value",
                    155.5, resultSet.getFloat("Grade"), 0.0001);
        } catch (final SQLException e) {
            e.printStackTrace();
        }
        connection.close();
    }

    @Test
    public void testJDBCTwentyThree() throws SQLException {
        final Connection connection = createUseDatabase("School");
        try {
            final Statement statement = connection.createStatement();
            statement.execute("Create table Student (ID int, Name varchar, "
                    + "Grade float)");
            int count = statement.executeUpdate("INSERT INTO Student (ID, "
                    + "Name, Grade)"
                    + " VALUES (1 ,'Ahmed Khaled', 90.5)");
            Assert.assertEquals("Table Insertion did not return 1", 1, count);
            count = statement.executeUpdate("INSERT INTO Student (ID, Name, "
                    + "Grade)"
                    + " VALUES (2 ,'Ahmed El Naggar', 90.2)");
            Assert.assertEquals("Table Insertion did not return 1", 1, count);
            count = statement.executeUpdate("InseRT inTO Student valUES (2,"
                    + "'tolba',155.5)");
            Assert.assertEquals("Table Insertion did not return 1", 1, count);
            count = statement.executeUpdate("InseRT inTO Student valUES (800,"
                    + "'tolba fam',125.5)");
            Assert.assertEquals("Table Insertion did not return 1", 1, count);
            count = statement.executeUpdate("InseRT inTO Student valUES (88,"
                    + "'Bars',1889.5)");
            Assert.assertEquals("Table Insertion did not return 1", 1, count);
            count = statement.executeUpdate("InseRT inTO Student valUES (2,"
                    + "'Naggor',855.5)");
            Assert.assertEquals("Table Insertion did not return 1", 1, count);
            count = statement.executeUpdate("InseRT inTO Student valUES "
                    + "(2007,' ehYaEtchy ',1577.5)");
            Assert.assertEquals("Table Insertion did not return 1", 1, count);
            count = statement.executeUpdate("InseRT inTO Student valUES "
                    + "(2828,'eh ya Barry',15588.5)");
            Assert.assertEquals("Table Insertion did not return 1", 1, count);
            count = statement.executeUpdate("InseRT inTO Student valUES "
                    + "(2828,'88282',588.5)");
            Assert.assertEquals("Table Insertion did not return 1", 1, count);
            count = statement.executeUpdate("InseRT inTO Student valUES "
                    + "(2828,'one1919',15588.5)");
            Assert.assertEquals("Table Insertion did not return 1", 1, count);
            count = statement.executeUpdate("InseRT inTO Student valUES "
                    + "(2828,'TWO202',158828.5)");
            Assert.assertEquals("Table Insertion did not return 1", 1, count);
            count = statement.executeUpdate("InseRT inTO Student valUES "
                    + "(2828,'Tree552',15992988.5)");
            Assert.assertEquals("Table Insertion did not return 1", 1, count);
            count = statement.executeUpdate("InseRT inTO Student valUES "
                    + "(2828,'fatfatfat',159222.5)");
            Assert.assertEquals("Table Insertion did not return 1", 1, count);
            count = statement.executeUpdate("InseRT inTO Student valUES "
                    + "(2828,'eh ya Barry',15588.5)");
            Assert.assertEquals("Table Insertion did not return 1", 1, count);
            count = statement.executeUpdate("InseRT inTO Student valUES "
                    + "(2828,'eh ya etchy',2016.5)");
            Assert.assertEquals("Table Insertion did not return 1", 1, count);
            count = statement.executeUpdate("upDATE Student set id = 500 "
                    + "where grade > 10.5 ");
            Assert.assertEquals("Table Insertion did not return 1", 15, count);
            count = statement.executeUpdate("delETE from StUDENT wheRE grade "
                    + "< 100.0");
            Assert.assertEquals("Table Insertion did not return 1", 2, count);
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM"
                    + " Student");
            resultSet.next();
            Assert.assertEquals("Failed to get Correct Float Value",
                    155.5, resultSet.getFloat("Grade"), 0.0001);
        } catch (final SQLException e) {
            e.printStackTrace();
        }
        connection.close();

    }


    @Test
    public void testJDBCTwentyFour() throws SQLException {
        final Connection connection = createUseDatabase("Students");
        try {
            final Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE STUDENT1 (ID INT, NAME VARCHAR ,GRADE FLOAT , BIRTH DATE )");
            int count = statement.executeUpdate("INSERT INTO Student1 (ID, "
                    + "Name, Grade)"
                    + " VALUES (1 ,'Ahmed Khaled', 90.5)");
            Assert.assertEquals("Table Insertion did not return 1", 1, count);
            count = statement.executeUpdate("INSERT INTO Student1 (ID, "
                    + "Name, Grade)"
                    + " VALUES (1 ,'Ahmed Khaled', 15.0)");
            Assert.assertEquals("Table Insertion did not return 1", 1, count);
            count = statement.executeUpdate("INSERT INTO Student1 (ID, "
                    + "Name, Grade)"
                    + " VALUES (1 ,'Ahmed NAGGAR', 27.0)");
            Assert.assertEquals("Table Insertion did not return 1", 1, count);
            count = statement.executeUpdate("InseRT inTO Student1 valUES "
                    + "(2828,'barrs',158828.5,'2001-10-05')");
            Assert.assertEquals("Table Insertion did not return 1", 1, count);
            count = statement.executeUpdate("InseRT inTO Student1 valUES "
                    + "(2828,'walid',158828.5 , '1996-12-01')");
            Assert.assertEquals("Table Insertion did not return 1", 1, count);
            count = statement.executeUpdate("upDATE Student1 set id = 10 "
                    + "where birth > '1995-10-01' ");
            Assert.assertEquals("Table Insertion did not return 1", 2, count);
            statement.execute("CREATE TABLE STUDENT2 (ID INT, NAME VARCHAR ,GRADE FLOAT , BIRTH DATE )");
            //TODO check regex for the below test.
//            count = statement.executeUpdate("INSERT INTO Student2 (ID, "
//                    + "Name, Grade)"
//                    + " VALUES (,'',)");
//            Assert.assertEquals("Table Insertion did not return 1", 1, count);
            //TODO check the date regex for accepting 0yrs 0 months and 0 days.
            count = statement.executeUpdate("INSERT INTO Student2 (ID, "
                    + "Name, Grade , birth)"
                    + " VALUES (500,'tolba', 15.0000001,'0000-03-00' )");
            Assert.assertEquals("Table Insertion did not return 1", 1, count);
            count = statement.executeUpdate("InseRT inTO Student1 valUES "
                    + "(2828,'500658478278722287822711787fcbgfuybe7298786bcryte68718',158828.5 , '1996-12-01')");
            Assert.assertEquals("Table Insertion did not return 1", 1, count);
            count = statement.executeUpdate("InseRT inTO Student1 valUES "
                    + "(2828,'500658478278722287822711787729878668718',158828.5 , '1996-12-30')");
            Assert.assertEquals("Table Insertion did not return 1", 1, count);
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM"
                    + " Student1");
            resultSet.next();
            resultSet.next();
            Assert.assertEquals("Failed to get Correct Float Value",
                    15.0, resultSet.getFloat("Grade"), 0.0001);

        } catch (final SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testJDBCTwentyFive() throws SQLException {
        final Connection connection = createUseDatabase("Students");
        try {
            final Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE STUDENT1 (ID INT, NAME VARCHAR ,GRADE FLOAT , BIRTH DATE )");
            int count = statement.executeUpdate("INSERT INTO Student1 (ID, "
                    + "Name, Grade)"
                    + " VALUES (1 ,'Ahmed Khaled', 90.5)");
            Assert.assertEquals("Table Insertion did not return 1", 1, count);
            count = statement.executeUpdate("INSERT INTO Student1 (ID, "
                    + "Name, Grade)"
                    + " VALUES (1 ,'Ahmed Khaled', 15.0)");
            Assert.assertEquals("Table Insertion did not return 1", 1, count);
            count = statement.executeUpdate("INSERT INTO Student1 (ID, "
                    + "Name, Grade)"
                    + " VALUES (1 ,'Ahmed NAGGAR', 27.0)");
            Assert.assertEquals("Table Insertion did not return 1", 1, count);
            count = statement.executeUpdate("InseRT inTO Student1 valUES "
                    + "(2828,'barrs',158828.5,'2001-10-05')");
            Assert.assertEquals("Table Insertion did not return 1", 1, count);
            count = statement.executeUpdate("InseRT inTO Student1 valUES "
                    + "(2828,'walid',158828.5 , '1996-12-01')");
            Assert.assertEquals("Table Insertion did not return 1", 1, count);
            count = statement.executeUpdate("upDATE Student1 set id = 10 "
                    + "where birth > '1995-10-01' ");
            Assert.assertEquals("Table Insertion did not return 1", 2, count);
            statement.execute("CREATE TABLE STUDENT2 (ID INT, NAME VARCHAR ,GRADE FLOAT , BIRTH DATE )");
            count = statement.executeUpdate("InseRT inTO Student1 valUES "
                    + "(2828,'500658478278722287822711787fcbgfuybe7298786bcryte68718',158828.5 , '1996-12-01')");
            Assert.assertEquals("Table Insertion did not return 1", 1, count);
            count = statement.executeUpdate("InseRT inTO Student1 valUES "
                    + "(2828,'500658478278722287822711787729878668718',1588280000000.00000000005 , '1996-12-30')");
            Assert.assertEquals("Table Insertion did not return 1", 1, count);
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM"
                    + " Student1");
            resultSet.next();
            resultSet.next();
            Assert.assertEquals("Failed to get Correct date Value",
                    null, resultSet.getDate("birth"));
            resultSet.next();
            resultSet.next();
            resultSet.next();
            resultSet.next();
            resultSet.next();
            Assert.assertEquals("Failed to get Correct float value",
                    1588280033280.00000000005,(double)resultSet.getFloat("grade"),0.0000000000001);
        } catch (final SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testJDBCTwentySix() throws SQLException {
        final Connection connection = createUseDatabase("Students");
        try {
            final Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE STUDENT1 (ID INT, NAME VARCHAR ,GRADE FLOAT , BIRTH DATE )");
            int count = statement.executeUpdate("INSERT INTO Student1 (ID, "
                    + "Name, Grade)"
                    + " VALUES (1 ,'Ahmed Khaled', 90.5)");
            Assert.assertEquals("Table Insertion did not return 1", 1, count);
            count = statement.executeUpdate("INSERT INTO Student1 (ID, "
                    + "Name, Grade)"
                    + " VALUES (1 ,'Ahmed Khaled', 15.0)");
            Assert.assertEquals("Table Insertion did not return 1", 1, count);
            count = statement.executeUpdate("INSERT INTO Student1 (ID, "
                    + "Name, Grade)"
                    + " VALUES (1 ,'Ahmed NAGGAR', 27.0)");
            Assert.assertEquals("Table Insertion did not return 1", 1, count);
            count = statement.executeUpdate("InseRT inTO Student1 valUES "
                    + "(2828,'barrs',158828.5,'2001-10-05')");
            Assert.assertEquals("Table Insertion did not return 1", 1, count);
            count = statement.executeUpdate("InseRT inTO Student1 valUES "
                    + "(2828,'walid',158828.5 , '1996-12-01')");
            Assert.assertEquals("Table Insertion did not return 1", 1, count);
            count = statement.executeUpdate("upDATE Student1 set id = 10 "
                    + "where birth > '1995-10-01' ");
            Assert.assertEquals("Table Insertion did not return 1", 2, count);
            statement.execute("CREATE TABLE STUDENT2 (ID INT, NAME VARCHAR ,GRADE FLOAT , BIRTH DATE )");
            count = statement.executeUpdate("InseRT inTO Student1 valUES "
                    + "(2828,'500658478278722287822711787fcbgfuybe7298786bcryte68718',158828.5 , '1996-12-01')");
            Assert.assertEquals("Table Insertion did not return 1", 1, count);
            count = statement.executeUpdate("InseRT inTO Student1 valUES "
                    + "(2828,'500658478278722287822711787729878668718',1588280000000.00000000005 , '1996-12-30')");
            Assert.assertEquals("Table Insertion did not return 1", 1, count);
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM"
                    + " Student1 order by birth DESC where ((birth >= '2000-01-02') or (grade >= 90.6))");

            resultSet.next();
            resultSet.next();
            Assert.assertEquals("Failed to get Correct name value",
                    "500658478278722287822711787729878668718",resultSet.getString("name"));

            final ResultSet resultSetMod = statement.executeQuery("SELECT * FROM"
                    + " Student1 order by birth DESC where ((birth >= '2000-01-02') and (id >= 10))");

        } catch (final SQLException e) {
            e.printStackTrace();
        }
    }
}