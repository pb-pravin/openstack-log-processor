package com.keedio.flink.utils

import java.sql.Timestamp

import org.apache.commons.lang3.time.FastDateFormat
import org.joda.time.DateTime
import org.junit.{Assert, Test}

import scala.collection.Map

/**
  * Created by luislazaro on 27/2/17.
  * lalazaro@keedio.com
  * Keedio
  */
class ProcessorHelperTest {

  val listOfKeys: Map[String, Int] = Map("1h" -> 3600, "6h" -> 21600, "12h" -> 43200, "24h" -> 86400, "1w" ->
    604800, "1m" -> 2419200)

  @Test
  def testIsValidPeriodTime() = {
    //limit condition: time of log cannot be bigger than now
    listOfKeys.foreach(kv => Assert.assertFalse(ProcessorHelper.isValidPeriodTime("2016-03-05 16:00:00", kv._2, new DateTime(2016, 3, 5, 14, 0, 0))))
    //"1h"
    Assert.assertFalse(ProcessorHelper.isValidPeriodTime("2016-03-05 14:00:00", 3600, new DateTime(2016, 3, 5, 16, 30, 0)))
    Assert.assertTrue(ProcessorHelper.isValidPeriodTime("2016-03-05 15:00:00", 3600, new DateTime(2016, 3, 5, 16, 0, 0)))
    //6h
    Assert.assertFalse(ProcessorHelper.isValidPeriodTime("2016-03-05 09:00:00", 21600, new DateTime(2016, 3, 5, 16, 30, 0)))
    Assert.assertTrue(ProcessorHelper.isValidPeriodTime("2016-03-05 14:00:00", 21600, new DateTime(2016, 3, 5, 19, 30, 0)))
    //1m
    Assert.assertFalse(ProcessorHelper.isValidPeriodTime("2016-02-05 09:00:00", 2419200, new DateTime(2016, 3, 5, 16, 30, 0)))
    Assert.assertTrue(ProcessorHelper.isValidPeriodTime("2016-02-15 14:00:00", 2419200, new DateTime(2016, 3, 5, 19, 30, 0)))
  }

  @Test
  def testComputeTTL = {
    listOfKeys.foreach(kv => Assert.assertTrue(ProcessorHelper.computeTTL("2016-03-05 14:30:00", kv._2, new DateTime(2016, 3 , 5, 17, 0, 0)) < kv._2))
    listOfKeys.foreach(kv => Assert.assertFalse(ProcessorHelper.computeTTL("2016-03-05 14:30:00", kv._2, new DateTime(2016, 3 , 5, 13, 0, 0)) < kv._2))
  }

  @Test
  def testToTimestamp = {
    println("2017-03-07T16:33:33.562422+00:00")
    println(ProcessorHelper.toTimestamp("2017-03-07T16:33:33.562422+00:00"))
    println(new Timestamp(FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ss.SSSSSSZZ").parse("2017-03-07T16:33:33.562422+00:00").getTime))
    println(new Timestamp(FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'+'SS:SS").parse("2017-03-07T16:33:33.562422+00:00").getTime))

    println(new Timestamp(0L).toString)
  }


}
