open module fr.raksrinana.youtubestatistics {
	requires org.slf4j;
	requires ch.qos.logback.classic;
	requires info.picocli;
	requires progressbar;
	requires com.fasterxml.jackson.core;
	requires com.fasterxml.jackson.annotation;
	requires com.fasterxml.jackson.databind;
	requires org.threeten.extra;
	requires google.api.client;
	requires com.google.api.client;
	requires com.google.api.client.auth;
	requires com.google.api.client.extensions.java6.auth;
	requires com.google.api.client.extensions.jetty.auth;
	requires com.google.api.client.json.gson;
	requires com.google.api.services.youtube;
	requires jdk.httpserver;
	requires static lombok;
}