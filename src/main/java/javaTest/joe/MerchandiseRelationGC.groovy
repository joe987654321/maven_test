//#!/usr/bin/env groovy
//
//@Grab('com.google.guava:guava')
//import groovy.json.internal.LazyMap
//@Grab('org.codehaus.groovy.modules.http-builder:http-builder')
//import groovyx.net.http.RESTClient
//
//import static groovyx.net.http.Method.GET
//import static groovyx.net.http.ContentType.JSON
//
//import static Constants.*
//
//class Constants {
//    static def MERCHANDISE_URL = "http://merchandise.nevec.yahoo.com:4080"
//    static def YCA = "yahoo.nevec.hgp-merchandise_egs.env-prod"
//    static def ycaCert
//    static def QPS = 50
//    static def PRODUCT_URL = "http://product.nevec.yahoo.com:4080"
//    static def outDatedTimestamp
//    static def reportFilePath = "/home/y/logs/product_relation_gc/report"
//}
//
////def file = new File(reportFilePath)
////BufferedWriter fileWriter = file.newWriter()
//
////part 1: get all removable relation from get_product_relation_for_gc api
//ycaCert = "/home/y/bin/yca-cert-util --show ${YCA}".execute().text.split()[1]
//localhost = "/bin/hostname".execute().text
//
//StringBuilder outputString = new StringBuilder()
//LazyMap removableRecord = null
//LazyMap removableProduct = null
//def start = new Date().getTime()
//outDatedTimestamp = (long)(start/1000) - 86400*30
//def getRelationSuccess = false
//try {
//    def client = new RESTClient(localhost)
//    client.getClient().getParams().setParameter("http.connection.timeout", 5000)
//    client.getClient().getParams().setParameter("http.socket.timeout",     1200000)
//
//    client.setHeaders("Yahoo-App-Auth" : ycaCert)
//    client.request(GET, JSON) {
//        uri.path = "/v1/egs/merchandise/get_product_relation_for_gc/"
//        uri.query = [
//                "arg0" : "$outDatedTimestamp"
//        ]
//        response.success = { resp, json ->
//            removableRecord = json.response_data.removableRecord
//
//            removableProduct = json.response_data.removableProduct
//
//            outputString.append("Try remove relation pid:[mid1, mid2, ...] ... \n")
//            outputString.append("============================================= \n")
//            outputString.append("Relations which present removable products are:\n")
//            def count = 0
//            removableProduct.each { pid, mids ->
//                outputString.append(pid + " : " + mids.toString() + "\n")
//                count++
//                if (count % 1000 == 0) {
//                    println outputString
//                    outputString.setLength(0)
//                }
//            }
//            outputString.append("============================================= \n")
//            outputString.append("Relations which present removable records are:\n")
//
//            count = 0
//            removableRecord.each { pid, mids ->
//                outputString.append(pid + " : " +  mids.toString() + "\n")
//                count++
//                if (count % 1000 == 0) {
//                    println outputString
//                    outputString.setLength(0)
//                }
//            }
//            outputString.append("============================================= \n")
//
//            getRelationSuccess = true
//        }
//        response.failure = { resp ->
//            def end = new Date().getTime()
//            def used = end - start
//            outputString.append("${end}: get product relation failed: ${resp.statusLine}, used ${used} ms\n")
//        }
//    }
//} catch (Exception e) {
//    def end = new Date().getTime()
//    def used = end - start
//    outputString.append("${end}: get product relation got excpetion: ${e}, used ${used} ms\n")
//}
//
//println outputString