package news.agoda.com.sample.screens;

/**
 * Created by Sundararaghavan on 11/27/2018.
 */

import android.content.pm.ActivityInfo;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.NoActivityResumedException;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.json.JSONException;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import news.agoda.com.sample.R;
import news.agoda.com.sample.TestApplication;
import news.agoda.com.sample.network.MockWebServiceDataInterator;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.fail;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityErrorTest {
      @Rule
    public ActivityTestRule<MainActivity> activityActivityTestRule = new ActivityTestRule<>(MainActivity.class, true, false);
    private TestApplication application;
    MockWebServiceDataInterator dataInterator;


    @Before
    public void setup() throws JSONException, IOException {
        application = (TestApplication) InstrumentationRegistry.getTargetContext().getApplicationContext();
        dataInterator = (MockWebServiceDataInterator) application.provideNetworkDataInteractor();
        dataInterator.setResult(null);
        activityActivityTestRule.launchActivity(null);
    }


    @Test
    public void testWebServiceError() {
        activityActivityTestRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        onView(withId(R.id.news_error)).check(matches(isDisplayed()));
        try {
            pressBack();
            fail("Should have thrown NoActivityResumedException");
        } catch (NoActivityResumedException expected) {
        }
    }

    @Test
    public void testWebServiceErrorLand() {
        activityActivityTestRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


        onView(withId(R.id.news_error)).check(matches(isDisplayed()));

        try {
            pressBack();
            fail("Should have thrown NoActivityResumedException");
        } catch (NoActivityResumedException expected) {
        }
    }


    @After
    public void end() {
        if (activityActivityTestRule.getActivity() != null)
            activityActivityTestRule.getActivity().finish();
    }

}