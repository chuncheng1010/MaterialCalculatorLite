package com.project.material.helper;

import android.text.InputFilter;
import android.text.Spanned;

public class InputFilterMinMax implements InputFilter {
    private int max;
    private int min;

    public InputFilterMinMax(int paramInt1, int paramInt2) {
        this.min = paramInt1;
        this.max = paramInt2;
    }

    public InputFilterMinMax(String paramString1, String paramString2) {
        this.min = Integer.parseInt(paramString1);
        this.max = Integer.parseInt(paramString2);
    }

    private boolean isInRange(int paramInt1, int paramInt2, int paramInt3) {
        boolean i = true;
        if (paramInt2 <= paramInt1) {
            if ((paramInt3 < paramInt2) || (paramInt3 > paramInt1))
                i = false;
        } else if ((paramInt3 < paramInt1) || (paramInt3 > paramInt2))
            i = false;
        return i;
    }

    @Override
    public CharSequence filter(CharSequence paramCharSequence, int paramInt1,
                               int paramInt2, Spanned paramSpanned, int paramInt3, int paramInt4) {
        String str = "";
        try {
            int i = Integer.parseInt(paramSpanned.toString()
                    + paramCharSequence.toString());
            boolean bool = isInRange(this.min, this.max, i);
            if (bool) {
                str = null;
                return str;
            }
        } catch (NumberFormatException localNumberFormatException) {
            while (true)
                str = "";
        }

        return str;
    }

}

/*
 * Location: C:\New Folder\classes_dex2jar.jar Qualified Name:
 * com.project.pavingcalculator.helper.InputFilterMinMax JD-Core Version: 0.6.0
 */