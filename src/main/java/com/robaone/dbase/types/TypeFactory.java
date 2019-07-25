package com.robaone.dbase.types;

import com.robaone.dbase.*;

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
 * @author Ansel
 *
 */

public class TypeFactory {
  public static int STRING = 0;
  public static int BIGDECIMAL = 1;
  public static int BOOLEAN = 2;
  public static int INTEGER = 3;
  public static int INT = 3;
  public static int LONG = 4;
  public static int FLOAT = 5;
  public static int DOUBLE = 6;
  public static int DATE = 7;
  public static int TIME = 8;
  public static int TIMESTAMP = 9;
  public static int OBJECT = 10;
  public static int CLOB = 11;
  public static int BLOB = 12;
  public static int ARRAY = 13;
  public static int STRUCT = 14;
  public static int REF = 15;

  public TypeFactory(){}
  public static DBType getType(String javatype) throws DBSQLValidationException{
    if(javatype.equals("String")){
      return TypeFactory.getType(0);
    }else if(javatype.equals("BigDecimal")){
      return TypeFactory.getType(1);
    }else if(javatype.equals("Boolean")){
      return TypeFactory.getType(2);
    }else if(javatype.equals("Integer")){
      return TypeFactory.getType(3);
    }else if(javatype.equals("Long")){
      return TypeFactory.getType(4);
    }else if(javatype.equals("Float")){
      return TypeFactory.getType(5);
    }else if(javatype.equals("Double")){
      return TypeFactory.getType(6);
    }else if(javatype.equals("Date")){
      return TypeFactory.getType(7);
    }else if(javatype.equals("Time")){
      return TypeFactory.getType(8);
    }else if(javatype.equals("Timestamp")){
      return TypeFactory.getType(9);
    }else if(javatype.equals("Object")){
      return TypeFactory.getType(10);
    }else if(javatype.equals("Clob")){
      return TypeFactory.getType(11);
    }else if(javatype.equals("Blob")){
      return TypeFactory.getType(12);
    }else if(javatype.equals("Array")){
      return TypeFactory.getType(13);
    }else if(javatype.equals("Struct")){
      return TypeFactory.getType(14);
    }else if(javatype.equals("Ref")){
      return TypeFactory.getType(15);
    }
    throw new DBSQLValidationException("Type \""+javatype+"\" not found");
  }
  public static DBType getType(int javatype) throws DBSQLValidationException{
    switch (javatype){
      case 0:
        return (DBType)new StringType();
      case 1:
        return (DBType)new BigDecimalType();
      case 2:
        return (DBType)new BooleanType();
      case 3:
        return (DBType)new IntegerType();
      case 4:
        return (DBType)new LongType();
      case 5:
        return (DBType)new FloatType();
      case 6:
        return (DBType)new DoubleType();
      case 7:
        return (DBType)new DateType();
      case 8:
        return (DBType)new TimeType();
      case 9:
        return (DBType)new TimestampType();
      case 10:
        //return (DBType)new ObjectType();
      case 11:
        //return (DBType)new ClobType();
      case 12:
        //return (DBType)new BlobType();
      case 13:
        //return (DBType)new ArrayType();
      case 14:
        //return (DBType)new StructType();
      case 15:
        //return (DBType)new RefType();
    }
    throw new DBSQLValidationException("Cannot find type "+javatype);
  }
}