package com.robaone.dbase;

import java.sql.*;

/**
 * <pre>   Copyright Mar 21, 2012 Ansel Robateau
         http://www.robaone.com

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.</pre>
 * @author Ansel Robateau
 * @version 1.0
 */
public class DBManager {

  public DBManager() {
  }
  /**
  Create a database connection to a jdbc data source
  @param driver The driver string
  @param url The url to the database
  @param username Username to authenticate to the jdbc data source
  @param password Password to authenticate to the jdbc data source
  @return The JDBC database connection.
  */
  public static Connection getConnection(String driver,String url,String username,String password) throws Exception {
    try{
      Class.forName(driver);
    }catch (Exception E){
      System.err.println("Unable to load driver.");
      E.printStackTrace();
      throw E;
    }
    try{
      Connection C = DriverManager.getConnection(url,username,password);
      return C;
    }catch(SQLException E) {
      System.err.println("SQLException: "+E.getMessage());
      E.printStackTrace();
      throw E;
    }
  }
  public static int displayResults(ResultSet rs) throws java.sql.SQLException
  {
    if (rs == null) {
      System.err.println("ERROR in displayResult(): No data in ResulSet");
      return 0;
    }

    int numCols = 0;

    ResultSetMetaData meta = rs.getMetaData();
    int cols = meta.getColumnCount();
    int[] width = new int[cols];

    // To Display column headers
    //
    boolean first=true;
    StringBuffer head = new StringBuffer();
    StringBuffer line = new StringBuffer();

    // fetch each row
    //
    while (rs.next()) {

      // get the column, and see if it matches our expectations
      //
      String text = new String();
      for (int ii=0; ii<cols; ii++) {
        String value = rs.getString(ii+1);

        if (first) {
          width[ii] = 0;
          if (value != null) width[ii] = value.length();
          if (meta.getColumnName(ii+1).length() > width[ii])
            width[ii] = meta.getColumnName(ii+1).length();

          head.append(forceToSize(meta.getColumnName(ii+1), width[ii], " "));
          head.append(" ");
          line.append(forceToSize(null, width[ii], "="));
          line.append(" ");
        }

        text += forceToSize(value, width[ii], " ");
        text += " ";   // the gap between the columns
      }

      if (first) {
        System.out.println(head.toString());
        System.out.println(line.toString());
        first = false;
      }
      System.out.println(text);
      numCols++;
    }

    return numCols;
  }
  /**
  Cut or padd the string to the given size
  @param a string
  @param size the wanted length
  @param padChar char to use for padding (must be of length()==1!)
  @return the string with correct lenght, padded with pad if necessary
  */
  public static String forceToSize(String str, int size, String padChar)
  {
    if (str != null && str.length() == size)
      return str;

    StringBuffer tmp;
    if (str == null)
      tmp = new StringBuffer(size);
    else
      tmp = new StringBuffer(str);

    if (tmp.length() > size) {
      return tmp.toString().substring(0, size);  // do cutting
    }
    else {
      // or add some padding to the end of the string
      StringBuffer pad = new StringBuffer(size);
      int numBlanks = size - tmp.length();
      for (int p = 0; p < numBlanks; p++) {
        pad.append(padChar);
      }
      return tmp.append(pad).toString();
    }
  }
  /**
  Formatted output to stdout
  @return number of tuples
  */
  public static int displayMetaData(ResultSet rs) throws java.sql.SQLException
  {
    if (rs == null) {
      System.err.println("ERROR in displayMetaData(): No data in ResulSet");
      return 0;
    }

    int numCols = 0;

    ResultSetMetaData meta = rs.getMetaData();
    int cols = meta.getColumnCount();
    System.out.println("# of Columns: "+cols);
    int[] width = new int[2];

    // To Display column headers
    //
    boolean first=true;
    StringBuffer head = new StringBuffer();
    StringBuffer line = new StringBuffer();

    String[] headers = new String[2];
    headers[0] = "Column Name";
    headers[1] = "Column Type";
    // fetch each row
    //
    for (int jj = 0; jj < meta.getColumnCount();jj++) {

      // get the column, and see if it matches our expectations
      //
      String text = new String();
      for (int ii=0; ii<2; ii++) {
        String value = "";
        if(ii == 0) value = meta.getColumnName(jj+1);
        else if( ii == 1) value = meta.getColumnTypeName(jj+1);

        if (first) {
          width[ii] = 0;
          if (value != null) width[ii] = value.length();
          if (headers[ii].length() > width[ii])
            width[ii] = headers[ii].length();

          head.append(forceToSize(headers[ii], width[ii], " "));
          head.append(" ");
          line.append(forceToSize(null, width[ii], "="));
          line.append(" ");
        }

        text += forceToSize(value, width[ii], " ");
        text += " ";   // the gap between the columns
      }

      if (first) {
        System.out.println(head.toString());
        System.out.println(line.toString());
        first = false;
      }
      System.out.println(text);
      numCols++;
    }

    return numCols;
  }
}