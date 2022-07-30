package cn.iocoder.yudao.module.stucms.utils;

public class NumberUtils {

    /**
     * 去除数字后面的.0,如12.0->12  12.5保留12.5
     *
     * @param value
     * @return
     */
    public static String getFloatValue(String value) {
        Float f = Float.valueOf(value);
        int itemp = Math.round((f - f.intValue()) * 100);
        if (itemp % 100 == 0) {
            value = String.format("%.0f", f);
        } else if (itemp % 10 == 0) {
            value = String.format("%.1f", f);
        } else {
            value = String.format("%.2f", f);
        }

        return value;
    }
}
