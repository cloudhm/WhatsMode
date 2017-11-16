/**
 * author：caoyamin
 * email：yamin.cao@whatsmode.com
 * Copyright © 2016 Yedao Inc. All rights reserved.
 */

package com.whatsmode.shopify.ui.helper;


public interface SwipeTrigger {
    void onPrepare();

    void onMove(int y, boolean isComplete, boolean automatic);

    void onRelease();

    void onComplete();

    void onReset();
}
