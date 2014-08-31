package com.example.pinglist.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 */
public class HostManager {

    /**
     * An array of sample (dummy) items.
     */
    public static List<Host> HOSTS = new ArrayList<Host>();

    /**
     * A map of sample (dummy) items, by ip.
     */
    public static Map<String, Host> HOST_MAP = new HashMap<String, Host>();
    
    public enum PingState {
    	PENDING, OK, ERROR;
    }

    static {
    	addItem(new Host("217.76.87.104", "Stefan Larsson", "Gr�stensv�gen 3", "070-7441919"));
    	addItem(new Host("217.76.87.103", "Magnus Grenfeldt", "Gr�stensv�gen 2", "073-3668877"));
    	addItem(new Host("217.76.87.102", "Siverling", "Byv 12", "073-5415565"));
//    	addItem(new Host("217.76.87.115", "Eriksson", "J�kelv 13", "08-6210377"));
    	addItem(new Host("217.76.87.100", "Peter L�rne", "J�kelv 11", "08-7608516"));
    	addItem(new Host("217.76.87.117", "Dags eee", "", ""));
    	addItem(new Host("217.76.87.123", "Anders Thorsell", "J�kelv", ""));
    	addItem(new Host("217.76.87.124", "S�renson", "Gr�stensv 14", "08-7609686"));
//    	addItem(new Host("217.76.87.111", "Hallden", "Gr�stensv 30", "08-7515414"));
//    	addItem(new Host("217.76.87.109", "Leufvenmark", "Brotorpsv 30", "08-6210304"));
    	addItem(new Host("217.76.87.119", "Zoran Vuksanovic", "Rullstensv 11", "070-4048133"));
    	addItem(new Host("217.76.87.116", "Lars Palm", "Byv�gen", ""));
    	addItem(new Host("217.76.87.113", "Eiler Engberg", "J�kelv 10A", "08-361233, 070-8368091"));
    	addItem(new Host("217.76.87.114", "Kent Carlen�s", "J�kelv 10", "08-367362, 070-1711639"));
    	addItem(new Host("217.76.87.125", "Stefan Skog", "Rullstensv 3", "08-734 89 99"));
    	addItem(new Host("217.76.87.118", "Smedshage", "Rullstensv 7", "08-278984"));
//    	addItem(new Host("217.76.87.121", "Mattias Jonasson", "Rullstensv 9", "070-5470016"));
    	addItem(new Host("217.76.87.97", "Router", "Solna-Smedshage", ""));
    	addItem(new Host("217.76.87.126", "Router", "Sj�holm-Gullik", ""));
    	addItem(new Host("217.76.87.130", "Torsell", "Toppbrinken 17", "070-5166164"));
    	addItem(new Host("217.76.87.120", "Rende", "Grpstensv 5", "076-127 9277"));
    	addItem(new Host("109.74.0.102", "dev.find-out.se", "", ""));
    }

    private static void addItem(Host item) {
        HOSTS.add(item);
        HOST_MAP.put(item.ipNo, item);
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class Host {
        public String ipNo;
		public String name;
		public String addr;
		public String telNo;
		public PingState pingState;

        public Host(String ipNo, String name, String addr, String telNo) {
        	this.ipNo = ipNo;
			this.name = name;
			this.addr = addr;
			this.telNo = telNo;
            this.pingState = PingState.PENDING;
        }

        @Override
        public String toString() {
            return ipNo;
        }
    }
}