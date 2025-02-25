package Application.DAO;

import Application.Model.Author;
import Application.Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A DAO is a class that mediates the transformation of data between the format of objects in Java to rows in a
 * database. The methods here are mostly filled out, you will just need to add a SQL statement.
 *
 * We may assume that the database has already created a table named 'author'.
 * It contains similar values as the Author class:
 * id, which is of type int and is a primary key,
 * name, which is of type varchar(255).
 */
public class AuthorDAO {

    /**
     * TODO: retrieve all authors from the Author table.
     * You only need to change the sql String.
     * @return all Authors.
     */
    public List<Author> getAllAuthors(){
        Connection connection = ConnectionUtil.getConnection();
        List<Author> authors = new ArrayList<>();
        try {
            //Write SQL logic here
            String sql = "SELECT * FROM Author"; 
            // String sql = "SELECT * FROM Author WHERE id = ? AND name = ?";  // this if .setInt(1, ..) x2
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Author author = new Author(rs.getInt("id"), rs.getString("name"));
                authors.add(author);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return authors;
    }

    /**
     * TODO: insert an author into the Author table.
     * The author_id should be automatically generated by the sql database if it is not provided because it was
     * set to auto_increment. Therefore, you only need to insert a record with a single column (name).
     * You only need to change the sql String and leverage PreparedStatements' setString methods.
     */
    public Author insertAuthor(Author author){
        Connection connection = ConnectionUtil.getConnection();
        try {
//          Write SQL logic here. You should only be inserting with the name column, so that the database may
//          automatically generate a primary key.

            // "id" NOT included as it's set to AUTO_INCREMENT (SERIAL) SQL constraint -- unique # auto generated
            String sql = "INSERT INTO Author (name) VALUES (?)";    
            
            // 'PreparedStatement' interface to write SQL query b/c it pre-compiles SQL query/statement -- lower chances of SQL injection attack from user input (FE)
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            //write preparedStatement's setString method here.
            // invoke .getName() method from Author class obj to retrieve arg 'author' name field & set it to ? placeholder in SQL query above
            preparedStatement.setString(1, author.getName());

            // use .executeUpdate() PreparedStatement method to execute SQL statement & return number of rows/records affected
            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();

            // if there's more rows in 'Author' table -- aka point to next row
            if(pkeyResultSet.next()){
                // retrieve 'id' column value from SQL, type-cast to primitive type int & initialize to variable
                int generated_author_id = (int) pkeyResultSet.getLong(1);
                // insert new record/row to 'Author' table
                return new Author(generated_author_id, author.getName());
            }
            // catch any SQL Exceptions that are thrown
        }catch(SQLException e){
            // output exception code to console
            System.out.println(e.getMessage());
        }
        // otherwise return 'falsy' value null if above return does NOT prompt
        return null;
    }
}
