/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.adaptive.media.image.internal.test;

import com.liferay.adaptive.media.AdaptiveMediaImageConfigurationException;
import com.liferay.adaptive.media.image.configuration.AdaptiveMediaImageConfigurationEntry;
import com.liferay.adaptive.media.image.configuration.AdaptiveMediaImageConfigurationHelper;
import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.Sync;
import com.liferay.portal.kernel.test.rule.SynchronousDestinationTestRule;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Sergio González
 */
@RunWith(Arquillian.class)
@Sync
public class AdaptiveMediaImageAddConfigurationTest
	extends BaseAdaptiveMediaImageConfigurationTestCase {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			SynchronousDestinationTestRule.INSTANCE);

	@Test(
		expected = AdaptiveMediaImageConfigurationException.InvalidNameException.class
	)
	public void testAddConfigurationEntryWithBlankName() throws Exception {
		AdaptiveMediaImageConfigurationHelper configurationHelper =
			serviceTracker.getService();

		Map<String, String> properties = new HashMap<>();

		properties.put("max-height", "100");
		properties.put("max-width", "100");

		configurationHelper.addAdaptiveMediaImageConfigurationEntry(
			TestPropsValues.getCompanyId(), StringPool.BLANK, "1", properties);
	}

	@Test(
		expected = AdaptiveMediaImageConfigurationException.InvalidUuidException.class
	)
	public void testAddConfigurationEntryWithBlankUuid() throws Exception {
		AdaptiveMediaImageConfigurationHelper configurationHelper =
			serviceTracker.getService();

		Map<String, String> properties = new HashMap<>();

		properties.put("max-height", "100");
		properties.put("max-width", "100");

		configurationHelper.addAdaptiveMediaImageConfigurationEntry(
			TestPropsValues.getCompanyId(), "one", StringPool.BLANK,
			properties);
	}

	@Test
	public void testAddConfigurationEntryWithExistingDisabledConfiguration()
		throws Exception {

		AdaptiveMediaImageConfigurationHelper configurationHelper =
			serviceTracker.getService();

		Map<String, String> properties = new HashMap<>();

		properties.put("max-height", "100");
		properties.put("max-width", "100");

		configurationHelper.addAdaptiveMediaImageConfigurationEntry(
			TestPropsValues.getCompanyId(), "one", "1", properties);

		configurationHelper.disableAdaptiveMediaImageConfigurationEntry(
			TestPropsValues.getCompanyId(), "1");

		properties = new HashMap<>();

		properties.put("max-height", "200");
		properties.put("max-width", "200");

		configurationHelper.addAdaptiveMediaImageConfigurationEntry(
			TestPropsValues.getCompanyId(), "two", "2", properties);

		Collection<AdaptiveMediaImageConfigurationEntry> configurationEntries =
			configurationHelper.getAdaptiveMediaImageConfigurationEntries(
				TestPropsValues.getCompanyId(), configurationEntry -> true);

		Assert.assertEquals(
			configurationEntries.toString(), 2, configurationEntries.size());

		Iterator<AdaptiveMediaImageConfigurationEntry> iterator =
			configurationEntries.iterator();

		AdaptiveMediaImageConfigurationEntry configurationEntry =
			iterator.next();

		Assert.assertEquals("1", configurationEntry.getUUID());

		configurationEntry = iterator.next();

		Assert.assertEquals("2", configurationEntry.getUUID());
	}

	@Test
	public void testAddConfigurationEntryWithMaxHeightOnly() throws Exception {
		AdaptiveMediaImageConfigurationHelper configurationHelper =
			serviceTracker.getService();

		Map<String, String> properties = new HashMap<>();

		properties.put("max-height", "100");

		configurationHelper.addAdaptiveMediaImageConfigurationEntry(
			TestPropsValues.getCompanyId(), "one", "1", properties);

		Optional<AdaptiveMediaImageConfigurationEntry>
			configurationEntryOptional =
				configurationHelper.getAdaptiveMediaImageConfigurationEntry(
					TestPropsValues.getCompanyId(), "1");

		Assert.assertTrue(configurationEntryOptional.isPresent());

		AdaptiveMediaImageConfigurationEntry configurationEntry =
			configurationEntryOptional.get();

		Map<String, String> actualProperties =
			configurationEntry.getProperties();

		Assert.assertEquals("100", actualProperties.get("max-height"));
		Assert.assertNull(actualProperties.get("max-width"));
	}

	@Test
	public void testAddConfigurationEntryWithMaxWidthOnly() throws Exception {
		AdaptiveMediaImageConfigurationHelper configurationHelper =
			serviceTracker.getService();

		Map<String, String> properties = new HashMap<>();

		properties.put("max-width", "100");

		configurationHelper.addAdaptiveMediaImageConfigurationEntry(
			TestPropsValues.getCompanyId(), "one", "1", properties);

		Optional<AdaptiveMediaImageConfigurationEntry>
			configurationEntryOptional =
				configurationHelper.getAdaptiveMediaImageConfigurationEntry(
					TestPropsValues.getCompanyId(), "1");

		Assert.assertTrue(configurationEntryOptional.isPresent());

		AdaptiveMediaImageConfigurationEntry configurationEntry =
			configurationEntryOptional.get();

		Map<String, String> actualProperties =
			configurationEntry.getProperties();

		Assert.assertNull(actualProperties.get("max-height"));
		Assert.assertEquals("100", actualProperties.get("max-width"));
	}

	@Test(
		expected = AdaptiveMediaImageConfigurationException.InvalidHeightException.class
	)
	public void testAddConfigurationEntryWithNegativeNumberMaxHeight()
		throws Exception {

		AdaptiveMediaImageConfigurationHelper configurationHelper =
			serviceTracker.getService();

		Map<String, String> properties = new HashMap<>();

		properties.put("max-height", "-10");

		configurationHelper.addAdaptiveMediaImageConfigurationEntry(
			TestPropsValues.getCompanyId(), "one", "1", properties);
	}

	@Test(
		expected = AdaptiveMediaImageConfigurationException.InvalidWidthException.class
	)
	public void testAddConfigurationEntryWithNegativeNumberMaxWidth()
		throws Exception {

		AdaptiveMediaImageConfigurationHelper configurationHelper =
			serviceTracker.getService();

		Map<String, String> properties = new HashMap<>();

		properties.put("max-width", "-10");

		configurationHelper.addAdaptiveMediaImageConfigurationEntry(
			TestPropsValues.getCompanyId(), "one", "1", properties);
	}

	@Test(
		expected = AdaptiveMediaImageConfigurationException.InvalidHeightException.class
	)
	public void testAddConfigurationEntryWithNotNumberMaxHeight()
		throws Exception {

		AdaptiveMediaImageConfigurationHelper configurationHelper =
			serviceTracker.getService();

		Map<String, String> properties = new HashMap<>();

		properties.put("max-height", "Invalid");

		configurationHelper.addAdaptiveMediaImageConfigurationEntry(
			TestPropsValues.getCompanyId(), "one", "1", properties);
	}

	@Test(
		expected = AdaptiveMediaImageConfigurationException.InvalidWidthException.class
	)
	public void testAddConfigurationEntryWithNotNumberMaxWidth()
		throws Exception {

		AdaptiveMediaImageConfigurationHelper configurationHelper =
			serviceTracker.getService();

		Map<String, String> properties = new HashMap<>();

		properties.put("max-width", "Invalid");

		configurationHelper.addAdaptiveMediaImageConfigurationEntry(
			TestPropsValues.getCompanyId(), "one", "1", properties);
	}

	@Test(
		expected = AdaptiveMediaImageConfigurationException.RequiredWidthOrHeightException.class
	)
	public void testAddConfigurationEntryWithoutMaxHeightNorMaxWidth()
		throws Exception {

		AdaptiveMediaImageConfigurationHelper configurationHelper =
			serviceTracker.getService();

		Map<String, String> properties = new HashMap<>();

		configurationHelper.addAdaptiveMediaImageConfigurationEntry(
			TestPropsValues.getCompanyId(), "one", "1", properties);
	}

	@Test(
		expected = AdaptiveMediaImageConfigurationException.DuplicateAdaptiveMediaImageConfigurationNameException.class
	)
	public void testAddDuplicateConfigurationEntryName() throws Exception {
		AdaptiveMediaImageConfigurationHelper configurationHelper =
			serviceTracker.getService();

		Map<String, String> properties = new HashMap<>();

		properties.put("max-height", "100");
		properties.put("max-width", "100");

		configurationHelper.addAdaptiveMediaImageConfigurationEntry(
			TestPropsValues.getCompanyId(), "one", "1", properties);

		properties = new HashMap<>();

		properties.put("max-height", "200");
		properties.put("max-width", "200");

		configurationHelper.addAdaptiveMediaImageConfigurationEntry(
			TestPropsValues.getCompanyId(), "one", "2", properties);
	}

	@Test(
		expected = AdaptiveMediaImageConfigurationException.DuplicateAdaptiveMediaImageConfigurationUuidException.class
	)
	public void testAddDuplicateConfigurationEntryUuid() throws Exception {
		AdaptiveMediaImageConfigurationHelper configurationHelper =
			serviceTracker.getService();

		Map<String, String> properties = new HashMap<>();

		properties.put("max-height", "100");
		properties.put("max-width", "100");

		configurationHelper.addAdaptiveMediaImageConfigurationEntry(
			TestPropsValues.getCompanyId(), "one", "1", properties);

		properties = new HashMap<>();

		properties.put("max-height", "200");
		properties.put("max-width", "200");

		configurationHelper.addAdaptiveMediaImageConfigurationEntry(
			TestPropsValues.getCompanyId(), "two", "1", properties);
	}

}