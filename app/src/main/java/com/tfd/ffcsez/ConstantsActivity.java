package com.tfd.ffcsez;

import android.content.SharedPreferences;

import java.util.HashMap;

public class ConstantsActivity {

    public static HashMap<String, int[]> slotList;
    public static HashMap<Integer, String> numList;
    public static HashMap<Integer, String[]> labTiming;
    public static HashMap<Integer, String[]> theoryTiming;

    public static int selectedTimeTableId = -1;

    public ConstantsActivity() { }

    public static int getSelectedTimeTableId() {
        return selectedTimeTableId;
    }

    public static void setSelectedTimeTableId(int selectedTimeTableId) {
        ConstantsActivity.selectedTimeTableId = selectedTimeTableId;
    }

    public static HashMap<Integer, String[]> getTheoryTiming(){

        if(theoryTiming == null){
            theoryTiming = new HashMap<>();

            // Morning
            theoryTiming.put(1, new String[]{"8:00 AM","8:45 AM"});
            theoryTiming.put(2, new String[]{"9:00 AM","9:45 AM"});
            theoryTiming.put(3, new String[]{"10:00 AM","10:45 AM"});
            theoryTiming.put(4, new String[]{"11:00 AM","11:45 AM"});
            theoryTiming.put(5, new String[]{"12:00 PM","12:45 PM"});
            theoryTiming.put(6, new String[]{"-","-"});

            theoryTiming.put(7, new String[]{"8:00 AM","8:45 AM"});
            theoryTiming.put(8, new String[]{"9:00 AM","9:45 AM"});
            theoryTiming.put(9, new String[]{"10:00 AM","10:45 AM"});
            theoryTiming.put(10, new String[]{"11:00 AM","11:45 AM"});
            theoryTiming.put(11, new String[]{"12:00 PM","12:45 PM"});
            theoryTiming.put(12, new String[]{"-","-"});

            theoryTiming.put(13, new String[]{"8:00 AM","8:45 AM"});
            theoryTiming.put(14, new String[]{"9:00 AM","9:45 AM"});
            theoryTiming.put(15, new String[]{"10:00 AM","10:45 AM"});
            theoryTiming.put(16, new String[]{"11:00 AM","11:45 AM"});
            theoryTiming.put(17, new String[]{"12:00 PM","12:45 PM"});
            theoryTiming.put(18, new String[]{"-","-"});

            theoryTiming.put(19, new String[]{"8:00 AM","8:45 AM"});
            theoryTiming.put(20, new String[]{"9:00 AM","9:45 AM"});
            theoryTiming.put(21, new String[]{"10:00 AM","10:45 AM"});
            theoryTiming.put(22, new String[]{"11:00 AM","11:45 AM"});
            theoryTiming.put(23, new String[]{"12:00 PM","12:45 PM"});
            theoryTiming.put(24, new String[]{"-","-"});

            theoryTiming.put(25, new String[]{"8:00 AM","8:45 AM"});
            theoryTiming.put(26, new String[]{"9:00 AM","9:45 AM"});
            theoryTiming.put(27, new String[]{"10:00 AM","10:45 AM"});
            theoryTiming.put(28, new String[]{"11:00 AM","11:45 AM"});
            theoryTiming.put(29, new String[]{"12:00 PM","12:45 PM"});
            theoryTiming.put(30, new String[]{"-","-"});


            // Afternoon
            theoryTiming.put(31, new String[]{"2:00 PM","2:45 PM"});
            theoryTiming.put(32, new String[]{"3:00 PM","3:45 AM"});
            theoryTiming.put(33, new String[]{"4:00 PM","4:45 PM"});
            theoryTiming.put(34, new String[]{"5:00 PM","5:45 PM"});
            theoryTiming.put(35, new String[]{"6:00 PM","6:45 PM"});
            theoryTiming.put(36, new String[]{"7:00 PM","7:45 PM"});

            theoryTiming.put(37, new String[]{"2:00 PM","2:45 PM"});
            theoryTiming.put(38, new String[]{"3:00 PM","3:45 AM"});
            theoryTiming.put(39, new String[]{"4:00 PM","4:45 PM"});
            theoryTiming.put(40, new String[]{"5:00 PM","5:45 PM"});
            theoryTiming.put(41, new String[]{"6:00 PM","6:45 PM"});
            theoryTiming.put(42, new String[]{"7:00 PM","7:45 PM"});

            theoryTiming.put(43, new String[]{"2:00 PM","2:45 PM"});
            theoryTiming.put(44, new String[]{"3:00 PM","3:45 AM"});
            theoryTiming.put(45, new String[]{"4:00 PM","4:45 PM"});
            theoryTiming.put(46, new String[]{"5:00 PM","5:45 PM"});
            theoryTiming.put(47, new String[]{"6:00 PM","6:45 PM"});
            theoryTiming.put(48, new String[]{"7:00 PM","7:45 PM"});

            theoryTiming.put(49, new String[]{"2:00 PM","2:45 PM"});
            theoryTiming.put(50, new String[]{"3:00 PM","3:45 AM"});
            theoryTiming.put(51, new String[]{"4:00 PM","4:45 PM"});
            theoryTiming.put(52, new String[]{"5:00 PM","5:45 PM"});
            theoryTiming.put(53, new String[]{"6:00 PM","6:45 PM"});
            theoryTiming.put(54, new String[]{"7:00 PM","7:45 PM"});

            theoryTiming.put(55, new String[]{"2:00 PM","2:45 PM"});
            theoryTiming.put(56, new String[]{"3:00 PM","3:45 AM"});
            theoryTiming.put(57, new String[]{"4:00 PM","4:45 PM"});
            theoryTiming.put(58, new String[]{"5:00 PM","5:45 PM"});
            theoryTiming.put(59, new String[]{"6:00 PM","6:45 PM"});
            theoryTiming.put(60, new String[]{"7:00 PM","7:45 PM"});
        }

        return theoryTiming;
    }
    public static HashMap<Integer, String[]> getLabTiming(){

        if(labTiming == null){
            labTiming = new HashMap<>();

            // Morning
            labTiming.put(1, new String[]{"8:00 AM","8:45 AM"});
            labTiming.put(2, new String[]{"8:46 AM","9:30 AM"});
            labTiming.put(3, new String[]{"10:00 AM","10:45 AM"});
            labTiming.put(4, new String[]{"10:46 AM","11:30 AM"});
            labTiming.put(5, new String[]{"11:31 AM","12:15 PM"});
            labTiming.put(6, new String[]{"12:16 PM","1:00 PM"});

            labTiming.put(7, new String[]{"8:00 AM","8:45 AM"});
            labTiming.put(8, new String[]{"8:46 AM","9:30 AM"});
            labTiming.put(9, new String[]{"10:00 AM","10:45 AM"});
            labTiming.put(10, new String[]{"10:46 AM","11:30 AM"});
            labTiming.put(11, new String[]{"11:31 AM","12:15 PM"});
            labTiming.put(12, new String[]{"12:16 PM","1:00 PM"});

            labTiming.put(13, new String[]{"8:00 AM","8:45 AM"});
            labTiming.put(14, new String[]{"8:46 AM","9:30 AM"});
            labTiming.put(15, new String[]{"10:00 AM","10:45 AM"});
            labTiming.put(16, new String[]{"10:46 AM","11:30 AM"});
            labTiming.put(17, new String[]{"11:31 AM","12:15 PM"});
            labTiming.put(18, new String[]{"12:16 PM","1:00 PM"});

            labTiming.put(19, new String[]{"8:00 AM","8:45 AM"});
            labTiming.put(20, new String[]{"8:46 AM","9:30 AM"});
            labTiming.put(21, new String[]{"10:00 AM","10:45 AM"});
            labTiming.put(22, new String[]{"10:46 AM","11:30 AM"});
            labTiming.put(23, new String[]{"11:31 AM","12:15 PM"});
            labTiming.put(24, new String[]{"12:16 PM","1:00 PM"});

            labTiming.put(25, new String[]{"8:00 AM","8:45 AM"});
            labTiming.put(26, new String[]{"8:46 AM","9:30 AM"});
            labTiming.put(27, new String[]{"10:00 AM","10:45 AM"});
            labTiming.put(28, new String[]{"10:46 AM","11:30 AM"});
            labTiming.put(29, new String[]{"11:31 AM","12:15 PM"});
            labTiming.put(30, new String[]{"12:16 PM","1:00 PM"});

            // Afternoon
            labTiming.put(31, new String[]{"2:00 PM","2:45 PM"});
            labTiming.put(32, new String[]{"2:46 PM","3:30 AM"});
            labTiming.put(33, new String[]{"4:00 PM","4:45 PM"});
            labTiming.put(34, new String[]{"4:46 PM","5:30 PM"});
            labTiming.put(35, new String[]{"5:31 PM","6:15 PM"});
            labTiming.put(36, new String[]{"6:00 PM","7:00 PM"});

            labTiming.put(37, new String[]{"2:00 PM","2:45 PM"});
            labTiming.put(38, new String[]{"2:46 PM","3:30 AM"});
            labTiming.put(39, new String[]{"4:00 PM","4:45 PM"});
            labTiming.put(40, new String[]{"4:46 PM","5:30 PM"});
            labTiming.put(41, new String[]{"5:31 PM","6:15 PM"});
            labTiming.put(42, new String[]{"6:00 PM","7:00 PM"});

            labTiming.put(43, new String[]{"2:00 PM","2:45 PM"});
            labTiming.put(44, new String[]{"2:46 PM","3:30 AM"});
            labTiming.put(45, new String[]{"4:00 PM","4:45 PM"});
            labTiming.put(46, new String[]{"4:46 PM","5:30 PM"});
            labTiming.put(47, new String[]{"5:31 PM","6:15 PM"});
            labTiming.put(48, new String[]{"6:00 PM","7:00 PM"});

            labTiming.put(49, new String[]{"2:00 PM","2:45 PM"});
            labTiming.put(50, new String[]{"2:46 PM","3:30 AM"});
            labTiming.put(51, new String[]{"4:00 PM","4:45 PM"});
            labTiming.put(52, new String[]{"4:46 PM","5:30 PM"});
            labTiming.put(53, new String[]{"5:31 PM","6:15 PM"});
            labTiming.put(54, new String[]{"6:00 PM","7:00 PM"});

            labTiming.put(55, new String[]{"2:00 PM","2:45 PM"});
            labTiming.put(56, new String[]{"2:46 PM","3:30 AM"});
            labTiming.put(57, new String[]{"4:00 PM","4:45 PM"});
            labTiming.put(58, new String[]{"4:46 PM","5:30 PM"});
            labTiming.put(59, new String[]{"5:31 PM","6:15 PM"});
            labTiming.put(60, new String[]{"6:00 PM","7:00 PM"});
        }

        return labTiming;
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
