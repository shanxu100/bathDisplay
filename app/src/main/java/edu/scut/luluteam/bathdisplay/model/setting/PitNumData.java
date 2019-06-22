package edu.scut.luluteam.bathdisplay.model.setting;

/**
 * @author Guan
 * @date Created on 2019/4/15
 */
public class PitNumData extends BaseSetting {

    private int numManSit;
    private int numManStand;
    private int numWomanSit;
    private int numDisabledSit;
    private int numDisabledStand;

    public PitNumData(int numManSit, int numManStand, int numWomanSit, int numDisabledSit, int numDisabledStand) {
        super(BaseSetting.SETTING_TYPE_PIT_NUM);
        this.numManSit = numManSit;
        this.numManStand = numManStand;
        this.numWomanSit = numWomanSit;
        this.numDisabledSit = numDisabledSit;
        this.numDisabledStand = numDisabledStand;
    }

    public void Update(int numManSit, int numManStand, int numWomanSit, int numDisabledSit, int numDisabledStand) {
        this.numManSit = numManSit;
        this.numManStand = numManStand;
        this.numWomanSit = numWomanSit;
        this.numDisabledSit = numDisabledSit;
        this.numDisabledStand = numDisabledStand;
    }


    public int getNumManSit() {
        return numManSit;
    }

    public void setNumManSit(int numManSit) {
        this.numManSit = numManSit;
    }

    public int getNumManStand() {
        return numManStand;
    }

    public void setNumManStand(int numManStand) {
        this.numManStand = numManStand;
    }

    public int getNumWomanSit() {
        return numWomanSit;
    }

    public void setNumWomanSit(int numWomanSit) {
        this.numWomanSit = numWomanSit;
    }

    public int getNumDisabledSit() {
        return numDisabledSit;
    }

    public void setNumDisabledSit(int numDisabledSit) {
        this.numDisabledSit = numDisabledSit;
    }

    public int getNumDisabledStand() {
        return numDisabledStand;
    }

    public void setNumDisabledStand(int numDisabledStand) {
        this.numDisabledStand = numDisabledStand;
    }


}
