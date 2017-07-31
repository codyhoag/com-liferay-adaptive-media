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

package com.liferay.adaptive.media.processor;

import aQute.bnd.annotation.ProviderType;

import com.liferay.adaptive.media.internal.messaging.AdaptiveMediaProcessorCommand;
import com.liferay.portal.kernel.exception.PortalException;

/**
 * Generates a specific type of media asynchronously.
 *
 * <p>
 * Delegates the generation of the media in {@link AdaptiveMediaProcessor} by 
 * invoking it in an asynchronous manner.
 * </p>
 *
 * <p>
 * The type parameter <code>M</code> specifies the model used by the processor
 * to generate the media. The type parameter <code>T</code> restricts the valid
 * {@link com.liferay.adaptive.media.AdaptiveMediaAttribute} set available.
 * </p>
 *
 * @author Sergio González
 */
@ProviderType
public interface AdaptiveMediaAsyncProcessor<M, T> {

	/**
	 * Removes the specified model from the given commmand's queue of execution.
	 *
	 * @param command the command that is pending execution on the model
	 * @param modelId the model ID to remove from the queue
	 *
	 */
	public void cleanQueue(
		AdaptiveMediaProcessorCommand command, String modelId);

	/**
	 * Asynchronously removes any generated media from the specified model.
	 *
	 * @param  model the model to remove all generated media from
	 * @param  modelId the model ID for the model
	 * @throws PortalException if an error occurred while calling any Liferay
	 *         services
	 *
	 */
	public void triggerCleanUp(M model, String modelId) throws PortalException;

	/**
	 * Asynchronously generates the media for the model.
	 *
	 * @param  model the model
	 * @param  modelId the model ID for the model
	 * @throws PortalException if an error occurred while calling any Liferay
	 *         services
	 *
	 */
	public void triggerProcess(M model, String modelId) throws PortalException;

}