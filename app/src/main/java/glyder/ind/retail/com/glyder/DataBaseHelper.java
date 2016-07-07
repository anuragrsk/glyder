package glyder.ind.retail.com.glyder;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Random;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by anuraag on 6/25/16.
 */

public class DataBaseHelper extends SQLiteOpenHelper {
    private Context context;
    private static String DATABASE_NAME="GLYDER";
    public DataBaseHelper(Context con){
        super(con, DATABASE_NAME , null, 1);
        this.context=con;
    }

    public void setUp(){
        SQLiteDatabase mydatabase = context.openOrCreateDatabase("GLYDER",MODE_PRIVATE,null);
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS USER(UserName VARCHAR,Password VARCHAR,FirstName VARCHAR,LastName VARCHAR,Phone VARCHAR,Pin VARCHAR,CardNumber VARCHAR,ExpireDate VARCHAR);");
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS PRODUCT(ProductName VARCHAR,Desc VARCHAR,Price VARCHAR,BarCode VARCHAR);");

        mydatabase.execSQL("INSERT INTO USER(UserName,Password,FirstName,LastName) VALUES('anurag@yahoo.com','test1234','Anurag','Sharma');");
        mydatabase.execSQL("INSERT INTO USER(UserName,Password,FirstName,LastName) VALUES('ashish@yahoo.com','test1234','Ashish','Kumar');");

        mydatabase.execSQL("INSERT INTO PRODUCT VALUES('Pepsi','Soda can','3.25','11223344');");
        mydatabase.execSQL("INSERT INTO PRODUCT VALUES('Oreo','Biscuit','2.22','995577');");

        mydatabase.close();
    }

    public User validate(String userName,String password){
        User u =null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select FirstName,LastName from USER where UserName='"+userName+"' and Password='"+password+"';", null );
        boolean flag=false;

        flag=res.moveToFirst();
        if(flag)
            u=new User();
        while(res.moveToNext()){
            u.setFirstName(res.getString(0));
            u.setLastName(res.getString(1));
        }
        db.close();
        return u;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //setUp();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS USER");
        db.execSQL("DROP TABLE IF EXISTS PRODUCT");
        onCreate(db);

    }

  public boolean createAccount(User user){
      SQLiteDatabase db=null;
      try {

          if(isUserExist(user)){
              return false;
          }else{
              db = this.getWritableDatabase();
              db.execSQL("INSERT INTO USER(UserName ,Password ,FirstName ,LastName ,Phone ,Pin,CardNumber,ExpireDate) VALUES" +
                      "('"+user.getEmail()+"','"+user.getPassword()+"','"+user.getFirstName()+"','"+user.getLastName()+"'," +
                      "'"+user.getPhone()+"','"+user.getPin()+"','"+user.getCardNumber()+"','"+user.getExpireDate()+"');");
              return true;
          }

      }catch (Exception e){
          e.printStackTrace();
            return false;
      }
      finally {
          db.close();
      }


  }
public boolean isUserExist(User user){
    SQLiteDatabase db=null;
    boolean flag=false;
    try {
        db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from USER where UserName='"+user.getEmail()+"' OR Phone='"+user.getPhone()+"';", null );
        while(res.moveToNext()){
            flag=true;
            break;
        }
    }catch (Exception e){
        return false;
    }
    finally {
        db.close();
    }
return flag;
}

    public boolean isProductExist(String user){
        SQLiteDatabase db=null;
        boolean flag=false;
        try {
            db = this.getReadableDatabase();
            Cursor res =  db.rawQuery( "select * from PRODUCT where BarCode='"+user+"';", null );
            while(res.moveToNext()){
                flag=true;
                break;
            }
        }catch (Exception e){
            return false;
        }
        finally {
            db.close();
        }
        return flag;
    }
    public boolean createProduct(Product product){
        SQLiteDatabase db=null;
        try {

            if(isProductExist(product.getBarcode())){
                return false;
            }else{
                db = this.getWritableDatabase();
                db.execSQL("INSERT INTO PRODUCT(ProductName ,Desc,Price ,BarCode) VALUES" +
                        "('"+"Product"+"','"+"Desc"+"','"+ new Random().nextInt(20)+"','"+product.getBarcode()+"');");
                return true;
            }

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        finally {
            db.close();
        }


    }

    public Product getProduct(String barcode){
        SQLiteDatabase db=null;
        Product u=null;
        boolean flag=false;
        try {
            db = this.getReadableDatabase();
            Cursor res =  db.rawQuery( "select ProductName ,Price ,BarCode from PRODUCT where BarCode='"+barcode+"';", null );


            while(res.moveToNext()){
                u=new Product();
                flag=true;
                u.setName(res.getString(0));
                u.setPrice(res.getString(1));
                u.setBarcode(res.getString(2));
            }
            db.close();
        }catch (Exception e){
            return null;
        }
        finally {
            db.close();
        }
        return u;
    }
}
