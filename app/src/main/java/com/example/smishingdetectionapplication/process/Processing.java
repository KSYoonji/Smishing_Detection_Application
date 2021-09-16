package com.example.smishingdetectionapplication.process;

import com.example.smishingdetectionapplication.R;

public class Processing {

    public static Integer percentImg (String percent) {

        int color;

        String p = percent.split("%")[0];

        if (p.equals("주의 "))
            color = R.drawable.green;
        else if (Integer.parseInt(p) >= 85)
            color = R.drawable.red;
        else if (Integer.parseInt(p) <= 85 && Integer.parseInt(p) >= 70)
            color = R.drawable.yellow;
        else
            color = R.drawable.green;

        return color;
    }
}
