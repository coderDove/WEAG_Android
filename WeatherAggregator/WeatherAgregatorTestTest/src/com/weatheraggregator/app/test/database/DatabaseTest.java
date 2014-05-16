package com.weatheraggregator.app.test.database;

import android.test.ActivityInstrumentationTestCase2;

import com.weatheraggregator.activity.MainActivity_;
import com.weatheraggregator.app.test.utils.Utils;


public class DatabaseTest extends ActivityInstrumentationTestCase2<MainActivity_> {
    private static final String TEST_DATABASE_NAME = "TestDatabase.db";

    public DatabaseTest() {
        super(MainActivity_.class);
    }

    public DatabaseTest(Class<MainActivity_> activityClass) {
        super(activityClass);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        getActivity();
//		createDataBase();
    }

//	private void createDataBase()
//	{
//		ActiveAndroid.dispose();
//		Configuration config = new Configuration.Builder(getActivity()).setDatabaseName(TEST_DATABASE_NAME).create();
//		ActiveAndroid.initialize(config);
//		Utils.writeLogCat("create database for path: " + ActiveAndroid.getDatabase().getPath());
//	}
//
//	private void deleteDataBase()
//	{
//		getActivity().getApplicationContext().deleteDatabase(TEST_DATABASE_NAME);
//	}
//
//	@SmallTest
//	public void testInsertCityToDataBase()
//	{
//		Utils.writeLogCat("DatabaseTest insertCityToDataBase");
//		ActiveAndroid.beginTransaction();
//		try
//		{
//			for (int i = 0; i < 20; i++)
//			{
//				City city = new City();
//				city.setName("City test " + i);
//				city.save();
//			}
//			ActiveAndroid.setTransactionSuccessful();
//		}
//		finally
//		{
//			ActiveAndroid.endTransaction();
//		}
//	}
//
//	@Override
//	protected void tearDown() throws Exception
//	{
//		super.tearDown();
//		Utils.writeLogCat("DatabaseTest tearDown");
//		deleteDataBase();
//		Utils.writeLogCat("delete database for path: " + ActiveAndroid.getDatabase().getPath());
//	}

}
