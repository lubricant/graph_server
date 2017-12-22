package com.soga.social.config;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class ConfigLoader {

	final static Config configInstance;
	
	static {
		try {
			configInstance = loadConfig();
		} catch (Exception e) {
			throw new Error("Fail to load configuration.", e);
		}
	}
	
	private static Config mergeConfig(List<Config> configs) {
		return configs.stream().reduce((a,b)->b.withFallback(a)).orElse(ConfigFactory.empty());
	}
	
	private static Config loadClasspathConfig(String profile) throws Exception {
		
		Config appConfig = ConfigFactory.empty();
		List<Config> configs = new ArrayList<>();
		
		URL appFile = ClassLoader.getSystemResource("config/application.conf");
		if (appFile != null) {
			appConfig = ConfigFactory.parseURL(appFile);
			configs.add(appConfig);
		}
		
		if (StringUtils.isBlank(profile))
			return appConfig;
		
		URL profileUrl = ClassLoader.getSystemResource("config/" + profile);
		if (profileUrl == null) {
			throw new IllegalStateException(
					"Profile direcotry [config/" + profile + "] is not exist.");
		}
		
		File profileDir = Paths.get(profileUrl.toURI()).toFile(); 
		if (!profileDir.isDirectory()) {
			throw new IllegalStateException(
					"Profile direcotry [" + profileDir.getPath() + " is not a directory.");
		}
		
		Optional.of(profileDir.listFiles()).ifPresent(
				files -> Stream.of(files).map(ConfigFactory::parseFile).forEach(configs::add));
		
		return mergeConfig(configs);
	}
	
	private static Config loadExternalConfig(String path) throws Exception {
		
		if (StringUtils.isBlank(path))
			return ConfigFactory.empty();
		
		List<Path> paths = Stream.of(StringUtils.split(path, File.pathSeparatorChar)).
				filter(StringUtils::isNotBlank).
				map(Paths::get).
				collect(Collectors.toList());
		
		List<String> notFound = 
				paths.stream().filter(Files::notExists).map(Path::toString).collect(Collectors.toList());
		
		if (! notFound.isEmpty()) {
			throw new IllegalArgumentException(
					"Not found external config files: " + notFound);
		}
		
		return mergeConfig(
			paths.stream().
				map(Path::toFile).
				map(ConfigFactory::parseFile).
				collect(Collectors.toList()));
	}
	
	private static Config loadConfig() throws Exception {
		
		String profile = System.getProperty("sys.profile");
		if (StringUtils.isNotBlank(profile)) {
			if (!"development".equals(profile) && 
				!"integratetest".equals(profile) &&
				!"production".equals(profile)) {
				throw new IllegalArgumentException("Invalid profile name：" + profile);
			}
		}
		
		Config classpathConfig = loadClasspathConfig(profile);
		Config externalConfig = loadExternalConfig(System.getProperty("sys.config"));
		return externalConfig.withFallback(classpathConfig);
	}
	

	public static RpcConfig getRpcConfig() {
		return new RpcConfig();
	}
	
	public static GraphConfig getGraphConfig() { 
		return new GraphConfig();
	}
	
	public static SessionConfig getSessionConfig() {
		return new SessionConfig();
	}
}
