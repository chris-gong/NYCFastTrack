package com.chrisgong.nycfasttrack;

import java.util.HashMap;

/**
 * Created by User on 2/18/2017.
 */

public class NYCGraph {
    public static LinkedList[] vertices = new LinkedList[499]; //499 total/distinguishable stops according to stops.txt
    static NYCGraph graph = new NYCGraph();
    public static LinkedList route1 = graph.new LinkedList(); //route 1 has 38 stops, also, each route will be made going down (south, which means the stopIDs increment ironically)
    public static LinkedList route2 = graph.new LinkedList();
    public static LinkedList route3 = graph.new LinkedList();
    public static LinkedList route4 = graph.new LinkedList();
    public static LinkedList route5 = graph.new LinkedList();//is 5X 5S?
    public static LinkedList route6 = graph.new LinkedList();
    public static LinkedList route6X = graph.new LinkedList(); //same as 6
    public static LinkedList route7 = graph.new LinkedList(); //is 7X 7S?
    public static LinkedList route7X = graph.new LinkedList();
    public static LinkedList routeGS = graph.new LinkedList();
    public static LinkedList routeA = graph.new LinkedList();
    public static LinkedList routeB = graph.new LinkedList();
    public static LinkedList routeC = graph.new LinkedList();
    public static LinkedList routeD = graph.new LinkedList();
    public static LinkedList routeE = graph.new LinkedList();
    public static LinkedList routeF = graph.new LinkedList();
    public static LinkedList routeFS = graph.new LinkedList();
    public static LinkedList routeG = graph.new LinkedList();
    public static LinkedList routeJ = graph.new LinkedList();
    public static LinkedList routeL = graph.new LinkedList();
    public static LinkedList routeM = graph.new LinkedList();
    public static LinkedList routeN = graph.new LinkedList();
    public static LinkedList routeQ = graph.new LinkedList();
    public static LinkedList routeR = graph.new LinkedList();
    public static LinkedList routeH = graph.new LinkedList();
    public static LinkedList routeW = graph.new LinkedList();
    public static LinkedList routeZ = graph.new LinkedList();
    public static LinkedList routeSI = graph.new LinkedList();
    public static HashMap<String, LinkedList> routeLinkedLists = new HashMap<String, LinkedList>();
    public class LinkedList {
        Vertex head = null;
        int length = 0;
        public void addToEnd(Vertex v){
            if(head == null) {
                head = v; //1st vertex is both the head and tail of the list
            }
            else {
                Vertex curr = head;
                while(curr.next != null){
                    curr = curr.next;
                }
                curr.next = v;
            }
            length++;
        }
    }

