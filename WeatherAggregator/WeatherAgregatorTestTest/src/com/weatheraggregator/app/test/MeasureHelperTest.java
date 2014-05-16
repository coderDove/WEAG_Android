package com.weatheraggregator.app.test;

import org.mockito.Mockito;

import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.LargeTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;

import com.weatheraggregator.entity.User;
import com.weatheraggregator.model.MeasureUnit;
import com.weatheraggregator.util.MeasureUnitHelper;

public class MeasureHelperTest extends InstrumentationTestCase {
    private static final String TAG = "MeasureHelperTest";

    @Override
    protected void setUp() throws Exception {
	Log.d(TAG, "setUp(): " + getName());
	super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
	Log.d(TAG, "tearDown(): " + getName());
	super.tearDown();
    }

    @SmallTest
    public void testCheckSingletone() {
	MeasureUnitHelper testSingleton = MeasureUnitHelper.getSingleton();
	assertEquals("Singleton not created", true,
		testSingleton == MeasureUnitHelper.getSingleton());
    }

    @SmallTest
    public void testConvertTemperatureFahrenheit() {
	final double FAHRENHEIT = 1.8;
	User user = Mockito.mock(User.class);
	Mockito.when(user.getTemperatureUnit()).thenReturn(
		MeasureUnit.FAHRENHEIT);
	MeasureUnitHelper.getSingleton().init(user);
	assertEquals(true,
		MeasureUnitHelper.getSingleton().convertTemperature(10)
			.doubleValue() == (10 * FAHRENHEIT));
    }

    @SmallTest
    public void testConvertTemperatureCelsius() {
	User user = Mockito.mock(User.class);
	Mockito.when(user.getTemperatureUnit()).thenReturn(MeasureUnit.CELSIUS);
	MeasureUnitHelper.getSingleton().init(user);

	assertEquals(
		true,
		MeasureUnitHelper.getSingleton().convertTemperature(null) == null);
	assertEquals(true,
		MeasureUnitHelper.getSingleton().convertTemperature(10)
			.intValue() == 10);
    }

    @SmallTest
    public void testPressureIN_HG() {
	User user = Mockito.mock(User.class);
	Mockito.when(user.getPressureUnit()).thenReturn(MeasureUnit.IN_HG);
	MeasureUnitHelper.getSingleton().init(user);
	final double PRESSURE_inHg = 25.4;
	assertEquals(true,
		MeasureUnitHelper.getSingleton().convertPressure(null) == null);
	assertEquals(true, MeasureUnitHelper.getSingleton()
		.convertPressure(5.0).doubleValue() == (5.0 * PRESSURE_inHg));
    }

    @SmallTest
    public void testPressureMM_HG() {
	User user = Mockito.mock(User.class);
	Mockito.when(user.getPressureUnit()).thenReturn(MeasureUnit.MM_HG);
	MeasureUnitHelper.getSingleton().init(user);
	final double PRESSURE_mmHg = 1;
	assertEquals(true,
		MeasureUnitHelper.getSingleton().convertPressure(null) == null);
	assertEquals(true, MeasureUnitHelper.getSingleton()
		.convertPressure(5.0).doubleValue() == (5.0 * PRESSURE_mmHg));
    }

    @SmallTest
    public void testPressureMBAR() {
	User user = Mockito.mock(User.class);
	Mockito.when(user.getPressureUnit()).thenReturn(MeasureUnit.MBAR);
	MeasureUnitHelper.getSingleton().init(user);
	final double PRESSURE_mbar = 1.33322;
	assertEquals(true,
		MeasureUnitHelper.getSingleton().convertPressure(null) == null);
	assertEquals(true, MeasureUnitHelper.getSingleton()
		.convertPressure(5.0).doubleValue() == (5.0 * PRESSURE_mbar));
    }

    @SmallTest
    public void testPressureK_PA() {
	User user = Mockito.mock(User.class);
	Mockito.when(user.getPressureUnit()).thenReturn(MeasureUnit.PA);
	MeasureUnitHelper.getSingleton().init(user);
	final double PRESSURE_kPa = 133.322368;
	assertEquals(true,
		MeasureUnitHelper.getSingleton().convertPressure(null) == null);
	assertEquals(true, MeasureUnitHelper.getSingleton()
		.convertPressure(5.0).doubleValue() == (5.0 * PRESSURE_kPa));
    }

    @SmallTest
    public void testPressurePSI() {
	User user = Mockito.mock(User.class);
	Mockito.when(user.getPressureUnit()).thenReturn(MeasureUnit.PSI);
	MeasureUnitHelper.getSingleton().init(user);
	final double PRESSURE_psi = 0.01933678;
	assertEquals(true,
		MeasureUnitHelper.getSingleton().convertPressure(null) == null);
	assertEquals(true, MeasureUnitHelper.getSingleton()
		.convertPressure(5.0).doubleValue() == (5.0 * PRESSURE_psi));
    }

