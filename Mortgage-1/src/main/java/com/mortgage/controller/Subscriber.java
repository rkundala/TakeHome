package com.mortgage.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mortgage.domain.TakeHome;
import com.mortgage.service.TakeHomeClientLocalServiceImpl;
import com.mortgage.service.TakeHomeClientRemoteServiceImpl;

import reactor.core.publisher.FluxSink;

@Component
public class Subscriber {
 
	private static final Logger logger = LoggerFactory.getLogger(TakeHomeController.class);
	@Autowired
	
	private static TakeHomeClientLocalServiceImpl takeHomeClientLocalServiceImpl;
	
	private static TakeHomeClientRemoteServiceImpl  takeHomeClientRemoteServiceImpl;

	private static List<TakeHome> takeHomeList = new ArrayList<>();

	public static TakeHome highestValueVA = new TakeHome();
	
	@Autowired
	private TakeHomeClientLocalServiceImpl takeHomeClientLocalServiceImplauto;

	@Autowired
	private TakeHomeClientRemoteServiceImpl takeHomeClientRemoteServiceImplauto;
	
    private Subscriber(){}
    
    @PostConstruct
    public void init() {
    	Subscriber.takeHomeClientLocalServiceImpl = takeHomeClientLocalServiceImplauto;
    	Subscriber.takeHomeClientRemoteServiceImpl = takeHomeClientRemoteServiceImplauto;
    }
 
    static void count(FluxSink<String> sink, String[] ids) {
        SubscriberRunnable runnable = new SubscriberRunnable(sink, ids);
        Thread t = new Thread(runnable);
        t.start();
    }
 
    public static class SubscriberRunnable implements Runnable {
 
        FluxSink<String> sink;
        
        String[] ids;
 
        public SubscriberRunnable(FluxSink<String> sink2, String[] ids) {
            this.sink = sink2;
            this.ids = ids;
        }
 
        public void run() {
            getRemoteCallList(sink,ids);
            sink.complete();
        }
    }
    
	private static void getRemoteCallList(FluxSink<String> sink, String[] propertyIds) {
		for (String id : Arrays.asList(propertyIds)) {
			takeHomeClientRemoteServiceImpl.getPropertyDetails(id)
					.subscribe(value -> {
											takeHomeList.add(value);
											sink.next(value.toString());
											logger.info("subscribe-consumer takeHomeList: values:{} size:{} ", value.toString(), takeHomeList.size());
										}, 
							            error -> error.printStackTrace(), 
							            () -> {
									       		 highestValueVA = takeHomeList
									     				.stream()
									     				.filter(ida -> Objects.nonNull(ida.getAddress().getState().equals("VA")))
									     				.max(Comparator.comparing(TakeHome::getValue))
									     				.orElse(new TakeHome());
									     			logger.info("subscribe:runnable:highestValueVA:{} ",Subscriber.highestValueVA);
									     		}
							            
							   );
				
		}

	}
}