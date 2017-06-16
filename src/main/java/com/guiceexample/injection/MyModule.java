/*
 * Copyright 1995-2013 Caplin Systems Ltd
 */
package com.guiceexample.injection;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.guiceexample.service.QuoteService;
import com.guiceexample.service.YahooQuoteService;
import com.guiceexample.util.AuditLogger;

import javax.inject.Inject;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MyModule extends AbstractModule
{
	@Override
	protected void configure() {
		String file = "pair.txt";
		String pair;
		try {
			pair = getPair(file, Charset.defaultCharset());
		} catch (IOException e) {
			e.printStackTrace();
			pair = "CADUSD";
		}
		bind(String.class).toInstance(pair);
		bind(ScheduledExecutorService.class).toInstance(Executors.newSingleThreadScheduledExecutor());
	}

	@Provides
	@Inject
	public QuoteService provideQuoteService(AuditLogger auditLogger)
	{
		return new YahooQuoteService(auditLogger);
	}

	private String getPair(String file, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(file));
  		return new String(encoded, encoding);
	}
}