    @SmallTest
    public void testSpeedM_S() {
	User user = Mockito.mock(User.class);
	Mockito.when(user.getSpeedUnit()).thenReturn(MeasureUnit.M_S);
	MeasureUnitHelper.getSingleton().init(user);
	final int WIND_METR_SEC = 1;
	assertEquals(true,
		MeasureUnitHelper.getSingleton().convertWindSpeed(null) == null);
	assertEquals(true, MeasureUnitHelper.getSingleton().convertWindSpeed(5.0)
		.doubleValue() == (5 * WIND_METR_SEC));
    }

    @SmallTest
    public void testSpeedKM_SEC() {
	User user = Mockito.mock(User.class);
	Mockito.when(user.getSpeedUnit()).thenReturn(MeasureUnit.KM_S);
	MeasureUnitHelper.getSingleton().init(user);
	final double WIND_KM_SEC = 0.001;
	assertEquals(true,
		MeasureUnitHelper.getSingleton().convertWindSpeed(null) == null);
	assertEquals(true, MeasureUnitHelper.getSingleton().convertWindSpeed(5.0)
		.doubleValue() == (5 * WIND_KM_SEC));
    }

    @SmallTest
    public void testSpeedKM_H() {
	User user = Mockito.mock(User.class);
	Mockito.when(user.getSpeedUnit()).thenReturn(MeasureUnit.KM_H);
	MeasureUnitHelper.getSingleton().init(user);
	final double WIND_KM_HOUR = 0.277778;
	assertEquals(true,
		MeasureUnitHelper.getSingleton().convertWindSpeed(null) == null);
	assertEquals(true, MeasureUnitHelper.getSingleton().convertWindSpeed(5.0)
		.doubleValue() == (5 * WIND_KM_HOUR));
    }

    @SmallTest
    public void testSpeedMI_H() {
	User user = Mockito.mock(User.class);
	Mockito.when(user.getSpeedUnit()).thenReturn(MeasureUnit.MI_H);
	MeasureUnitHelper.getSingleton().init(user);
	final double WIND_MI_H = 0.44704;
	assertEquals(true,
		MeasureUnitHelper.getSingleton().convertWindSpeed(null) == null);
	assertEquals(true, MeasureUnitHelper.getSingleton().convertWindSpeed(5.0)
		.doubleValue() == (5 * WIND_MI_H));
    }

    @SmallTest
    public void testConvertHumidity() {
	Double humidity = MeasureUnitHelper.getSingleton().convertHumidity(
		Double.valueOf(5.0));
	assertEquals(true,
		MeasureUnitHelper.getSingleton().convertHumidity(null) == null);
	assertEquals(true, humidity.doubleValue() == Double.valueOf(5.0));
    }

    @SmallTest
    public void testConvertPrecipitation() {
	Integer precipitation = MeasureUnitHelper.getSingleton()
		.convertPrecipitation(Integer.valueOf(5));
	assertEquals(true, MeasureUnitHelper.getSingleton()
		.convertPrecipitation(null) == null);
	assertEquals(true, precipitation.intValue() == Integer.valueOf(5));
    }
    
    @SmallTest
    public void testGetTempFromPosition(){
	assertEquals(MeasureUnitHelper.getSingleton().getTempFromPosition(0), MeasureUnit.CELSIUS.getCode());
	assertEquals(MeasureUnitHelper.getSingleton().getTempFromPosition(1), MeasureUnit.FAHRENHEIT.getCode());
    }
    
    @SmallTest
    public void testGetWindSpeedFromPosition(){
	assertEquals(MeasureUnitHelper.getSingleton().getWindSpeedFromPosition(0), MeasureUnit.M_S.getCode());
	assertEquals(MeasureUnitHelper.getSingleton().getWindSpeedFromPosition(1), MeasureUnit.KM_H.getCode());
	assertEquals(MeasureUnitHelper.getSingleton().getWindSpeedFromPosition(2), MeasureUnit.MI_H.getCode());
    }
    
    @SmallTest
    public void testGetPressureFromPosition(){
	assertEquals(MeasureUnitHelper.getSingleton().getPressureFromPosition(0), MeasureUnit.MM_HG.getCode());
	assertEquals(MeasureUnitHelper.getSingleton().getPressureFromPosition(1), MeasureUnit.MBAR.getCode());
	assertEquals(MeasureUnitHelper.getSingleton().getPressureFromPosition(2), MeasureUnit.PA.getCode());
	assertEquals(MeasureUnitHelper.getSingleton().getPressureFromPosition(3), MeasureUnit.IN_HG.getCode());
	assertEquals(MeasureUnitHelper.getSingleton().getPressureFromPosition(4), MeasureUnit.PSI.getCode());
    }
     
}
