package lk.uom.fit.qms.util.enums;

public enum BloodGroup {
    A_P("A+"),
    B_P("B+"),
    AB_P("AB+"),
    O_P("O+"),
    A_N("A-"),
    B_N("B-"),
    AB_N("AB-"),
    O_N("O-");

    public final String label;

    BloodGroup(String s) {
        this.label =s;
    }

    public static BloodGroup findByValue(String s){
        BloodGroup bg[] = BloodGroup.values();

        for(int i=0; i<bg.length; i++ ){
            System.out.println(bg[i].label);
            if(bg[i].label.trim().equals(s.trim())){
                return bg[i];
            }
        }
        return null;
    }
}