    public class Vertex {
        Vertex next;
        String stopID;
        String stopName;
        double latitude;
        double longitude;
        public Vertex(String stopID, String stopName, double latitude, double longitude, Vertex next){
            this.stopID = stopID;
            this.stopName = stopName;
            this.latitude = latitude;
            this.longitude = longitude;
            this.next = next;
        }
    }
    public static void makeNYCGraph(){
        //start with each route
        routeLinkedLists.put("1",route1);
        routeLinkedLists.put("2",route2);
        routeLinkedLists.put("3",route3);
        routeLinkedLists.put("4",route4);
        routeLinkedLists.put("5",route5);
        routeLinkedLists.put("6",route6);
        routeLinkedLists.put("7",route7);
        routeLinkedLists.put("GS",routeGS);
        routeLinkedLists.put("A",routeA);
        routeLinkedLists.put("B",routeB);
        routeLinkedLists.put("C",routeC);
        routeLinkedLists.put("D",routeD);
        routeLinkedLists.put("E",routeE);
        routeLinkedLists.put("F",routeF);
        routeLinkedLists.put("FS",routeFS);
        routeLinkedLists.put("G",routeG);
        routeLinkedLists.put("J",routeJ);
        routeLinkedLists.put("L",routeL);
        routeLinkedLists.put("M",routeM);
        routeLinkedLists.put("N",routeN);
        routeLinkedLists.put("Q",routeQ);
        routeLinkedLists.put("R",routeR);
        routeLinkedLists.put("H",routeH);
        routeLinkedLists.put("SI",routeSI);
        HashMap<String, GTFSParser.Stop> subwayStops = GTFSParser.getSubwayStops();
        NYCGraph graph = new NYCGraph();
        route1.addToEnd(graph.new Vertex("101", subwayStops.get("101").stopName, subwayStops.get("101").latitude, subwayStops.get("101").longitude, null));
        route1.addToEnd(graph.new Vertex("103", subwayStops.get("103").stopName, subwayStops.get("103").latitude, subwayStops.get("103").longitude, null));
        route1.addToEnd(graph.new Vertex("104", subwayStops.get("104").stopName, subwayStops.get("104").latitude, subwayStops.get("104").longitude, null));
        for(int i = 106; i <= 140; i++){
            String stopID = String.valueOf(i);
            GTFSParser.Stop stop = subwayStops.get(stopID);
            route1.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        route2.addToEnd(graph.new Vertex("201", subwayStops.get("201").stopName, subwayStops.get("201").latitude, subwayStops.get("201").longitude, null));
        for(int i = 204; i <= 222; i++){
            String stopID = String.valueOf(i);
            GTFSParser.Stop stop = subwayStops.get(stopID);
            route2.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        for(int i = 224; i <= 227; i++){
            String stopID = String.valueOf(i);
            GTFSParser.Stop stop = subwayStops.get(stopID);
            route2.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        for(int i = 120; i <= 137; i++){
            String stopID = String.valueOf(i);
            GTFSParser.Stop stop = subwayStops.get(stopID);
            route2.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        for(int i = 228; i <= 239; i++){
            String stopID = String.valueOf(i);
            GTFSParser.Stop stop = subwayStops.get(stopID);
            route2.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        for(int i = 241; i <= 247; i++){
            String stopID = String.valueOf(i);
            GTFSParser.Stop stop = subwayStops.get(stopID);
            route2.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        for(int i = 301; i <= 302; i++){
            String stopID = String.valueOf(i);
            GTFSParser.Stop stop = subwayStops.get(stopID);
            route3.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        for(int i = 224; i <= 227; i++){
            String stopID = String.valueOf(i);
            GTFSParser.Stop stop = subwayStops.get(stopID);
            route3.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        for(int i = 120; i <= 120; i++){
            String stopID = String.valueOf(i);
            GTFSParser.Stop stop = subwayStops.get(stopID);
            route3.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        for(int i = 123; i <= 123; i++){
            String stopID = String.valueOf(i);
            GTFSParser.Stop stop = subwayStops.get(stopID);
            route3.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        for(int i = 127; i <= 128; i++){
            String stopID = String.valueOf(i);
            GTFSParser.Stop stop = subwayStops.get(stopID);
            route3.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        for(int i = 132; i <= 132; i++){
            String stopID = String.valueOf(i);
            GTFSParser.Stop stop = subwayStops.get(stopID);
            route3.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        for(int i = 137; i <= 137; i++){
            String stopID = String.valueOf(i);
            GTFSParser.Stop stop = subwayStops.get(stopID);
            route3.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        for(int i = 228; i <= 239; i++){
            String stopID = String.valueOf(i);
            GTFSParser.Stop stop = subwayStops.get(stopID);
            route3.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        for(int i = 248; i <= 257; i++){
            String stopID = String.valueOf(i);
            GTFSParser.Stop stop = subwayStops.get(stopID);
            route3.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        for(int i = 401; i <= 402; i++){
            String stopID = String.valueOf(i);
            GTFSParser.Stop stop = subwayStops.get(stopID);
            route4.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        for(int i = 405; i <= 416; i++){
            String stopID = String.valueOf(i);
            GTFSParser.Stop stop = subwayStops.get(stopID);
            route4.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        for(int i = 621; i <= 621; i++){
            String stopID = String.valueOf(i);
            GTFSParser.Stop stop = subwayStops.get(stopID);
            route4.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        for(int i = 626; i <= 626; i++){
            String stopID = String.valueOf(i);
            GTFSParser.Stop stop = subwayStops.get(stopID);
            route4.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        for(int i = 629; i <= 629; i++){
            String stopID = String.valueOf(i);
            GTFSParser.Stop stop = subwayStops.get(stopID);
            route4.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        for(int i = 631; i <= 631; i++){
            String stopID = String.valueOf(i);
            GTFSParser.Stop stop = subwayStops.get(stopID);
            route4.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        for(int i = 635; i <= 635; i++){
            String stopID = String.valueOf(i);
            GTFSParser.Stop stop = subwayStops.get(stopID);
            route4.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        for(int i = 640; i <= 640; i++){
            String stopID = String.valueOf(i);
            GTFSParser.Stop stop = subwayStops.get(stopID);
            route4.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        for(int i = 418; i <= 420; i++){
            String stopID = String.valueOf(i);
            GTFSParser.Stop stop = subwayStops.get(stopID);
            route4.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        for(int i = 423; i <= 423; i++){
            String stopID = String.valueOf(i);
            GTFSParser.Stop stop = subwayStops.get(stopID);
            route4.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        for(int i =229; i <= 230; i++){
            String stopID = String.valueOf(i);
            GTFSParser.Stop stop = subwayStops.get(stopID);
            route4.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        for(int i = 234; i <= 235; i++){
            String stopID = String.valueOf(i);
            GTFSParser.Stop stop = subwayStops.get(stopID);
            route4.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        for(int i = 239; i <= 239; i++){
            String stopID = String.valueOf(i);
            GTFSParser.Stop stop = subwayStops.get(stopID);
            route4.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        for(int i = 250; i <= 250; i++){
            String stopID = String.valueOf(i);
            GTFSParser.Stop stop = subwayStops.get(stopID);
            route4.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        //5 and 5X are the same
        for(int i = 204; i <=213; i++){
            String stopID = String.valueOf(i);
            GTFSParser.Stop stop = subwayStops.get(stopID);
            route5.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        for(int i = 221; i <=222; i++){
            String stopID = String.valueOf(i);
            GTFSParser.Stop stop = subwayStops.get(stopID);
            route5.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        for(int i = 416; i <=416; i++){
            String stopID = String.valueOf(i);
            GTFSParser.Stop stop = subwayStops.get(stopID);
            route5.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        for(int i = 621; i <=621; i++){
            String stopID = String.valueOf(i);
            GTFSParser.Stop stop = subwayStops.get(stopID);
            route5.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        for(int i = 626; i <=626; i++){
            String stopID = String.valueOf(i);
            GTFSParser.Stop stop = subwayStops.get(stopID);
            route5.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        for(int i = 629; i <=629; i++){
            String stopID = String.valueOf(i);
            GTFSParser.Stop stop = subwayStops.get(stopID);
            route5.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        for(int i = 631; i <=631; i++){
            String stopID = String.valueOf(i);
            GTFSParser.Stop stop = subwayStops.get(stopID);
            route5.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        for(int i = 635; i <=635; i++){
            String stopID = String.valueOf(i);
            GTFSParser.Stop stop = subwayStops.get(stopID);
            route5.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        for(int i = 640; i <=640; i++){
            String stopID = String.valueOf(i);
            GTFSParser.Stop stop = subwayStops.get(stopID);
            route5.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        for(int i = 418; i <=420; i++){
            String stopID = String.valueOf(i);
            GTFSParser.Stop stop = subwayStops.get(stopID);
            route5.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }

        for(int i = 608; i <=619; i++){
            String stopID = String.valueOf(i);
            GTFSParser.Stop stop = subwayStops.get(stopID);
            route6.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        for(int i = 621; i <=640; i++){
            String stopID = String.valueOf(i);
            GTFSParser.Stop stop = subwayStops.get(stopID);
            route6.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        for(int i = 701; i <=702; i++){
            String stopID = String.valueOf(i);
            GTFSParser.Stop stop = subwayStops.get(stopID);
            route7.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        for(int i = 705; i <=716; i++){
            String stopID = String.valueOf(i);
            GTFSParser.Stop stop = subwayStops.get(stopID);
            route7.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        for(int i = 718; i <=721; i++){
            String stopID = String.valueOf(i);
            GTFSParser.Stop stop = subwayStops.get(stopID);
            route7.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        for(int i = 723; i <=726; i++){
            String stopID = String.valueOf(i);
            GTFSParser.Stop stop = subwayStops.get(stopID);
            route7.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        for(int i = 902; i <=901; i--){
            String stopID = String.valueOf(i);
            GTFSParser.Stop stop = subwayStops.get(stopID);
            routeGS.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        String stopID = "G05";
        GTFSParser.Stop stop = subwayStops.get(stopID);
        routeE.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "G06";
        stop = subwayStops.get(stopID);
        routeE.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "G07";
        stop = subwayStops.get(stopID);
        routeE.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "F05";
        stop = subwayStops.get(stopID);
        routeE.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "F06";
        stop = subwayStops.get(stopID);
        routeE.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "F07";
        stop = subwayStops.get(stopID);
        routeE.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "G08";
        stop = subwayStops.get(stopID);
        routeE.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "G14";
        stop = subwayStops.get(stopID);
        routeE.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "G21";
        stop = subwayStops.get(stopID);
        routeE.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "F09";
        stop = subwayStops.get(stopID);
        routeE.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "F11";
        stop = subwayStops.get(stopID);
        routeE.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "F12";
        stop = subwayStops.get(stopID);
        routeE.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "D14";
        stop = subwayStops.get(stopID);
        routeE.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "A25";
        stop = subwayStops.get(stopID);
        routeE.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "A27";
        stop = subwayStops.get(stopID);
        routeE.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "A28";
        stop = subwayStops.get(stopID);
        routeE.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "A30";
        stop = subwayStops.get(stopID);
        routeE.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "A31";
        stop = subwayStops.get(stopID);
        routeE.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "A32";
        stop = subwayStops.get(stopID);
        routeE.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "A33";
        stop = subwayStops.get(stopID);
        routeE.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "A34";
        stop = subwayStops.get(stopID);
        routeE.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "E01";
        stop = subwayStops.get(stopID);
        routeE.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        for(int i = 1; i <=7; i++){
            stopID = "F0" + String.valueOf(i);
            stop = subwayStops.get(stopID);
            routeF.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        stopID = "G08";
        stop = subwayStops.get(stopID);
        routeF.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "G14";
        stop = subwayStops.get(stopID);
        routeF.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "B04";
        stop = subwayStops.get(stopID);
        routeF.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "B06";
        stop = subwayStops.get(stopID);
        routeF.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "B08";
        stop = subwayStops.get(stopID);
        routeF.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "B10";
        stop = subwayStops.get(stopID);
        routeF.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        for(int i = 15; i <=21; i++){
            stopID = "D" + String.valueOf(i);
            stop = subwayStops.get(stopID);
            routeF.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        for(int i = 14; i <=16; i++){
            stopID = "F" + String.valueOf(i);
            stop = subwayStops.get(stopID);
            routeF.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        stopID = "F18";
        stop = subwayStops.get(stopID);
        routeF.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "A41";
        stop = subwayStops.get(stopID);
        routeF.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        for(int i = 20; i <=27; i++){
            stopID = "F" + String.valueOf(i);
            stop = subwayStops.get(stopID);
            routeF.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        for(int i = 29; i <=36; i++){
            stopID = "F" + String.valueOf(i);
            stop = subwayStops.get(stopID);
            routeF.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        for(int i = 38; i <=39; i++){
            stopID = "F" + String.valueOf(i);
            stop = subwayStops.get(stopID);
            routeF.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        for(int i = 42; i <=43; i++){
            stopID = "D" + String.valueOf(i);
            stop = subwayStops.get(stopID);
            routeF.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        stopID = "S01";
        stop = subwayStops.get(stopID);
        routeFS.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        for(int i = 3; i <=4; i++){
            stopID = "S0" + String.valueOf(i);
            stop = subwayStops.get(stopID);
            routeFS.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        stopID = "D26";
        stop = subwayStops.get(stopID);
        routeFS.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "G22";
        stop = subwayStops.get(stopID);
        routeG.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "G24";
        stop = subwayStops.get(stopID);
        routeG.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "G26";
        stop = subwayStops.get(stopID);
        routeG.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        for(int i = 28; i <=36; i++){
            stopID = "G" + String.valueOf(i);
            stop = subwayStops.get(stopID);
            routeG.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        stopID = "A42";
        stop = subwayStops.get(stopID);
        routeG.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        for(int i = 20; i <=27; i++){
            stopID = "F" + String.valueOf(i);
            stop = subwayStops.get(stopID);
            routeG.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        stopID = "H04";
        stop = subwayStops.get(stopID);
        routeH.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        for(int i = 12; i <=15; i++){
            stopID = "H" + String.valueOf(i);
            stop = subwayStops.get(stopID);
            routeH.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        for(int i = 5; i <=6; i++){
            stopID = "G0" + String.valueOf(i);
            stop = subwayStops.get(stopID);
            routeJ.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        stopID = "J12";
        stop = subwayStops.get(stopID);
        routeJ.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "J14";
        stop = subwayStops.get(stopID);
        routeJ.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "J15";
        stop = subwayStops.get(stopID);
        routeJ.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "J17";
        stop = subwayStops.get(stopID);
        routeJ.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "J20";
        stop = subwayStops.get(stopID);
        routeJ.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "J21";
        stop = subwayStops.get(stopID);
        routeJ.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "J23";
        stop = subwayStops.get(stopID);
        routeJ.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "J24";
        stop = subwayStops.get(stopID);
        routeJ.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "J27";
        stop = subwayStops.get(stopID);
        routeJ.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "J28";
        stop = subwayStops.get(stopID);
        routeJ.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "J30";
        stop = subwayStops.get(stopID);
        routeJ.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "M11";
        stop = subwayStops.get(stopID);
        routeJ.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "M16";
        stop = subwayStops.get(stopID);
        routeJ.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        for(int i = 18; i <=23; i++){
            stopID = "M" + String.valueOf(i);
            stop = subwayStops.get(stopID);
            routeJ.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        for(int i = 1; i <=3; i++){
            stopID = "L0" + String.valueOf(i);
            stop = subwayStops.get(stopID);
            routeL.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        stopID = "L05";
        stop = subwayStops.get(stopID);
        routeL.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "L06";
        stop = subwayStops.get(stopID);
        routeL.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "L08";
        stop = subwayStops.get(stopID);
        routeL.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        for(int i = 10; i <=17; i++){
            stopID = "L" + String.valueOf(i);
            stop = subwayStops.get(stopID);
            routeL.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        for(int i = 19; i <=22; i++){
            stopID = "L" + String.valueOf(i);
            stop = subwayStops.get(stopID);
            routeL.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        for(int i = 24; i <=29; i++){
            stopID = "L" + String.valueOf(i);
            stop = subwayStops.get(stopID);
            routeL.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        stopID = "M18";
        stop = subwayStops.get(stopID);
        routeM.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "M16";
        stop = subwayStops.get(stopID);
        routeM.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "M14";
        stop = subwayStops.get(stopID);
        routeM.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "M13";
        stop = subwayStops.get(stopID);
        routeM.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "M12";
        stop = subwayStops.get(stopID);
        routeM.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "M11";
        stop = subwayStops.get(stopID);
        routeM.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "M10";
        stop = subwayStops.get(stopID);
        routeM.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        for(int i = 9; i >=8; i--){
            stopID = "M0" + String.valueOf(i);
            stop = subwayStops.get(stopID);
            routeM.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        for(int i = 6; i >=4; i--){
            stopID = "M0" + String.valueOf(i);
            stop = subwayStops.get(stopID);
            routeM.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        stopID = "M01";
        stop = subwayStops.get(stopID);
        routeM.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        for(int i = 5; i >=3; i--){
            stopID = "Q0" + String.valueOf(i);
            stop = subwayStops.get(stopID);
            routeQ.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        stopID = "B08";
        stop = subwayStops.get(stopID);
        routeQ.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "R14";
        stop = subwayStops.get(stopID);
        routeQ.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "R16";
        stop = subwayStops.get(stopID);
        routeQ.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "R17";
        stop = subwayStops.get(stopID);
        routeQ.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "R20";
        stop = subwayStops.get(stopID);
        routeQ.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "Q01";
        stop = subwayStops.get(stopID);
        routeQ.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "R30";
        stop = subwayStops.get(stopID);
        routeQ.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        for(int i =24; i <=35; i++){
            stopID = "D" + String.valueOf(i);
            stop = subwayStops.get(stopID);
            routeQ.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        for(int i =37; i <=43; i++){
            stopID = "D" + String.valueOf(i);
            stop = subwayStops.get(stopID);
            routeQ.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        stopID = "R01";
        stop = subwayStops.get(stopID);
        routeN.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        for(int i =3; i <=6; i++){
            stopID = "R0" + String.valueOf(i);
            stop = subwayStops.get(stopID);
            routeN.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        for(int i =8; i <=9; i++){
            stopID = "R0" + String.valueOf(i);
            stop = subwayStops.get(stopID);
            routeN.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        stopID = "R11";
        stop = subwayStops.get(stopID);
        routeN.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        for(int i =13; i <=22; i++){
            stopID = "R" + String.valueOf(i);
            stop = subwayStops.get(stopID);
            routeN.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        stopID = "Q01";
        stop = subwayStops.get(stopID);
        routeN.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "R31";
        stop = subwayStops.get(stopID);
        routeN.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "R36";
        stop = subwayStops.get(stopID);
        routeN.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "R41";
        stop = subwayStops.get(stopID);
        routeN.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        for(int i =2; i <=9; i++){
            stopID = "N0" + String.valueOf(i);
            stop = subwayStops.get(stopID);
            routeN.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        stopID = "N10";
        stop = subwayStops.get(stopID);
        routeN.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "D43";
        stop = subwayStops.get(stopID);
        routeN.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "G08";
        stop = subwayStops.get(stopID);
        routeR.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "G09";
        stop = subwayStops.get(stopID);
        routeR.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        for(int i =10; i <=16; i++){
            stopID = "G" + String.valueOf(i);
            stop = subwayStops.get(stopID);
            routeR.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        for(int i =18; i <=21; i++){
            stopID = "G" + String.valueOf(i);
            stop = subwayStops.get(stopID);
            routeR.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        stopID = "R11";
        stop = subwayStops.get(stopID);
        routeR.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        for(int i =13; i <=36; i++){
            stopID = "R" + String.valueOf(i);
            stop = subwayStops.get(stopID);
            routeR.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        for(int i =39; i <=45; i++){
            stopID = "R" + String.valueOf(i);
            stop = subwayStops.get(stopID);
            routeR.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        stopID = "A02";
        stop = subwayStops.get(stopID);
        routeA.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "A03";
        stop = subwayStops.get(stopID);
        routeA.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "A05";
        stop = subwayStops.get(stopID);
        routeA.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "A06";
        stop = subwayStops.get(stopID);
        routeA.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "A07";
        stop = subwayStops.get(stopID);
        routeA.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "A09";
        stop = subwayStops.get(stopID);
        routeA.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "A12";
        stop = subwayStops.get(stopID);
        routeA.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "A15";
        stop = subwayStops.get(stopID);
        routeA.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "A24";
        stop = subwayStops.get(stopID);
        routeA.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "A27";
        stop = subwayStops.get(stopID);
        routeA.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "A28";
        stop = subwayStops.get(stopID);
        routeA.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "A31";
        stop = subwayStops.get(stopID);
        routeA.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "A32";
        stop = subwayStops.get(stopID);
        routeA.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "A34";
        stop = subwayStops.get(stopID);
        routeA.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "A36";
        stop = subwayStops.get(stopID);
        routeA.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "A38";
        stop = subwayStops.get(stopID);
        routeA.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "A40";
        stop = subwayStops.get(stopID);
        routeA.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "A41";
        stop = subwayStops.get(stopID);
        routeA.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "A42";
        stop = subwayStops.get(stopID);
        routeA.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "A46";
        stop = subwayStops.get(stopID);
        routeA.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "A48";
        stop = subwayStops.get(stopID);
        routeA.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "A51";
        stop = subwayStops.get(stopID);
        routeA.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "A55";
        stop = subwayStops.get(stopID);
        routeA.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "A57";
        stop = subwayStops.get(stopID);
        routeA.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "A59";
        stop = subwayStops.get(stopID);
        routeA.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "A60";
        stop = subwayStops.get(stopID);
        routeA.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "A61";
        stop = subwayStops.get(stopID);
        routeA.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        for(int i =2; i <=4; i++){
            stopID = "H0" + String.valueOf(i);
            stop = subwayStops.get(stopID);
            routeA.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        for(int i =6; i <=9; i++){
            stopID = "H0" + String.valueOf(i);
            stop = subwayStops.get(stopID);
            routeA.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        for(int i =10; i <=11; i++){
            stopID = "H" + String.valueOf(i);
            stop = subwayStops.get(stopID);
            routeA.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        stopID = "D13";
        stop = subwayStops.get(stopID);
        routeB.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        for(int i =14; i <=22; i++){
            stopID = "A" + String.valueOf(i);
            stop = subwayStops.get(stopID);
            routeB.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        stopID = "A24";
        stop = subwayStops.get(stopID);
        routeB.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        for(int i =14; i <=17; i++){
            stopID = "D" + String.valueOf(i);
            stop = subwayStops.get(stopID);
            routeB.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        for(int i =20; i <=22; i++){
            stopID = "D" + String.valueOf(i);
            stop = subwayStops.get(stopID);
            routeB.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        stopID = "R30";
        stop = subwayStops.get(stopID);
        routeB.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        for(int i =24; i <=26; i++){
            stopID = "D" + String.valueOf(i);
            stop = subwayStops.get(stopID);
            routeB.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        stopID = "D28";
        stop = subwayStops.get(stopID);
        routeB.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "D31";
        stop = subwayStops.get(stopID);
        routeB.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "D35";
        stop = subwayStops.get(stopID);
        routeB.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "D39";
        stop = subwayStops.get(stopID);
        routeB.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "D40";
        stop = subwayStops.get(stopID);
        routeB.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "A09";
        stop = subwayStops.get(stopID);
        routeC.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        for(int i =10; i <=12; i++){
            stopID = "A" + String.valueOf(i);
            stop = subwayStops.get(stopID);
            routeC.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        for(int i =14; i <=22; i++){
            stopID = "A" + String.valueOf(i);
            stop = subwayStops.get(stopID);
            routeC.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        for(int i =24; i <=25; i++){
            stopID = "A" + String.valueOf(i);
            stop = subwayStops.get(stopID);
            routeC.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        for(int i =27; i <=28; i++){
            stopID = "A" + String.valueOf(i);
            stop = subwayStops.get(stopID);
            routeC.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        for(int i =30; i <=34; i++){
            stopID = "A" + String.valueOf(i);
            stop = subwayStops.get(stopID);
            routeC.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        stopID = "A36";
        stop = subwayStops.get(stopID);
        routeC.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "A38";
        stop = subwayStops.get(stopID);
        routeC.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        for(int i =40; i <=55; i++){
            stopID = "A" + String.valueOf(i);
            stop = subwayStops.get(stopID);
            routeC.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        stopID = "D01";
        stop = subwayStops.get(stopID);
        routeD.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        for(int i =3; i <=9; i++){
            stopID = "D0" + String.valueOf(i);
            stop = subwayStops.get(stopID);
            routeD.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        for(int i =10; i <=13; i++){
            stopID = "D" + String.valueOf(i);
            stop = subwayStops.get(stopID);
            routeD.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        stopID = "A15";
        stop = subwayStops.get(stopID);
        routeD.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "A24";
        stop = subwayStops.get(stopID);
        routeD.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        for(int i =14; i <=17; i++){
            stopID = "D" + String.valueOf(i);
            stop = subwayStops.get(stopID);
            routeD.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        for(int i =20; i <=22; i++){
            stopID = "D" + String.valueOf(i);
            stop = subwayStops.get(stopID);
            routeD.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        stopID = "R31";
        stop = subwayStops.get(stopID);
        routeD.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "R36";
        stop = subwayStops.get(stopID);
        routeD.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        for(int i =12; i <=23; i++){
            stopID = "B" + String.valueOf(i);
            stop = subwayStops.get(stopID);
            routeD.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        stopID = "D43";
        stop = subwayStops.get(stopID);
        routeD.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "F01";
        stop = subwayStops.get(stopID);
        routeE.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "F03";
        stop = subwayStops.get(stopID);
        routeE.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "F06";
        stop = subwayStops.get(stopID);
        routeE.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "G08";
        stop = subwayStops.get(stopID);
        routeE.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "G14";
        stop = subwayStops.get(stopID);
        routeE.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "G21";
        stop = subwayStops.get(stopID);
        routeE.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "F09";
        stop = subwayStops.get(stopID);
        routeE.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "F11";
        stop = subwayStops.get(stopID);
        routeE.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "F12";
        stop = subwayStops.get(stopID);
        routeE.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "D14";
        stop = subwayStops.get(stopID);
        routeE.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "A25";
        stop = subwayStops.get(stopID);
        routeE.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        for(int i =27; i <=28; i++){
            stopID = "A" + String.valueOf(i);
            stop = subwayStops.get(stopID);
            routeE.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        for(int i =30; i <=34; i++){
            stopID = "A" + String.valueOf(i);
            stop = subwayStops.get(stopID);
            routeE.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        stopID = "E01";
        stop = subwayStops.get(stopID);
        routeE.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        for(int i =31; i >=13; i--){
            stopID = "S" + String.valueOf(i);
            stop = subwayStops.get(stopID);
            routeSI.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        }
        stopID = "S11";
        stop = subwayStops.get(stopID);
        routeSI.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
        stopID = "S09";
        stop = subwayStops.get(stopID);
        routeSI.addToEnd(graph.new Vertex(stopID, stop.stopName, stop.latitude, stop.longitude, null));
    }


}
