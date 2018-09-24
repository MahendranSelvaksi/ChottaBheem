package com.unsullied.chottabheem.utils;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * Created by Value Stream Technologies on 17-01-2018.
 */

public class SymbolsRemover implements InputFilter {
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        String blockCharacterSet = "~#^|$%*!/()-'\":;,?{}=!$^';,?×÷<>{}€£¥₩%~`¤♡♥_|《》¡¿°•○●□■◇◆♧♣▲▼▶◀↑↓←→☆★▪:-);-):-D:-(:'(:";
        for (int i = start; i < end; i++) {
            /*if (!Character.isLetterOrDigit(source.charAt(i))) {
                return "";
            }*/
            if (blockCharacterSet.contains("" + source)) {
                return "";
            }
        }
        return null;
    }
}
