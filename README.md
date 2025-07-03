In your module's (app's) build.gradle.kts file, include the following:

dependencies {
    implementation("androidx.room:room-runtime:2.7.2")
    annotationProcessor("androidx.room:room-compiler:2.7.2")
}

Note: Choose only one of ksp or annotationProcessor. Don't include both.

Room Components

Room is made up of three major components:

1.  Entity: A table in the database that is represented by an entity.
2.  DAO: This class contains the methods for accessing the database.
3.  Database: This contains the database holder and serves as the primary access point for your app's persisted, relational data.

Entity:

@Entity(tableName = "expense")
public class Expense {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "amount")
    private String amount;

    Expense(int id, String title, String amount)
    {
        this.id = id;
        this.amount = amount;
        this.title = title;
    }

    @Ignore
    Expense(String title, String amount)
    {
        this.amount = amount;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}

DAO (interface):

@Dao
public interface ExpenseDao {

    @Query("SELECT * FROM expense")
    List<Expense> getAllExpenses();

    @Insert
    void addTx(Expense expense);

    @Update
    void updateTx(Expense expense);

    @Delete
    void deleteTx(Expense expense);

}

Database Helper Class (abstract):

@Database(entities = Expense.class, exportSchema = false, version = 1)
public abstract class DatabaseHelper extends RoomDatabase {

    private static final String DB_NAME = "expenseDB";
    private static DatabaseHelper instance;

    public static synchronized DatabaseHelper getDB(Context context)
    {
        if(instance == null)
        {
            instance = Room.databaseBuilder(context, DatabaseHelper.class, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public abstract ExpenseDao expenseDao();

}

MainActivity.java

        DatabaseHelper databaseHelper = DatabaseHelper.getDB(this);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titleText = title.getText().toString();
                String amountText = amount.getText().toString();

                databaseHelper.expenseDao().addTx(new Expense(titleText, amountText));

                title.setText("");
                amount.setText("");

                ArrayList<Expense> data = (ArrayList<Expense>) databaseHelper.expenseDao().getAllExpenses();
                for(int i = 0; i < data.size(); i++)
                {
                    Log.d("Data : ", "Id: " + data.get(i).getId() + " Title: " +
                            data.get(i).getTitle() + " Amount: " + data.get(i).getAmount());
                }
            }
        });

