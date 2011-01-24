/*
 * Copyright (C) 2010 PIRAmIDE-SP3 authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * This software consists of contributions made by many individuals, 
 * listed below:
 *
 * Author: Aitor Almeida <aitor.almeida@deusto.es>
 *         Pablo Orduña <pablo.orduna@deusto.es>
 *         Eduardo Castillejo <eduardo.castillejo@deusto.es>
 *
 */
package piramide.interaction.reasoner.db.deploy;

class WurlfInfo {
	
	private String id;
	private String brandName;
	private String marketingName;
	private String modelName;
	private String realWidth;
	private String realHeight;
	private String resoWidth;
	private String resoHeight;
	
	WurlfInfo(String id, String brandName, String marketingName,
			String modelName, String realWidth, String realHeight,
			String resoWidth, String resoHeight) {
		super();
		this.id = id;
		this.brandName = brandName;
		this.marketingName = marketingName;
		this.modelName = modelName;
		this.realWidth = realWidth;
		this.realHeight = realHeight;
		this.resoWidth = resoWidth;
		this.resoHeight = resoHeight;
	}

	String getId() {
		return this.id;
	}

	String getBrandName() {
		return this.brandName;
	}

	String getMarketingName() {
		return this.marketingName;
	}

	String getModelName() {
		return this.modelName;
	}

	String getRealWidth() {
		return this.realWidth;
	}

	String getRealHeight() {
		return this.realHeight;
	}

	String getResoWidth() {
		return this.resoWidth;
	}

	String getResoHeight() {
		return this.resoHeight;
	}

	void setId(String id) {
		this.id = id;
	}

	void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	void setMarketingName(String marketingName) {
		this.marketingName = marketingName;
	}

	void setModelName(String modelName) {
		this.modelName = modelName;
	}

	void setRealWidth(String realWidth) {
		this.realWidth = realWidth;
	}

	void setRealHeight(String realHeight) {
		this.realHeight = realHeight;
	}

	void setResoWidth(String resoWidth) {
		this.resoWidth = resoWidth;
	}

	void setResoHeight(String resoHeight) {
		this.resoHeight = resoHeight;
	}
}
