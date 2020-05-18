package lk.uom.fit.qms.util.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yasas Pansilu Jayasuriya
 * @version 1.0
 * @E-mail jayasuriyay@gmail.com
 * @Telephone +94777332170
 * @project qms
 * @user Yasas_105071
 * @created on 4/1/2020
 * @Package lk.uom.fit.qms.util.enums.
 */
public enum Rank {
    Lt_Gen("OFFICER"),
    Maj_GEN("OFFICER"),
    Brig("OFFICER"),
    Col("OFFICER"),
    Lt_Col("OFFICER"),
    Maj("OFFICER"),
    Capt("OFFICER"),
    Lt("OFFICER"),
    Lt_2("OFFICER"),
    Po("OFFICER"),
    O_Cdt("OFFICER"),
    WO_1("OTHER"),
    WO_2("OTHER"),
    S_Sgt("OTHER"),
    Sgt("OTHER"),
    Cpl("OTHER"),
    L_Cpl("OTHER"),
    Pte("OTHER"),
    Recruit("OTHER");

    public final String label;

    Rank(String s) {
        this.label =s;
    }

    public static List<Rank> getOfficers(){
        Rank r[] = Rank.values();
        List<Rank> ranks = new ArrayList<>();

        for(int i=0; i<r.length; i++ ){
            if(r[i].label.trim().equals("OFFICER")){
                ranks.add(r[i]);
            }
        }
        return ranks;
    }

    public static List<Rank> getOthers(){
        Rank r[] = Rank.values();
        List<Rank> ranks = new ArrayList<>();

        for(int i=0; i<r.length; i++ ){
            if(r[i].label.trim().equals("OTHER")){
                ranks.add(r[i]);
            }
        }
        return ranks;
    }

    public static List<Rank> getAll(){
        Rank r[] = Rank.values();
        List<Rank> ranks = new ArrayList<>();

        for(int i=0; i<r.length; i++ ){
            ranks.add(r[i]);
        }
        return ranks;
    }
}
