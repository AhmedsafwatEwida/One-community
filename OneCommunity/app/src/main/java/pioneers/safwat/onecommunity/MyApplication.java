package pioneers.safwat.onecommunity;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * Created by safwa on 4/22/2017.
 */

public class MyApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
