package edu.scut.luluteam.bathdisplay.manager.odor;

import java.util.Observable;

/**
 * @author Guan
 * @date Created on 2019/6/17
 */
public class OdorManager extends Observable {

    public static final int ODOR_MAN = 1;
    public static final int ODOR_WOMAN = 2;
    public static final int ODOR_DISABLED = 3;

    public static final int ODOR_NH3 = 11;
    public static final int ODOR_H2S = 12;


    /**
     * 氨气的九个变量
     */
    private int NH3_MAN_201 = -1;
    private int NH3_MAN_203 = -1;
    private int NH3_MAN_205 = -1;
    private int NH3_WOMAN_208 = -1;
    private int NH3_WOMAN_210 = -1;
    private int NH3_WOMAN_212 = -1;
    private int NH3_DISABLED_214 = -1;
    private int NH3_DISABLED_216 = -1;
    private int NH3_DISABLED_218 = -1;

    /**
     * 硫化氢的九个变量
     */
    private int H2S_MAN_202 = -1;
    private int H2S_MAN_204 = -1;
    private int H2S_MAN_206 = -1;
    private int H2S_WOMAN_209 = -1;
    private int H2S_WOMAN_211 = -1;
    private int H2S_WOMAN_213 = -1;
    private int H2S_DISABLED_215 = -1;
    private int H2S_DISABLED_217 = -1;
    private int H2S_DISABLED_219 = -1;


    public static OdorManager instance = null;

    public static OdorManager getInstance() {
        if (instance == null) {
            synchronized (OdorManager.class) {
                if (instance == null) {
                    instance = new OdorManager();
                }
            }
        }
        return instance;
    }

    private OdorManager() {
    }

    public void updateValue(String deviceNum, int value) {
        switch (deviceNum) {
            //先是氨气
            case "201":
                NH3_MAN_201 = value;
                notifyManNH3();
                break;
            case "203":
                NH3_MAN_203 = value;
                notifyManNH3();
                break;
            case "205":
                NH3_MAN_205 = value;
                notifyManNH3();
                break;
            case "208":
                NH3_WOMAN_208 = value;
                notifyWomanNH3();
                break;
            case "210":
                NH3_WOMAN_210 = value;
                notifyWomanNH3();
                break;
            case "212":
                NH3_WOMAN_212 = value;
                notifyWomanNH3();
                break;
            case "214":
                NH3_DISABLED_214 = value;
                notifyDisabledNH3();
                break;
            case "216":
                NH3_DISABLED_216 = value;
                notifyDisabledNH3();
                break;
            case "218":
                NH3_DISABLED_218 = value;
                notifyDisabledNH3();
                break;

            //这里接着是硫化氢
            case "202":
                H2S_MAN_202 = value;
                notifyManH2S();
                break;
            case "204":
                H2S_MAN_204 = value;
                notifyManH2S();
                break;
            case "206":
                H2S_MAN_206 = value;
                notifyManH2S();
                break;
            case "209":
                H2S_WOMAN_209 = value;
                notifyWomanH2S();
                break;
            case "211":
                H2S_WOMAN_211 = value;
                notifyWomanH2S();
                break;
            case "213":
                H2S_WOMAN_213 = value;
                notifyWomanH2S();
                break;
            case "215":
                H2S_DISABLED_215 = value;
                notifyDisabledH2S();
                break;
            case "217":
                H2S_DISABLED_217 = value;
                notifyDisabledH2S();
                break;
            case "219":
                H2S_DISABLED_219 = value;
                notifyDisabledH2S();
                break;
            default:
                break;
        }

    }

    private void notifyManNH3() {
        int count = 0;
        int sum = 0;
        if (NH3_MAN_201 != -1) {
            count++;
            sum += NH3_MAN_201;
        }
        if (NH3_MAN_203 != -1) {
            count++;
            sum += NH3_MAN_203;
        }
        if (NH3_MAN_205 != -1) {
            count++;
            sum += NH3_MAN_205;
        }

        if (count != 0) {
            OdorArg arg = new OdorArg();
            arg.arg0 = ODOR_MAN;
            arg.arg1 = ODOR_NH3;
            arg.value = (sum + 0.0) / count;
            setChanged();
            notifyObservers(arg);
        }


    }

