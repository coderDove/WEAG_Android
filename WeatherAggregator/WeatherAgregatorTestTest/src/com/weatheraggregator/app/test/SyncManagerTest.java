package com.weatheraggregator.app.test;

import java.util.List;

import org.mockito.Mockito;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.test.InstrumentationTestCase;
import android.test.mock.MockContext;
import android.test.suitebuilder.annotation.LargeTest;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.SmallTest;

import com.weatheraggregator.activity.MainActivity_;
import com.weatheraggregator.entity.City;
import com.weatheraggregator.entity.User;
import com.weatheraggregator.entity.WeatherService;
import com.weatheraggregator.util.SyncHelper;
import com.weatheraggregator.util.Util;
import com.weatheraggregator.webservice.ServiceManager;
import com.weatheraggregator.webservice.exception.InternetException;
import com.weatheraggregator.webservice.exception.ParseException;

public class SyncManagerTest extends InstrumentationTestCase {

    @SmallTest
    public void testSyncUserValid() throws InternetException, ParseException {
	User spyUser = Mockito.mock(User.class);

	Mockito.when(spyUser.getName()).thenReturn("androidtestuser@gmail.com");
	Mockito.when(spyUser.getObjectId()).thenReturn(
		"b56fc597-d79b-42e2-bb21-2cb8ac403432");

	Mockito.when(spyUser.save()).thenReturn(true);

	ServiceManager sm = Mockito.mock(ServiceManager.class);
	Context context = new MockContext();

	Mockito.when(sm.updateUser(context, spyUser)).thenReturn(true);

	SyncHelper syncUser = new SyncHelper(sm);
	SyncHelper spySyncUser = Mockito.spy(syncUser);

	spySyncUser.syncUserMeasure(spyUser, context);

	Mockito.verify(sm).updateUser(context, spyUser);
	Mockito.verify(spySyncUser).syncUserMeasure(spyUser, context);
    }

    @SmallTest
    public void testSyncCities() throws InternetException, ParseException {
	final List<City> cities = Mockito.mock(List.class);
	City city = Mockito.mock(City.class);
	Mockito.when(city.getLocalObjectIdByServiceObjectId()).thenReturn(
		Long.valueOf(0));
	Mockito.when(city.save(Long.valueOf(0))).thenReturn(true);
	Mockito.when(cities.get(0)).thenReturn(city);

	User spyUser = Mockito.mock(User.class);
	Mockito.when(spyUser.getObjectId()).thenReturn(
		"b56fc597-d79b-42e2-bb21-2cb8ac403432");

	Context context = new MockContext();
	ServiceManager sm = Mockito.mock(ServiceManager.class);
	Mockito.when(
		sm.updateFavoriteCity(context, cities, spyUser.getObjectId()))
		.thenReturn(true);

	SyncHelper syncUser = new SyncHelper(sm);
	SyncHelper spySyncUser = Mockito.spy(syncUser);

	spySyncUser.syncCities(cities, context, spyUser.getObjectId());

	Mockito.verify(sm).updateFavoriteCity(context, cities,
		spyUser.getObjectId());
	Mockito.verify(spySyncUser).syncCities(cities, context,
		spyUser.getObjectId());
    }

    @MediumTest
    public void testSyncServices() throws InternetException, ParseException {
	final List<WeatherService> services = Mockito.mock(List.class);
	WeatherService service = Mockito.mock(WeatherService.class);
	Mockito.when(service.getLocalObjectIdByServiceObjectId()).thenReturn(
		Long.valueOf(0));
	Mockito.when(service.save(Long.valueOf(0))).thenReturn(true);
	Mockito.when(services.get(0)).thenReturn(service);

	User spyUser = Mockito.mock(User.class);
	Mockito.when(spyUser.getObjectId()).thenReturn(
		"b56fc597-d79b-42e2-bb21-2cb8ac403432");

	Context context = new MockContext();
	ServiceManager sm = Mockito.mock(ServiceManager.class);
	Mockito.when(sm.updateService(context, services, spyUser.getObjectId()))
		.thenReturn(true);

	SyncHelper syncUser = new SyncHelper(sm);
	SyncHelper spySyncUser = Mockito.spy(syncUser);

	spySyncUser
		.syncWeatherService(services, context, spyUser.getObjectId());

	Mockito.verify(sm).updateService(context, services,
		spyUser.getObjectId());
	Mockito.verify(spySyncUser).syncWeatherService(services, context,
		spyUser.getObjectId());
    }
}
