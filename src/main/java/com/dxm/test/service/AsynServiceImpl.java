package com.dxm.test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AsynServiceImpl implements AsynService {


    @Autowired
    private JedisCacheClient jedisCacheClient;


    @Async
    @Override
    public void PermutationAndcombination(String uuid) {

        System.out.println("detach==========>>>");
        try {
            for (int i = 0; i < 51; i++) {
                Thread.sleep(1000);
                System.out.println("............");
                jedisCacheClient.set(uuid, String.valueOf(i * 2));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("detach= over =========>>>");
    }


}
