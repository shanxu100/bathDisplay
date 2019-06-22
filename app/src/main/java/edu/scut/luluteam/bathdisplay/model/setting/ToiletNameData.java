package edu.scut.luluteam.bathdisplay.model.setting;

public class ToiletNameData extends BaseSetting {

    private String toiletName;
    private String toiletAddress;

    public ToiletNameData(String toiletName, String toiletAddress) {
        super(BaseSetting.SETTING_TYPE_TOILET_NAME);
        this.toiletName = toiletName;
        this.toiletAddress = toiletAddress;
    }

    public void Update(String toiletName, String toiletAddress) {
        this.toiletName = toiletName;
        this.toiletAddress = toiletAddress;
    }

    public String getToiletName() {
        return toiletName;
    }

    public void setToiletName(String toiletName) {
        this.toiletName = toiletName;
    }

    public String getToiletAddress() {
        return toiletAddress;
    }

    public void setToiletAddress(String toiletAddress) {
        this.toiletAddress = toiletAddress;
    }
}
