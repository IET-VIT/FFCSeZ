package com.tfd.ffcsez;

import java.util.HashMap;

public class ConstantsActivity {

    public static HashMap<String, int[]> slotList;
    public static HashMap<Integer, String> numList;

    public ConstantsActivity() {
    }

    public static HashMap<String, int[]> getSlotList() {

        if (slotList == null){

            slotList = new HashMap<>();
            slotList.put("A1", new int[]{1, 14});
            slotList.put("TA1", new int[]{27});
            slotList.put("TAA1", new int[]{11});
            slotList.put("A2", new int[]{31, 44});
            slotList.put("TA2", new int[]{57});
            slotList.put("TAA2", new int[]{41});
            slotList.put("B1", new int[]{7, 20});
            slotList.put("TB1", new int[]{4});
            slotList.put("B2", new int[]{37, 50});
            slotList.put("TB2", new int[]{34});
            slotList.put("TBB2", new int[]{47});
            slotList.put("C1", new int[]{13, 26});
            slotList.put("TC1", new int[]{10});
            slotList.put("TCC1", new int[]{23});
            slotList.put("C2", new int[]{43, 56});
            slotList.put("TC2", new int[]{40});
            slotList.put("TCC2", new int[]{53});
            slotList.put("D1", new int[]{3, 19});
            slotList.put("TD1", new int[]{29});
            slotList.put("D2", new int[]{33, 49});
            slotList.put("TD2", new int[]{46});
            slotList.put("TDD2", new int[]{59});
            slotList.put("E1", new int[]{9, 25});
            slotList.put("TE1", new int[]{22});
            slotList.put("E2", new int[]{39, 55});
            slotList.put("TE2", new int[]{52});
            slotList.put("F1", new int[]{2, 15});
            slotList.put("TF1", new int[]{28});
            slotList.put("F2", new int[]{32, 45});
            slotList.put("TF2", new int[]{58});
            slotList.put("G1", new int[]{8, 21});
            slotList.put("TG1", new int[]{5});
            slotList.put("G2", new int[]{38, 51});
            slotList.put("TG2", new int[]{35});
            slotList.put("V1", new int[]{16});
            slotList.put("V2", new int[]{17});
            slotList.put("V3", new int[]{36});
            slotList.put("V4", new int[]{42});
            slotList.put("V5", new int[]{48});
            slotList.put("V6", new int[]{54});
            slotList.put("V7", new int[]{60});
        }

        return slotList;
    }

    public static HashMap<Integer, String> getNumList() {

        if (numList == null){

            numList = new HashMap<>();
            numList.put(1, "A1 / L1");
            numList.put(2, "F1 / L2");
            numList.put(3, "D1 / L3");
            numList.put(4, "TB1 / L4");
            numList.put(5, "TG1 / L5");
            numList.put(6, "L6");
            numList.put(7, "B1 / L7");
            numList.put(8, "G1 / L8");
            numList.put(9, "E1 / L9");
            numList.put(10, "TC1 / L10");
            numList.put(11, "TAA1 / L11");
            numList.put(12, "L12");
            numList.put(13, "C1 / L13");
            numList.put(14, "A1 / L14");
            numList.put(15, "F1 / L15");
            numList.put(16, "V1 / L16");
            numList.put(17, "V2 / L17");
            numList.put(18, "L18");
            numList.put(19, "D1 / L19");
            numList.put(20, "B1 / L20");
            numList.put(21, "G1 / L21");
            numList.put(22, "TE1 / L22");
            numList.put(23, "TCC1 / L23");
            numList.put(24, "L24");
            numList.put(25, "E1 / L25");
            numList.put(26, "C1 / L26");
            numList.put(27, "TA1 / L27");
            numList.put(28, "TF1 / L28");
            numList.put(29, "TD1 / L29");
            numList.put(30, "L30");
            numList.put(31, "A2 / L31");
            numList.put(32, "F2 / L32");
            numList.put(33, "D2 / L33");
            numList.put(34, "TB2 / L34");
            numList.put(35, "TG2 / L35");
            numList.put(36, "V3 / L36");
            numList.put(37, "B2 / L37");
            numList.put(38, "G2 / L38");
            numList.put(39, "E2 / L39");
            numList.put(40, "TC2 / L40");
            numList.put(41, "TAA2 / L41");
            numList.put(42, "V4 / L42");
            numList.put(43, "C2 / L43");
            numList.put(44, "A2 / L44");
            numList.put(45, "F2 / L45");
            numList.put(46, "TD2 / L46");
            numList.put(47, "TBB2 / L47");
            numList.put(48, "V5 / L48");
            numList.put(49, "D2 / L49");
            numList.put(50, "B2 / L50");
            numList.put(51, "G2 / L51");
            numList.put(52, "TE2 / L52");
            numList.put(53, "TCC2 / L53");
            numList.put(54, "V6 / L54");
            numList.put(55, "E2 / L55");
            numList.put(56, "C2 / L56");
            numList.put(57, "TA2 / L57");
            numList.put(58, "TF2 / L58");
            numList.put(59, "TDD2 / L59");
            numList.put(60, "V7 / L60");
        }

        return numList;
    }

}