    private void notifyManH2S() {
        int count = 0;
        int sum = 0;
        if (H2S_MAN_202 != -1) {
            count++;
            sum += H2S_MAN_202;
        }
        if (H2S_MAN_204 != -1) {
            count++;
            sum += H2S_MAN_204;
        }
        if (H2S_MAN_206 != -1) {
            count++;
            sum += H2S_MAN_206;
        }

        if (count != 0) {
            OdorArg arg = new OdorArg();
            arg.arg0 = ODOR_MAN;
            arg.arg1 = ODOR_H2S;
            arg.value = (sum + 0.0) / count;
            setChanged();
            notifyObservers(arg);
        }
    }

    private void notifyWomanNH3() {
        int count = 0;
        int sum = 0;
        if (NH3_WOMAN_208 != -1) {
            count++;
            sum += NH3_WOMAN_208;
        }
        if (NH3_WOMAN_210 != -1) {
            count++;
            sum += NH3_WOMAN_210;
        }
        if (NH3_WOMAN_212 != -1) {
            count++;
            sum += NH3_WOMAN_212;
        }

        if (count != 0) {
            OdorArg arg = new OdorArg();
            arg.arg0 = ODOR_WOMAN;
            arg.arg1 = ODOR_NH3;
            arg.value = (sum + 0.0) / count;
            setChanged();
            notifyObservers(arg);
        }
    }

    private void notifyWomanH2S() {
        int count = 0;
        int sum = 0;
        if (H2S_WOMAN_209 != -1) {
            count++;
            sum += H2S_WOMAN_209;
        }
        if (H2S_WOMAN_211 != -1) {
            count++;
            sum += H2S_WOMAN_211;
        }
        if (H2S_WOMAN_213 != -1) {
            count++;
            sum += H2S_WOMAN_213;
        }

        if (count != 0) {
            OdorArg arg = new OdorArg();
            arg.arg0 = ODOR_WOMAN;
            arg.arg1 = ODOR_H2S;
            arg.value = (sum + 0.0) / count;
            setChanged();
            notifyObservers(arg);
        }
    }

    private void notifyDisabledNH3() {
        int count = 0;
        int sum = 0;
        if (NH3_DISABLED_214 != -1) {
            count++;
            sum += NH3_DISABLED_214;
        }
        if (NH3_DISABLED_216 != -1) {
            count++;
            sum += NH3_DISABLED_216;
        }
        if (NH3_DISABLED_218 != -1) {
            count++;
            sum += NH3_DISABLED_218;
        }

        if (count != 0) {
            OdorArg arg = new OdorArg();
            arg.arg0 = ODOR_DISABLED;
            arg.arg1 = ODOR_NH3;
            arg.value = (sum + 0.0) / count;
            setChanged();
            notifyObservers(arg);
        }
    }

    private void notifyDisabledH2S() {
        int count = 0;
        int sum = 0;
        if (H2S_DISABLED_215 != -1) {
            count++;
            sum += H2S_DISABLED_215;
        }
        if (H2S_DISABLED_217 != -1) {
            count++;
            sum += H2S_DISABLED_217;
        }
        if (H2S_DISABLED_219 != -1) {
            count++;
            sum += H2S_DISABLED_219;
        }

        if (count != 0) {
            OdorArg arg = new OdorArg();
            arg.arg0 = ODOR_DISABLED;
            arg.arg1 = ODOR_H2S;
            arg.value = (sum + 0.0) / count;
            setChanged();
            notifyObservers(arg);
        }
    }

    /**
     * 封装传递给前端的参数
     */
    public static class OdorArg {

        public int arg0;
        public int arg1;

        public double value;


    }

}
