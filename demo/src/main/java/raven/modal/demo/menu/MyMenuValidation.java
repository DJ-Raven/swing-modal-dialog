package raven.modal.demo.menu;

import raven.modal.drawer.menu.MenuValidation;

public class MyMenuValidation extends MenuValidation {

    @Override
    public boolean menuValidation(int[] index) {
        return validation(index);
    }

    private static boolean checkMenu(int[] index, int[] indexHide) {
        if (index.length == indexHide.length) {
            for (int i = 0; i < index.length; i++) {
                if (index[i] != indexHide[i]) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    public static boolean validation(int[] index) {
        boolean status
                // `Modal`
                = checkMenu(index, new int[]{2, 0})
                // `Components`->`Toast`
                && checkMenu(index, new int[]{2, 1})
                // `Forms`->`Responsive Layout`
                && checkMenu(index, new int[]{1, 2});

        return status;
    }
}
