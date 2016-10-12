//J-
package com.whippy.poker.ai.utils;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.whippy.poker.ai.utils.beans.Distribution;
import com.whippy.poker.ai.utils.beans.WinPercentages;
import com.whippy.poker.common.beans.Card;
import com.whippy.poker.common.beans.Hand;

public class DistributionBuilderTest {

        static int count = 0;

        @Test
        public void test() throws InterruptedException{



                List<Hand> allMyHands = General.generateEveryHand(new ArrayList<Card>());


                for(int i=1 ;i <allMyHands.size(); i++){
                        Hand myHand = allMyHands.get(i);
                        System.out.println(myHand.getHand()[0] + " " + myHand.getHand()[1] + " : " + count);
                        PrintWriter writer;
                        try {
                                writer = new PrintWriter(myHand.getHand()[0] + " " + myHand.getHand()[1] + ".csv", "UTF-8");
                                writer.println("opposingHand,winningChance,drawChance,lostChance");
                                Distribution distribution = DistributionBuilder.buildBasicDistribution(Arrays.asList(myHand.getHand()));
                                writer.println(myHand);
                                WinPercentages myWinPercentages = WinPercentageBuilder.calculateWinPercentages(myHand, distribution, writer);
                                Map<Hand, Float> handToProbability = myWinPercentages.getHandToProbability();
                                DistributionBuilderTest.count++;
                                System.out.println("done " + DistributionBuilderTest.count);
                                writer.close();
                        } catch (FileNotFoundException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                        } catch (UnsupportedEncodingException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                        }

                }

        }

        @Test
        public void testFullDistribution() throws FileNotFoundException, UnsupportedEncodingException, InterruptedException{

                ExecutorService threadPool = Executors.newFixedThreadPool(250);

                List<Hand> allMyHands = General.generateEveryHand(new ArrayList<Card>());
                Hand myHand = allMyHands.get(0);
                System.out.println(myHand.getHand()[0] + " " + myHand.getHand()[1] + " : " + count);
                PrintWriter writer;
                try {
                        writer = new PrintWriter(myHand.getHand()[0] + " " + myHand.getHand()[1] + ".csv", "UTF-8");
                        writer.println("opposingHand,winningChance,drawChance,lostChance");
                        Distribution distribution = DistributionBuilder.buildBasicDistribution(Arrays.asList(myHand.getHand()));
                        writer.println(myHand);
                        WinPercentages myWinPercentages = WinPercentageBuilder.calculateWinPercentages(myHand, distribution, writer);
                        Map<Hand, Float> handToProbability = myWinPercentages.getHandToProbability();
                        DistributionBuilderTest.count++;
                        System.out.println("done " + DistributionBuilderTest.count);
                        writer.close();
                } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }

                for(int i=1 ;i <allMyHands.size(); i++){
                        threadPool.submit(new runAnalaysis(allMyHands.get(i)));
                }

                // once you've submitted your last job to the service it should be shut down
                threadPool.shutdown();
                // wait for the threads to finish if necessary
                threadPool.awaitTermination(1000000, TimeUnit.SECONDS);

        }


        private class runAnalaysis implements Runnable{

                private Hand myHand;

                public runAnalaysis(Hand myHand) {
                        super();
                        this.myHand = myHand;
                }

                @Override
                public void run() {

                        System.out.println(myHand.getHand()[0] + " " + myHand.getHand()[1] + " : " + count);
                        PrintWriter writer;
                        try {
                                writer = new PrintWriter(myHand.getHand()[0] + " " + myHand.getHand()[1] + ".csv", "UTF-8");
                                writer.println("opposingHand,winningChance,drawChance,lostChance");
                                Distribution distribution = DistributionBuilder.buildBasicDistribution(Arrays.asList(myHand.getHand()));
                                writer.println(myHand);
                                WinPercentages myWinPercentages = WinPercentageBuilder.calculateWinPercentages(myHand, distribution, writer);
                                Map<Hand, Float> handToProbability = myWinPercentages.getHandToProbability();
                                DistributionBuilderTest.count++;
                                System.out.println("done " + DistributionBuilderTest.count);
                                writer.close();
                        } catch (FileNotFoundException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                        } catch (UnsupportedEncodingException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                        }
                }
        }
}
//J+
