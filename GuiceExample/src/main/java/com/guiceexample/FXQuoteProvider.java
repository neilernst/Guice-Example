/*
 * Copyright 1995-2013 Caplin Systems Ltd
 */
package com.guiceexample;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.guiceexample.service.QuoteService;

@Singleton
public class FXQuoteProvider
{
	private final QuoteService quoteService;
	private final ScheduledExecutorService scheduledExecutor;
	private final QuoteBuilder quoteBuilder;
	
	@Inject
	public FXQuoteProvider(QuoteService quoteService, ScheduledExecutorService scheduledExecutor, QuoteBuilder quoteBuilder) {
		this.quoteService = quoteService;
		this.scheduledExecutor = scheduledExecutor;
		this.quoteBuilder = quoteBuilder;
	}
	
	public void subscribe(final String currencyPair, final FXQuoteListener listener) throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(5);
		scheduledExecutor.scheduleAtFixedRate(new MyRunner(latch, quoteService, quoteBuilder, currencyPair, listener), 0, 1, TimeUnit.SECONDS);
		latch.await();
		System.out.println("Shutting down service...");
		scheduledExecutor.shutdown();
		//scheduledExecutor.scheduleAtFixedRate(new updateTask, );
	}
}

class 	MyRunner implements Runnable {

	CountDownLatch latch;
	QuoteService quoteService;
	QuoteBuilder quoteBuilder;
	String currencyPair;
	FXQuoteListener listener;
	MyRunner(CountDownLatch latch, QuoteService quoteService , QuoteBuilder quoteBuilder, final String currencyPair, final FXQuoteListener listener) {
		this.latch = latch;
		this.quoteBuilder = quoteBuilder;
		this.quoteService = quoteService;
		this.listener = listener;
		this.currencyPair = currencyPair;
	}

	@Override
	public void run() {
		double midPrice = quoteService.getMidPrice(currencyPair);
		Quote quote = quoteBuilder.createQuote(currencyPair, midPrice);
		listener.onQuote(currencyPair, quote);
		latch.countDown();
	}
}