package com.soga.social.service;

public class SocialServiceClientBuilder {

	String host;
	int port;
	
	public SocialServiceClientBuilder bind(String host, int port) {
		this.host = host;
		this.port = port;
		return this;
	}
	
	public SocialServiceClient build() {
		return new SocialServiceClient(this);
	}

}
