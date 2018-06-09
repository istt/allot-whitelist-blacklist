package com.ft.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to App.
 * <p>
 * Properties are configured in the application.yml file.
 * See {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

	/**
	 * Location of Blacklist File
	 */
	public String blacklistFilePath;

	/**
	 * Location of Whitelist File
	 */
	public String whitelistFilePath;

	public String getBlacklistFilePath() {
		return blacklistFilePath;
	}

	public void setBlacklistFilePath(String blacklistFilePath) {
		this.blacklistFilePath = blacklistFilePath;
	}

	public String getWhitelistFilePath() {
		return whitelistFilePath;
	}

	public void setWhitelistFilePath(String whitelistFilePath) {
		this.whitelistFilePath = whitelistFilePath;
	}

}
