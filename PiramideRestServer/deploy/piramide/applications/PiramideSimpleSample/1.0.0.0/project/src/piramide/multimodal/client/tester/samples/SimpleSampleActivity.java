package piramide.multimodal.client.tester.samples;

import android.app.Activity;
import android.os.Bundle;

public class SimpleSampleActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String message = "default";
        //# message = "${piramide.user.name}";
        setTitle(message);
        setContentView(R.layout.main);
    }
}