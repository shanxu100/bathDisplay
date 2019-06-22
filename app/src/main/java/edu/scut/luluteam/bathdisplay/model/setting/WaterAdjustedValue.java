package edu.scut.luluteam.bathdisplay.model.setting;

/**
 * @author Guan
 * @date Created on 2019/4/15
 */
public class WaterAdjustedValue extends BaseSetting {

    private float value;

    public WaterAdjustedValue(float value) {
        super(BaseSetting.SETTING_TYPE_WATER_ADJUSTED_VALUE);
        this.value = value;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
