package com.landonlib.service.consumer;

import org.junit.Assert;
import org.junit.Test;

public class ServiceConsumerTest {
    @Test
    public void searchTest(){
        LastFmService lastFmService = new LastFmService();

        Assert.assertTrue("Search result does not contail \"nevermind\"", lastFmService.search("nevermind").contains("nevermind"));
    }
}
